import Visuals.Table
import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem, ScrollPane, SelectionMode, Slider, SplitPane, Tab, TabPane, TableColumn, TableView, TextField, TextInputDialog, TreeView}
import scalafx.scene.layout.{BorderPane, StackPane}
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.*
import scalafx.Includes.*
import scalafx.beans.property.{ReadOnlyStringWrapper, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Orientation
import scalafx.scene.control.cell.TextFieldTableCell

import java.io.File
import scala.io.StdIn.readLine

object Main extends JFXApp3:

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Networth tracker"
      width = 800
      height = 600

    val rootPane = new BorderPane

    val scene = Scene(parent = rootPane)
    stage.scene = scene

    /**Menu bar*/

    val menuBar = new MenuBar

    val fileMenu = new Menu("File")
    val importFile = new MenuItem("Import")
    val exportFile = new MenuItem("Export")
    val openFile = new MenuItem("Open")
    fileMenu.items = List(importFile, exportFile, openFile)

    val createMenu = new Menu("Create")
    val createLinePlot = new MenuItem("Line Plot")
    val createColumnPlot = new MenuItem("Column Plot")
    val createScatterPlot = new MenuItem("Scatter Plot")
    val createTable = new MenuItem("Table")
    val createTab = new MenuItem("Tab")
    createMenu.items = List(createLinePlot, createColumnPlot, createScatterPlot ,createTable, createTab)

    val controlMenu = new Menu("Control Panel")
    val hideTable = new MenuItem("Hide Table")

    controlMenu.items = List(hideTable)

    menuBar.menus = List(fileMenu, createMenu, controlMenu)

    rootPane.top = menuBar

    /** Menubar actions */
    //importFile.onAction = Data.DataReader().fetchAPI()
    //exportFile.onAction = ???
    openFile.onAction = (e: ActionEvent) => {
      val fileChooser = new FileChooser
      val selected = fileChooser.showOpenDialog(stage)
    }


    /** Tab Pane */
    def showSymbol(fileName: String) =
      Data.StockData().getSymbol(fileName)
    def showPrice(fileName: String) =
      Data.StockData().latestPrice(fileName)

    val testdata = ObservableBuffer(Table(showSymbol("Apple.json"), showPrice("Apple.json"), 10.0.toString, (showPrice("Apple.json").toDouble*10.0).toString),
      Table(showSymbol("Netflix.json"), showPrice("Netflix.json"), 5.0.toString, (showPrice("Netflix.json").toDouble*5.0).toString))
    //val linePlotChart = Visuals.Chart().makeLinePlot
    val linePlot = Visuals.LinePlot().makeLinePlot("Apple.json")
    val combinedStocksPlot = Visuals.ColumnChart().makeMultiColumnChart(Array("Apple.json", "Netflix.json"))
    val pieChart = Visuals.Pie().makePie(Array(("Apple.json", 10.0), ("Netflix.json", 5.0)))

    val tableView = new TableView[Table](testdata)
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
      prefWidth = 75
      }
      val dateCol =  new TableColumn[Table, String] {
      text = "Volume"
      cellValueFactory = _.value.date
      prefWidth = 50
      }
      val holdingCol = new TableColumn[Table, String] {
      text = "Holdings"
      cellValueFactory = _.value.holding
      prefWidth = 75
      }
      tableView.columns ++= List(stockCol, priceCol, dateCol, holdingCol)

    val tab = new TabPane
    createTab.onAction = (e: ActionEvent) => tab += makeTab()
    rootPane.center = tab


    def makeTab(): Tab = {
      val tabTable = tableView
      val scroller = new ScrollPane
      scroller.content = tableView

      val propertyPane = new ScrollPane
      propertyPane.content = pieChart
      val left = new SplitPane
      left.orientation = Orientation.Vertical
      left.items ++= List(propertyPane, scroller)

      val topBorder = new BorderPane
      //content = cards
      val bottomBorder = new ScrollPane
      bottomBorder.content = combinedStocksPlot

      val slider = new Slider(0, 800, 0)
      topBorder.top = slider
      val right = new SplitPane
      right.orientation = Orientation.Vertical
      right.items ++= List(topBorder, bottomBorder)
      right.dividerPositions = 0.3

      val top = new SplitPane
      top.items ++= List(left, right)
      top.dividerPositions = 0.3

      val makeTab = new Tab
      makeTab.text = "Untitled"
      makeTab.content = top
      makeTab
    }
    /** Tables */

    /**val data = ObservableBuffer(Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json").head.toString, "2024-01-02"),
    Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json")(1).toString, "2024-01-03"),
    Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json")(2).toString, "2024-01-04"),
    Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json")(3).toString, "2024-01-05")) */

    createTable.onAction = (e: ActionEvent) => tab += makeTableTab()

    def makeTableTab(): Tab = {
    val tableView = new TableView[Table](testdata)
    val stockCol = new TableColumn[Table, String] {
    text = "Stock Symbol"
    cellValueFactory = _.value.stock
    prefWidth = 100
    }
    val priceCol = new TableColumn[Table, String] {
    text = "Price"
    cellValueFactory = _.value.price
    prefWidth = 75
    }
    val amountCol =  new TableColumn[Table, String] {
    text = "Amount"
    cellValueFactory = _.value.date
    prefWidth = 50
    }
    val holdingCol = new TableColumn[Table, String] {
    text = "Holding"
    cellValueFactory = _.value.holding
    prefWidth = 75
    }
    tableView.columns ++= List(stockCol, priceCol, dateCol, holdingCol)

    val makeTab = new Tab
    makeTab.text = "Table"
    makeTab.content = tableView
    makeTab
    }

     createColumnPlot.onAction = (e: ActionEvent) => {
       val text = new TextInputDialog(defaultValue = "Default Value")
       text.title = "Stock"
       text.headerText = "Input file: "
       text.contentText = "File: "

       val result = text.showAndWait()
       result match {
         case Some(fileName) => tab += makeColumnPlot(fileName)
         case _ => None
       }
     }

     createLinePlot.onAction = (e: ActionEvent) => {
       val text = new TextInputDialog(defaultValue = "Default Value")
       text.title = "Stock"
       text.headerText = "Input file: "
       text.contentText = "File: "

       val result = text.showAndWait()
       result match {
         case Some(fileName) => tab += makeLinePlot(fileName)
         case _ => None
       }
     }

     createScatterPlot.onAction = (e: ActionEvent) => {
       val text = new TextInputDialog(defaultValue = "Default Value")
       text.title = "Stock"
       text.headerText = "Input file: "
       text.contentText = "File: "

       val result = text.showAndWait()
       result match {
         case Some(fileName) => tab += makeScatterPlot(fileName)
         case _ => None
       }
     }

    def makeColumnPlot(fileName: String): Tab = {
      val columnPlot = Visuals.ColumnChart().makeColumnChart(fileName)
      val makeTab = new Tab
      makeTab.text = "Column Chart"
      makeTab.content = columnPlot
      makeTab
    }

     def makeLinePlot(fileName: String): Tab = {
      val linePlot = Visuals.LinePlot().makeLinePlot(fileName)
      val makeTab = new Tab
      makeTab.text = "Line Chart"
      makeTab.content = linePlot
      makeTab
    }

    def makeScatterPlot(fileName: String): Tab = {
      val scatterPlot = Visuals.ScatterPlot().makeScatterPlot(fileName)
      val makeTab = new Tab
      makeTab.text = "Scatter Chart"
      makeTab.content = scatterPlot
      makeTab
    }


    //hideTable.onAction = (e: ActionEvent) => tableView.visible = false

    //Data.DataReader().fetchAPI()




  end start

end Main

