package Data

import io.StdIn.*
import sys.process._
import java.net.URL
import java.io.File
import upickle.default._


class Price:


  def openPrice() =
    val jsonString = os.read(os.pwd/"src"/"main"/"scala"/"Data"/"APIFiles"/"Appletest.json")
    val data = ujson.read(jsonString)
    println(data("results").arr.toString)


  /** vw = volume
   *  o = open
   *  c = close
   *  h = high
   *  l = low*/


end Price

