package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}

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