package Data

import io.StdIn.*
import sys.process._
import java.net.URL
import java.io.File
import upickle.default._


class Price:

  def openingPrice(fileName: String) =
    val jsonString = os.read(os.pwd/"src"/"main"/"scala"/"Data"/"APIFiles"/fileName)
    val priceData = ujson.read(jsonString)
    val oPrice = priceData("results")(0)("o").num
    println(oPrice)

  def closingPrice(fileName: String) =
    val jsonString = os.read(os.pwd/"src"/"main"/"scala"/"Data"/"APIFiles"/fileName)
    val priceData = ujson.read(jsonString)
    val cPrice = priceData("results")(0)("c").num
    println(cPrice)


  /** vw = volume
   *  o = open
   *  c = close
   *  h = high
   *  l = low*/

end Price

