package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, LineChart, NumberAxis, ScatterChart, XYChart}

class Chart:

  def makeChart[T <: XYChart[String, Number]](fileName: String, purchaseDate: String, chartType: String, color: String): XYChart[String, Number]  =
    val priceData = Data.StockData().zipDatesAndPrices(fileName)
    val priceDataM = Data.StockData().pricesFromMonth(priceData, purchaseDate)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceDataM.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val chart = chartType.toLowerCase match {
      case "bar" =>
         val barChart = new BarChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
         chartData.getData.forEach( data =>
           val dataNode = data.getNode
           dataNode.setStyle("-fx-bar-fill: " + color + "; "))
         barChart
      case "line" =>
         val lineChart = new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
         chartData.getData.forEach( data =>
           val dataNode = data.getNode
           dataNode.setStyle("-fx-background-color: " + color + "; "))
         lineChart
      case "scatter" =>
        val scatterChart = new ScatterChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
         chartData.getData.forEach( data =>
           val dataNode = data.getNode
           dataNode.setStyle("-fx-background-color: " + color + "; "))
        scatterChart
    }

    chart

  def makeMultiChart[T <: XYChart[String, Number]](stockList: Array[(String, Double)], purchaseDates: Array[String], chartType: String, color: String): XYChart[String, Number] =
    val priceData = Data.StockData().combineAndMultiply(stockList, purchaseDates)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val chart = chartType.toLowerCase match {
      case "bar" =>
         val barChart = new BarChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
         chartData.getData.forEach( data =>
           val dataNode = data.getNode
           dataNode.setStyle("-fx-bar-fill: " + color + "; "))
         barChart
      case "line" =>
         val lineChart = new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
         chartData.getData.forEach( data =>
           val dataNode = data.getNode
           dataNode.setStyle("-fx-background-color: " + color + "; "))
         lineChart
      case "scatter" =>
        val scatterChart = new ScatterChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
         chartData.getData.forEach( data =>
           val dataNode = data.getNode
           dataNode.setStyle("-fx-background-color: " + color + "; "))
        scatterChart
    }
    chart


  def makeTwoSeriesChart(stockList: Array[String], purchaseDate: String): XYChart[String, Number] = {
    val priceData1 = Data.StockData().zipDatesAndPrices(stockList(0))
    val priceData2 = Data.StockData().zipDatesAndPrices(stockList(1))

    val priceDataM1 = Data.StockData().pricesFromMonth(priceData1, purchaseDate)
    val priceDataM2 = Data.StockData().pricesFromMonth(priceData2, purchaseDate)

    val chartData1 = new XYChart.Series[String, Number]
    chartData1.setName(stockList(0))
    chartData1.data = priceDataM1.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val chartData2 = new XYChart.Series[String, Number]
    chartData2.setName(stockList(1))
    chartData2.data = priceDataM2.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData1, chartData2))
  }
    

  




