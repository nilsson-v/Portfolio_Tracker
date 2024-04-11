package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.paint.Color


class ColumnChart:
  
  def makeColumnChart(fileName: String, purchaseDate: String, color: String) =
    Chart().makeChart(fileName, purchaseDate, "bar", color)

  def makeMultiColumnChart(stockList: Array[(String, Double)], purchaseDates: Array[String], color: String) =
    Chart().makeMultiChart(stockList, purchaseDates, "bar", color)
