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
   //val Range: String = readLine("Insert the desired multiplier: ") //example 1
   //val timeSpan: String = readLine("Choose desired timespan(day, week, month): " ) //day, week, month, year
   //val startDate: String = readLine("Insert start date in the form year-month-day:" ) //example: "2023-01-09"
   //val endDate: String = readLine("Insert end date in the form year-month-day:" ) //example: "2023-06-09"
   val fileName: String = readLine("Insert the name of the file: ") //

   //val oldUrl = "https://api.polygon.io/v2/aggs/ticker/"+
     //Ticker+"/range/1"/"+timeSpan+"/"+startDate+"/"+endDate
      //"?adjusted=true&sort=asc&limit=120&apiKey=pQidfuXRC5_WyjW5pS5QmxyWY_eLggpZ

   val url = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=" + Ticker + "&apikey=SZPPN9IYEA485BC9&datatype=json"

   os.makeDir.all(folderPath)

   Using(scala.io.Source.fromURL(url)) { source =>
     val fileContent = source.mkString
     val filePath = folderPath/fileName
     os.write.over(filePath, fileContent)
   }

   println("File: " + fileName + " was imported succesfully " + url)















