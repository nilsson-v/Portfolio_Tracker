package Visuals

import scalafx.application.Platform
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, FontWeight, Text}

class Card:

  val font = Font.font("Arial", FontWeight.Bold, 20)

  def sum(numbers: Array[Double]) =
    var count = 0.0
    for i <- numbers.indices do
      count = count + numbers(i)
    count

  def getPrices(stockList: Array[(String, Double)]) =
    var priceList = Array[Double]()
    val getStocks: Array[Array[(String, Double)]] = stockList.map((stock, multiplier)
      => Data.StockData().multiplyStocks(stock, multiplier))
    for i <- getStocks.indices do
      priceList ++= Array(getStocks(i).last._2)
    priceList

  def makeSumCard(stockList: Array[(String, Double)]) =
    val priceList = getPrices(stockList)
    val priceSum = sum(priceList)
    val roundedSum = BigDecimal(priceSum).setScale(1, BigDecimal.RoundingMode.HALF_UP)

    val cardContent = new VBox
    val label = new Label("Total holdings")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val sumCard = new Rectangle
    sumCard.width = 250
    sumCard.height = 150
    sumCard.fill = Color.LightGrey

    val content = new Text
    content.setText("$"+roundedSum.toString)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val sumNode: scalafx.scene.Node = new scalafx.scene.Group(sumCard, cardContent)

    sumNode

  end makeSumCard


  def makeGrowthCard(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    val priceData = Data.StockData().pricesFromMonthv3(stockList, purchaseDates)
    val growthAmount = if stockList.nonEmpty then (priceData.last._2 - priceData.head._2) else 0
    val roundedGrowth = BigDecimal(growthAmount).setScale(1, BigDecimal.RoundingMode.HALF_UP)
    val sign: String = if growthAmount > 0 then
      "+"
    else " "
    val boxColor = if growthAmount > 0 then
      Color.LightGreen
    else if  growthAmount < 0 then
      Color.Red
    else
      Color.Yellow

    val cardContent = new VBox
    val label = new Label("Growth")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val growthCard = new Rectangle
    growthCard.width = 250
    growthCard.height = 150
    growthCard.fill = boxColor

    val content = new Text
    content.setText(sign + "$" + roundedGrowth.toString)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val growthNode: scalafx.scene.Node = new scalafx.scene.Group(growthCard, cardContent)

    growthNode

  end makeGrowthCard

  def makeMaxCard(stockList: Array[(String, Double)], purchaseDates: Array[String]): scalafx.scene.Node =

    val priceData = Data.StockData().pricesFromMonthv3(stockList, purchaseDates)
    val maxValue = if priceData.nonEmpty then priceData.map(_._2).max else 0
    val maxValuePrint = BigDecimal(maxValue).setScale(1, BigDecimal.RoundingMode.HALF_UP)

    val cardContent = new VBox
    val label = new Label("All-time high")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val maxCard = new Rectangle
    maxCard.width = 250
    maxCard.height = 150
    maxCard.fill = Color.LightSkyBlue

    val content = new Text
    content.setText("$"+maxValuePrint.toString)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val maxNode: scalafx.scene.Node = new scalafx.scene.Group(maxCard, cardContent)

    maxNode

  end makeMaxCard


  def makeMinCard(stockList: Array[(String, Double)], purchaseDates: Array[String]): scalafx.scene.Node =

    val priceData = Data.StockData().pricesFromMonthv3(stockList, purchaseDates)
    val minValue = if priceData.nonEmpty then priceData.map(_._2).min else 0
    val minValuePrint = BigDecimal(minValue).setScale(1, BigDecimal.RoundingMode.HALF_UP)

    val cardContent = new VBox
    val label = new Label("All-time low")
    cardContent.setAlignment(Pos.Center)
    label.setTextFill(Color.Black)
    label.setFont(font)

    val minCard = new Rectangle
    minCard.width = 250
    minCard.height = 150
    minCard.fill = Color.LightSalmon

    val content = new Text
    content.setText("$" + minValuePrint.toString)
    content.setFill(Color.Black)
    content.setFont(font)

    cardContent.getChildren.addAll(label, content)

    val minNode: scalafx.scene.Node = new scalafx.scene.Group(minCard, cardContent)

    minNode

  end makeMinCard





















end Card

