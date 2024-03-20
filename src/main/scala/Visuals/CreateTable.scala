package Visuals

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}

class CreateTable:
  
  def showSymbol(fileName: String) =
    Data.StockData().getSymbol(fileName)
  def showPrice(fileName: String) =
    Data.StockData().latestPrice(fileName)
  
  val testdata = ObservableBuffer(Table(showSymbol("Apple.json"), showPrice("Apple.json"), 10.0.toString, (showPrice("Apple.json").toDouble*10.0).toString),
      Table(showSymbol("Netflix.json"), showPrice("Netflix.json"), 5.0.toString, (showPrice("Netflix.json").toDouble*5.0).toString))
  val tableView = new TableView[Table](testdata)
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

