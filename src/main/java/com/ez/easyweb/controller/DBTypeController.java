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

import com.ez.easyweb.db.DBTypeOperations;
import com.google.gson.Gson;

@RestController
@RequestMapping("/types")
public class DBTypeController {
	
	
	@PostMapping("/insert/{table}")
	public ResponseEntity<Object> insert(@PathVariable final String table,@Valid @RequestBody String requestBody) {
		HashMap<String, Object>  response =  new HashMap<>();
		DBTypeOperations dbOps = new DBTypeOperations(table);

		//response =  dbOps.delete(table, UUID);
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	@PostMapping("/update/{table}")
	public ResponseEntity<Object> update(@PathVariable final String table,@Valid @RequestBody String requestBody) {
		HashMap<String, Object>  response =  new HashMap<>();
		DBTypeOperations dbOps = new DBTypeOperations(table);

		//response =  dbOps.update(table, UUID);
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@PostMapping("/delete/{table}")
	public ResponseEntity<Object> delete(@PathVariable final String table) {
		HashMap<String, Object>  response =  new HashMap<>();
		DBTypeOperations dbOps = new DBTypeOperations(table);

		//response =  dbOps.delete(table, UUID);
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@GetMapping("/list/")
	public ResponseEntity<Object> selectAll(@PathVariable final String table) {
		HashMap<String, Object>  response =  new HashMap<>();
		DBTypeOperations dbOps = new DBTypeOperations(table);

		response =  dbOps.selectAll();
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
	@GetMapping("/select/{table}")
	public ResponseEntity<Object> select(@PathVariable final String table) {
		HashMap<String, Object>  response =  new HashMap<>();
		DBTypeOperations dbOps = new DBTypeOperations(table);

		//response =  dbOps.select(table);
		Gson gson = new Gson();
		return ResponseEntity.ok().body(gson.toJson(response));

	}
	
}
