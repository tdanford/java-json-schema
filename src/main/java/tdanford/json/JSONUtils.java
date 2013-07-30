package tdanford.json;

import org.json.*;
import java.util.*;

/**
 * Utilities for manipulating and comparing JSON objects and arrays. 
 * 
 * @author Timothy Danford
 **/
public abstract class JSONUtils { 

	/**
	 * Determines whether two objects are equal, in a way that respects the isJSONObjectEqual() method
	 * for JSONObjects.  It operates in the following manner: 
	 * <ul>
	 * <li>If the two objects are both JSONObjects, it calls isJSONObjectEqual().</li>
	 * <li>If they are JSONArrays, then it iteratively calls JSONUtils.isEqual() on each 
	 * successive element (returning false if the two arrays are of different lengths, or if any 
	 * of the successive pairs is unequal).</li>
	 * <li>If the two objects are of any other (pair of) types, it simple calls o1.isEqual(o2).</li>
	 * </ul> 
	 * 
	 * @param o1 The first object to compare.
	 * @param o2 The second object to compare.
	 * @return True if the two objects satisfy the above conditions, false otherwise.
	 */
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
	
	/**
	 * Determines whether two JSONObjects are "equal," where equality is determined in a key-order-independent
	 * manner.
	 * @param o1
	 * @param o2
	 * @return If either object has a key that the other lacks, or if one object's key value returns false
	 * when JSONUtils.isEqual() is called on that object, and given the other objects key value as the second
	 * argument.
	 */
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
