package com.example.spark.example

import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable.ArrayBuffer

/**
  * 字符串合并为数组的累加器, driver显示结果，取决于各个Executor的值到达的顺序
  */
class MyAccumulator extends AccumulatorV2[String, ArrayBuffer[String]] {

  private var result = ArrayBuffer[String]()

  // 判断累加器当前值是否为零值
  override def isZero: Boolean = this.result.size == 0

  // 设置为新建累加器，并把result赋给新的累加器
  override def copy(): AccumulatorV2[String, ArrayBuffer[String]] = {
    val newAccum = new MyAccumulator
    newAccum.result = this.result
    newAccum
  }

  // result设置为新的ArrayBuffer
  override def reset(): Unit = new ArrayBuffer[String]()

  // 把传进来的字符串添加到result
  override def add(v: String): Unit = this.result += v

  // 把两个累加器的result合并起来
  override def merge(other: AccumulatorV2[String, ArrayBuffer[String]]): Unit = {
    result.++=:(other.value)
  }

  override def value: ArrayBuffer[String] = this.result
}
