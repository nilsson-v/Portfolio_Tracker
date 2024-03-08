package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{CategoryAxis, LineChart, NumberAxis, XYChart}


class LinePlot:

  def makeLinePlot(fileName: String) =
    val priceData = Data.StockData().zipDatesAndPrices(fileName)
    
    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val makeLinePlot = new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))
    makeLinePlot









