package Data

import io.StdIn.*
import sys.process.*
import java.net.URL
import java.io.File
import scala.io.Source
import scala.util.Using


class DataReader:

  val folderPath = os.Path("/APIFiles")

  def fetchAPI() =
   val Ticker: String = readLine("Insert the stocks symbol:" ) //example AAPL

   val fileName: String = readLine("Insert the name of the file: ") //

   val url = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=" + Ticker + "&apikey=SZPPN9IYEA485BC9&datatype=json"

   os.makeDir.all(folderPath)

   Using(scala.io.Source.fromURL(url)) { source =>
     val fileContent = source.mkString
     val filePath = folderPath/fileName
     os.write.over(filePath, fileContent)
   }

   println("File: " + fileName + " was imported succesfully " + url)















