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

    val testdata = ObservableBuffer(Table("Amount", "1245", "2023"))
    //val linePlotChart = Visuals.Chart().makeLinePlot
    val linePlot = Visuals.LinePlot().makeLinePlot("Apple.json")
    val combinedStocksPlot = Visuals.ColumnChart().makeMultiColumnChart(Array("Apple.json", "Netflix.json"))

    val tableView = new TableView[Table](testdata)
      tableView.prefWidth = 240
      tableView.prefHeight = 300
      val stockCol = new TableColumn[Table, String] {
      text = "Stock Symbol"
      cellValueFactory = _.value.stock
      prefWidth = 100
      }
      val priceCol = new TableColumn[Table, String] {
      text = "Price"
      cellValueFactory = _.value.price
      prefWidth = 50
      }
      val dateCol =  new TableColumn[Table, String] {
      text = "Date"
      cellValueFactory = _.value.date
      prefWidth = 75
      }
      tableView.columns ++= List(stockCol, priceCol, dateCol)

    val tab = new TabPane
    createTab.onAction = (e: ActionEvent) => tab += makeTab()
    rootPane.center = tab


    def makeTab(): Tab = {
      val tabTable = tableView
      val scroller = new ScrollPane
      scroller.content = tableView

      val propertyPane = new ScrollPane
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
      right.dividerPositions = 0.7

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
    prefWidth = 50
    }
    val dateCol =  new TableColumn[Table, String] {
    text = "Date"
    cellValueFactory = _.value.date
    prefWidth = 75
    }
    tableView.columns ++= List(stockCol, priceCol, dateCol)

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

