package Visuals
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{CategoryAxis, NumberAxis, ScatterChart, XYChart}
import scalafx.scene.paint.Color

class ScatterChart extends Chart: 

  def makeScatterChart(fileName: String, purchaseDate: String, color: String) =
    makeChart(fileName, purchaseDate, "scatter", color)

  def makeMultiScatterChart(stockList: Array[(String, Double)], purchaseDates: Array[String], color: String) =
    makeMultiChart(stockList, purchaseDates, "scatter", color)
    