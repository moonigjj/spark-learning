package com.example.spark.example

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object WordCountStream {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("WordCountStream")
    val spark = SparkSession.builder().config(conf).getOrCreate();

    import spark.implicits._
    // 创建流
    val lines = spark.readStream.format("socket")
      .option("host", "localhost").option("port", "9999")
      .load()
    // 进行单词统计
    val words = lines.as[String].flatMap(_.split(" "))
    val wordCount = words.groupBy("values").count()

    // 创建查询句柄
    val query = wordCount.writeStream.outputMode("complete")
      .format("console").start()

    query.awaitTermination();
  }
}
