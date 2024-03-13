package Visuals

import scalafx.beans.property.StringProperty


class Table(var Stock: String, var Price: String, var Date: String):
  val stock = new StringProperty(this, "Stock", Stock)
  val price = new StringProperty(this, "Price", Price)
  val date = new StringProperty(this, "Date", Date)
  
end Table








