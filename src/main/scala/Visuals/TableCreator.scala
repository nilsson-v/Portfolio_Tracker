package Visuals


import Data.StockData
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}

class TableCreator:
  
  def showSymbol(fileName: String) =
    StockData().getSymbol(fileName)
  def showPrice(fileName: String) =
    StockData().latestPrice(fileName)
  

  def createTable(stockList: Array[(String, Double)]) =
    val symbols = stockList.map{ case(stock, _) => showSymbol(stock)}
    val latestPrices = stockList.map{ case(stock, _) => showPrice(stock)}
    val multipliers = stockList.map{ case(_, multiplier ) => multiplier}
    val tempData = ObservableBuffer[Table]()

    for i <- symbols.indices do
      tempData += Table(symbols(i), latestPrices(i), multipliers(i).toString, (latestPrices(i).toDouble*multipliers(i)).toString)

    val tableView = new TableView[Table](tempData)
        tableView.prefWidth = 300
        tableView.prefHeight = 300
        val stockCol = new TableColumn[Table, String] {
        text = "Stock Symbol"
        cellValueFactory = _.value.stock
        prefWidth = 100
        }
        val priceCol = new TableColumn[Table, String] {
        text = "Price"
        cellValueFactory = _.value.price
        prefWidth = 100
        }
        val dateCol =  new TableColumn[Table, String] {
        text = "Volume"
        cellValueFactory = _.value.date
        prefWidth = 100
        }
        val holdingCol = new TableColumn[Table, String] {
        text = "Holdings"
        cellValueFactory = _.value.holding
        prefWidth = 100
        }
        tableView.columns ++= List(stockCol, priceCol, dateCol, holdingCol)

    tableView

end TableCreator

