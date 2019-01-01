package learning.jaxb;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hamcrest.core.Is;
import org.junit.Test;

import service.sql.jaxb.SqlType;
import service.sql.jaxb.Sqlmap;

public class JaxbTest {

	@Test
	public void readSqlmap() throws JAXBException, IOException {
		String contextPath = Sqlmap.class.getPackage().getName();
		JAXBContext context = JAXBContext.newInstance(contextPath);
		
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Sqlmap sqlMap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("sqlmap.xml"));
		
		List<SqlType> sqlList = sqlMap.getSql();
		
		assertThat(sqlList.size(), Is.is(3));
		assertThat(sqlList.get(0).getKey(), Is.is("add"));
		assertThat(sqlList.get(0).getValue(), Is.is("insert"));
		assertThat(sqlList.get(1).getKey(), Is.is("get"));
		assertThat(sqlList.get(1).getValue(), Is.is("select"));
		assertThat(sqlList.get(2).getKey(), Is.is("delete"));
		assertThat(sqlList.get(2).getValue(), Is.is("delete"));
	}
}
