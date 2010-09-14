package tdanford.json.schema;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectType implements JSONType { 

	private SchemaEnv env;
	private String name;
	private String description;
	private Map<java.lang.String,JSONType> properties;

	public JSONObjectType(SchemaEnv env, JSONObject obj) throws SchemaException { 
		this.env = env;
		this.name = null;
		this.description = null;
		properties = new TreeMap<java.lang.String,JSONType>();
		
		try {
			java.lang.String type = obj.getString("type").toLowerCase(); 
			if(!type.equals("object")) { 
				throw new SchemaException(java.lang.String.format(
						"Cannot convert expression %s into JSONObjectType", obj.toString()));
			}
			
			if(obj.has("name")) { 
				java.lang.String name = obj.getString("name"); 
				env.addType(name, this);
			}
			
			if(obj.has("properties")) { 
				JSONObject propObj = obj.getJSONObject("properties"); 
				Iterator<java.lang.String> itr = (Iterator<java.lang.String>)propObj.keys();
				while(itr.hasNext()) { 
					java.lang.String propName = itr.next();
					properties.put(propName, env.evaluate(propObj.get(propName)));
				}
			}			
			
		} catch(JSONException e) { 
			throw new SchemaException(obj.toString(), e);
		}
	}
	
	public boolean contains(Object obj) { 
		if(obj == null || !(obj instanceof JSONObject)) { 
			return false; 
		}
		
		JSONObject json = (JSONObject)obj;
		Iterator keys = json.keys();
		Set<java.lang.String> seen = new TreeSet<java.lang.String>(properties.keySet());
		
		while(keys.hasNext()) { 
			java.lang.String key = (java.lang.String)keys.next();
			if(!seen.contains(key)) { 
				return false;
			}

			try { 
				if(!properties.get(key).contains(json.get(key))) { 
					return false;
				}
			} catch(JSONException e) { 
				throw new IllegalArgumentException(java.lang.String.format(
						"%s in %s", key, obj.toString()));
			}
			
			seen.remove(key);
		}
		
		if(!seen.isEmpty()) { 
			return false;
		}

		return true;
	}
}
