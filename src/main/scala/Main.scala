import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem, TableColumn, TableView}
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.*
import scalafx.Includes.*

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

    val viewMenu = new Menu("View")
    menuBar.menus = List(fileMenu, createMenu, viewMenu)

    rootPane.top = menuBar

    //importFile.onAction = Data.DataReader().fetchAPI()
    //exportFile.onAction = ???
    openFile.onAction = (e: ActionEvent) => {
      val fileChooser = new FileChooser
      val selected = fileChooser.showOpenDialog(stage)
    }

    createTable.onAction = (e: ActionEvent) => {

    val table = new TableView[Visuals.Table]
    table.editable = true
    table.prefWidth = 400
    table.prefHeight = 400

    val stockCol = new TableColumn[Visuals.Table, String] {
    text = "Stock Symbol"
    cellValueFactory = _.value.stock
    prefWidth = 100
    }
    val priceCol = new TableColumn[Visuals.Table, String] {
    text = "Price"
    cellValueFactory = _.value.price
    prefWidth = 50
    }
    val dateCol =  new TableColumn[Visuals.Table, String] {
    text = "Date"
    cellValueFactory = _.value.date
    prefWidth = 75
    }

    table.columns ++= List(stockCol, priceCol, dateCol)

    rootPane.center = table

    }



    //Data.DataReader().fetchAPI()




  end start

end Main

