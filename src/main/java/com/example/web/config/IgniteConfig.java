/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.config;

import com.example.web.common.CacheName;
import com.example.web.model.UserShop;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.WALMode;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 顺序加载bean
 * @author tangyue
 * @version $Id: IgniteConfig.java, v 0.1 2019-01-07 11:01 tangyue Exp $$
 */
@Configuration
@EnableIgniteRepositories
public class IgniteConfig {

    @Bean(name = "ignite")
    @ConditionalOnMissingBean(Ignite.class)
    public Ignite ignite() {

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("sparkDataNode");
        cfg.setPeerClassLoadingEnabled(true);

        Slf4jLogger gridLog = new Slf4jLogger();
        cfg.setGridLogger(gridLog);
        // 持久化
        DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration();


        DataRegionConfiguration regionConfiguration = new DataRegionConfiguration();
        regionConfiguration.setPersistenceEnabled(true);

        dataStorageConfiguration.setDefaultDataRegionConfiguration(regionConfiguration);
        // Set WAL Mode
        dataStorageConfiguration.setWalMode(WALMode.LOG_ONLY);
        dataStorageConfiguration.setWalCompactionEnabled(true);
        dataStorageConfiguration.setWalCompactionLevel(9);
        cfg.setDataStorageConfiguration(dataStorageConfiguration);
        // 数据缓存
        CacheConfiguration<Long, UserShop> userShops = new CacheConfiguration<>();
        userShops.setName(CacheName.USER_SHOP);
        userShops.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        userShops.setIndexedTypes(Long.class, UserShop.class);
        cfg.setCacheConfiguration(userShops);

        Ignite ignite = Ignition.start(cfg);
        ignite.cluster().active(true);
        return ignite;
    }

    /**
     * ID生成器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IgniteAtomicSequence.class)
    @DependsOn("ignite")
    public IgniteAtomicSequence igniteAtomicSequence() {

        final IgniteAtomicSequence sequence = ignite().atomicSequence(
                "seqUserId", // sequence name
                0, // Initial value for sequence
                true // create if it dose not exist
        );
        return sequence;
    }
}
