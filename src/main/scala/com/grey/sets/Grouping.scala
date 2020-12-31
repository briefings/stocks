package com.grey.sets

import com.grey.metadata.CaseClassOf.Stocks
import org.apache.spark.sql.{Dataset, SparkSession}

import org.apache.spark.sql.functions.{sum, avg}

class Grouping(spark: SparkSession) {

  def grouping(stocks: Dataset[Stocks]): Unit = {

    // Dataset Approach
    println("\n\nCase: Dataset Grouping")

    //Implicits
    import spark.implicits._


    // Volume per month
    stocks.select($"year", $"month", $"volume").groupBy($"year", $"month")
      .sum("volume").as("volume").orderBy($"year", $"month").show(11)

    stocks.select($"year", $"month", $"volume").groupBy($"year", $"month")
      .agg(sum($"volume").as("volume")).orderBy($"year", $"month").show(11)


    // Yearly ...
    stocks.select($"year", ($"close" - $"open").as("daily_delta")).groupBy($"year")
      .agg(avg($"daily_delta").as("avg_daily_delta")).orderBy($"year").show()




  }

}
