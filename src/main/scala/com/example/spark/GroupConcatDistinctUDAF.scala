package com.example.spark

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, StringType, StructField, StructType}

/**
  * 弱类型 UDAF 函数
  * 用户自定义聚合函数
  * UDAF电商业务的实现城市信息累加
  */
class GroupConcatDistinctUDAF extends UserDefinedAggregateFunction{
  // 输入数据类型
  override def inputSchema: StructType = StructType(StructField("cityInfo", StringType) :: Nil)

  // 缓冲数据类型
  override def bufferSchema: StructType = StructType(StructField("bufferCityInfo", StringType) :: Nil)

  // 输出数据类型
  override def dataType: DataType = StringType

  // 一致性校验
  override def deterministic: Boolean = true

  //
  override def initialize(buffer: MutableAggregationBuffer): Unit = buffer(0) = ""

  /**
    * 更新
    * 实现拼接的逻辑
    * @param buffer
    * @param input
    */
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    // 缓冲中的已经拼接过的城市信息串
    val bufferCityInfo = buffer.getString(0)

    // 刚刚传进来的某个城市信息
    val cityInfo = input.getString(0)


    concat(buffer, bufferCityInfo, cityInfo)
    /*if (!bufferCityInfo.contains(cityInfo)) {
      if ("".eq(bufferCityInfo)) {
        bufferCityInfo += cityInfo
      } else {
        bufferCityInfo += "," + cityInfo
      }

      buffer.update(0, bufferCityInfo)
    }*/
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    val bufferCityInfo1 = buffer1.getString(0)
    val bufferCityInfo2 = buffer2.getString(0)
    for (cityInfo <- bufferCityInfo2.split(",")) {

      concat(buffer1, bufferCityInfo1, cityInfo)
      /*if (!bufferCityInfo1.contains(cityInfo)){
        bufferCityInfo1 += cityInfo
      } else {
        bufferCityInfo1 += "," + cityInfo
      }

      buffer1.update(0, bufferCityInfo1)*/
    }
  }

  override def evaluate(buffer: Row): Any = buffer.getString(0)

  // 已存在不需要拼接
  private def concat(buffer: MutableAggregationBuffer, info: String, cityInfo: String): Unit ={
    var bufferCityInfo = info
    if (!bufferCityInfo.contains(cityInfo)) {
      if ("".eq(bufferCityInfo)) {
        bufferCityInfo += cityInfo
      } else {
        bufferCityInfo += "," + cityInfo
      }

      buffer.update(0, bufferCityInfo)
    }
  }
}
