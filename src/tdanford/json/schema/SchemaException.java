package tdanford.json.schema;

public class SchemaException extends Exception {

	public SchemaException() {
	}

	public SchemaException(String message) {
		super(message);
	}

	public SchemaException(Throwable cause) {
		super(cause);
	}

	public SchemaException(String message, Throwable cause) {
		super(message, cause);
	}
}
