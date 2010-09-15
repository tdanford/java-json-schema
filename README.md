
Purpose
=======

A validator for JSON objects, using JSON Schema, written in Java.  

http://json-schema.org/

As part of testing another project, a web app, I need to ensure that the responses I get from a web server are complete and well-formed according to JSON schema documents in the requirements.  This library forms a part of the testing harness for that application.  

Usage
=====

The simplest way to use the schema is to parse the schema object (int a "type"), parse the JSON object, and then ask if the first "contains" the second.

    JSONType type = new JSONObjectType(new SchemaEnv(), new JSONObject(... schema string ...));
	JSONObject value = new JSONObject(... value string ...); 
	if(type.contains(value)) { 
		...
	}

More involved examples include loading mutually recursive definitions from a directory of files, using the SchemaEnv class. 
