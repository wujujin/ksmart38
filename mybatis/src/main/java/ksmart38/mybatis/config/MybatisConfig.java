package ksmart38.mybatis.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.jasypt.encryption.StringEncryptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(value = "ksmart38.mybatis.dao"
		   ,sqlSessionFactoryRef = "mybatisSqlSessionFactory")
@EnableTransactionManagement

public class MybatisConfig {
/*
	//1. datasource DBCP (hikari pool)
	@Bean(name="hikariDataSource")
	public DataSource datasource(@Qualifier("jasyptStringEncryptor") StringEncryptor stringEncryptor) {
		
		String jdbcUrl = stringEncryptor.decrypt("1ebJmnBTOk5UYll5J+y8eEe/FQh96P9FZaZCy3ffnAEltr+aDydlLi9/h8o9ElY9CdGvWIFc8s526Ie9lp+wL8GIkTKwBocBetvK0wTkfABQgo2r8uCa/c4xPwb9G6cp/QWP4BTY16g=");
		String userName = stringEncryptor.decrypt("ALa63qW6IPE+r0kkdMdCmnuC2yqE+khQ");
		String password = stringEncryptor.decrypt("eHyjdAeo4dVw9ETaCQKz/qBb95+0fRxF");
		
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		hikariConfig.setJdbcUrl(jdbcUrl);
		hikariConfig.setUsername(userName);
		hikariConfig.setPassword(password);
		
		hikariConfig.setMaximumPoolSize(15);
		hikariConfig.setMaxLifetime(1000 * 60 * 30);
		
		return new HikariDataSource(hikariConfig);
	}
*/
	//2. Mybatis 연동을 위한 mybatisSqlSessionFactory 설정
	@Bean(name = "mybatisSqlSessionFactory")
	public SqlSessionFactory mybatisSqlSessionFactory(
			// @Qualifier("hikariDataSource")
			DataSource dataSource,
			 ApplicationContext context) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(context.getResources("classpath:mapper/**/*.xml"));
		sqlSessionFactoryBean.setTypeAliasesPackage("ksmart38.mybatis.domain");
		
		return sqlSessionFactoryBean.getObject();
	}
	//3. Mybatis SqlSessionTemplate 설정
	@Bean(name = "mybatisSqlSessionTemplate")
	public SqlSessionTemplate mybatisSqlSessionTemplate( 
			@Qualifier("mybatisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	//3. Mybatis TransactionManager 설정
	
	  @Bean(name ="mybatisTransactionManager") public PlatformTransactionManager
	  transactionManager(
	  //@Qualifier("hikariDataSource") 
			  DataSource dataSource ) {
	  
	  return new DataSourceTransactionManager(dataSource); }
	 
	
}
