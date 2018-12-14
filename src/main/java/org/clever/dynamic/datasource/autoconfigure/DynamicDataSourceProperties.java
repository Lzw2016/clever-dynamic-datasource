package org.clever.dynamic.datasource.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.clever.dynamic.datasource.autoconfigure.druid.DruidConfig;
import org.clever.dynamic.datasource.autoconfigure.hikari.HikariCpConfig;
import org.clever.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import org.clever.dynamic.datasource.strategy.LoadBalanceDynamicDataSourceStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.Ordered;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DynamicDataSourceProperties
 */
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
@Getter
@Setter
public class DynamicDataSourceProperties implements Serializable {

    /**
     * AOP 切面顺序，默认优先级最高
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * 多数据源选择算法clazz，默认负载均衡算法
     */
    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;
    /**
     * Druid全局参数配置
     */
    @NestedConfigurationProperty
    private DruidConfig globalDruid = new DruidConfig();
    /**
     * HikariCp全局参数配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig globalHikari = new HikariCpConfig();
    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";
    /**
     * 多数据源配置数据源
     */
    private Map<String, DataSourceProperty> datasourceMap = new LinkedHashMap<>();
}
