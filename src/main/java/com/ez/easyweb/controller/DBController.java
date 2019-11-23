package com.ez.easyweb.controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ez.easyweb.db.DBConstants;
import com.ez.easyweb.db.DBMethod;
import com.ez.easyweb.db.DBMethodFactory;
import com.ez.easyweb.db.DBOperations;
import com.google.gson.Gson;

@RestController
@RequestMapping("/tables")
public class DBController {
	
	@PostMapping("/insert/{table}")
	public ResponseEntity<Object> insert(@PathVariable final String table, @Valid @RequestBody String requestBody) {
		
		DBMethodFactory dbMethodFactory = new DBMethodFactory();
		DBMethod dbMethod = dbMethodFactory.getDBMethod(DBConstants.INSERT, table);
		HashMap<String, Object>  response = dbMethod.execute(requestBody, null);
		
/*		DBOperations dbOps = new DBOperations(table);
		HashMap<String, Object>  response =  dbOps.insert(requestBody);*/
		Gson gson = new Gson();		
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@PostMapping("/update/{table}")
	public ResponseEntity<Object> update(@PathVariable final String table, @Valid @RequestBody String requestBody) {
/*		DBOperations dbOps = new DBOperations(table);
		HashMap<String, Object>  response =  dbOps.update(requestBody);*/
		DBMethodFactory dbMethodFactory = new DBMethodFactory();
		DBMethod dbMethod = dbMethodFactory.getDBMethod(DBConstants.UPDATE, table);
		HashMap<String, Object>  response = dbMethod.execute(requestBody, null);
		Gson gson = new Gson();		
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@PostMapping("/createTable/{table}")
	public ResponseEntity<Object> insertTable(@PathVariable final String table) {
/*		DBOperations dbOps = new DBOperations(table);
		
		HashMap<String, Object>  response =  dbOps.insertTable();*/
		DBMethodFactory dbMethodFactory = new DBMethodFactory();
		DBMethod dbMethod = dbMethodFactory.getDBMethod(DBConstants.INSERT_TABLE, table);
		HashMap<String, Object>  response = dbMethod.execute(null, null);
		Gson gson = new Gson();		
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@GetMapping("/selectAll/{table}")
	public ResponseEntity<Object> selectAll(@PathVariable final String table) {
		HashMap<String, Object>  response =  new HashMap<>();
/*		DBOperations dbOps = new DBOperations(table);

		response =  dbOps.selectAll();*/
		DBMethodFactory dbMethodFactory = new DBMethodFactory();
		DBMethod dbMethod = dbMethodFactory.getDBMethod(DBConstants.LIST, table);
		response = dbMethod.execute(null, null);
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@GetMapping("/select/{table}/{UUID}")
	public ResponseEntity<Object> selectAll(@PathVariable final String table,@PathVariable final String UUID) {
		HashMap<String, Object>  response =  new HashMap<>();
/*		DBOperations dbOps = new DBOperations(table);

		response =  dbOps.select(table, UUID);*/
		DBMethodFactory dbMethodFactory = new DBMethodFactory();
		DBMethod dbMethod = dbMethodFactory.getDBMethod(DBConstants.SELECT, table);
		response = dbMethod.execute(null, UUID);
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@PostMapping("/delete/{table}/{UUID}")
	public ResponseEntity<Object> delete(@PathVariable final String table,@PathVariable final String UUID) {
		HashMap<String, Object>  response =  new HashMap<>();
/*		DBOperations dbOps = new DBOperations(table);

		response =  dbOps.delete(table, UUID);*/
		DBMethodFactory dbMethodFactory = new DBMethodFactory();
		DBMethod dbMethod = dbMethodFactory.getDBMethod(DBConstants.DELETE, table);
		response = dbMethod.execute(null, UUID);
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	
	
}
