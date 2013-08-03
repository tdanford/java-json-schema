package tdanford.json.schema;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import java.io.StringReader;
import java.util.*;

import org.testng.annotations.*;
import tdanford.json.schema.*;

import org.json.JSONObject;
import org.json.JSONException;

public class SchemaTest {
	
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
	
	public static JSONObject test1Succeed1() { 
		return json("{ foo : \"grok\", bar : 3 }");
	}

	public static JSONObject test1Succeed2() { 
		return json("{ foo : \"grok\", bar : 3, quux : true }");
	}
	
	public static JSONObject test1Fail1() { 
		return json("{ foo : \"grok\", bar : \"quux\" }");
	}

	public static JSONObjectType test2Schema() throws SchemaException { 
		return new JSONObjectType(top, 
				json(
						"{ " +
						"  name : \"test2\", " +
						"  type : \"object\", " +
						"  properties : { " +
						"     arr : { " +
						"        type : \"array\"," +
						"        items : { type : \"integer\" }," + 
						"     }," + 
						"  }" +
						"}"
				)
			);		
	}

	public static JSONObject test2Succeed() { 
		return json("{ arr: [ 1, 2, 3 ] }");
	}

	public static JSONObject test2Fail1() { 
		return json("{ arr : [ 1, 2, \"grok\" ] }");
	}

	public static JSONObjectType test3Schema() throws SchemaException { 
		return new JSONObjectType(top, 
				json(
						"{ " +
						"  name : \"test3\", " +
						"  type : \"object\", " +
						"  properties : { " +
						"     foo : { " +
						"        type : \"integer\"," +
						"     }," +
						"     bar : { " +
						"		type : \"integer\"," +
						"		optional : true," +
						"	  }," +  
						"  }" +
						"}"
				)
			);		
	}

	public static JSONObject test3Succeed1() { 
		return json("{ foo : 1, bar : 2 }");
	}

	public static JSONObject test3Succeed2() { 
		return json("{ foo : 1 }");
	}

	public static JSONObject test3Fail1() { 
		return json("{ bar : 2 }");
	}


	public void checkTrue(SchemaValidator validator, Object value, String schemaName) { 
		assertTrue(validator.validate(value, schemaName), validator.explain(value, schemaName));
	}
	
	public void checkFalse(SchemaValidator validator, Object value, String schemaName) { 
		assertFalse(validator.validate(value, schemaName), validator.explain(value, schemaName));
	}
	
	@Test
	public void checkSimpleTypes() { 
		SchemaValidator validator = new SchemaValidator();
		
		checkTrue(validator, "foo", "string");
		checkTrue(validator, 1, "integer");
		checkTrue(validator, 1.0, "double");
		checkTrue(validator, true, "boolean");

		checkFalse(validator, "foo", "integer");
		checkFalse(validator, true, "double");
	}
	
	
	@Test
	public void checkJSONTypes() throws SchemaException { 
		SchemaValidator validator = new SchemaValidator();

		validator.addObjectType(test1Schema());
		checkTrue(validator, test1Succeed1(), "test1");
		checkTrue(validator, test1Succeed2(), "test1");
		checkFalse(validator, test1Fail1(), "test1");
		
		validator.addObjectType(test2Schema());
		checkTrue(validator, test2Succeed(), "test2");
		checkFalse(validator, test2Fail1(), "test2");

		validator.addObjectType(test3Schema());
		checkTrue(validator, test3Succeed1(), "test3");
		checkTrue(validator, test3Succeed2(), "test3");
		checkFalse(validator, test3Fail1(), "test3");
	}
}
