package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}

class CreateTable:
  
  def showSymbol(fileName: String) =
    Data.StockData().getSymbol(fileName)
  def showPrice(fileName: String) =
    Data.StockData().latestPrice(fileName)
  
  /**val testdata = ObservableBuffer(Table(showSymbol("AAPL"), showPrice("AAPL"), 10.0.toString, (showPrice("AAPL").toDouble*10.0).toString),
      Table(showSymbol("NFLX"), showPrice("NFLX"), 5.0.toString, (showPrice("NFLX").toDouble*5.0).toString))*/
  
  val tempData = ObservableBuffer(Table("AAPL", 200.0.toString, 10.0.toString, 2000.0.toString),
      Table("NFLX", 620.5.toString, 5.0.toString, 3200.toString))
  val tableView = new TableView[Table](tempData)
      tableView.prefWidth = 300
      tableView.prefHeight = 300
      val stockCol = new TableColumn[Table, String] {
      text = "Stock Symbol"
      cellValueFactory = _.value.stock
      prefWidth = 10
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

end CreateTable

