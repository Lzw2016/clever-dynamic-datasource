package org.clever.dynamic.datasource.provider;

import lombok.extern.slf4j.Slf4j;
import org.clever.dynamic.datasource.autoconfigure.DataSourceProperty;
import org.clever.dynamic.datasource.autoconfigure.DynamicDataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * YML数据源提供者
 */
@Slf4j
public class YmlDynamicDataSourceProvider implements DynamicDataSourceProvider {

    /**
     * 多数据源参数
     */
    private DynamicDataSourceProperties properties;
    /**
     * 多数据源创建器
     */
    private DynamicDataSourceCreator dynamicDataSourceCreator;

    public YmlDynamicDataSourceProvider(DynamicDataSourceProperties properties) {
        this.properties = properties;
        this.dynamicDataSourceCreator = new DynamicDataSourceCreator(properties.getGlobalDruid(), properties.getGlobalHikari());
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSourceProperty> dataSourcePropertiesMap = properties.getDatasourceMap();
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size());
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            String dataSourceName = item.getKey();
            DataSourceProperty dataSourceProperty = item.getValue();
            dataSourceProperty.setPollName(dataSourceName);
            dataSourceMap.put(dataSourceName, dynamicDataSourceCreator.createDataSource(dataSourceProperty));
        }
        return dataSourceMap;
    }
}
