package com.grey.sets

import com.grey.sources.CaseClassOf.Stocks
import org.apache.spark.sql.{Dataset, SparkSession}

import org.apache.spark.sql.functions.{sum, avg, min, max}

class Grouping(spark: SparkSession) {

  def grouping(stocks: Dataset[Stocks]): Unit = {

    // Dataset Approach
    println("\n\nCase: Dataset Grouping")

    //Implicits
    import spark.implicits._

    /*
    * Volume per month
    * In order to avoid field names issues, use this approach instead of
    *   stocks.select($"year", $"month", $"volume").groupBy($"year", $"month")
          .sum("volume").alias("volume").orderBy($"year", $"month").
    */
    stocks.select($"year", $"month", $"volume").groupBy($"year", $"month")
      .agg(sum($"volume").as("volume")).orderBy($"year", $"month").show(5)

    // Average of the daily delta values per year
    stocks.select($"year", ($"close" - $"open").as("daily_delta")).groupBy($"year")
      .agg(avg($"daily_delta").as("avg_daily_delta")).orderBy($"year").show(5)

    // Extrema of prices per month
    stocks.select($"year", $"month", $"low", $"high").groupBy($"year", $"month")
      .agg(min($"low").as("min_price"), max($"high").as("max_price"))
      .orderBy($"year", $"month").show(5)

    // The 'having' proxy
    stocks.select($"year", $"month", $"low", $"high").groupBy($"year", $"month")
      .agg(min($"low").as("min_price"), max($"high").as("max_price"))
      .filter($"max_price" > 100)
      .orderBy($"year", $"month").show(5)

  }

}
