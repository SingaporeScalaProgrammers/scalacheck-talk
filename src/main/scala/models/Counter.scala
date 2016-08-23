package models

class Counter {
  private var n = 0
  def inc = n += 1
  def dec = n -= 1
  def get = n
  def reset = n = 0
}