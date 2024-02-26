package Data

import io.StdIn.*
import sys.process._
import java.net.URL
import java.io.File
import upickle.default._

class Price(file: File):

  val jsonString = file.toString

  val json: ujson.Value = ujson.read(jsonString)

  def openPrice = println(json("o"))




  /** vw = volume
   *  o = open
   *  c = close
   *  h = high
   *  l = low*/



end Price

