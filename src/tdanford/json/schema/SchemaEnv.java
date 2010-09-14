package tdanford.json.schema;

import java.util.*;
import java.util.regex.*;
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;

public class SchemaEnv { 

	private SchemaEnv parent;
	private Map<String,JSONType> types;

	public SchemaEnv() { 
		parent = null;
		types.put("string", new JSONType.String());
		types.put("integer", new JSONType.Integer());
		types.put("double", new JSONType.Double());
		types.put("boolean", new JSONType.Boolean());
	}
	
	public SchemaEnv(File dir) { 
		this();
		File[] lst = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".js");
			} 
		});
		
		Pattern jsFilenamePattern= Pattern.compile("^(.*)\\.js$");
		
		for(File jsFile : lst) { 
			Matcher jsMatcher = jsFilenamePattern.matcher(jsFile.getName());
			if(jsMatcher.matches()) { 
				String typeName = jsMatcher.group(1);
				addType(typeName, new JSONFileType(this, jsFile));
			} else { 
				throw new IllegalArgumentException(String.format(jsFile.getName()));
			}
		}
	}

	public SchemaEnv(SchemaEnv p) {
		this();
		parent = p;
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

	public JSONType evaluate(Object obj) throws SchemaException {
		try { 
			if(obj == null) { 
				throw new SchemaException("null schema object.");

			} else if(obj instanceof String) { 
				return lookupType((String)obj);

			} else if (obj instanceof JSONObject) {
				JSONObject json = (JSONObject)obj;
				if(json.has("type")) { 
					String type = json.getString("type").toLowerCase();
					
					if(type.equals("array")) { 
						return new JSONArrayType(this, json);
						
					} else if (type.equals("object")) {
						return new JSONObjectType(this, json);
						
					} else { 
						throw new SchemaException(String.format("Unrecognized schema type: %s", type));
					}

				} else { 
					throw new SchemaException("Schema object doesn't contain a 'types' property.");				
				}

			} else { 
				throw new SchemaException(obj.getClass().getSimpleName() + " isn't a valid schema type");
			}
		} catch(JSONException e) { 
			throw new SchemaException(String.valueOf(obj), e);
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
