package docu.test.importer.tool.configuration;

import org.springframework.beans.factory.annotation.Value;

public class DatabaseConfiguration {

	@Value("${driver.class.name}")
	private String driverClassName;
	
	@Value("${db.url}")
	private String jdbcUrl;
	
	@Value("${db.username}")
	private String dbUsername;
	
	@Value("${db.password}")
	private String dbPassword;

	public String getDriverClassName() {
		return driverClassName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}
}
