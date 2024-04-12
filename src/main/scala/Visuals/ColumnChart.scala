package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.paint.Color


class ColumnChart extends Chart:
  
  def makeColumnChart(fileName: String, purchaseDate: String, color: String) =
    makeChart(fileName, purchaseDate, "bar", color)

  def makeMultiColumnChart(stockList: Array[(String, Double)], purchaseDates: Array[String], color: String) =
    makeMultiChart(stockList, purchaseDates, "bar", color)
