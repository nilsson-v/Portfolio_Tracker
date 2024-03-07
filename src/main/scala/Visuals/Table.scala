package Visuals


import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem, ScrollPane, TableColumn, TableView}
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.*
import scalafx.Includes.*
import scalafx.beans.property.StringProperty
import scalafx.scene.shape.Rectangle
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.StringProperty


class Table(var Stock: String, var Price: String, var Date: String):
  val stock = new StringProperty(this, "Stock", Stock)
  val price = new StringProperty(this, "Price", Price)
  val date = new StringProperty(this, "Date", Date)
  
end Table








