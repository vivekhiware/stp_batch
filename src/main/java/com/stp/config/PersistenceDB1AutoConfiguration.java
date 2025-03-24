package com.stp.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.stp.dao.db1", entityManagerFactoryRef = "db1EntityManager", transactionManagerRef = "bookTransactionManager")
public class PersistenceDB1AutoConfiguration {

	@Autowired
	private Environment env;

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.db1-datasource")
	public DataSource bookDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = { "db1EntityManager" })
	public LocalContainerEntityManagerFactoryBean db1EntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(bookDataSource());
		em.setPackagesToScan("com.stp.model.db1");

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

	@Primary
	@Bean(name = "bookTransactionManager")
	public PlatformTransactionManager bookTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(db1EntityManager().getObject());
		return transactionManager;
	}
}
