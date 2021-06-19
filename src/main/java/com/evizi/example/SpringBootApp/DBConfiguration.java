package com.evizi.example.SpringBootApp;

import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DBConfiguration {

	@Autowired
	private Environment environment;

	@Bean(name = "oracleDataSource")
	public DataSource mrrDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getProperty("mrr.datasource.driverClassName"));
		dataSource.setUrl(environment.getProperty("mrr.datasource.url"));
		dataSource.setUsername(environment.getProperty("mrr.datasource.username"));
		dataSource.setPassword(environment.getProperty("mrr.datasource.password"));

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDataSource(dataSource);
		hikariConfig.setPoolName(environment.getProperty("mrr.datasource.poolName"));
		hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("mrr.datasource.maximumPoolSize")));
        hikariConfig.setConnectionTestQuery(environment.getProperty("mrr.datasource.connectionTestQuery"));

		return new HikariDataSource(hikariConfig);
	}
    
    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean hibernateSessionFactoryBean() throws SystemException {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(mrrDataSource());

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.default_schema", environment.getProperty("hibernate.default_schema"));
        properties.setProperty("hibernate.transaction.auto_close_session", environment.getProperty("hibernate.transaction.auto_close_session"));
        properties.setProperty("hibernate.connection.release_mode", environment.getProperty("hibernate.connection.release_mode"));
        factoryBean.setHibernateProperties(properties);

        return factoryBean;
    }
	
	@Bean(name = "oracleBpmDataSource")
	public DataSource mrrBpmDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(environment.getProperty("mrr.bpm.datasource.driverClassName"));
        config.setJdbcUrl(environment.getProperty("mrr.bpm.datasource.url"));
        config.setUsername(environment.getProperty("mrr.bpm.datasource.username"));
        config.setPassword(environment.getProperty("mrr.bpm.datasource.password"));
        config.setPoolName(environment.getProperty("mrr.bpm.datasource.poolName"));
        config.setMaximumPoolSize(Integer.parseInt(environment.getProperty("mrr.bpm.datasource.maximumPoolSize")));
        config.setConnectionTestQuery(environment.getProperty("mrr.bpm.datasource.connectionTestQuery"));
        return new HikariDataSource(config);
	}

	@Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }
	
}
