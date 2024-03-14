package Visuals

class Calculations:

  def average(numbers: Array[Double]) =
    var count = 0.0
    for i <- numbers.indices do
      count = count + numbers(i)
    count / numbers.length

  def sum(numbers: Array[Double]) =
    var count = 0.0
    for i <- numbers.indices do
      count = count + numbers(i)
    count






end Calculations

