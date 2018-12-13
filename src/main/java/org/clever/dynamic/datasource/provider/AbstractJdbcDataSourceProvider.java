///**
// * Copyright © 2018 organization baomidou
// * <pre>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * <pre/>
// */
//package org.clever.sharding.jdbc.datasource.provider;
//
//import lombok.extern.slf4j.Slf4j;
//import org.clever.common.utils.exception.ExceptionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.support.JdbcUtils;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * JDBC数据源提供者(抽象)
// */
//@Slf4j
//public abstract class AbstractJdbcDataSourceProvider implements DynamicDataSourceProvider {
//
//    @Autowired(required = false)
//    protected DynamicDataSourceProperties dynamicDataSourceProperties;
//
//    @Autowired
//    private DynamicDataSourceCreator dynamicDataSourceCreator;
//
//    /**
//     * JDBC driver
//     */
//    private String driverClassName;
//    /**
//     * JDBC url 地址
//     */
//    private String url;
//    /**
//     * JDBC 用户名
//     */
//    private String username;
//    /**
//     * JDBC 密码
//     */
//    private String password;
//
//    public AbstractJdbcDataSourceProvider(String driverClassName, String url, String username, String password) {
//        this.driverClassName = driverClassName;
//        this.url = url;
//        this.username = username;
//        this.password = password;
//    }
//
//    @Override
//    public Map<String, DataSource> loadDataSources() {
//        Connection conn = null;
//        Statement stmt = null;
//        try {
//            Class.forName(driverClassName);
//            log.info("成功加载数据库驱动程序 -> {}", driverClassName);
//            conn = DriverManager.getConnection(url, username, password);
//            log.info("成功获取数据库连接 -> {}", url);
//            stmt = conn.createStatement();
//            Map<String, DataSourceProperty> dataSourcePropertiesMap = executeStmt(stmt);
//            Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size());
//            for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
//                String pollName = item.getKey();
//                DataSourceProperty dataSourceProperty = item.getValue();
//                dataSourceProperty.setPollName(pollName);
//                dataSourceMap.put(pollName, dynamicDataSourceCreator.createDataSource(dataSourceProperty));
//            }
//            return dataSourceMap;
//        } catch (Exception e) {
//            log.error("### 加载数据源失败", e);
//            throw ExceptionUtils.unchecked(e);
//        } finally {
//            JdbcUtils.closeConnection(conn);
//            JdbcUtils.closeStatement(stmt);
//        }
//    }
//
//    /**
//     * 执行语句获得数据源参数
//     *
//     * @param statement 语句
//     * @return 数据源参数
//     * @throws SQLException sql异常
//     */
//    protected abstract Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException;
//}
