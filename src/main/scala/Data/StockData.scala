package Data

import upickle.default.*
import org.json4s
import org.json4s.JsonAST.JField
import org.json4s.{DefaultFormats, JField, JObject, JString, jvalue2extractable, jvalue2monadic}
import org.json4s.jackson.{JsonMethods, parseJson, parseJsonOpt}
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source as AkkaSource}
import scala.concurrent.duration._
import scalafx.collections.ObservableBuffer

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.io.Source


class StockData:

  implicit val formats: DefaultFormats = DefaultFormats
  implicit val system: ActorSystem = ActorSystem("ListToObservableBufferExample")

  private def s2toDouble(list: List[(String, String)]) =
    list.map((s, s2) => (s, s2.toDouble))

  def listToObservableBuffer(list: Array[(String, Double)]) =
    val observableBuffer = AkkaSource(list).fold(ListBuffer.empty[(String, Double)]) { (buffer, element)
    => buffer += element }.runWith(Sink.head)
    val listBufferResult = Await.result(observableBuffer, 5.seconds)
    val observableBufferResult= ObservableBuffer(listBufferResult.toList)
    observableBufferResult

  private def singleClosePrice(fileName: String, date: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val openValue = (parsedJson \ "Monthly Adjusted Time Series" \ date \ "5. adjusted close").extract[String]
    openValue

  private def closePrice(fileName: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val closingValues = (parsedJson \\ "Monthly Adjusted Time Series").children.
      flatMap {
      case JObject(fields) => fields.collect {
      case ("5. adjusted close", JString(value)) => value
    }
    }
    closingValues

  private def extractDates(fileName: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val dates = (parsedJson \ "Monthly Adjusted Time Series").asInstanceOf[JObject].values.keys.toList
    val sorted = dates.sorted
    sorted

  def zipDatesAndPrices(fileName: String) =
    val dateList = extractDates(fileName)
    val priceList = closePrice(fileName).reverse
    val zipped = dateList.zip(priceList)
    val listStringDouble = s2toDouble(zipped)
    listStringDouble.toArray

  def pricesFromMonth(stockList: Array[(String, Double)], purchaseDate: String) =
    val filteredList = stockList.dropWhile{ case (month, _) => month.take(7) < purchaseDate}
    val result = filteredList.map((x,y)=>(x, y))
    result

  def latestPrice(fileName: String) =
    val closingPrices = closePrice(fileName)
    closingPrices.head

  def combineStockPrices(stockList: Array[String]): Array[(String, Double)] =
    val getStocks: Array[Array[(String, Double)]] = stockList.map(stock => zipDatesAndPrices(stock))
    val groupedStocks = getStocks.flatten.groupBy(_._1)
    val combinedStocks = groupedStocks.map((key, values) => (key, values.map(_._2).sum))
    val stocksToArray = combinedStocks.toArray
    val sorted = stocksToArray.sorted
    sorted

  def multiplyStocks(fileName: String, multiplier: Double) =
    val stockArray = zipDatesAndPrices(fileName)
    val result = stockArray.map((key, values) => (key, values * multiplier))
    result

  def getSymbol(fileName: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val symbol = (parsedJson \ "Meta Data" \ "2. Symbol").extract[String]
    symbol

    //takes as parameter an Array with the stocks and their corresponding multiplier
  def multiplyAndCombine(stockList: Array[(String, Double)]): Array[(String, Double)] =
    val makeArray = stockList.map((stock, multiplier) => multiplyStocks(stock, multiplier))
    val groupedStocks = makeArray.flatten.groupBy(_._1)
    val combinedStocks = groupedStocks.map((key, values) => (key, values.map(_._2).sum))
    val stocksToArray = combinedStocks.toArray
    val sorted = stocksToArray.sorted
    sorted







end StockData




