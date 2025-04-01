package io.spring.config;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class JpaConfig {

    private static final Logger logger = LoggerFactory.getLogger(JpaConfig.class);

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
            JpaProperties jpaProperties) {

        logger.info("Creating EntityManagerFactory...");

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("io.spring.domain");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        em.setJpaPropertyMap(Map.of(
                "hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect",
                "hibernate.hbm2ddl.auto", "update",
                "hibernate.connection.provider_disables_autocommit", "true",
                "hibernate.transaction.jta.platform",
                "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform",
                "hibernate.jdbc.time_zone", "UTC",
                "hibernate.jdbc.batch_size", "100",
                "hibernate.order_inserts", "true",
                "hibernate.format_sql", "true"));

        logger.info("EntityManagerFactory created with properties: {}", em.getJpaPropertyMap());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        logger.info("Initializing TransactionManager...");

        PlatformTransactionManager transactionManager = new JpaTransactionManager(emf);

        logger.info("TransactionManager initialized successfully.");

        return transactionManager;
    }
}
