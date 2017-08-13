package docu.test.importer.tool.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import docu.test.arkiv.dao.ArchiveDao;
import docu.test.arkiv.dao.impl.ArchiveDaoImpl;
import docu.test.arkiv.service.ArchiveService;
import docu.test.unmarshall.XMLReader;

@Configuration
@PropertySource("classpath:application.properties")
public class ImporterToolConfig {

	@Bean
	public XMLReader xmlReader() {
		return new XMLReader();
	}

	@Bean
	public DriverManagerDataSource driverManagerDataSource(DatabaseConfiguration databaseConfiguration) {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(databaseConfiguration.getDriverClassName());
		driverManagerDataSource.setUrl(databaseConfiguration.getJdbcUrl());
		driverManagerDataSource.setUsername(databaseConfiguration.getDbUsername());
		driverManagerDataSource.setPassword(databaseConfiguration.getDbPassword());
		return driverManagerDataSource;
	}
	
	@Bean
	public DatabaseConfiguration databaseConfiguration() {
		return new DatabaseConfiguration();
	}
	
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DriverManagerDataSource driverManagerDataSource){
		 DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		 dataSourceTransactionManager.setDataSource(driverManagerDataSource);
		 return dataSourceTransactionManager;
	}
	
	@Bean 
	public ArchiveDao archiveDao(){
		return new ArchiveDaoImpl();
	}
	
	@Bean 
	public ArchiveService archiveService(){
		return new ArchiveService(); 
	}
}
