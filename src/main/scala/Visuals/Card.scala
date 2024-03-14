package Visuals

import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

class Card:

  def sum(numbers: Array[Double]) =
    var count = 0.0
    for i <- numbers.indices do
      count = count + numbers(i)
    count


  def getPrices(stockList: Array[(String, Double)]) =
    var priceList = Array[Double]()
    val getSymbols = stockList.map((stock, multiplier) => Data.StockData().getSymbol(stock))
    val getStocks: Array[Array[(String, Double)]] = stockList.map((stock, multiplier)
      => Data.StockData().multiplyStocks(stock, multiplier))
    for i <- getStocks.indices do
      priceList ++= Array(getStocks(i).last._2)
    priceList

  def makeSumCard(stockList: Array[(String, Double)]) =
    val priceList = getPrices(stockList)
    val priceSum = sum(priceList)

    val sumCard = new Rectangle
    sumCard.width = 200
    sumCard.height = 150
    sumCard.fill = Color.LightGray

    val content = new Text
    content.text = priceSum.toString
    content.layoutX = 80
    content.layoutY = 80
    content.fill = Color.Black

    val sumNode: javafx.scene.Node = new javafx.scene.Group(sumCard, content)

    sumNode

















end Card

