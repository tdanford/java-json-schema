package tdanford.json.schema;

public interface JSONType { 

	public boolean contains(Object obj);
	
	public static class Everything implements JSONType { 
		public boolean contains(Object obj) { 
			return obj != null;
		}
	}
	
	public static class Empty implements JSONType { 
		public boolean contains(Object obj) { 
			return false;
		}
	}

	public static class String implements JSONType { 
		public boolean contains(Object obj) { 
			return obj != null && obj instanceof java.lang.String;
		}
	}

	public static class Integer implements JSONType { 
		public boolean contains(Object obj) { 
			return obj != null && obj instanceof java.lang.Integer;
		}
	}

	public static class Double implements JSONType { 
		public boolean contains(Object obj) { 
			return obj != null && obj instanceof java.lang.Double;
		}
	}

	public static class Boolean implements JSONType { 
		public boolean contains(Object obj) { 
			return obj != null && obj instanceof java.lang.Boolean;
		}
	}
}

