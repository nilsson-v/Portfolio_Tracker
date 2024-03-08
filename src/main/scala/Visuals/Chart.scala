package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{CategoryAxis, LineChart, NumberAxis, ScatterChart, XYChart}


class Chart:

  val priceData = Data.StockData().zipDatesAndPrices("Apple.json")

  val chartData = new XYChart.Series[String, Number]
  chartData.setName("Monthly Prices")
  chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))


  val xAxis = CategoryAxis()
  val yAxis = NumberAxis()

  val makeScatter = new LineChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))






