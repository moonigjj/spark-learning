package com.example.spark

import org.apache.spark.streaming.{Minutes, Seconds}
import org.apache.spark.streaming.dstream.DStream

class Util {

  /**
    *
    * 转化为时间粒度进行过滤，抛弃province，userId ，cityid，格式为：   <yyyyMMddHHMM_adid,1L>，基于reduceByKeyAndWindow进行聚合
    *
    * 最终结果展开 (date,hour,minute,adid,clickCount)
    * @param adRealTimeValueDStream
    */
  def calculateAdClickCountByWindow(adRealTimeValueDStream: DStream[String]): Unit = {

    // 映射成<yyyyMMddHHMM_adid,1L>格式
    //(timestamp province city userid adid)
    val pairDStream = adRealTimeValueDStream.map{case consumerRecord =>
        val logSplited = consumerRecord.split(" ")
        //val timeMinute =
        val adId = logSplited(4).toLong

        (adId, 1L)
    }

    // 计算窗口函数，1小时滑动窗口内的广告点击趋势
    val aggrRDD = pairDStream.reduceByKeyAndWindow((a: Long, b: Long) => (a + b), Minutes(60L), Seconds(10L))
    // 最近1小时内，各分钟的点击量

  }

  /**
    * 实时统计每天每个省份top3热门广告
    * 转化为省粒度进行过滤，抛弃userId ，cityid，格式为：(yyyyMMdd_province_adid,clickCount)
    *
    * 注册成表，基于ROW_NUMBER()实现窗聚合,按照province分区，实现topN排序
    * @param adRealTimeStatDStream
    */
  def calculateProvinceTopNAd(adRealTimeStatDStream:DStream[(String, Long)], topN: Int): Unit = {

    // 每一个batch rdd，都代表了最新的全量的每天各省份各城市各广告的点击量
    //（yyyyMMdd_province_city_adid,clickCount）
    /*val rowsDStream = adRealTimeStatDStream.transform{rdd =>
      // <yyyyMMdd_province_city_adid, clickCount>
      // <yyyyMMdd_province_adid, clickCount>

      // 计算出每天各省份各广告的点击量
      val mappedRDD = rdd.map(case (key, count) =>
        val keySplited = key.split("_")
      val date = keySplited(0)
      val province = keySplited(1)
      val adid = keySplited(3).toLong
      val clickCount = count

      val key = date + "_" + province + "_" + adid
      (key, clickCount)
      )
    }*/

  }
}
