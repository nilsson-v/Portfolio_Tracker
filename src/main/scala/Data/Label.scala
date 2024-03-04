package Data

import io.StdIn.*
import sys.process._
import java.net.URL
import java.io.File

class Label:

  def labelName(fileName: String): String =
    val jsonString = os.read(os.pwd/"APIFiles"/fileName)
    val priceData = ujson.read(jsonString)
    val ticker = priceData("ticker").str
    ticker


end Label
