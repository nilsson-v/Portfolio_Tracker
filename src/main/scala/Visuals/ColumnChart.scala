package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}


class ColumnChart:
  
  def makeColumnChart(fileName: String, purchaseDate: String) =
    Chart().makeChart(fileName, purchaseDate, "bar")

  def makeMultiColumnChart(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    Chart().makeMultiChart(stockList, purchaseDates, "bar")
