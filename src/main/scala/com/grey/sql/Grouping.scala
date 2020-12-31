package com.grey.sql

import org.apache.spark.sql.SparkSession

class Grouping(spark: SparkSession) {

  def grouping(): Unit = {

    // Programmatic SQL Approach
    println("\n\nCase: SQL Grouping")

    // Volume per month
    spark.sql("SELECT year, month, SUM(volume) as volume " +
      "FROM stocks GROUP BY year, month ORDER BY year, month").show(11)


  }

}
