package service.sql;

import java.util.Map;

import exception.SqlRetrievalFailureException;

public class SimpleSqlService implements SqlService {

	private Map<String, String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}


	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if(sql == null) {
			throw new SqlRetrievalFailureException(key + "�� ���� SQL�� ã�� �� �����ϴ�.");
		} else {
			return sql;
		}
	}

}