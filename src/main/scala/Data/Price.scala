package Data

import scalafx.collections.ObservableBuffer

import io.StdIn.*
import sys.process.*
import java.net.URL
import java.io.File
import upickle.default.*

import scala.collection.mutable.Buffer


class Price:

  def count(fileName: String) =
    val jsonString = os.read(os.pwd/"APIFiles"/fileName)
    val priceData = ujson.read(jsonString)
    val count = priceData("count").num
    count.toInt

  def openingPrice(fileName: String) =
    var pricesList = ObservableBuffer[Double]()
    val jsonString = os.read(os.pwd/"APIFiles"/fileName)
    val priceData = ujson.read(jsonString)
    for i <- 0 until count(fileName) do
     val oPrice = priceData("results")(i)("o").num
     pricesList = pricesList :+ oPrice
    pricesList

  def closingPrice(fileName: String) =
    var pricesList = ObservableBuffer[Double]()
    val jsonString = os.read(os.pwd/"APIFiles"/fileName)
    val priceData = ujson.read(jsonString)
    for i <- 0 until count(fileName) do
     val oPrice = priceData("results")(i)("c").num
     pricesList = pricesList :+ oPrice
    pricesList


end Price

