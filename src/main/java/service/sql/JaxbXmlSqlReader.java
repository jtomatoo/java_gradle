package service.sql;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import dao.UserDao;
import service.sql.jaxb.SqlType;
import service.sql.jaxb.Sqlmap;

public class JaxbXmlSqlReader implements SqlReader {

	private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
	
	private String sqlmapFile = DEFAULT_SQLMAP_FILE;
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
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
