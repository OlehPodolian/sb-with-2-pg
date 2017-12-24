package oleg.podolyan.multidb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "productEntityManagerFactory",
		transactionManagerRef = "productTransactionManager",
		basePackages = {"oleg.podolyan.multidb.repository.product"}
)
public class ProductDataSourceConfig {

	@Bean(name = "productDataSource")
	@ConfigurationProperties(prefix = "product.datasource")
	public DataSource dataSource(){
		return DataSourceBuilder
				.create()
				.build();
	}

	@Bean(name = "productEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(
			EntityManagerFactoryBuilder builder,
			@Qualifier("productDataSource") DataSource dataSource
	){
		return builder
				.dataSource(dataSource)
				.properties(hibernateProperties())
				.packages("oleg.podolyan.multidb.domain.product")
				.persistenceUnit("product")
				.build();
	}

	@Bean(name = "productTransactionManager")
	public PlatformTransactionManager productTransactionManager(
			@Qualifier("productEntityManagerFactory") EntityManagerFactory entityManagerFactory
	){
		return new JpaTransactionManager(entityManagerFactory);
	}

	private Map<String, Object> hibernateProperties(){
		Resource resource = new ClassPathResource("hibernate.properties");

		try {
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);

			return properties
					.entrySet()
					.stream()
					.collect(Collectors.toMap(
							prop -> prop.getKey().toString(),
							prop -> prop.getValue()
					));

		} catch (IOException e){
			return new HashMap<>();
		}
	}
}
