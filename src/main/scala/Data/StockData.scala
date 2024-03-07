package Data
import upickle.default.*
import org.json4s
import org.json4s.{DefaultFormats, JObject, JString, jvalue2extractable, jvalue2monadic}
import org.json4s.jackson.{JsonMethods, parseJson}



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


end StockData




