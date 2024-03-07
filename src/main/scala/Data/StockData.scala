package Data
import javafx.collections.FXCollections
import upickle.default.*
import org.json4s
import org.json4s.JsonAST.JField
import org.json4s.{DefaultFormats, JField, JObject, JString, jvalue2extractable, jvalue2monadic}
import org.json4s.jackson.{JsonMethods, parseJson, parseJsonOpt}



class StockData:

  implicit val formats: DefaultFormats = DefaultFormats

  def singleOpenPrice(fileName: String, date: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val openValue = (parsedJson \ "Monthly Adjusted Time Series" \ date \ "1. open").extract[String]
    openValue

  def openPrice(fileName: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val openingValues = (parsedJson \\ "Monthly Adjusted Time Series").children.
      flatMap {
      case JObject(fields) => fields.collect {
      case ("1. open", JString(value)) => value
    }
    }
    openingValues

  def extractDates(fileName: String) =
    val jsonString = os.read(os.pwd / "APIFiles" / fileName)
    val parsedJson = parseJson(jsonString)
    val dates = (parsedJson \ "Monthly Adjusted Time Series").asInstanceOf[JObject].values.keys.toList
    val sorted = dates.sorted
    sorted

  def zipDatesAndPrices(fileName: String) =
    val dateList = extractDates(fileName)
    val priceList = openPrice(fileName).reverse
    val zipped = dateList.zip(priceList)
    zipped


end StockData




