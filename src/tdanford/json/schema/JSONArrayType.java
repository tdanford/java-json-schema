package tdanford.json.schema;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONArrayType implements JSONType { 

	private SchemaEnv env;
	private JSONType itemsType;

	public JSONArrayType(SchemaEnv env, JSONObject schema) throws SchemaException { 
		this.env = env;
		itemsType = new JSONType.Everything();
		
		if(schema.has("items")) {
			try { 
				itemsType = env.evaluate(schema.get("items"));
			} catch(JSONException e) { 
				throw new SchemaException(e);
			}
		}
	}
	
	public boolean contains(Object obj) { 
		if(obj == null || !(obj instanceof JSONArray)) { 
			return false; 
		}
		
		JSONArray array = (JSONArray)obj;
		for(int i = 0; i < array.length(); i++) { 
			try {
				if(!itemsType.contains(array.get(i))) { 
					return false;
				}
			} catch (JSONException e) {
				throw new IllegalArgumentException(array.toString());
			}
		}

		return true;
	}
}
