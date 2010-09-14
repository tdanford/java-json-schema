package tdanford.json.schema.tests;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.*;
import org.junit.*;
import tdanford.json.schema.*;

import org.json.*;

public class SchemaTests {
	
	public static JSONObject json(String input) { 
		try {
			return new JSONObject(input);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static SchemaEnv top = new SchemaEnv();
	
	public static JSONObjectType test1Schema() throws SchemaException {  
		return new JSONObjectType(top, 
				json(
						"{ " +
						"  name : \"test1\", " +
						"  type : \"object\", " +
						"  properties : { " +
						"    foo : \"string\"," +
						"    bar : \"integer\"," +
						"  }" +
						"}"
				)
			);
	}
	
	public static JSONObject test1Succeed() { 
		return json("{ foo : \"grok\", bar : 3 }");
	}

	public static JSONObject test1Fail1() { 
		return json("{ foo : \"grok\", bar : \"quux\" }");
	}

	public static JSONObject test1Fail2() { 
		return json("{ foo : \"grok\", bar : 3, quux : true }");
	}

	public void checkTrue(SchemaValidator validator, Object value, String schemaName) { 
		assertTrue(validator.explain(value, schemaName), 
				validator.validate(value, schemaName));
	}
	
	public void checkFalse(SchemaValidator validator, Object value, String schemaName) { 
		assertFalse(validator.explain(value, schemaName), 
				validator.validate(value, schemaName));
	}
	
	@org.junit.Test 
	public void checkSimpleTypes() { 
		SchemaValidator validator = new SchemaValidator();
		
		checkTrue(validator, "foo", "string");
		checkTrue(validator, 1, "integer");
		checkTrue(validator, 1.0, "double");
		checkTrue(validator, true, "boolean");

		checkFalse(validator, "foo", "integer");
		checkFalse(validator, true, "double");
	}
	
	
	
	@org.junit.Test
	public void checkJSONTypes() throws SchemaException { 
		SchemaValidator validator = new SchemaValidator();
		validator.addObjectType(test1Schema());
	
		checkTrue(validator, test1Succeed(), "test1");
		checkFalse(validator, test1Fail1(), "test1");
		checkFalse(validator, test1Fail2(), "test1");
	}
}
