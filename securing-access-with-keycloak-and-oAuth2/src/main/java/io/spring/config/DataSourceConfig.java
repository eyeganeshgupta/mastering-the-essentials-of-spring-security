package io.spring.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.spring.utils.TenantContext;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${custom.datasource.driver-class-name}")
    private String driverClass;
    @Value("${custom.datasource.url}")
    private String url;
    @Value("${custom.datasource.username}")
    private String username;
    @Value("${custom.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClass);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setAutoCommit(false);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        logger.info("Configuring DataSource with URL: {}", url);

        return new DelegatingDataSource(new HikariDataSource(config)) {
            @Override
            public Connection getConnection() throws SQLException {
                logger.info("Obtaining connection from the DataSource...");
                return processConnection(super.getConnection());
            }

            @Override
            public Connection getConnection(String u, String p) throws SQLException {
                logger.info("Obtaining connection with username: {}", u);
                return processConnection(super.getConnection(u, p));
            }

            private Connection processConnection(Connection conn) throws SQLException {
                String tenant = TenantContext.getTenant();
                if (tenant != null && !tenant.isBlank()) {
                    logger.info("Switching schema to tenant: {}", tenant);
                    setSchema(conn, tenant);
                } else {
                    logger.info("No tenant found, using default schema.");
                }
                return conn;
            }

            private void setSchema(Connection conn, String schema) {
                try (PreparedStatement ps = conn.prepareStatement("USE " + schema)) {
                    ps.execute();
                    logger.info("Schema switched successfully to: {}", schema);
                } catch (SQLException e) {
                    logger.error("Schema switch failed for schema: {}", schema, e);
                    throw new RuntimeException("Schema switch failed", e);
                }
            }
        };
    }
}
