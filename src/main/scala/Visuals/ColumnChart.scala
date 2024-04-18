package Visuals




class ColumnChart extends Chart:
  
  /** Creates a column chart of one stock */
  def makeColumnChart(fileName: String, purchaseDate: String, color: String) =
    makeChart(fileName, purchaseDate, "bar", color)

  /** Creates a column chart of multiple stocks */
  def makeMultiColumnChart(stockList: Array[(String, Double)], purchaseDates: Array[String], color: String) =
    makeMultiChart(stockList, purchaseDates, "bar", color)
