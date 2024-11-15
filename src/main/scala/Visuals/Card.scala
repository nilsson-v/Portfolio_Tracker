package Visuals

import Data.StockData
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, FontWeight, Text, TextAlignment}

import scala.collection.mutable.ArrayBuffer

class Card:

  val font = Font.font("Arial", FontWeight.Bold, 25)


  /** Finds how much each stock entry has grown since it was bought.
   * Adds all the growth values to find the total growth  */
  def findGrowthValue(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    val cuttedStocks = ArrayBuffer[Array[(String, Double)]]()
    val priceDifferences = ArrayBuffer[Double]()
    val multiplied = stockList.map( (stock, multiplier) => StockData().multiplyStocks(stock, multiplier))
    val stockArray = for i <- multiplied.indices do
      cuttedStocks += StockData().pricesFromMonth(multiplied(i), purchaseDates(i))
    for i <- cuttedStocks.indices do
      priceDifferences += cuttedStocks(i).lastOption.map(_._2).getOrElse(0.0) - cuttedStocks(i).headOption.map(_._2).getOrElse(0.0)
    priceDifferences.sum

  /** Takes as parameter a list with stocks and their multipliers¨
   * multiplies each price with their corresponding stock and adds all the prices to one list*/
  def getPrices(stockList: Array[(String, Double)]) =
    var priceList = Array[Double]()
    val getStocks: Array[Array[(String, Double)]] = stockList.map((stock, multiplier)
      => StockData().multiplyStocks(stock, multiplier))
    for i <- getStocks.indices do
      priceList ++= Array(getStocks(i).last._2)
    priceList

  /** Creates the card that shows the total value of the portfolio */
  def makeSumCard(stockList: Array[(String, Double)]) =
    val priceList = getPrices(stockList)
    val priceSum = priceList.sum
    val roundedSum = BigDecimal(priceSum).setScale(1, BigDecimal.RoundingMode.HALF_UP)

    val cardContent = new VBox
    val label = new Label("Total holdings")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val sumCard = new Rectangle
    sumCard.width = 200
    sumCard.height = 150
    sumCard.fill = Color.LightGrey

    val content = new Text
    content.setText("$"+roundedSum.toString)
    content.setTextAlignment(TextAlignment.Center)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val sumNode: scalafx.scene.Node = new scalafx.scene.Group(sumCard, cardContent)

    sumNode

  end makeSumCard

  /** Creates the card that shows the portfolios total grwoth */
  def makeGrowthCard(stockList: Array[(String, Double)], purchaseDates: Array[String]) =

    val priceData = findGrowthValue(stockList, purchaseDates)
    val roundedGrowth = BigDecimal(priceData).setScale(1, BigDecimal.RoundingMode.HALF_UP)
    val sign: String = if priceData > 0 then
      "+"
    else " "
    val boxColor = if priceData > 0 then
      Color.LightGreen
    else if  priceData < 0 then
      Color.Red
    else
      Color.Yellow

    val cardContent = new VBox
    val label = new Label("Growth")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val growthCard = new Rectangle
    growthCard.width = 200
    growthCard.height = 150
    growthCard.fill = boxColor

    val content = new Text
    content.setText(sign + "$" + roundedGrowth.toString)
    content.setTextAlignment(TextAlignment.Center)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val growthNode: scalafx.scene.Node = new scalafx.scene.Group(growthCard, cardContent)

    growthNode

  end makeGrowthCard

  /** Finds the max value, meaning the all-time high of the portfolio */
  def makeMaxCard(stockList: Array[(String, Double)], purchaseDates: Array[String]): scalafx.scene.Node =

    val priceData = StockData().combineAndMultiply(stockList, purchaseDates)
    val maxValue = if priceData.nonEmpty then priceData.map(_._2).max else 0
    val maxValuePrint = BigDecimal(maxValue).setScale(1, BigDecimal.RoundingMode.HALF_UP)

    val cardContent = new VBox
    val label = new Label("All-time high")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val maxCard = new Rectangle
    maxCard.width = 200
    maxCard.height = 150
    maxCard.fill = Color.LightSkyBlue

    val content = new Text
    content.setText("$"+maxValuePrint.toString)
    content.setTextAlignment(TextAlignment.Center)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val maxNode: scalafx.scene.Node = new scalafx.scene.Group(maxCard, cardContent)

    maxNode

  end makeMaxCard

  /**Finds the min value, meaning the all-time low of the portfolio*/
  def makeMinCard(stockList: Array[(String, Double)], purchaseDates: Array[String]): scalafx.scene.Node =

    val priceData = StockData().combineAndMultiply(stockList, purchaseDates)
    val minValue = if priceData.nonEmpty then priceData.map(_._2).min else 0
    val minValuePrint = BigDecimal(minValue).setScale(1, BigDecimal.RoundingMode.HALF_UP)

    val cardContent = new VBox
    val label = new Label("All-time low")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val minCard = new Rectangle
    minCard.width = 200
    minCard.height = 150
    minCard.fill = Color.LightSalmon

    val content = new Text
    content.setText("$" + minValuePrint.toString)
    content.setTextAlignment(TextAlignment.Center)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val minNode: scalafx.scene.Node = new scalafx.scene.Group(minCard, cardContent)

    minNode

  end makeMinCard





















end Card

