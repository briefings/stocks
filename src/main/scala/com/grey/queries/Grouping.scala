package com.grey.queries

import com.grey.sources.CaseClassOf.Stocks
import org.apache.spark.sql.functions.{avg, max, min, sum}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
  *
  * @param spark: An instance of SparkSession
  */
class Grouping(spark: SparkSession) {

  /**
    * Focus: group by, having/having proxy
    *
    * @param stocks: The stocks Dataset
    */
  def grouping(stocks: Dataset[Stocks]): Unit = {


    println("\n\nGrouping\n")


    /**
      * Import implicits for
      *   encoding (https://jaceklaskowski.gitbooks.io/mastering-apache-spark/spark-sql-Encoder.html)
      *   implicit conversions, e.g., converting a RDD to a DataFrames.
      *   access to the "$" notation.
      */
    import spark.implicits._


    /**
      * Group by
      */
    println("GROUP BY.  Volumes traded per month w.r.t. sql & dataset, respectively")

    spark.sql("SELECT year, month, SUM(volume) as volume " +
      "FROM stocks GROUP BY year, month ORDER BY volume DESC NULLS LAST").show(3)

    stocks.select($"year", $"month", $"volume").groupBy($"year", $"month")
      .agg(sum($"volume").as("volume")).orderBy($"volume".desc_nulls_last).show(3)


    /**
      * Group by
      */
    println("GROUP BY.  The average of each year's daily delta values w.r.t. sql & dataset, respectively")

    spark.sql("SELECT year, AVG(close - open) as avg_daily_delta " +
      "FROM stocks GROUP BY year ORDER BY avg_daily_delta DESC").show(3)

    stocks.select($"year", ($"close" - $"open").as("daily_delta"))
      .groupBy($"year").agg(avg($"daily_delta").as("avg_daily_delta"))
      .orderBy($"avg_daily_delta".desc_nulls_last).show(3)


    /**
      * Group by
      */
    println("GROUP BY.  Each month's minimum and maximum daily price value w.r.t. sql & dataset, respectively")

    spark.sql("SELECT year, month, MIN(low) as min_price, MAX(high) as max_price " +
      "FROM stocks GROUP BY year, month ORDER BY max_price DESC NULLS LAST").show(3)

    stocks.select($"year", $"month", $"low", $"high")
      .groupBy($"year", $"month").agg(min($"low").as("min_price"), max($"high").as("max_price"))
      .orderBy($"max_price".desc_nulls_last).show(3)


    /**
      * SQL Having & Dataset[Row] Having Proxy
      */
    val having: DataFrame = spark.sql("SELECT year, month, MIN(low) as min_price, MAX(high) as max_price " +
      "FROM stocks GROUP BY year, month HAVING MAX(high) > 100")

    val havingSet: Dataset[Row] = stocks.select($"year", $"month", $"low", $"high")
      .groupBy($"year", $"month").agg(min($"low").as("min_price"), max($"high").as("max_price"))
      .filter($"max_price" > 100)

    println(s"HAVING & HAVING PROXY.  The # of months wherein the maximum daily high price " +
      s"exceeds 100\nsql: ${having.count()}, dataset: ${havingSet.count()}")



  }

}
