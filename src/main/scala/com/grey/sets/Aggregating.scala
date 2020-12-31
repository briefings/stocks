package com.grey.sets

import com.grey.metadata.CaseClassOf.Stocks
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.{count, isnull, sum, min, max, avg}

class Aggregating(spark: SparkSession) {

  def aggregating(stocks: Dataset[Stocks]): Unit = {

    // Dataset Approach
    println("\n\nCase: Dataset Aggregating")

    // Numeric Fields
    // val numericFields: Array[Column] = stocks.columns
    //    .filterNot(field => field == "date" | field == "year" | field == "month").map(field => col(field))
    val numericFields: Array[String] = stocks.columns
      .filterNot(field => field == "date" | field == "year" | field == "month" )

    // Implicits
    import spark.implicits._

    // Counts
    stocks.select(count("*").as("n")).show()
    stocks.filter(!isnull($"high")).select(count($"high").as("count_of_nonnull_high")).show()
    stocks.select(count($"high").as("count_of_high")).show()
    stocks.select(count($"date").as("count_of_date")).show()
    stocks.summary("count").show()

    // Sums
    val totals = numericFields.map(field => sum(field).as(field))
    stocks.agg(totals.head, totals.tail: _*).show()

    // Extrema
    stocks.select(min($"volume").as("min_volume"), max($"volume").as("max_volume")).show()
    stocks.agg(min($"low").as("lowest_price")).show()
    stocks.select(max($"close" - $"open").as("highest_single_day_inc")).show()

    // Averages
    val averages = numericFields.map(field => avg(field).as("avg_" + field))
    stocks.agg(averages.head, averages.tail: _*).show()

  }

}
