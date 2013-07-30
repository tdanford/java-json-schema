package tdanford.json.schema;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class SchemaEnvTest {

	@Test
	public void simpleBindingTest() { 
		SchemaEnv env = new SchemaEnv();

        assertTrue(env.names().hasNext(), "No default bindings in SchemaEnv");
        assertFalse(env.containsType("foo"));


		env.addType("foo", new JSONType.Everything());

        assertTrue(env.containsType("foo"));
        assertFalse(env.lookupType("foo").contains(null));
        assertTrue(env.lookupType("foo").contains(false));
	}
}
