package com.foryuum.frontend.common.service;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManager {
	protected Connection con = null;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected Connection getConnection() throws Exception {
		// Xml Config에서 설정 값을 Get 한다.
		// getXmlDBConfig();

		try {
			String resource = "config/properties/jdbc.properties";
			Properties properties = new Properties();
			Properties props = System.getProperties();
			Reader reader = Resources.getResourceAsReader(resource);
			properties.load(reader);

			String driver = properties.getProperty("driverClassName");
			String url = properties.getProperty("url");
			String uid = props.getProperty("jasypt.encrypt.uid");
			String pass = props.getProperty("jasypt.encrypt.pass");
			Class.forName(driver);
			con = DriverManager.getConnection(url, uid, pass);
		} catch (ClassNotFoundException e) {
			log.error("Connetion Exception : ", e);
		} catch (SQLException e) {
			log.error("", e);
		}
		return con;
	}

	protected void startTransaction() {
		try {
			this.con = getConnection();
			this.con.setAutoCommit(false);
		} catch (Exception se) {
			log.debug("Start Transaction Exception", se);
		}
	}

	protected void endTransaction() {
		try {
			if (this.con != null) {
				this.con.commit();
				this.con.setAutoCommit(true);
				this.con.close();
			}
		} catch (SQLException se) {
			try {
				con.rollback();
			} catch (Exception e2) {
				log.debug(e2.getMessage());
			}
			log.debug("End Transaction Exception", se);
		}
	}

	protected void openConnection() {
		try {
			this.con = getConnection();
		} catch (Exception se) {
			log.debug("Start Connection Exception", se);
		}
	}

	protected void closeConnection() {
		try {
			if (this.con != null) {
				this.con.close();
			}
		} catch (SQLException se) {
			log.debug("End Connection Exception", se);
		}
	}

}
