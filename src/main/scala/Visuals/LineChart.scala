package Visuals



class LineChart extends Chart:

  /** Creates a line chart of one stock */
  def makeLineChart(fileName: String, purchaseDate: String, color: String) =
    makeChart(fileName, purchaseDate, "line", color)

  /** Creates a line chart of multiple stocks */
  def makeMultiLineChart(stockList: Array[(String, Double)], purchaseDates: Array[String], color: String) =
    makeMultiChart(stockList, purchaseDates, "line", color)

  /** Creates a two series line chart of two stocks */
  def makeTwoSeriesLineChart(stockList: Array[String], purchaseDate: String) =
    makeTwoSeriesChart(stockList, purchaseDate)
    






