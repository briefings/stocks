package com.grey.metadata

import java.sql.Date

object CaseClassOf {

  case class Stocks(date: Date, open: Float, high: Float, low: Float, close: Float, volume: Long)

}
