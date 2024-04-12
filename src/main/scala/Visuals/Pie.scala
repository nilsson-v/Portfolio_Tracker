package Visuals

import Data.StockData
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.PieChart

class Pie:

  
  def stockPrices(stockAndMultiplier: Array[(String, Double)]) =
    var priceList = Array[Double]()
    val getSymbols = stockAndMultiplier.map((stock, multiplier) => StockData().getSymbol(stock))
    val getStocks: Array[Array[(String, Double)]] = stockAndMultiplier.map((stock, multiplier)
    => StockData().multiplyStocks(stock, multiplier))
    for i <- getStocks.indices do
      priceList ++= Array(getStocks(i).last._2)
    val combined = getSymbols.zip(priceList)
    Data.StockData().listToObservableBuffer(combined).flatten

  def makePie(data: Array[(String, Double)]) =
    val stockData = stockPrices(data)
    val pieChartData = ObservableBuffer[javafx.scene.chart.PieChart.Data]()
      stockData.foreach((stock, value) =>
        pieChartData.add(new javafx.scene.chart.PieChart.Data(stock, value)))

    val makePieChart = new PieChart
    makePieChart.setTitle("Portfolio")
    makePieChart.setData(pieChartData)

    makePieChart










end Pie
