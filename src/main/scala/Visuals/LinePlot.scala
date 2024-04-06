package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, LineChart, NumberAxis, XYChart}


class LinePlot:

  def makeLinePlot(fileName: String, purchaseDate: String) =
    val priceData = Data.StockData().zipDatesAndPrices(fileName)
    val priceDataM = Data.StockData().pricesFromMonth(priceData, purchaseDate)
    
    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceDataM.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val makeLinePlot = new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))
    makeLinePlot
    
    
  def makeMultiLineChart(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    val priceData = Data.StockData().pricesFromMonthv3(stockList, purchaseDates)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val makeLineChart = new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))

    makeLineChart









