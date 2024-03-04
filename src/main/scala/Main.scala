import Visuals.Table
import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem, SelectionMode, TableColumn, TableView, Button}
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.*
import scalafx.Includes.*
import scalafx.beans.property.{ReadOnlyStringWrapper, StringProperty}
import scalafx.collections.ObservableBuffer

object Main extends JFXApp3:

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Financial tracker"
      width = 800
      height = 600

    val rootPane = new BorderPane

    val scene = Scene(parent = rootPane)
    stage.scene = scene

    //Menu bar

    val menuBar = new MenuBar

    val fileMenu = new Menu("File")
    val importFile = new MenuItem("Import")
    val exportFile = new MenuItem("Export")
    val openFile = new MenuItem("Open")
    fileMenu.items = List(importFile, exportFile, openFile)

    val createMenu = new Menu("Create")
    val createGraph = new MenuItem("Graph")
    val createTable = new MenuItem("Table")
    createMenu.items = List(createGraph,createTable)

    val controlMenu = new Menu("Control Panel")
    val hideTable = new MenuItem("Hide Table")
    controlMenu.items = List(hideTable)

    menuBar.menus = List(fileMenu, createMenu, controlMenu)

    rootPane.top = menuBar

    //importFile.onAction = Data.DataReader().fetchAPI()
    //exportFile.onAction = ???
    openFile.onAction = (e: ActionEvent) => {
      val fileChooser = new FileChooser
      val selected = fileChooser.showOpenDialog(stage)
    }

    /** The code that controls the tables are located under this text */

    val data = ObservableBuffer(Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json").head.toString, "2024-01-02"),
    Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json")(1).toString, "2024-01-03"),
    Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json")(2).toString, "2024-01-04"),
    Table(Data.Label().labelName("NetflixTest.json"), Data.Price().closingPrice("NetflixTest.json")(3).toString, "2024-01-05"))

    createTable.onAction = (e: ActionEvent) => {

    val table = new TableView[Table](data)
    table.editable = true
    table.prefWidth = 400
    table.prefHeight = 400

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

    hideTable.onAction = (e: ActionEvent) => table.visible = false

    table.columns ++= List(stockCol, priceCol, dateCol)

    table.refresh()

    table.selectionModel().setSelectionMode(SelectionMode.Multiple)

    rootPane.center = table


    }



    //Data.DataReader().fetchAPI()




  end start

end Main

