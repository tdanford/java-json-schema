package tdanford.json.schema;

import static java.lang.String.*;

/**
 * JSONType is implemented by classes which represent individual JSON schemas or types.  
 *   
 * A JSONType determines a subset of Objects, through its <tt>contains</tt> method.
 * 
 * Given a JSONType, users should call the {@link JSONType.contains} method on values to see if each 
 * value is in the type.  
 *
 * @author Timothy Danford
 **/
public interface JSONType { 

	/**
	 * The central method of JSONType; the type encompasses the values for which <tt>contains</tt> returns <tt>true</tt>.
	 *
	 * @return <tt>true</tt>if the given Object satisfies the type, <tt>false</tt> otherwise.
	 */
	public boolean contains(Object obj);

	/**
	 * Returns a non-null String explanation, suitable for display to a user, explaining why the given object fails to conform to the JSONType's <tt>contains</tt> method.
	 *
	 * @arg obj A value for which <tt>contains(obj)</tt> returns <tt>false</tt>.
	 * @return an explanatory string if <tt>contains(arg)</tt> is <tt>false</tt>, or <tt>null</tt> if <tt>contains(arg)</tt> is <tt>true</tt>.
	 */
	public java.lang.String explain(Object obj);

	/**
	 * @return <tt>true</tt> if the type will accept "missing" (i.e. <tt>null</tt>) values, 
	 * <tt>false</tt> otherwise.
	 **/
	public boolean isOptional();
	
	public static abstract class AbstractType implements JSONType { 
		
		public java.lang.String explain(Object obj) { return null; }
		public boolean isOptional() { return false; }
	}
	
	public static class Everything extends AbstractType { 
		public boolean contains(Object obj) { 
			return obj != null;
		}
	}
	
	public static class Empty extends AbstractType { 
		public boolean contains(Object obj) { 
			return false;
		}
		public java.lang.String explain(Object obj) {
			return java.lang.String.format(
					"REJECT: \"%s\" not in Empty", java.lang.String.valueOf(obj));
		}
	}

	public static class String extends AbstractType { 
		public boolean contains(Object obj) { 
			return obj != null && obj instanceof java.lang.String;
		}

		public java.lang.String explain(Object obj) {
			java.lang.String typeName = "String";
			if(contains(obj)) { 
				return null;
			} else if (obj == null) { 
				return java.lang.String.format("REJECT: null value");
			} else {  
				return java.lang.String.format(
						"REJECT: \"%s\" has type %s not in %s", 
						java.lang.String.valueOf(obj), 
						obj.getClass().getSimpleName(), 
						typeName);
			}
		}
	}

	public static class Integer extends AbstractType { 
		public boolean contains(Object obj) { 
			if(obj == null) { return false; }
			if(obj instanceof java.lang.Integer) { return true; }
			try { 
				java.lang.Integer.parseInt(obj.toString());
				return true;
			} catch(NumberFormatException e) { 
				return false;
			}
		}
		public java.lang.String explain(Object obj) {
			if(obj == null) { return format("REJECT: null value"); } 
			if(obj instanceof java.lang.Integer) { return null; }
			try { 
				java.lang.Integer.parseInt(obj.toString());
				return null;
			} catch(NumberFormatException e) { 
				return format("REJECT: %s", e.getMessage());
			}
		}
	}

	public static class Double extends AbstractType { 
		public boolean contains(Object obj) { 
			if(obj == null) { return false; }
			if(obj instanceof java.lang.Double) { return true; }
			try { 
				java.lang.Double.parseDouble(obj.toString());
				return true;
			} catch(NumberFormatException e) { 
				return false;
			}
		}
		public java.lang.String explain(Object obj) {
			if(obj == null) { return format("REJECT: null value"); } 
			if(obj instanceof java.lang.Double) { return null; }
			try { 
				java.lang.Double.parseDouble(obj.toString());
				return null;
			} catch(NumberFormatException e) { 
				return format("REJECT: %s", e.getMessage());
			}
		}
	}

	public static class Boolean extends AbstractType { 
		public boolean contains(Object obj) { 
			if(obj == null) { return false; }
			if(obj instanceof java.lang.Boolean) { return true; }
			try { 
				java.lang.Boolean.parseBoolean(obj.toString());
				return true;
			} catch(NumberFormatException e) { 
				return false;
			}
		}
		public java.lang.String explain(Object obj) {
			if(obj == null) { return format("REJECT: null value"); } 
			if(obj instanceof java.lang.Boolean) { return null; }
			try { 
				java.lang.Boolean.parseBoolean(obj.toString());
				return null;
			} catch(NumberFormatException e) { 
				return format("REJECT: %s", e.getMessage());
			}
		}
	}
}

