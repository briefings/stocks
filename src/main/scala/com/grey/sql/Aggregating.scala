package com.grey.sql

import org.apache.spark.sql.SparkSession

class Aggregating(spark: SparkSession) {

  def aggregating(): Unit = {

    // Programmatic SQL Approach
    println("\n\nCase: SQL")

    // Counts
    spark.sql("SELECT COUNT(*) AS n FROM stocks").show()
    spark.sql("SELECT COUNT(high) AS count_of_nonnull_high FROM stocks WHERE high IS NOT NULL").show()
    spark.sql("SELECT COUNT(high) AS count_of_high FROM stocks").show()
    spark.sql("SELECT COUNT(date) AS count_of_date FROM stocks").show()

    // Sums
    spark.sql("SELECT SUM(volume) AS volume FROM stocks").show()

    spark.sql("SELECT SUM(open) AS open, SUM(high) AS high, SUM(low) AS low, " +
      "SUM(close) AS close,  SUM(volume) AS volume FROM stocks").show()

    // Extrema
    spark.sql("SELECT MIN(volume) as min_volume, MAX(volume) as max_volume FROM stocks").show()

    spark.sql("SELECT MIN(low) as lowest_price FROM stocks").show()

    spark.sql("SELECT MAX(close - open) as highest_single_day_inc FROM stocks").show()

    // Averages
    spark.sql("SELECT AVG(high) as avg_high, AVG(volume) as avg_volume FROM stocks").show()

  }

}
