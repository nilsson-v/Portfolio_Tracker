package Data

import org.json4s.jackson.{Json, parseJson}

import io.StdIn.*
import sys.process.*
import java.net.URL
import java.io.{BufferedInputStream, BufferedWriter, File, FileOutputStream, FileWriter}
import scala.io.Source
import scala.util.Using
import requests.*


class DataReader:

  val folderPath = os.Path("/APIFiles")

  def fetchAPIfromURL(ticker: String, fileName: String) =
    val url = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=" + ticker + "&apikey=SZPPN9IYEA485BC9&datatype=json"
    val connection = new URL(url).openConnection()
    val in = new BufferedInputStream(connection.getInputStream)
    val out = new FileOutputStream(fileName)
    try {
      val buffer = new Array[Byte](1024)
      var bytesRead = in.read(buffer)
      while (bytesRead != -1) {
        out.write(buffer, 0, bytesRead)
        bytesRead = in.read(buffer)
      }
    } finally {
      in.close()
      out.close()
    }


  def getAPI(ticker: String) =

    val url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=" + ticker + "&apikey=SZPPN9IYEA485BC9&datatype=json")
    val jsonString = Source.fromInputStream(url.openStream()).mkString
    jsonString

  def fetchAPI(ticker: String, fileName: String) =
   val url = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=" + ticker + "&apikey=SZPPN9IYEA485BC9&datatype=json"
   //val jsonString = Source.fromURL(url).mkString
   //val parsed = parseJson(jsonString)
   //os.makeDir.all(folderPath)

   Using(scala.io.Source.fromURL(url)) { source =>
     val fileContent = source.mkString
     val filePath = folderPath/fileName
     os.write.over(filePath, fileContent)
   }

   println("File: " + fileName + " was imported succesfully " + url)
















