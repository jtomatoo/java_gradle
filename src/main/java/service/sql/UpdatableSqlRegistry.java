package service.sql;

import java.util.Map;

import exception.SqlUpdateFailureException;

public interface UpdatableSqlRegistry extends SqlRegistry {

	public void updateSql(String key, String sql) throws SqlUpdateFailureException;
	
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;

}
