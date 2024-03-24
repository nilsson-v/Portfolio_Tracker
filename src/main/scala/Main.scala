import Visuals.Table
import scalafx.scene.control
import scalafx.scene.control.Tooltip
import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem, ScrollPane, SelectionMode, Slider, SplitPane, Tab, TabPane, TableColumn, TableView, TextField, TextInputDialog, TreeView}
import scalafx.scene.layout.{BorderPane, HBox, StackPane}
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.*
import scalafx.Includes.*
import scalafx.beans.property.{ReadOnlyStringWrapper, StringProperty}
import scalafx.geometry.{Orientation, Pos}
import scalafx.scene.chart.{BarChart, PieChart}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scala.jdk.CollectionConverters

import scala.collection.mutable.ArrayBuffer


object Main extends JFXApp3:

  def start() =

    /** Creates the main stage */
    stage = new JFXApp3.PrimaryStage:
      title = "Networth tracker"
      width = 800
      height = 600

    val rootPane = new BorderPane

    val scene = Scene(parent = rootPane)
    stage.scene = scene

    /**This section contains the menu bar with all the other options
     * menubar consists of: fileMenu, createMenu, controlMenu and ChartMenu*/

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
    val addButton = new MenuItem("Add stock")
    val removeButton = new MenuItem("Remove stock")
    controlMenu.items = List(addButton, removeButton)

    val chartMenu = new Menu("Chart type")
    val lineType = new MenuItem("Line Plot")
    val columnType = new MenuItem("Column Plot")
    val scatterType = new MenuItem("Scatter Plot")
    chartMenu.items = List(columnType, lineType, scatterType)

    menuBar.menus = List(fileMenu, createMenu, controlMenu, chartMenu)

    rootPane.top = menuBar

    /** Menubar actions */
    openFile.onAction = (e: ActionEvent) => {
      val fileChooser = new FileChooser
      val selected = fileChooser.showOpenDialog(stage)
    }

    /** This section is the most relevant for making the dashboard tab
     * first we create a new TabPane */
    val tab = new TabPane

    /**stockEntries: ArrayBuffer that takes as parameters stock files and their multipliers
     *dateEntries: ArrayBuffer that saves the purchaseDates to create the graph with the correct timeline */
    var stockEntries: ArrayBuffer[(String, Double)] = ArrayBuffer()
    val dateEntries: ArrayBuffer[String] = ArrayBuffer()

    /** The remove button: this button is responsible for removing stocks.
     * When pressing the button an prompt will appear asking for what stock and how much of it should be removed */
    removeButton.onAction = (e: ActionEvent) => {
      val stockDialog = new TextInputDialog(defaultValue = "Default Value")
      stockDialog.title = "Stock"
      stockDialog.headerText = "Enter stock"
      stockDialog.contentText = "Stock: "
      val stockResult = stockDialog.showAndWait()

      val amountDialog = new TextInputDialog(defaultValue = "Default Value")
      amountDialog.title = "Amount"
      amountDialog.headerText = "Enter amount"
      amountDialog.contentText = "Amount: "
      val amountResult = amountDialog.showAndWait()

    /** The input will be matched and handled accordingly
      * The stock input will be used to check where the stock is in the array.
      * When the stocks position has been found in the array, the desired amount will be reduced from that stocks current amount
      * Lastly the stockEntries will be update with the new value*/
      (stockResult, amountResult) match {
        case (Some(stock), Some(holdings)) =>
          try {
            val stockValue = stock
            val holdingResult = holdings.toDouble
          } catch {
            case _: NumberFormatException => ("Invalid", 0.0) }
            val index = stockEntries.indexWhere(_._1 == stock)
            if (index != -1) then
              val (key, value) = stockEntries(index)
              val newValue = value - holdings.toDouble
              if newValue == 0 then
                stockEntries = stockEntries.filter((key, _) => key != stock)
                if dateEntries.length > 1 then
                  dateEntries.remove(index)
              else
                stockEntries(index) = (key, newValue)
            else
              println("Stock not found")
    /** The visualisations will also be updated after the array has been updated */
          val stocksVisualize = stockEntries.toArray
          val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize, dateEntries.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize, dateEntries.toArray)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize, dateEntries.toArray)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize, dateEntries.toArray)
          val tableView = Visuals.CreateTable().tableView
    /** finally we update the tab and remove the old one */
          if tab.tabs.exists(tab => tab.getText == "Tracker") then
            tab.getTabs.removeIf(tab => tab.getText == "Tracker")
            tab += makeTab(stock, tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab
          else
            tab += makeTab(stock, tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab

        case _ => println("Dialog cancelled") } }

    /** The add button: responsible for adding stocks to the dashboard.
     * When pressing the addbutton the user will be prompted to input the stock, its amount and purchaseDate*/
    //todo Error handling if stock is not in files!!
    addButton.onAction = (e: ActionEvent) => {
      val stockDialog = new TextInputDialog(defaultValue = "Default Value")
      stockDialog.title = "Stock"
      stockDialog.headerText = "Enter stock"
      stockDialog.contentText = "Stock: "
      val stockResult = stockDialog.showAndWait()

      val amountDialog = new TextInputDialog(defaultValue = "Default Value")
      amountDialog.title = "Amount"
      amountDialog.headerText = "Enter amount"
      amountDialog.contentText = "Amount: "
      val amountResult = amountDialog.showAndWait()

      val dateDialog = new TextInputDialog(defaultValue = "Default Value")
      dateDialog.title = "Purchase Date"
      dateDialog.headerText = "Enter purchase date"
      dateDialog.contentText = "Purchase Date (YYYY-MM): "
      val dateResult = dateDialog.showAndWait()
    /** The stock, amount and date will be matched.
      * the amount will be checked if it can be converted to a double.
      * The date will be checked if it is in the right format and added to the date entries array.
      * If theese two pass the stock will be checked if it is a new one or an existing one.
      * If it already exist, only the amount will be added to the existing part in the stocks array.
      * If it is a new one the whole package will be added to the stocks array. */
      (stockResult, amountResult, dateResult) match {
        case (Some(stock), Some(holdings), Some(purchaseDate)) =>
          try {
            val stockValue = stock
            val holdingResult = holdings.toDouble
            val date = purchaseDate
          } catch {
            case _: NumberFormatException => ("Invalid", 0.0, "Invalid")
          }
          val validDateFormat = "\\d{4}-\\d{2}".r
          if validDateFormat.findFirstIn(purchaseDate).isDefined then
            dateEntries += purchaseDate
            val existingStockIndex = stockEntries.indexWhere(_._1 == stock)
            if (existingStockIndex != -1) then
             stockEntries(existingStockIndex) = (stock -> (stockEntries(existingStockIndex)._2 + holdings.toDouble))
            else
             stockEntries += (stock -> holdings.toDouble)
    /** The visualisations will also be updated after the array has been updated */
          val stocksVisualize = stockEntries.toArray
          val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize, dateEntries.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize, dateEntries.toArray)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize, dateEntries.toArray)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize, dateEntries.toArray)
          val tableView = Visuals.CreateTable().tableView
    /** The tab gets an update and the old tab is removed. */
          if tab.tabs.exists(tab => tab.getText == "Tracker") then
            tab.getTabs.removeIf(tab => tab.getText == "Tracker")
            tab += makeTab(stock, tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab
          else
            tab += makeTab(stock, tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab

          stocksPlot.getData.foreach( series=> {
            series.getData.foreach( d => {
              val pointNode: scalafx.scene.Node = d.getNode
              val pointValue = d.getYValue.toString
              val pointMonth = d.getXValue
              val roundedValue = BigDecimal(pointValue).setScale(1, BigDecimal.RoundingMode.HALF_UP)
              val tooltip = new Tooltip()
              tooltip.setText(pointMonth + ": " + "$" + roundedValue.toString)
              tooltip.setStyle("-fx-background-color: yellow; " + "-fx-text-fill: black; ")
              Tooltip.install(pointNode, tooltip)
            })})

          val total = pieChart.getData.foldLeft(0.0) {(x, y) => x + y.getPieValue}

          pieChart.getData.foreach( d => {
            val sliceNode: scalafx.scene.Node = d.getNode
            val pieValue = d.getPieValue
            val percent = (pieValue / total) * 100
            val msg = "%s: %.2f (%.2f%%)".format(d.getName, pieValue, percent)
            val tt = new Tooltip()
            tt.setText(msg)
            tt.setStyle("-fx-background-color: yellow; " +  "-fx-text-fill: black; ")
            Tooltip.install(sliceNode, tt) })

        case _ => println("Dialog cancelled")
        }
     }

    /** The function makeTab is responsible for creating the dashboard that visualizes all the data.
      * It takes as parameters the stock, a tableView, a pieChart, the cards and a barchart.
      * This paramaters represent visualisations and will be placed suitably on the dashboard. */
    def makeTab(stock: String, tableView: TableView[Table], pieChart: PieChart, sumCard: Node, growthCard: Node, maxCard: Node, minCard: Node, stocksPlot: BarChart[String, Number]): Tab = {
      val tabTable = tableView
      val leftDown = new ScrollPane
      /** Left down tiles contains the table */
      leftDown.content = tableView
      tableView.prefWidth = 600
      tabTable.prefHeight = 500
      /** Left up tile contains the pie  */
      val leftUp = new ScrollPane
      leftUp.content = pieChart
      val left = new SplitPane
      left.orientation = Orientation.Vertical
      left.items ++= List(leftUp, leftDown)
      /** Right up tile contains all the cards with different calculations */
      val rightUp = new HBox
      rightUp.children.addAll(sumCard, growthCard, maxCard, minCard)
      /** Right down contains the barchart visualising the stocks growth */
      val rightDown = new ScrollPane
      rightDown.content = stocksPlot
      stocksPlot.prefHeight = 550
      stocksPlot.prefWidth = 750
      /** The following variables create the 2-by-2 tile format and the tab where the visuals are shown */
      val slider = new Slider(0, 800, 0)
      val right = new SplitPane
      right.orientation = Orientation.Vertical
      right.items ++= List(rightUp,rightDown)
      right.dividerPositions = 0.15
      val top = new SplitPane
      top.items ++= List(left, right)
      top.dividerPositions = 0.33
      val makeTab = new Tab
      makeTab.text = "Tracker"
      makeTab.content = top
      makeTab
    }

    createTable.onAction = (e: ActionEvent) => tab += makeTableTab()

    def makeTableTab(): Tab = {
      val tableView2 = Visuals.CreateTable().tableView
      val makeTab = new Tab
      makeTab.text = "Table"
      makeTab.content = tableView2
      makeTab
      }

    /** In this section are all the action buttons for creating the different plot/chart types.
      * These charts can be created on a separate tab and show the all time progress of a single stock.
      * These are great if the user wants to inspect one specific stock and its progress.*/
    /** The columnn plot **/
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
    /** The line plot */
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
    /** The scatter plot */
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

    /** In this section are the corresponding functions that create the plots on the action events above */
    /** The column plot */
    def makeColumnPlot(fileName: String): Tab = {
      val columnPlot = Visuals.ColumnChart().makeColumnChart(fileName)
      val makeTab = new Tab
      makeTab.text = "Column Chart"
      makeTab.content = columnPlot
      makeTab
    }
    /** The line plot */
     def makeLinePlot(fileName: String): Tab = {
      val linePlot = Visuals.LinePlot().makeLinePlot(fileName)
      val makeTab = new Tab
      makeTab.text = "Line Chart"
      makeTab.content = linePlot
      makeTab
    }
    /** The scatter plot */
    def makeScatterPlot(fileName: String): Tab = {
      val scatterPlot = Visuals.ScatterPlot().makeScatterPlot(fileName)
      val makeTab = new Tab
      makeTab.text = "Scatter Chart"
      makeTab.content = scatterPlot
      makeTab
    }

  end start

end Main

