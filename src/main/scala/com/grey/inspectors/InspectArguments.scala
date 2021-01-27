package com.grey.inspectors

import java.net.URL

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import scala.util.Try
import scala.util.control.Exception

object InspectArguments {

  def inspectArguments(args: Array[String]): Parameters = {

    // Is the string args(0) a URL?
    val isURL: Try[Boolean] = new IsURL().isURL(args(0))

    // If the string is a valid URL parse & verify its parameters
    val mapper: ObjectMapper = new ObjectMapper(new YAMLFactory())

    val getParameters: Try[Parameters] = if (isURL.isSuccess){
      Exception.allCatch.withTry(
        mapper.readValue(new URL(args(0)), classOf[Parameters])
      )
    } else {
      sys.error(isURL.failed.get.getMessage)
    }

    if (getParameters.isSuccess) {
      getParameters.get
    } else {
      sys.error(getParameters.failed.get.getMessage)
    }

  }

  // Verification of parameters
  // Missing: Verification of schema URL ... partly addressed in SchemaOf.scala
  class Parameters(@JsonProperty("dataPath") _dataPath: String,
                   @JsonProperty("typeOf") _typeOf: String,
                   @JsonProperty("schemaOf") _schemaOf: String){

    require(_dataPath != null, "Parameter dataPath, i.e., the data path w.r.t. the resources directory, is required.")
    val dataPath: String = _dataPath

    require(_typeOf != null, "Parameter typeOf, i.e., the extension string of the files, is required.")
    val typeOf: String = _typeOf

    require(_schemaOf != null, "Parameter schemaOf, i.e., the data schema URL, is required.")
    val schemaOf: String = _schemaOf

  }

}
