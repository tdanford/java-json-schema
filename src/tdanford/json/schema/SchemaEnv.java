package tdanford.json.schema;

import java.util.*;

public class SchemaEnv { 

	private SchemaEnv parent;
	private Map<String,JSONType> types;

	public SchemaEnv() { 
		this(null);
		types.put("string", new JSONType.String());
		types.put("integer", new JSONType.Integer());
		types.put("double", new JSONType.Double());
		types.put("boolean", new JSONType.Boolean());
	}

	public SchemaEnv(SchemaEnv p) { 
		parent = p;
		types = new TreeMap<String,JSONType>();
	}

	public JSONType lookupType(String name) { 
		if(types.containsKey(name)) { 
			return types.get(name);
		} else if (parent != null) { 
			return parent.lookupType(name);
		} else { 
			return null; 
		}
	}

	public void addType(String name, JSONType t) { 
		if(types.containsKey(name)) { 
			throw new IllegalArgumentException(String.format("Type for name %s already exists.", name));
		}
		types.put(name, t); 
	}

	public boolean containsType(String name) { return types.containsKey(name); }

	public Iterator<String> names() { return new TreeSet<String>(types.keySet()).iterator(); }
}
