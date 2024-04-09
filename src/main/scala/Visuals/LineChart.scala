package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{CategoryAxis, LineChart, NumberAxis, XYChart}


class LineChart:

  def makeLineChart(fileName: String, purchaseDate: String) =
    Chart().makeChart(fileName, purchaseDate, "line")

  def makeMultiLineChart(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    Chart().makeMultiChart(stockList, purchaseDates, "line")








