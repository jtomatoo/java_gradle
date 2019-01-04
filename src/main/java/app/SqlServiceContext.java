package app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import app.config.SqlMapConfig;
import service.sql.EmbeddedDbSqlRegistry;
import service.sql.OxmSqlService;
import service.sql.SqlRegistry;
import service.sql.SqlService;

@Configuration
public class SqlServiceContext {

	@Autowired
	private SqlMapConfig sqlMapConfig;
	
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
//		sqlService.setSqlmap(new ClassPathResource("sqlmap.xml", UserDao.class));
		sqlService.setSqlmap(this.sqlMapConfig.getSqlMapResource());
		
		return sqlService;
	}

	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		
		return sqlRegistry;
	}

	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("service.sql.jaxb");
		
		return marshaller;
	}
	
	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
					.setName("embeddedDatabase")
					.setType(EmbeddedDatabaseType.H2)
					.addScript("classpath:sql/schema.sql")
					.build();
	}
}
