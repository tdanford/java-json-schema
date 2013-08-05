package tdanford.json.schema;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Lazy type loading for JSON schema files.  
 * 
 * Objects of this class correspond to files on the filesystem, each given by a {@link java.io.File},
 * which contain JSON Schema expressions.  However, the JSON schema expression is only loaded and parsed
 * the first time that either <tt>contains</tt> or <tt>explain</tt> is called on this object: <em>lazy</em> 
 * loading.  This is so that we can support resolution of mutually-recursive types, from files in a single
 * directory. 
 * 
 * @author Timothy Danford
 *
 */
public class JSONResourceType extends AbstractType {

	private java.lang.String resource;
	private JSONType fileType;
	private SchemaEnv env;

	public JSONResourceType(SchemaEnv env, java.lang.String resource) {
		this.env = env;
        this.resource = resource;
		fileType = null;
	}

    public void loadType() {
        ClassLoader loader = getClass().getClassLoader();
        try {
            Reader reader = new InputStreamReader(loader.getResourceAsStream(this.resource), "UTF-8");
            try {
                loadType(reader);
            } finally {
                reader.close();
            }
        } catch(IOException e) {
            fileType = new Empty();
            throw new IllegalArgumentException(resource, e);
        }
    }

    private void loadType(Reader reader) {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        int read = -1;
        try {
            while((read = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, read);
            }

            java.lang.String jsonString = builder.toString();
            JSONObject obj = new JSONObject(jsonString);
            fileType = new JSONObjectType(env, obj);

        } catch(IOException e) {
            fileType = new Empty();
            throw new IllegalArgumentException(resource, e);

        } catch (JSONException e) {
            fileType = new Empty();
            throw new IllegalArgumentException(resource, e);

        } catch (SchemaException e) {
            fileType = new Empty();
            throw new IllegalArgumentException(resource, e);
        }
    }

	public boolean contains(Object obj) {
        if(fileType == null) { loadType(); }
		return fileType.contains(obj);
	}

	public java.lang.String explain(Object obj) {
        if(fileType == null) { loadType(); }
		return fileType.explain(obj);
	}
}
