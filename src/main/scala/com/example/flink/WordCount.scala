package com.example.flink

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

object WordCount {

  def main(args: Array[String]): Unit = {

    val params: ParameterTool = ParameterTool.fromArgs(args)

    val env = ExecutionEnvironment.getExecutionEnvironment
    env.getConfig.setGlobalJobParameters(params)

    val text = env.fromCollection(Array("i am chinese ! am"))

    val counts = text
      .flatMap(_.toUpperCase.split("\\W+").filter(_.nonEmpty))
      .map{(_, 1)}.groupBy(0).sum(1)

    counts.print()
    env.execute("Flink WordCount Example")
  }

}
