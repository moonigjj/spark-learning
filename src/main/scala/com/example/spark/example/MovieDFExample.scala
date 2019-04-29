package com.example.spark.example

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}

case class User(UserID: String, Gender: String, Age: String, OccupationID: String, ZipCode: String)
case class Rating(UserID: String, MovieID: String, Rating: Double, Timestamp: String)
object MovieDFExample {


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("MovieDFExample")
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

    println("功能一: 通过DataFrame实现某部电影观看者中男性和女性不同年龄人数")
    // 1.格式化users
    val schemaForUsers = StructType(
      "UserID::Gender::Age::Occupation::Zip-code".split("::")
        .map(column => StructField(column, StringType, true))
    )
    // 2.每一天数据变成以Row为单位的数据
    val usersRDDRows = usersRDD.map(_.split("::"))
      .map(line => Row(line(0).trim, line(1).trim, line(2).trim, line(3).trim, line(4).trim))

    // 3.用户的DataFrame
    val usersDataFrame = spark.createDataFrame(usersRDDRows, schemaForUsers)
    // 对不同StructField赋予不同的类型
    val schemaForRatings = StructType("UserID::MovieID".split("::")
      .map(column => StructField(column, StringType, true)))
      .add("Rating", DoubleType, true)
      .add("Timestamp", StringType, true)

    val ratingsRDDRows = ratingsRDD.map(_.split("::"))
      .map(line => Row(line(0).trim, line(1).trim, line(2).trim.toDouble, line(3).trim))
    // 评分的DataFrame
    val ratingsDataFrame = spark.createDataFrame(ratingsRDDRows, schemaForRatings)

    //
    val schemaForMovies = StructType("MovieID::Title::Genres".split("::")
      .map(column => StructField(column, StringType, true)))
    val moviesRDDRows = moviesRDD.map(_.split("::")).map(line => Row(line(0).trim, line(1).trim, line(2).trim))
    // 电影的DataFrame
    val moviesDataFrame = spark.createDataFrame(moviesRDDRows, schemaForMovies)

    ratingsDataFrame.filter(s" MovieID = 1193")
      .join(usersDataFrame, "UserID")
      .select("Gender", "Age")
      .groupBy("Gender", "Age")
      .count().show(10)

    //
    println("功能二: 用LocalTempView实现某部电影观看者中不同性别不同年龄分别有多少人？")
    ratingsDataFrame.createOrReplaceTempView("ratings")
    usersDataFrame.createOrReplaceTempView("users")
    // sql
    val sqlLocal = "SELECT Gender, Age, count(*) from users u join ratings as r on u.UserID = r.UserID" +
      "where MovieID = 1193 group by Gender, Age"

    spark.sql(sqlLocal).show(10)

    // MovieID | avg(Rating)
    import spark.sqlContext.implicits._
    ratingsDataFrame.select("MovieID", "Rating")
        .groupBy("MovieID").avg("Rating")
        .orderBy($"avg(Rating)".desc).show(10)

    // DataSet
    import spark.implicits._

    val usersForDSRDD = usersRDD.map(_.split("::")).map(line =>
      User(line(0).trim, line(1).trim, line(2).trim, line(3).trim, line(4).trim)
    )
    val usersDataSet = spark.createDataset[User](usersForDSRDD)
    usersDataSet.show(10)

    val ratingsForDSRDD = ratingsRDD.map(_.split("::")).map(line =>
      Rating(line(0).trim, line(1).trim, line(2).trim.toDouble, line(3).trim)
    )
    val ratingsDataSet = spark.createDataset(ratingsForDSRDD)
    ratingsDataSet.filter(s" MovieID = 1193")
      .join(usersDataSet, "UserID")
        .select("Gender", "Age").groupBy("Gender", "Age").count()
        .orderBy($"Gender".desc, $"Age").show(10)

    spark.stop()
  }
}
