package com.grey.sql

import org.apache.spark.sql.SparkSession

class Grouping(spark: SparkSession) {

  def grouping(): Unit = {

    // Programmatic SQL Approach
    println("\n\nCase: SQL Grouping")

    // Volume per month
    spark.sql("SELECT year, month, SUM(volume) as volume " +
      "FROM stocks GROUP BY year, month ORDER BY year, month").show(5)

    // Average of the daily delta values per year
    spark.sql("SELECT year, AVG(close - open) as avg_daily_delta " +
      "FROM stocks GROUP BY year ORDER BY year").show(5)

    // Extrema of prices per month
    spark.sql("SELECT year, month, MIN(low) as min_price, MAX(high) as max_price " +
      "FROM stocks GROUP BY year, month ORDER BY year, month").show(5)

    spark.sql("SELECT year, month, MIN(low) as min_price, MAX(high) as max_price " +
      "FROM stocks GROUP BY year, month HAVING MAX(high) > 100 ORDER BY year, month").show(5)

  }

}
