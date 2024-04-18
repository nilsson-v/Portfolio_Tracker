package Data


import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source as AkkaSource}
import org.json4s
import org.json4s.JsonAST.JField
import org.json4s.jackson.{JsonMethods, parseJson, parseJsonOpt}
import org.json4s.{DefaultFormats, JField, JObject, JString, jvalue2extractable, jvalue2monadic}
import scalafx.collections.ObservableBuffer
import upickle.default.*

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.*


/** If this program is used with the Alpha Vantage premium API:
 * - Give all "val jsonString" the following value: Data.DataReader().getAPI(fileName)
 * If this program is used with manually downloaded files
 * - Give all "val jsonString" the following value: os.read(os.pwd / "APIFiles" /fileName)*/

class StockData:

  implicit val formats: DefaultFormats = DefaultFormats
  implicit val system: ActorSystem = ActorSystem("ListToObservableBufferExample")

  /**turns the second String in the tuple to a Double*/
  private def s2toDouble(list: List[(String, String)]) =
    list.map((s, s2) => (s, s2.toDouble))

  /**Takes as parameter an Array[(String, Double)] and converts it to an observableBuffer*/
  def listToObservableBuffer(list: Array[(String, Double)]) =
    val observableBuffer = AkkaSource(list).fold(ListBuffer.empty[(String, Double)]) { (buffer, element)
    => buffer += element }.runWith(Sink.head)
    val listBufferResult = Await.result(observableBuffer, 5.seconds)
    val observableBufferResult= ObservableBuffer(listBufferResult.toList)
    observableBufferResult

  /**finds the correct close prices from the JSON file (adjusted close) and returns them in List*/
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

  /** finds the dates from the JSON files and returns them in a List */
  private def extractDates(fileName: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val dates = (parsedJson \ "Monthly Adjusted Time Series").asInstanceOf[JObject].values.keys.toList
    val sorted = dates.sorted
    sorted

  /** combines the price list with the datelist to return an Array that has matched each price with the correct monnth */
  def zipDatesAndPrices(fileName: String) =
    val dateList = extractDates(fileName)
    val priceList = closePrice(fileName).reverse
    val zipped = dateList.zip(priceList)
    val listStringDouble = s2toDouble(zipped)
    listStringDouble.toArray

  /**takes an array with months and their prices aswell as the purchase date and returns an array that has been cut at the purchase date*/
  def pricesFromMonth(stockList: Array[(String, Double)], purchaseDate: String) =
    val filteredList = stockList.dropWhile{ case (month, _) => month.take(7) < purchaseDate}
    val result = filteredList.map((x,y)=>(x, y))
    result

  /** finds the latest price of a stock */
  def latestPrice(fileName: String) =
    val closingPrices = closePrice(fileName)
    closingPrices.head

  /**multiplies the stocks value with desired multiplier (the persons holdings)*/
  def multiplyStocks(fileName: String, multiplier: Double) =
    val stockArray = zipDatesAndPrices(fileName)
    val result = stockArray.map((key, values) => (key, values * multiplier))
    result

  /**extracts the symbol of the stock from the JSON file*/
  def getSymbol(fileName: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val symbol = (parsedJson \ "Meta Data" \ "2. Symbol").extract[String]
    symbol

  /**takes as parameter an Array with the stocks and their corresponding multiplier
    takes as second parameter an array with each stocks purchase date
    returns an array with months and the combined price of each stock from that month */
  def combineAndMultiply(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    val zipList = stockList.zip(purchaseDates)
    val filteredStockData = zipList.map { case ((stock, multiplier), purchaseDate) =>
      val stockData = zipDatesAndPrices(stock)
      val filteredData = stockData.dropWhile { case (month, _) => month.take(7) < purchaseDate }
      filteredData.map { case (month, price) => (month, price * multiplier) }
    }
    val combinedData = filteredStockData.flatten.groupBy(_._1).view.mapValues(_.map(_._2).sum)
    combinedData.toArray.sorted


end StockData




