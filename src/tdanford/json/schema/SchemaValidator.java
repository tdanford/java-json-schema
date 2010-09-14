package tdanford.json.schema;

import java.io.*;
import java.util.*;

import org.json.JSONObject;

public class SchemaValidator { 
	
	private SchemaEnv env;
	
	public SchemaValidator() { 
		this(new SchemaEnv());
	}
	
	public SchemaValidator(File dir) { 
		this(new SchemaEnv(dir));
	}
	
	public SchemaValidator(SchemaEnv e) { 
		this.env = e;
	}
	
	public String explain(Object obj, String typeName) { 
		JSONType type = env.lookupType(typeName);
		if(type==null) { throw new IllegalArgumentException(typeName); }
		return type.explain(obj);		
	}
	
	public boolean validate(Object obj, String typeName) {
		JSONType type = env.lookupType(typeName);
		if(type==null) { throw new IllegalArgumentException(typeName); }
		return type.contains(obj);
	}
	
	public void addObjectType(JSONObjectType type) {
		if(type.getName() == null) { throw new IllegalArgumentException("No name in given type."); }
		env.addType(type.getName(), type);
	}
}
