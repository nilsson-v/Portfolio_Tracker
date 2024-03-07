package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}

class Chart:

  val priceData = Data.Price().closingPrice("NetflixTest.json")

  /**val chartData = XYChart.Series[Number, Number]("Month",
    ObservableBuffer(priceData.map( pd => XYChart.Data[Number, Number])
  val xAxis = NumberAxis()
  val yAxis = NumberAxis()



  val chartMake = new LineChart(xAxis, yAxis, ObservableBuffer(chartData)) */



