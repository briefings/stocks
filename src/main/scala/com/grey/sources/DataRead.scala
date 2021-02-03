package com.grey.sources

import java.io.File
import java.nio.file.Paths

import com.grey.directories.LocalSettings
import com.grey.inspectors.InspectArguments
import com.grey.sources.CaseClassOf.Stocks
import org.apache.spark.sql.functions.{month, year}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.collection.parallel.immutable.ParSeq
import scala.util.Try

class DataRead(spark: SparkSession) {

  private val localSettings = new LocalSettings()

  def dataRead(parameters: InspectArguments.Parameters): (DataFrame, Dataset[Stocks]) = {

    // Implicits
    import spark.implicits._

    // Schema of data
    val schemaOf: Try[StructType] = new SchemaOf(spark = spark).schemaOf(parameters = parameters)

    // The list of data files
    val listOfFiles: List[File] = new ListOfFiles().listOfFiles(
      dataDirectory = Paths.get(localSettings.resourcesDirectory, parameters.dataPath).toString,
      listOfExtensions = List(parameters.typeOf)
    )

    // The data
    val sections: ParSeq[DataFrame] = listOfFiles.par.map { file =>
      spark.read.schema(schemaOf.get)
        .format("csv")
        .option("header", value = true)
        .option("dateFormat", "yyyy-MM-dd")
        .option("encoding", "UTF-8")
        .load(file.toString)
    }

    // Reduce
    var stocksFrame: DataFrame = sections.reduce(_ union _)

    // Enhance
    stocksFrame = stocksFrame.withColumn("year", year($"date")).withColumn("month", month($"date"))

    // Create the Dataset version
    val stocksSet: Dataset[Stocks] = stocksFrame.as[Stocks]

    // Hence
    (stocksFrame, stocksSet)

  }

}
