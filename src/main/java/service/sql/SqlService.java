package service.sql;

import exception.SqlRetrievalFailureException;

public interface SqlService {

	public String getSql(String key) throws SqlRetrievalFailureException;
}
