package com.example.spark.example

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * 电影点评
  */
object MovieExample {

  // 二次排序
  class SecondarySortKey(val first: Double, val second: Double) extends Ordered[SecondarySortKey] with Serializable {
    override def compare(that: SecondarySortKey): Int = {
      if (this.first - that.first != 0) {
        (this.first - that.first).toInt
      } else {
        if (this.second - that.second > 0) {
          Math.ceil(this.second - that.second).toInt
        } else if (this.second - that.second < 0) {
          Math.floor(this.second - that.second).toInt
        } else {
          (this.second - that.second).toInt
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("MovieExample")
    val spark = SparkSession.builder().config(conf).getOrCreate();
    val sc = spark.sparkContext
    sc.setLogLevel("warn")

    val dataPath = ""
    // UserID::MovieID::Rating::Timestamp
    val ratingsRDD = sc.textFile(dataPath + "ratings.dat")
    // MovieID::Title::Genres
    val moviesRDD = sc.textFile(dataPath + "movies.dat")
    // UserID::Gender::Age::Occupation::Zip-code
    val usersRDD = sc.textFile(dataPath + "users.dat")

    println("所有电影平均得分最高的前10个电影名和平均评分")
    // 1.读取处理数据
    val movieInfo = moviesRDD.map(_.split("::"))
      .map(x => (x(0), x(1))).cache()
    val ratings = ratingsRDD.map(_.split("::"))
      .map(x => (x(0), x(1), x(2))).cache()

    //2.获取形如(movieID, (rating, 1))格式的RDD
    // (movieID, (Sum(ratings), count(ratings))
    val moviesAndRatings = ratings.map(x => (x._2, (x._3.toDouble, 1)))
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
    // 3. avg
    val avgRatings = moviesAndRatings.map(x => (x._1, x._2._1.toDouble / x._2._2))
    // 4. join
    avgRatings.join(movieInfo).map(item => (item._2._1, item._2._2))
        .sortByKey(false).take(10)
        .foreach(record => println(record._2 + "评分为：" + record._1))

    println("最受男性或女性喜爱的电影Top10")
    // 1.读取处理数据
    val usersGender = usersRDD.map(_.split("::"))
      .map(x => (x(0), x(1)))
    val genderRatings = ratings.map(x => (x._1, (x._1, x._2, x._3)))
        .join(usersGender).cache()
    genderRatings.take(10).foreach(println)

    // 2.分别过滤
    val maleFilteredRatings = genderRatings.filter(x => x._2._2.equals("M")).map(x => x._2._1)
    val femaleFilteredRatings = genderRatings.filter(x => x._2._2.equals("F")).map(x => x._2._1)
    println("所有电影中最受男性喜爱的电影Top10:")
    maleFilteredRatings.map(x => (x._2, (x._3.toDouble, 1)))
        .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
        .map(x => (x._1, x._2._1.toDouble / x._2._2))
        .join(movieInfo)
        .map(item => (item._2._1, item._2._2))
        .sortByKey(false).take(10)
        .foreach(record => println(record._2 + "评分为：" + record._1))

    println("所有电影中最受女性喜爱的电影Top10:")
    femaleFilteredRatings.map(x => (x._2, (x._3.toDouble, 1)))
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
      .map(x => (x._1, x._2._1.toDouble / x._2._2))
      .join(movieInfo)
      .map(item => (item._2._1, item._2._2))
      .sortByKey(false).take(10)
      .foreach(record => println(record._2 + "评分为：" + record._1))

    println("对电影评分数据以Timestamp和Rating两个维度进行二次降序排列")
    val pairWithSortKey = ratingsRDD.map(line => {
      val splited = line.split("::")
      (new SecondarySortKey(splited(3).toDouble, splited(2).toDouble), line)
    })
    // 直接调用sortByKey
    val sorted = pairWithSortKey.sortByKey(false)
    val sortedResult = sorted.map(sortedLine => sortedLine._2)
    sortedResult.take(10).foreach(println)
    spark.stop()
  }
}
