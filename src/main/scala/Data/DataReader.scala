package Data

import io.StdIn.*
import sys.process._
import java.net.URL
import java.io.File

class DataReader:

  val Ticker: String = readLine("Insert the stocks symbol:" ) //example AAPL
  val Range: String = readLine("Insert the desired multiplier: ") //example 1
  val timeSpan: String = readLine("Choose desired timespan(day, week, month): " ) //day, week, month, year
  val startDate: String = readLine("Insert start date in the form year-month-day:" ) //example: "2023-01-09"
  val endDate: String = readLine("Insert end date in the form year-month-day:" ) //example: "2023-06-09"

  val url = "https://api.polygon.io/v2/aggs/ticker/"+
    Ticker+"/range/"+Range+"/"+timeSpan+"/"+startDate+"/"+endDate+
    "?adjusted=true&sort=asc&limit=120&apiKey=pQidfuXRC5_WyjW5pS5QmxyWY_eLggpZ"

  val folderPath = "./APIFiles/"

  def fetchAPI(fileName: String) =
    new URL(url) #> new File(folderPath+fileName+".json")




