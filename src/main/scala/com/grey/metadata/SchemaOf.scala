package com.grey.metadata

import java.io.File
import java.net.URL
import java.nio.file.Paths

import com.typesafe.scalalogging.Logger

import org.apache.commons.io.FileUtils

import scala.util.Try
import scala.util.control.Exception
import com.grey.directories.{DataDirectories, LocalSettings}
import com.grey.inspectors.InspectArguments
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DataType, StructType}

class ReadSchemaOf(spark: SparkSession) {

  private val localSettings = new LocalSettings()
  private val dataDirectories = new DataDirectories()

  def readSchemaOf(parameters: InspectArguments.Parameters): Try[StructType] = {

    // Logging
    val logger: Logger = Logger(classOf[ReadSchemaOf])


    // The directory, schema file (including its extension), ...
    val directoryString = localSettings.resourcesDirectory + "schemata"
    val fileString: String = parameters.schemaOf.split("/").reverse.head
    val directoryAndFileString = Paths.get(directoryString, fileString).toString
    logger.info(fileString)

    // If the directory does not exist create it; at present, the
    // delete & re-create approach doesn't suffice
    val make: Try[Boolean] = dataDirectories.make(new File(directoryString))

    // Unload the schema file into a local directory ...
    val schemaOf: Try[Unit] = if (make.isSuccess) { Exception.allCatch.withTry(
        FileUtils.copyURLToFile(new URL(parameters.schemaOf),
          new File(directoryAndFileString))
      )} else {
      sys.error(make.failed.get.getMessage)
    }

    // Failure?
    if (schemaOf.isFailure) {
      sys.error(schemaOf.failed.get.getMessage)
    }

    // Read-in the schema
    val fieldProperties: Try[RDD[String]] = Exception.allCatch.withTry(
      spark.sparkContext.textFile(directoryAndFileString)
    )

    // Convert schema to StructType
    if (fieldProperties.isSuccess){
      Exception.allCatch.withTry(
        DataType.fromJson(fieldProperties.get.collect.mkString("")).asInstanceOf[StructType]
      )
    } else {
      sys.error(fieldProperties.failed.get.getMessage)
    }

  }

}
