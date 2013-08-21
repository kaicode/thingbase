package com.kaicube.thingbase.dao;

public interface DAO {

	long store(String entityType, String entity) throws Exception;

	String load(String entityType, long id) throws Exception;

}
