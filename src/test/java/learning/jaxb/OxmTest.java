package learning.jaxb;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.sql.jaxb.SqlType;
import service.sql.jaxb.Sqlmap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/OxmTest-context.xml")
public class OxmTest {
	
	@Autowired
	private Unmarshaller unmarshaller;
	
	@Test
	public void unmarchallSqlMap() throws XmlMappingException, IOException {
		Source xmlSource = new StreamSource(getClass().getResourceAsStream("sqlmap.xml"));
		
		Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(xmlSource);
		
		List<SqlType> sqlList = sqlmap.getSql();
		assertThat(sqlList.size(), Is.is(3));
		assertThat(sqlList.get(0).getKey(), Is.is("add"));
		assertThat(sqlList.get(0).getValue(), Is.is("insert"));
		assertThat(sqlList.get(1).getKey(), Is.is("get"));
		assertThat(sqlList.get(1).getValue(), Is.is("select"));
		assertThat(sqlList.get(2).getKey(), Is.is("delete"));
		assertThat(sqlList.get(2).getValue(), Is.is("delete"));
	}
}
