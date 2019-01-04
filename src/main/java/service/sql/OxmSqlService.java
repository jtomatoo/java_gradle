package service.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import dao.UserDao;
import exception.SqlNotFoundException;
import exception.SqlRetrievalFailureException;
import service.sql.jaxb.SqlType;
import service.sql.jaxb.Sqlmap;

public class OxmSqlService implements SqlService {

	private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
	
	private final BaseSqlService baseSqlService = new BaseSqlService();

	private SqlRegistry sqlRegistry = new HashMapSqlRegistry();
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.oxmSqlReader.setUnmarshaller(unmarshaller);
	}
/*
	public void setSqlmapFile(String sqlmapFile) {
		this.oxmSqlReader.setSqlmapFile(sqlmapFile);
	}
*/
	public void setSqlmap(Resource sqlmap) {
		this.oxmSqlReader.setSqlmap(sqlmap);
	}
	
	private class OxmSqlReader implements SqlReader {

		private final static String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
		
		private Unmarshaller unmarshaller;
		
		public void setUnmarshaller(Unmarshaller unmarshaller) {
			this.unmarshaller = unmarshaller;
		}
		
		/*
		private String sqlmapFile = DEFAULT_SQLMAP_FILE;
		
		public void setSqlmapFile(String sqlmapFile) {
			this.sqlmapFile = sqlmapFile;
		}
		*/
//		private Resource sqlmap = new ClassPathResource("sqlmap.xml", UserDao.class);
		private Resource sqlmap = new ClassPathResource("/sqlmap.xml");

		public void setSqlmap(Resource sqlmap) {
			this.sqlmap = sqlmap;
		}


		@Override
		public void read(SqlRegistry sqlResitry) {
			try {
				Source source = new StreamSource(sqlmap.getInputStream());
//				Source source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
				Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(source);
				for (SqlType sqlType : sqlmap.getSql()) {
					sqlResitry.registerSql(sqlType.getKey(), sqlType.getValue());
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(this.sqlmap.getFilename() + "을 가져올 수 없습니다.");
//				throw new IllegalArgumentException(this.sqlmapFile + "을 가져올 수 없습니다.");
			}
		}
	}
	
	@PostConstruct
	public void loadSql() {
		this.oxmSqlReader.read(this.sqlRegistry);
		this.baseSqlService.setSqlReader(this.oxmSqlReader);
		this.baseSqlService.setSqlRegistry(this.sqlRegistry);
		
		this.baseSqlService.loadSql();
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		return this.baseSqlService.getSql(key);
		/*
		try {
			return this.sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e.getMessage(), e);
		}
		*/
	}
}
