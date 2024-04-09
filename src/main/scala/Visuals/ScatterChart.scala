package Visuals
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{CategoryAxis, NumberAxis, ScatterChart, XYChart}

class ScatterChart: 

  def makeScatterChart(fileName: String, purchaseDate: String) =
    Chart().makeChart(fileName, purchaseDate, "scatter")

  def makeMultiScatterChart(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    Chart().makeMultiChart(stockList, purchaseDates, "scatter")