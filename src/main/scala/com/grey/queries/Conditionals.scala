package com.grey.queries

import com.grey.sources.CaseClassOf.Stocks
import org.apache.spark.sql.functions.{when, count}
import org.apache.spark.sql.{Dataset, SparkSession}

/**
  *
  * @param spark: An instance of SparkSession
  */
class Conditionals(spark: SparkSession) {

  /**
    * Focus: Conditionals
    *
    * @param stocks: The stocks Dataset
    */
  def conditionals(stocks: Dataset[Stocks]): Unit = {


    println("\n\nConditionals & Case Statements\n")


    /**
      * Import implicits for
      *   encoding (https://jaceklaskowski.gitbooks.io/mastering-apache-spark/spark-sql-Encoder.html)
      *   implicit conversions, e.g., converting a RDD to a DataFrames.
      *   access to the "$" notation.
      */
    import spark.implicits._


    /**
      * Conditionals and the CASE statement
      */
    println("After creating the labels" +
      "\n\texcellent [100, inf), take note [65, 100), promising [35, 65), infancy (-inf, 35)" +
      "\nw.r.t. the open prices, the frequency tables in relation to sql & dataset, respectively, are")

    // queries
    val labelling = spark.sql("SELECT date, open, CASE " +
      "WHEN open >= 100 THEN 'excellent' " +
      "WHEN open >= 65 AND open < 100 THEN 'take note' " +
      "WHEN open >= 35 AND open < 65 THEN 'promising' " +
      "ELSE 'infancy' END AS label FROM stocks")

    val labellingSet =stocks.select($"date", $"open",
      when(condition = $"open" >= 100, value = "excellent")
        .when(condition = $"open" >= 65 && $"open" < 100, value = "take note")
        .when(condition = $"open" >= 35 && $"open" < 65, value = "promising")
        .otherwise(value = "infancy").as("label"))

    // frequencies
    labelling.select($"label").groupBy($"label")
      .agg(count($"label").as("N")).show()

    labellingSet.select($"label").groupBy($"label")
      .agg(count($"label").as("N")).show()

  }

}
