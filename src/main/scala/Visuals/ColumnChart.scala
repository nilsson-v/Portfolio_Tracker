package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.control
import javafx.scene.control.Tooltip
import scalafx.scene.Node


class ColumnChart:
  
  def makeColumnChart(fileName: String) = 
    val priceData = Data.StockData().zipDatesAndPrices(fileName)
    
    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val makeColumnChart = new BarChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))
    makeColumnChart
    
  def makeMultiColumnChart(stockList: Array[(String, Double)], purchaseDate: String) =
    val combinedStocks = Data.StockData().multiplyAndCombine(stockList)
    val priceData = Data.StockData().pricesFromMonth(combinedStocks, purchaseDate)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val makeColumnChart = new BarChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))

    makeColumnChart