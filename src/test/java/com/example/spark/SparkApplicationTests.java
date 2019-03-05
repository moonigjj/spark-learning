package com.example.spark;

import com.example.spark.common.CacheName;
import com.example.spark.model.UserShop;
import com.example.spark.repository.UserRepository;
import com.example.spark.service.SparkSqlService;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SparkApplicationTests {

	@Autowired
	private Ignite ignite;

	@Autowired
	private UserRepository repository;

	@Autowired
	private SparkSqlService sqlService;

	@Test
	public void contextLoads() {

		UserShop userShop = this.repository.findById(10L).get();
		System.out.println("userShop: " + userShop);
	}

	@Test
	public void sparkSql() {
		sqlService.process();
	}

	@Test
	public void testCache() {

		IgniteCache<Long, UserShop> cache = ignite.getOrCreateCache(CacheName.USER_SHOP);
		cache.put(100L, new UserShop(100L, 10000100L, 4L, 2, 4,"杭州", "2019-02-27"));
	}

	@Test
	public void testCacheGet() {

		IgniteCache<Long, UserShop> cache = ignite.getOrCreateCache(CacheName.USER_SHOP);
		UserShop userShop = cache.get(100L);
		System.out.println(userShop);
		cache.remove(100L);
	}
}

