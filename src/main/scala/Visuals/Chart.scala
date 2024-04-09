package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, LineChart, NumberAxis, ScatterChart, XYChart}

class Chart:


  def makeChart[T <: XYChart[String, Number]](fileName: String, purchaseDate: String, chartType: String): XYChart[String, Number]  =
    val priceData = Data.StockData().zipDatesAndPrices(fileName)
    val priceDataM = Data.StockData().pricesFromMonth(priceData, purchaseDate)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceDataM.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val chart = chartType.toLowerCase match {
      case "bar" => new BarChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
      case "line" => new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
      case "scatter" => new ScatterChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
    }

    chart

  def makeMultiChart[T <: XYChart[String, Number]](stockList: Array[(String, Double)], purchaseDates: Array[String], chartType: String): XYChart[String, Number] =
    val priceData = Data.StockData().combineAndMultiply(stockList, purchaseDates)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val chart = chartType.toLowerCase match {
      case "bar" => new BarChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
      case "line" => new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
      case "scatter" => new ScatterChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData)).asInstanceOf[T]
    }

    chart



