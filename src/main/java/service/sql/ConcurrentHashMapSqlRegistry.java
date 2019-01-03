package service.sql;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import exception.SqlNotFoundException;
import exception.SqlUpdateFailureException;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

	private Map<String, String> sqlMap = new ConcurrentHashMap<String, String>();
	
	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		if(sqlMap.get(key) == null) {
			throw new SqlUpdateFailureException(key + "�� �ش��ϴ� SQL�� ã�� �� �����ϴ�.");
		}
		
		sqlMap.put(key, sql);
	}

	@Override
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		for (Entry<String, String> entry : sqlmap.entrySet()) {
			updateSql(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String findSql(String key) {
		String sql = sqlMap.get(key);
		if(sql == null) {
			throw new SqlNotFoundException(key + "�� �̿��ؼ� SQL�� ã�� �� �����ϴ�");
		} else {
			return sql;
		}
	}

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

}
