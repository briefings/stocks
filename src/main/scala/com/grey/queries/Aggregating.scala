package com.grey.queries

import com.grey.sources.CaseClassOf.Stocks
import org.apache.spark.sql.functions.{count, max, min, sum, avg}
import org.apache.spark.sql.{Dataset, SparkSession}

class Aggregating(spark: SparkSession) {

  private val prices: Array[String] = Array("open", "high", "low", "close")

  def aggregating(stocks: Dataset[Stocks]): Unit = {

    println("\n\nAggregating")


    /**
      * Import implicits for
      *   encoding (https://jaceklaskowski.gitbooks.io/mastering-apache-spark/spark-sql-Encoder.html)
      *   implicit conversions, e.g., converting a RDD to a DataFrames.
      *   access to the "$" notation.
      */
    import spark.implicits._


    /**
      * Counts
      */
    val tally: Long = spark.sql("SELECT COUNT(*) AS n FROM stocks").head().getAs[Long]("n")
    val tallySet: Long = stocks.count()
    val tallySetAlt: Long = stocks.select(count("*").as("n")).head().getAs[Long]("n")
    println(s"\nThe # of Apple stock records\nsql: $tally, dataset: $tallySet, $tallySetAlt")

    val highNotNull: Long = spark.sql("SELECT COUNT(high) AS n FROM stocks WHERE high IS NOT NULL")
      .head().getAs[Long]("n")
    val highNotNullSet: Long = stocks.filter($"high".isNotNull).count()
    println(s"\nThe # of daily high price values that are not null\nsql: $highNotNull, dataset: $highNotNullSet")


    /**
      * Sums
      */
    println("\nPrice totals w.r.t. sql & dataset, respectively")

    spark.sql("SELECT SUM(open) AS open, SUM(high) AS high, SUM(low) AS low, " +
      "SUM(close) AS close FROM stocks").show()

    val totals = prices.map(field => sum(field).as(field))
    stocks.agg(totals.head, totals.tail: _*).show()


    /**
      * Extrema
      */
    println("Volume minima w.r.t. sql & dataset, respectively")
    spark.sql("SELECT MIN(volume) as min_volume, MAX(volume) as max_volume FROM stocks").show()
    stocks.select(min($"volume").as("min_volume"), max($"volume").as("max_volume")).show()

    println("Maximum single day price increase w.r.t. sql & dataset, respectively")
    spark.sql("SELECT MAX(close - open) as highest_single_day_inc FROM stocks").show()
    stocks.select(max($"close" - $"open").as("highest_single_day_inc")).show()


    /**
      * Averages
      */
    println("The average of the high & volume values w.r.t. sql & dataset, respectively")
    spark.sql("SELECT AVG(high) as avg_high, AVG(volume) as avg_volume FROM stocks").show()
    stocks.select($"high", $"volume")
      .agg(avg($"high").as("avg_high"), avg($"volume").as("avg_volume")).show()

  }

}
