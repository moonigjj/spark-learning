package com.example.spark

import java.io.{ObjectInputStream, ObjectOutputStream}

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.StreamingContext

import scala.reflect.ClassTag

/**
  * 通过包装器在DStream的foreachRDD中更新广播变量
  * 避免产生序列化问题
  * 利用unpersist()函数先将已经发布的广播变量删除，
  * 然后修改数据后重新进行广播
  * val yourBroadcast = BroadcastWrapper[yourType]
  *
  * yourBroadcast.update(newValue, true)
  * @param classTag$T
  * @tparam T
  */
case class BroadcastWrapper[T: ClassTag](
                                        @transient private val ssc: StreamingContext,
                                        @transient private val _v: T
                                        ) {
  @transient private var v = ssc.sparkContext.broadcast(_v)


  def update(newValue: T, blocking: Boolean = false): Unit = {
    // 删除RDD是否需要锁定
    v.unpersist(blocking)
    v = ssc.sparkContext.broadcast(newValue)
  }

  def value: T = v.value

  private def writeObject(out: ObjectOutputStream): Unit = {
    out.writeObject(v)
  }

  private def readObject(in: ObjectInputStream): Unit = {
    v = in.readObject().asInstanceOf[Broadcast[T]]
  }
}
