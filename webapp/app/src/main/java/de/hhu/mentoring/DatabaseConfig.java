package de.hhu.mentoring;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    
		// Container Profile
	    String profile = System.getProperty("spring.profiles.active");
		if (profile != null && profile.equals("container")) {
			String host = System.getenv("DB_HOST");
			String port = "3306";
			String user = System.getenv("DB_USER");
			String password = System.getenv("DB_PASSWORD");
			String database = System.getenv("DB_DATABASE");
			driverManagerDataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
			driverManagerDataSource.setUsername(user);
		    driverManagerDataSource.setPassword(password);
		} 
		
		// Default profile
		else {
			driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/mentoring");
			driverManagerDataSource.setUsername("admin");
		    driverManagerDataSource.setPassword("admin");
		}	    
	    
	    return driverManagerDataSource;
	    
	}
}
