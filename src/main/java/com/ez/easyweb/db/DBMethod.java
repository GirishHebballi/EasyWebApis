package com.ez.easyweb.db;

import java.util.HashMap;

public interface DBMethod {
	HashMap<String,Object>  executeSQLRequest(String sql);
	HashMap<String,Object> execute(String request, String uuid);

}
