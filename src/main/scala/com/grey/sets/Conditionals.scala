package com.grey.sets

import com.grey.sources.CaseClassOf.Stocks
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.when

class Conditionals(spark: SparkSession) {

  def conditionals(stocks: Dataset[Stocks]): Unit = {

    // Dataset Approach
    println("\n\nCase: Dataset Conditionals >> Case Statements")

    // Implicits
    import spark.implicits._

    // Simple Case
    stocks.select($"date", $"open",
      when($"open" > 100, "excellent")
        .when($"open" > 65 && $"open" <= 100, "take note")
        .when($"open" > 35 && $"open" <= 65, "promising")
        .otherwise("infancy").as("class")).show(5)

  }

}
