package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{CategoryAxis, LineChart, NumberAxis, XYChart}
import scalafx.scene.paint.Color


class LineChart:

  def makeLineChart(fileName: String, purchaseDate: String, color: String) =
    Chart().makeChart(fileName, purchaseDate, "line", color)

  def makeMultiLineChart(stockList: Array[(String, Double)], purchaseDates: Array[String], color: String) =
    Chart().makeMultiChart(stockList, purchaseDates, "line", color)

  def makeTwoSeriesLineChart(stockList: Array[String], purchaseDate: String) =
    Chart().makeTwoSeriesChart(stockList, purchaseDate)
    






