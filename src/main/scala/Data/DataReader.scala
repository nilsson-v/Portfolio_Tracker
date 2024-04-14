package Data

import org.json4s.jackson.{Json, parseJson}

import io.StdIn.*
import sys.process.*
import java.net.URL
import java.io.{BufferedInputStream, BufferedReader, BufferedWriter, File, FileOutputStream, FileReader, FileWriter}
import scala.io.Source
import scala.util.Using
import requests.*

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future



class DataReader:

  /** INSERT API Key here*/
  val APIKey = "SZPPN9IYEA485BC9"

  /** Takes a stock ticker as parameter and fetches that stock from the Alpha Vantage API
   * saves the json file in string fromat in the val jsonString */
  def getAPI(ticker: String) =
    val url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=" + ticker + "&apikey=" + APIKey + "&datatype=json")
    val jsonString = Source.fromInputStream(url.openStream()).mkString
    jsonString

/** Takes all the ArrayBuffers with the data shown on the dashboard as parameters.
 * Creates a file path and a file where the data will be stored.
 * Organises the data so that each ArrayBuffer is separated by a space and the stockEntries1 and 2 marked with headers*/
  def saveData(stockEntries1: ArrayBuffer[(String, Double)], dateEntries1: ArrayBuffer[String], stockEntries2: ArrayBuffer[(String, Double)], dateEntries2: ArrayBuffer[String], filePath: String): Unit = {
    val file = new File(filePath)
    val fileWriter = new FileWriter(file)
    val out = new BufferedWriter(fileWriter)
    try {
      out.write("StockEntries1\n")
      stockEntries1.foreach { case (stock, amount) =>
        out.write(s"$stock,$amount\n")
      }
      out.newLine()
      dateEntries1.foreach { date =>
        out.write(s"$date\n")
      }
      out.newLine()

      out.write("StockEntries2\n")
      stockEntries2.foreach { case (stock, amount) =>
        out.write(s"$stock,$amount\n")
      }
      out.newLine()
      dateEntries2.foreach { date =>
        out.write(s"$date\n")
      }
    } finally {
      out.close()
    }
  }

/**Takes all the ArrayBuffers with the data from the file
 * Goes through the the file and parses the arrays in order to asign their values to the correct ArrayBuffer*/
  def loadData(filePath: String): (ArrayBuffer[(String, Double)], ArrayBuffer[String], ArrayBuffer[(String, Double)], ArrayBuffer[String]) = {
    val stockEntries1 = ArrayBuffer[(String, Double)]()
    val dateEntries1 = ArrayBuffer[String]()
    val stockEntries2 = ArrayBuffer[(String, Double)]()
    val dateEntries2 = ArrayBuffer[String]()

    var currentSection: Option[String] = None

    
    val source = Source.fromFile(filePath)
    try {
      for (line <- source.getLines()) {
        val trimmedLine = line.trim()
        if (trimmedLine.nonEmpty) {
          trimmedLine match {
            case "StockEntries1" => currentSection = Some("StockEntries1")
            case "StockEntries2" => currentSection = Some("StockEntries2")
            case ""              => currentSection = None
            
            case data if currentSection.contains("StockEntries1") =>
              val parts = data.split(",")
              if (parts.length == 2) {
                stockEntries1 += (parts(0) -> parts(1).toDouble)
              } else if (parts.length == 1) {
                dateEntries1 += parts(0)
              } else {
                println(s"Invalid data format in section StockEntries1: $data")
              }
            case data if currentSection.contains("StockEntries2") =>
              val parts = data.split(",")
              if (parts.length == 2) {
                stockEntries2 += (parts(0) -> parts(1).toDouble)
              } else if (parts.length == 1) {
                dateEntries2 += parts(0)
              } else {
                println(s"Invalid data format in section StockEntries2: $data")
              }
            case _ => println(s"Ignoring invalid line: $trimmedLine")
          }
        }
      }
    } finally {
      source.close()
    }

    (stockEntries1, dateEntries1, stockEntries2, dateEntries2)
  }














