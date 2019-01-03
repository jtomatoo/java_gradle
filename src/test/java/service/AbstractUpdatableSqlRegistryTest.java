package service;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import exception.SqlNotFoundException;
import exception.SqlUpdateFailureException;
import service.sql.UpdatableSqlRegistry;

public abstract class AbstractUpdatableSqlRegistryTest {

	protected UpdatableSqlRegistry sqlRegistry;
	
	@Before
	public void setUp() {
		sqlRegistry = createUpdatableSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}

	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();
	
	@Test
	public void find() {
		checkFindResult("SQL1", "SQL2", "SQL3");
	}

	protected void checkFindResult(String expected1, String expected2, String expected3) {
		assertThat(sqlRegistry.findSql("KEY1"), Is.is(expected1));
		assertThat(sqlRegistry.findSql("KEY2"), Is.is(expected2));
		assertThat(sqlRegistry.findSql("KEY3"), Is.is(expected3));
	}
	
	@Test(expected=SqlNotFoundException.class)
	public void unknownKey() {
		sqlRegistry.findSql("SQL9999!@#$");
	}
	
	@Test
	public void updateSingle() {
		sqlRegistry.updateSql("KEY2", "Modified2");
		checkFindResult("SQL1", "Modified2", "SQL3");
	}
	
	
	@Test
	public void updateMulti() {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");
		
		sqlRegistry.updateSql(sqlmap);
		checkFindResult("Modified1", "SQL2", "Modified3");
	}
	
	@Test(expected=SqlUpdateFailureException.class)
	public void updateWithNotExistingKey() {
		sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
	}
}
