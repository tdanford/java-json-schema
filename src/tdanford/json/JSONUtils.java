package tdanford.json;

import org.json.*;
import java.util.*;

public abstract class JSONUtils { 

	public static boolean isEqual(Object o1, Object o2) { 
		if(o1 instanceof JSONObject) { 
			if(!(o2 instanceof JSONObject)) { 
				return false;
			}
			return isJSONObjectEqual((JSONObject)o1, (JSONObject)o2);

		} else if (o1 instanceof JSONArray) { 
			JSONArray a1 = (JSONArray)o1;
			if(!(o2 instanceof JSONArray)) { return false; }
			JSONArray a2 = (JSONArray)o2;
			try { 
				if(a1.length() != a2.length()) { return false; }
				for(int i = 0; i < a1.length(); i++) { 
					if(!isEqual(a1.get(i), a2.get(i))) { 
						return false;
					}
				}
			} catch(JSONException e) { 
				throw new IllegalArgumentException(e);
			}

			return true;
		} else { 
			return o1.equals(o2); 
		}
	}
	
	public static boolean isJSONObjectEqual(JSONObject o1, JSONObject o2) { 
		try { 
			Set<String> o1Keys = new TreeSet<String>(Arrays.asList(JSONObject.getNames(o1)));
			Set<String> o2Keys = new TreeSet<String>(Arrays.asList(JSONObject.getNames(o2)));
			if(o1Keys.size() != o2Keys.size()) { return false; }
			for(String key : o1Keys) { 
				if(!o2Keys.contains(key)) { return false; }
				if(!isEqual(o1.get(key), o2.get(key))) { 
					return false;
				}	
			}
		} catch(JSONException e) { 
			throw new IllegalArgumentException(e);
		}
		return true;
	}
}
