package com.grey.sql

import org.apache.spark.sql.SparkSession

class Aggregating(spark: SparkSession) {

  def aggregating(): Unit = {

    spark.sql("SELECT COUNT(*) as n FROM stocks").show()
    spark.sql("SELECT COUNT(high) as count_of_high FROM stocks").show()
    spark.sql("SELECT COUNT(date) as count_of_date FROM stocks").show()

  }

}
