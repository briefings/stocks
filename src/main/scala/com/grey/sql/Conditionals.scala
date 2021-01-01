package com.grey.sql

import org.apache.spark.sql.SparkSession

class Conditionals(spark: SparkSession) {

  def conditionals(): Unit = {

    // SQL Approach
    println("\n\nCase: SQL Conditionals >> Case Statements")

    // Simple Case
    spark.sql("SELECT date, open, CASE " +
      "WHEN open > 100 THEN 'excellent' " +
      "WHEN open > 65 AND open <= 100 THEN 'take note' " +
      "WHEN open > 35 AND open <= 65 THEN 'promising' " +
      "ELSE 'infancy' END AS class FROM stocks ORDER BY date").show(11)

  }

}
