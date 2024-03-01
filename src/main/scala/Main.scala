import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, MenuBar, Menu, MenuItem}
import scalafx.scene.layout.BorderPane
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.*

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
    fileMenu.items = List(importFile, exportFile)

    val createMenu = new Menu("Create")
    val createGraph = new MenuItem("Graph")
    val createTable = new MenuItem("Table")
    createMenu.items = List(createGraph,createTable)

    val viewMenu = new Menu("View")
    menuBar.menus = List(fileMenu, createMenu, viewMenu)

    rootPane.top = menuBar


    //Data.DataReader().fetchAPI()


  end start

end Main

