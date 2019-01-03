package learning;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDbTest {

	private EmbeddedDatabase db;
	private JdbcTemplate template;
	
	@Before
	public void setUp() {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:sql/schema.sql")
				.addScript("classpath:sql/data.sql")
				.build();
		
		template = new JdbcTemplate(db);
	}
	
	@After
	public void tearDown() {
		db.shutdown();
	}
	
	@Test
	public void initData() {
		assertThat(template.queryForObject("select count(*) from sqlmap", int.class), Is.is(2));
		
		List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");
		assertThat((String)list.get(0).get("key_"), Is.is("KEY1"));
		assertThat((String)list.get(0).get("sql_"), Is.is("SQL1"));
		assertThat((String)list.get(1).get("key_"), Is.is("KEY2"));
		assertThat((String)list.get(1).get("sql_"), Is.is("SQL2"));
	}
	
	@Test
	public void insert() {
		template.update("insert into sqlmap(key_, sql_) values(?,?)", "KEY3", "SQL3");
		assertThat(template.queryForObject("select count(*) from sqlmap", int.class), Is.is(3));
	}
}
