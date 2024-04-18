package Visuals


class ScatterChart extends Chart: 

  /** Creates a scatter chart of one stock */
  def makeScatterChart(fileName: String, purchaseDate: String, color: String) =
    makeChart(fileName, purchaseDate, "scatter", color)

  /** Creates a scatter chart of multiple stocks */
  def makeMultiScatterChart(stockList: Array[(String, Double)], purchaseDates: Array[String], color: String) =
    makeMultiChart(stockList, purchaseDates, "scatter", color)
    