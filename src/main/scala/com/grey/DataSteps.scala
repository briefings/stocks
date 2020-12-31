package com.grey

import java.io.File
import java.nio.file.Paths

import com.grey.directories.LocalSettings
import com.grey.inspectors.InspectArguments
import com.grey.metadata.CaseClassOf.Stocks
import com.grey.metadata.SchemaOf
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.types.StructType
import org.apache.spark.storage.StorageLevel

import scala.collection.parallel.immutable.ParSeq
import scala.util.Try

class DataSteps(spark: SparkSession) {

  val localSettings = new LocalSettings()

  def dataSteps(parameters: InspectArguments.Parameters): Unit = {

    // Implicits
    import spark.implicits._

    // Schema of data
    val schemaOf: Try[StructType] = new SchemaOf(spark = spark).schemaOf(parameters = parameters)

    // The list of data files
    val listOfFiles: List[File] = new ListOfFiles().listOfFiles(
      dataDirectory = Paths.get(localSettings.resourcesDirectory, parameters.dataPath).toString,
      listOfExtensions = List(parameters.typeOf)
    )

    // Sections
    val sections: ParSeq[DataFrame] = listOfFiles.par.map { file =>

      spark.read.schema(schemaOf.get)
        .format("csv")
        .option("header", value = true)
        .option("dateFormat", "yyyy-MM-dd")
        .option("encoding", "UTF-8")
        .load(file.toString)

    }

    // Reduce
    val pillar: DataFrame = sections.reduce(_ union _)
    pillar.persist(StorageLevel.MEMORY_ONLY)

    // Table
    pillar.createOrReplaceTempView("stocks")
    spark.sql("SELECT * FROM stocks LIMIT 5").show()

    // Dataset
    val stocks: Dataset[Stocks] = pillar.as[Stocks]
    stocks.show(5)

    // Hence
    // new com.grey.sql.Aggregating(spark = spark).aggregating()
    new com.grey.sets.Aggregating(spark = spark).aggregating(stocks = stocks)

  }

}
