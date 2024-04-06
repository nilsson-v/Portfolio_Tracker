import Visuals.Table
import scalafx.scene.{Node, Scene, SnapshotParameters, control}
import scalafx.scene.control.{Alert, Button, Menu, MenuBar, MenuItem, ScrollPane, SelectionMode, Slider, SplitPane, Tab, TabPane, TableColumn, TableView, TextField, TextInputDialog, Tooltip, TreeView}
import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.scene.layout.{BorderPane, HBox, StackPane}
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.*
import scalafx.Includes.*
import scalafx.beans.property.{ReadOnlyStringWrapper, StringProperty}
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.{Orientation, Pos}
import scalafx.scene.chart.{BarChart, Chart, PieChart, XYChart}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.image.WritableImage
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import java.io.{BufferedReader, BufferedWriter, File, FileReader, FileWriter}
import javax.imageio.ImageIO
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
    fileMenu.items = List(importFile, exportFile)

    val inspectMenu = new Menu("Inspect Stock")
    val createLinePlot = new MenuItem("Line Plot")
    val createColumnPlot = new MenuItem("Column Plot")
    val createScatterPlot = new MenuItem("Scatter Plot")
    inspectMenu.items = List(createLinePlot, createColumnPlot, createScatterPlot)

    val dashboardMenu = new Menu("Dashboard")
    val addButton = new MenuItem("Add stock")
    val removeButton = new MenuItem("Remove stock")
    dashboardMenu.items = List(addButton, removeButton)

    val controlMenu = new Menu("Control Panel (Dashboard)")
    val hideCards = new MenuItem("Hide Cards")
    val showCards = new MenuItem("Show Cards")
    val lineType = new MenuItem("Line Plot")
    val columnType = new MenuItem("Column Plot")
    val scatterType = new MenuItem("Scatter Plot")
    controlMenu.items = List(lineType, columnType, scatterType, hideCards, showCards)
    controlMenu.setVisible(false)

    menuBar.menus = List(fileMenu, dashboardMenu, inspectMenu, controlMenu)

    rootPane.top = menuBar

    def tooltipsHovering[T <: Chart](stocksPlot: T, pieChart: PieChart) = {
      stocksPlot match
         case xyChart: XYChart[_, _] => {
          xyChart.getData.foreach( series=> {
          series.getData.foreach( d => {
            val pointNode: scalafx.scene.Node = d.getNode
            val pointValue = d.getYValue.toString
            val pointMonth = d.getXValue.toString
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
        }
    }

    def plotHovering[T <: Chart](stocksPlot: T) = {
      stocksPlot match
         case xyChart: XYChart[_, _] => {
          xyChart.getData.foreach( series=> {
          series.getData.foreach( d => {
            val pointNode: scalafx.scene.Node = d.getNode
            val pointValue = d.getYValue.toString
            val pointMonth = d.getXValue.toString
            val roundedValue = BigDecimal(pointValue).setScale(1, BigDecimal.RoundingMode.HALF_UP)
            val tooltip = new Tooltip()
            tooltip.setText(pointMonth + ": " + "$" + roundedValue.toString)
            tooltip.setStyle("-fx-background-color: yellow; " + "-fx-text-fill: black; ")
            Tooltip.install(pointNode, tooltip)
          })})
      }
    }
    /** This section is the most relevant for making the dashboard tab
     * first we create a new TabPane */
    val tab = new TabPane

    /**stockEntries: ArrayBuffer that takes as parameters stock files and their multipliers
     *dateEntries: ArrayBuffer that saves the purchaseDates to create the graph with the correct timeline */
    var stockEntries: ArrayBuffer[(String, Double)] = ArrayBuffer()
    var stockEntries2: ArrayBuffer[(String, Double)] = ArrayBuffer()
    var dateEntries: ArrayBuffer[String] = ArrayBuffer()
    var dateEntries2: ArrayBuffer[String] = ArrayBuffer()

    /** Exports the data to selected folder in CSV format using saveData function*/
    exportFile.onAction = (e: ActionEvent) => {
      val fileChooser = new FileChooser()
      fileChooser.title = "Save data"
      fileChooser.extensionFilters.addAll( new FileChooser.ExtensionFilter("CSV Files", "*.csv"),new FileChooser.ExtensionFilter("All Files", "*.*"))
      val selectedFile = fileChooser.showSaveDialog(stage)
      if selectedFile != null then
        val filePath= selectedFile.getAbsolutePath
        Data.DataReader().saveData(stockEntries, dateEntries,stockEntries2, dateEntries2, filePath)
      else
        println("No file selected.")

    }
    /** Import a CSV file using loadData function.
     * After loading the data the stock- and date entries are cleared and replaced with the imported data.
     * After that the visuals are updated with the new values in the arrays.*/
    importFile.onAction = (e: ActionEvent) => {
      val fileChooser = new FileChooser()
      fileChooser.title = "Import data"
      fileChooser.extensionFilters.addAll(
      new FileChooser.ExtensionFilter("CSV Files", "*.csv"), new FileChooser.ExtensionFilter("All Files", "*.*"))
      val selectedFile = fileChooser.showOpenDialog(stage)
      if selectedFile != null then
        val filePath= selectedFile.getAbsolutePath
        val (importStockEntries, importDateEntries, importStockEntries2, importDateEntries2) = Data.DataReader().loadData(filePath)
        stockEntries.clear()
        stockEntries ++= importStockEntries
        dateEntries.clear()
        dateEntries ++= importDateEntries
        stockEntries2.clear()
        stockEntries2 ++= importStockEntries2
        dateEntries2.clear()
        dateEntries2 ++= importDateEntries2

        val stocksVisualize = stockEntries.toArray
        val stocksVisualize2 = stockEntries2.toArray
        val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize2, dateEntries2.toArray)
        val pieChart = Visuals.Pie().makePie(stocksVisualize)
        val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
        val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
        val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
        val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
        val tableView = Visuals.CreateTable().createTable(stocksVisualize)

        tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
        rootPane.center = tab

        tooltipsHovering(stocksPlot, pieChart)

    }

    lineType.onAction = (e: ActionEvent) => {
        val stocksVisualize = stockEntries.toArray
        val stocksVisualize2 = stockEntries2.toArray
        val stocksPlot = Visuals.LinePlot().makeMultiLineChart(stocksVisualize2, dateEntries2.toArray)
        val pieChart = Visuals.Pie().makePie(stocksVisualize)
        val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
        val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
        val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
        val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
        val tableView = Visuals.CreateTable().createTable(stocksVisualize)

        tab.getTabs.removeIf(tab => tab.getText == "Tracker")
        tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
        rootPane.center = tab

        tooltipsHovering(stocksPlot, pieChart)
    }

     scatterType.onAction = (e: ActionEvent) => {
          val stocksVisualize = stockEntries.toArray
          val stocksVisualize2 = stockEntries2.toArray
          val stocksPlot = Visuals.ScatterPlot().makeMultiScatterPlot(stocksVisualize2, dateEntries2.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
          val tableView = Visuals.CreateTable().createTable(stocksVisualize)

          tab.getTabs.removeIf(tab => tab.getText == "Tracker")
          tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
          rootPane.center = tab

          tooltipsHovering(stocksPlot, pieChart)
     }

      columnType.onAction = (e: ActionEvent) => {
          val stocksVisualize = stockEntries.toArray
          val stocksVisualize2 = stockEntries2.toArray
          val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize2, dateEntries2.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
          val tableView = Visuals.CreateTable().createTable(stocksVisualize)

          tab.getTabs.removeIf(tab => tab.getText == "Tracker")
          tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
          rootPane.center = tab

          tooltipsHovering(stocksPlot, pieChart)
    }

      hideCards.onAction = (e: ActionEvent) => {
          val stocksVisualize = stockEntries.toArray
          val stocksVisualize2 = stockEntries2.toArray
          val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize2, dateEntries2.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          sumCard.setVisible(false)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
          growthCard.setVisible(false)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
          maxCard.setVisible(false)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
          minCard.setVisible(false)
          val tableView = Visuals.CreateTable().createTable(stocksVisualize)

          tab.getTabs.removeIf(tab => tab.getText == "Tracker")
          tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
          rootPane.center = tab

          tooltipsHovering(stocksPlot, pieChart)
      }

       showCards.onAction = (e: ActionEvent) => {
          val stocksVisualize = stockEntries.toArray
          val stocksVisualize2 = stockEntries2.toArray
          val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize2, dateEntries2.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          sumCard.setVisible(true)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
          growthCard.setVisible(true)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
          maxCard.setVisible(true)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
          minCard.setVisible(true)
          val tableView = Visuals.CreateTable().createTable(stocksVisualize)

          tab.getTabs.removeIf(tab => tab.getText == "Tracker")
          tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
          rootPane.center = tab

          tooltipsHovering(stocksPlot, pieChart)
      }


    /** The remove button: this button is responsible for removing stocks.
     * When pressing the button an prompt will appear asking for what stock and how much of it should be removed */
    removeButton.onAction = (e: ActionEvent) => {
      val stockDialog = new TextInputDialog(defaultValue = "Default Value")
      stockDialog.initOwner(stage)
      stockDialog.title = "Stock"
      stockDialog.headerText = "Enter stock"
      stockDialog.contentText = "Stock: "
      val stockResult = stockDialog.showAndWait()

      val amountDialog = new TextInputDialog(defaultValue = "Default Value")
      amountDialog.initOwner(stage)
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
            if !stockEntries.map(_._1).contains(stock) then
              new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Error Dialog"
              headerText = "Input error."
              contentText = "You don't own this stock!"
            }.showAndWait()
              throw new IllegalArgumentException("Stock does not exist")
            if holdingResult < 0 then
              new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Error Dialog"
              headerText = "Input error."
              contentText = "Cannot remove negative value!"
            }.showAndWait()
              throw new IllegalArgumentException("Negative value")
          } catch {
            case _: NumberFormatException => ("Invalid", 0.0) }

            /** finds the corresponding indexes for each Array */
            val index = stockEntries.indexWhere(_._1 == stock)
            val index2 = stockEntries2.indexWhere(_._1 == stock)

            /** if there is more than one of the same stock we make a index list*/
            if (index != -1 && index2 != -1) then
              val indexes = stockEntries2.indices.filter(i => stockEntries2(i)._1 == stock).sorted(Ordering[Int].reverse)
              var remainingHoldings = holdings.toDouble

              /** iterate through the indexes, removing/updating holdings in stockEntries2's by updating last index first of the given stock
               * if the index goes to 0 the corresponding date gets removed and the process continues to the next index. */
              for i <- indexes do
                if remainingHoldings > 0 then
                  val (_, value) = stockEntries2(i)
                  val newValue = value - remainingHoldings
                  if newValue <= 0 then
                    stockEntries2.remove(i)
                    remainingHoldings = Math.abs(newValue)
                    if i < dateEntries2.length then
                      dateEntries2.remove(i)
                  else
                    stockEntries2(i) = (stock, newValue)
                    remainingHoldings = 0

              /** remove/update the index from the stockEntries */
              val (key, value) = stockEntries(index)
              val newValue = value - holdings.toDouble
               if (newValue <= 0) then
                 stockEntries.remove(index)
               else
                  stockEntries(index) = (stock, newValue)

              /** update/remove their corresponding dates */
               if newValue == 0 then
                 if dateEntries.length == 1 then
                   dateEntries.remove(index)
                   dateEntries2 = ArrayBuffer.empty[String]
                 else if dateEntries.nonEmpty then
                   dateEntries.remove(index)

            else println("Stock not found")

    /** The visualisations will also be updated after the array has been updated */
          val stocksVisualize = stockEntries.toArray
          val stocksVisualize2 = stockEntries2.toArray
          val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize2, dateEntries2.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
          val tableView = Visuals.CreateTable().createTable(stocksVisualize)
    /** finally we update the tab and remove the old one */
          if tab.tabs.exists(tab => tab.getText == "Tracker") then
            tab.getTabs.removeIf(tab => tab.getText == "Tracker")
            tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab
          else
            tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab

          tooltipsHovering(stocksPlot, pieChart)

        case _ => println("Dialog cancelled") } }

    /** The add button: responsible for adding stocks to the dashboard.
     * When pressing the addbutton the user will be prompted to input the stock, its amount and purchaseDate*/
    //todo Error handling if stock is not in files!!
    addButton.onAction = (e: ActionEvent) => {
      val stockDialog = new TextInputDialog(defaultValue = "Default Value")
      stockDialog.initOwner(stage)
      stockDialog.title = "Stock"
      stockDialog.headerText = "Enter stock"
      stockDialog.contentText = "Stock: "
      val stockResult = stockDialog.showAndWait()

      val amountDialog = new TextInputDialog(defaultValue = "Default Value")
      amountDialog.initOwner(stage)
      amountDialog.title = "Amount"
      amountDialog.headerText = "Enter amount"
      amountDialog.contentText = "Amount: "
      val amountResult = amountDialog.showAndWait()

      val dateDialog = new TextInputDialog(defaultValue = "Default Value")
      dateDialog.initOwner(stage)
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
            if holdingResult <= 0 then
              new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Error"
              headerText = "Input error."
              contentText = "Cannot input negative amount!"
            }.showAndWait()
              throw new IllegalArgumentException("Amount cannot be negative")
          } catch {
            case _: NumberFormatException => ("Invalid", 0.0, "Invalid")
          }
          val validDateFormat = "\\d{4}-\\d{2}".r
          if validDateFormat.findFirstIn(purchaseDate).isDefined then
            dateEntries2 += purchaseDate
            stockEntries2 += (stock -> holdings.toDouble)
            val existingStockIndex = stockEntries.indexWhere(_._1 == stock)
            if (existingStockIndex != -1) then
             stockEntries(existingStockIndex) = (stock -> (stockEntries(existingStockIndex)._2 + holdings.toDouble))
            else
             stockEntries += (stock -> holdings.toDouble)
             dateEntries += purchaseDate

    /** The visualisations will also be updated after the array has been updated */
          val stocksVisualize = stockEntries.toArray
          val stocksVisualize2 = stockEntries2.toArray
          val stocksPlot = Visuals.ColumnChart().makeMultiColumnChart(stocksVisualize2, dateEntries2.toArray)
          val pieChart = Visuals.Pie().makePie(stocksVisualize)
          val sumCard = Visuals.Card().makeSumCard(stocksVisualize)
          val growthCard = Visuals.Card().makeGrowthCard(stocksVisualize2, dateEntries2.toArray)
          val maxCard = Visuals.Card().makeMaxCard(stocksVisualize2, dateEntries2.toArray)
          val minCard = Visuals.Card().makeMinCard(stocksVisualize2, dateEntries2.toArray)
          val tableView = Visuals.CreateTable().createTable(stocksVisualize)
    /** The tab gets an update and the old tab is removed. */
          if tab.tabs.exists(tab => tab.getText == "Tracker") then
            tab.getTabs.removeIf(tab => tab.getText == "Tracker")
            tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab
          else
            tab += makeTab(tableView, pieChart, sumCard, growthCard, maxCard, minCard, stocksPlot)
            rootPane.center = tab

          tooltipsHovering(stocksPlot, pieChart)
          controlMenu.setVisible(true)

        case _ => println("Dialog cancelled")
        }
     }

    /** The function makeTab is responsible for creating the dashboard that visualizes all the data.
      * It takes as parameters a tableView, a pieChart, the cards and a barchart.
      * This paramaters represent visualisations and will be placed suitably on the dashboard. */
    def makeTab[T <: Chart](tableView: TableView[Table], pieChart: PieChart, sumCard: Node, growthCard: Node, maxCard: Node, minCard: Node, stocksPlot: T): Tab = {
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

    /** In this section are all the action buttons for creating the different plot/chart types.
      * These charts can be created on a separate tab and show the all time progress of a single stock.
      * These are great if the user wants to inspect one specific stock and its progress.*/
    /** The columnn plot **/
    createColumnPlot.onAction = (e: ActionEvent) => {
      val text = new TextInputDialog(defaultValue = "Default Value")
      text.initOwner(stage)
      text.title = "Stock"
      text.headerText = "Input file: "
      text.contentText = "File: "
      val priceResult = text.showAndWait()

      val dateDialog = new TextInputDialog(defaultValue = "Default Value")
      dateDialog.initOwner(stage)
      dateDialog.title = "Start date"
      dateDialog.headerText = "Enter date"
      dateDialog.contentText = "Date (YYYY-MM): "
      val dateResult = dateDialog.showAndWait()

      (priceResult, dateResult) match {
        case (Some(fileName), Some(date)) => {
          val validDateFormat = "\\d{4}-\\d{2}".r
          if validDateFormat.findFirstIn(date).isDefined then
           tab += makeColumnPlot(fileName, date)
           rootPane.center = tab }
        case _ => None
    }
    }

    /** The line plot */
    createLinePlot.onAction = (e: ActionEvent) => {
      val text = new TextInputDialog(defaultValue = "Default Value")
      text.initOwner(stage)
      text.title = "Stock"
      text.headerText = "Input file: "
      text.contentText = "File: "
      val priceResult = text.showAndWait()

      val dateDialog = new TextInputDialog(defaultValue = "Default Value")
      dateDialog.initOwner(stage)
      dateDialog.title = "Start date"
      dateDialog.headerText = "Enter date"
      dateDialog.contentText = "Date (YYYY-MM): "
      val dateResult = dateDialog.showAndWait()

      (priceResult, dateResult) match {
        case (Some(fileName), Some(date)) => {
          val validDateFormat = "\\d{4}-\\d{2}".r
          if validDateFormat.findFirstIn(date).isDefined then
           tab += makeLinePlot(fileName, date)
           rootPane.center = tab }
        case _ => None
       }
    }

    /** The scatter plot */
    createScatterPlot.onAction = (e: ActionEvent) => {
      val text = new TextInputDialog(defaultValue = "Default Value")
      text.initOwner(stage)
      text.title = "Stock"
      text.headerText = "Input file: "
      text.contentText = "File: "
      val priceResult = text.showAndWait()

      val dateDialog = new TextInputDialog(defaultValue = "Default Value")
      dateDialog.initOwner(stage)
      dateDialog.title = "Start date"
      dateDialog.headerText = "Enter date"
      dateDialog.contentText = "Date (YYYY-MM): "
      val dateResult = dateDialog.showAndWait()

      (priceResult, dateResult) match {
        case (Some(fileName), Some(date)) => {
          val validDateFormat = "\\d{4}-\\d{2}".r
          if validDateFormat.findFirstIn(date).isDefined then
           tab += makeScatterPlot(fileName, date)
           rootPane.center = tab }
        case _ => None
    }
  }
    /** In this section are the corresponding functions that create the plots on the action events above */
    /** The column plot */
    def makeColumnPlot(fileName: String, date: String) = {
      val columnPlot = Visuals.ColumnChart().makeColumnChart(fileName, date)
      plotHovering(columnPlot)
      val makeTab = new Tab
      makeTab.text = "Column Chart"
      makeTab.content = columnPlot
      makeTab
    }
    /** The line plot */
     def makeLinePlot(fileName: String, date: String): Tab = {
      val linePlot = Visuals.LinePlot().makeLinePlot(fileName, date)
      plotHovering(linePlot)
      val makeTab = new Tab
      makeTab.text = "Line Chart"
      makeTab.content = linePlot
      makeTab
    }
    /** The scatter plot */
    def makeScatterPlot(fileName: String, date: String): Tab = {
      val scatterPlot = Visuals.ScatterPlot().makeScatterPlot(fileName, date)
      plotHovering(scatterPlot)
      val makeTab = new Tab
      makeTab.text = "Scatter Chart"
      makeTab.content = scatterPlot
      makeTab
    }

  end start

end Main

