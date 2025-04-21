package com.stp.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.stp.dao.db2", entityManagerFactoryRef = "db2EntityManager", transactionManagerRef = "authorTransactionManager")
public class PersistenceDB2AutoConfiguration {

	private final Environment env;

	@Autowired
	public PersistenceDB2AutoConfiguration(Environment env) {
		this.env = env;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.db2-datasource")
	public DataSource authorDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "db2EntityManager")
	public LocalContainerEntityManagerFactoryBean db2EntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(authorDataSource());
		em.setPackagesToScan("com.stp.model.db2");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);

		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		properties.put("hibernate.spring.jpa.open-in-view", env.getProperty("hibernate.spring.jpa.open-in-view"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean(name = "authorTransactionManager")
	public PlatformTransactionManager authorTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(db2EntityManager().getObject());
		return transactionManager;
	}
}
