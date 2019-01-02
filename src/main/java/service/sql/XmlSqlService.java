package service.sql;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import dao.UserDao;
import exception.SqlNotFoundException;
import exception.SqlRetrievalFailureException;
import service.sql.jaxb.SqlType;
import service.sql.jaxb.Sqlmap;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	public XmlSqlService() {
		/*
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream("sqlmap.xml");
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
			
			for (SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*/
	}

	private String sqlmapFile;

	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	
	private SqlReader sqlReader;
	
	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	private SqlRegistry sqlRegistry;

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
		/*
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
			
			for (SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*/
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if(sql == null) {
			throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
		} else {
			return sql;
		}
	}
	
	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return this.sqlRegistry.findSql(key);
		} catch (Exception e) {
			throw new SqlRetrievalFailureException(e.getMessage(), e);
		}
		/*
		String sql = sqlMap.get(key);
		if(sql == null) {
			throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		} else {
			return sql;
		}
		*/
	}

	@Override
	public void read(SqlRegistry sqlResitry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
			
			for (SqlType sql : sqlmap.getSql()) {
				sqlResitry.registerSql(sql.getKey(), sql.getValue());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
