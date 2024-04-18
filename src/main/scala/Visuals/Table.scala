package Visuals

import scalafx.beans.property.StringProperty

/** custom class table that creates a tableView with the correct labels */
class Table(var Stock: String, var Price: String, var Amount: String, var Holdings: String):
 
  val stock = new StringProperty(this, "Stock", Stock)
  val price = new StringProperty(this, "Price", Price)
  val date = new StringProperty(this, "Date", Amount)
  val holding = new StringProperty(this, "Holdings", Holdings)
  
 
end Table








