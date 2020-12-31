package com.grey.sets

import com.grey.metadata.CaseClassOf.Stocks
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.count

class Aggregating(spark: SparkSession) {

  def aggregating(stocks: Dataset[Stocks]): Unit = {

    // Implicits
    import spark.implicits._

    // Counts
    stocks.select(count("*").as("n")).show()
    stocks.select(count($"high").as("count_of_high")).show()
    stocks.select(count($"date").as("count_of_date")).show()
    stocks.summary("count").show()

  }

}
