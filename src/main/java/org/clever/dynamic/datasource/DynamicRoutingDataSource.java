package org.clever.dynamic.datasource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.datasource.provider.DynamicDataSourceProvider;
import org.clever.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import org.clever.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 核心动态数据源组件
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 多数据源加载
     */
    @Setter
    protected DynamicDataSourceProvider provider;
    /**
     * 多数据源选择策略
     */
    @Setter
    protected Class<? extends DynamicDataSourceStrategy> strategy;
    /**
     * 默认数据源
     */
    @Setter
    protected String primary;
    /**
     * 所有数据源
     */
    private Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

    /**
     * 选择数据源
     */
    @Override
    public DataSource determineDataSource() {
        return getDataSource(DynamicDataSourceContextHolder.getDataSourceLookupKey());
    }

    private DataSource determinePrimaryDataSource() {
        log.debug("使用默认数据源 -> {}", primary);
        return dataSourceMap.get(primary);
    }

    /**
     * 获取当前所有的数据源
     *
     * @return 当前所有数据源
     */
    public Map<String, DataSource> getCurrentDataSources() {
        return dataSourceMap;
    }

    /**
     * 获取数据源
     *
     * @param ds 数据源名称
     * @return 数据源
     */
    public DataSource getDataSource(String ds) {
        if (!StringUtils.isEmpty(ds)) {
            log.debug("选择数据源 -> {}", ds);
            return dataSourceMap.get(ds);
        }
        return determinePrimaryDataSource();
    }

    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        dataSourceMap.put(ds, dataSource);
        log.info("加载数据源成功 -> {}", ds);
    }

    /**
     * 删除数据源
     *
     * @param ds 数据源名称
     */
    public synchronized void removeDataSource(String ds) {
        if (dataSourceMap.containsKey(ds)) {
            dataSourceMap.remove(ds);
            log.info("动态数据源-删除 {} 成功", ds);
        } else {
            log.warn("动态数据源-未找到 {} 数据源");
        }
    }

    public void init() {
        Map<String, DataSource> dataSources = provider.loadDataSources();
        log.info("初始共加载 {} 个数据源 -> {}", dataSources.size(), dataSources.keySet());
        //添加并分组数据源
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
        //检测默认数据源设置
        if (dataSourceMap.containsKey(primary)) {
            log.info("默认数据源是 -> {}", primary);
        } else {
            throw new RuntimeException("请检查primary默认数据库设置");
        }
    }
}