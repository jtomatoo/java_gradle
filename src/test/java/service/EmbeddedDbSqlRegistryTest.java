package service;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import exception.SqlUpdateFailureException;
import service.sql.EmbeddedDbSqlRegistry;
import service.sql.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

	private EmbeddedDatabase db;
	
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:sql/schema.sql")
				.build();
		
		EmbeddedDbSqlRegistry embeddedSqlRegistry = new EmbeddedDbSqlRegistry();
		embeddedSqlRegistry.setDataSource(db);
		
		return embeddedSqlRegistry;
	}

	@Test
	public void transactionUpdate() {
		checkFindResult("SQL1", "SQL2", "SQL3");
		
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified");
		sqlmap.put("KEY9999!@#$", "Modified9999");
		
		try {
			sqlRegistry.updateSql(sqlmap);
			fail();
		} catch (SqlUpdateFailureException e) {
			// TODO: handle exception
		}
		
		checkFindResult("SQL1", "SQL2", "SQL3");
	}
	
	@After
	public void tearDown() {
		db.shutdown();
	}

}
