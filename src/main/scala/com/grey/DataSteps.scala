package com.grey

import java.io.File
import java.nio.file.Paths

import com.grey.directories.LocalSettings
import com.grey.inspectors.InspectArguments
import com.grey.metadata.ReadSchemaOf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

import scala.collection.parallel.immutable.ParSeq
import scala.util.Try

class DataSteps(spark: SparkSession) {

  val localSettings = new LocalSettings()

  def dataSteps(parameters: InspectArguments.Parameters): Unit = {

    // Implicits
    import spark.implicits._

    // Schema of data
    val schemaOf: Try[StructType] = new ReadSchemaOf(spark = spark).readSchemaOf(parameters = parameters)

    // The list of data files
    val listOfFiles: List[File] = new ListOfFiles().listOfFiles(
      dataDirectory = Paths.get(localSettings.resourcesDirectory, parameters.dataPath).toString,
      listOfExtensions = List(parameters.typeOf),
      listOfPatterns = List("*")
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
    pillar.show()


  }

}
