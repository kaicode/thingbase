package com.kaicube.thingbase.dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.h2.jdbc.JdbcConnection;

public class DAOH2Impl implements DAO {

	public DAOH2Impl() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		JdbcConnection connection = getConnection();
		try {
            connection.prepareStatement("drop table IF EXISTS entity;").executeUpdate();
			connection.prepareStatement("create table IF NOT EXISTS entity (entityType varchar, id BIGINT AUTO_INCREMENT, document CLOB, UNIQUE(entityType, id));").executeUpdate();
		} finally {
			connection.close();
		}
	}

	@Override
	public long store(String entityType, String entityString) throws SQLException, XPathExpressionException, TransformerConfigurationException, TransformerException {
		JdbcConnection connection = getConnection();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement("insert into entity (entityType, document) values (?, ?);");
			prepareStatement.setString(1, entityType);
			prepareStatement.setString(2, entityString);
			prepareStatement.executeUpdate();
			ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
			generatedKeys.next();
			return generatedKeys.getLong(1);
		} finally {
			connection.close();
		}
	}
	
	@Override
	public String load(String entityType, long id) throws Exception {
		JdbcConnection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select document from entity where entityType = ? and id = ?");
			preparedStatement.setString(1, entityType);
			preparedStatement.setLong(2, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.first()) {
				return resultSet.getString(1);
			} else {
				return null;
			}
		} finally {
			connection.close();
		}
	}

	private JdbcConnection getConnection() throws SQLException {
		JdbcConnection connection = (JdbcConnection) DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
		return connection;
	}
	
}
