package tdanford.json.schema;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SchemaEnvTest {

	@Test
	public void simpleBindingTest() { 
		SchemaEnv env = new SchemaEnv();

		assertThat("No default bindings in SchemaEnv", 
				env.names().hasNext(), is(true)); 
		assertThat(env.containsType("foo"), is(false));
		
		env.addType("foo", new JSONType.Everything());
		assertThat(env.containsType("foo"), is(true));
		assertThat(env.lookupType("foo").contains(null), is(false));
		assertThat(env.lookupType("foo").contains(false), is(true));
	}
}
