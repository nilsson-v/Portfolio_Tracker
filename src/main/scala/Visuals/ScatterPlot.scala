package Visuals
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{CategoryAxis, LineChart, NumberAxis, ScatterChart, XYChart}

class ScatterPlot: 

  
  def makeScatterPlot(fileName: String) =
    val priceData = Data.StockData().zipDatesAndPrices(fileName)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()
  
    val makeScatterPlot = new ScatterChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))
    makeScatterPlot

  def makeMultiScatterPlot(stockList: Array[(String, Double)], purchaseDates: Array[String]) =
    val priceData = Data.StockData().pricesFromMonthv3(stockList, purchaseDates)

    val chartData = new XYChart.Series[String, Number]
    chartData.setName("Monthly Prices")
    chartData.data = priceData.map(pd => XYChart.Data[String, Number](pd._1, pd._2))

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis()

    val makeScatterChart = new ScatterChart[String, Number](xAxis, yAxis, ObservableBuffer(chartData))

    makeScatterChart