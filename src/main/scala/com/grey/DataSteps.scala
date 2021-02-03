package com.grey

import com.grey.directories.LocalSettings
import com.grey.inspectors.InspectArguments
import com.grey.sources.CaseClassOf.Stocks
import com.grey.sources.DataRead
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.storage.StorageLevel

class DataSteps(spark: SparkSession) {

  val localSettings = new LocalSettings()

  def dataSteps(parameters: InspectArguments.Parameters): Unit = {

    // Read the data
    val dataRead = new DataRead(spark = spark)
    val (stocksFrame: DataFrame, stocksSet: Dataset[Stocks]) = dataRead.dataRead(parameters = parameters)

    // Ensure persistence
    stocksFrame.persist(StorageLevel.MEMORY_ONLY)
    stocksSet.persist(StorageLevel.MEMORY_ONLY)
    stocksFrame.createOrReplaceTempView("stocks")

    // A summary of the temporary tables
    println("\n\nIn relation to SQL, the temporary tables are")
    spark.sql("SHOW TABLES").show()

    // Hence
    new com.grey.queries.Aggregating(spark = spark).aggregating(stocks = stocksSet)
    new com.grey.queries.Conditionals(spark = spark).conditionals(stocks = stocksSet)
    new com.grey.queries.Grouping(spark = spark).grouping(stocks = stocksSet)

  }

}
