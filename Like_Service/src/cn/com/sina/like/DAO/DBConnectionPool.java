package cn.com.sina.like.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;

public class DBConnectionPool {
	private static final String CONFIG_NAME = "mysql";
	private static final String MAX_CONNECTION_NUM = "mysql.config.maxConnectionNum";
	private static final String MYSQL_IP = "mysql.ip";
	private static final String MYSQL_PORT = "mysql.port";
	private static final String MYSQL_SCHEMA_NAME = "mysql.schemaName";
	private static final String MYSQL_USER_NAME = "mysql.userName";
	private static final String MYSQL_PASSWORD = "mysql.password";

	private int maxConnectionNum;
	private Vector<Connection> idleConnectionPool;
	private Vector<Connection> inUsedConnectionPool;
	private String mysqlIP;
	private String mysqlPort;
	private String mysqlSchemaName;
	private String mysqlUserName;
	private String mysqlPassword;

	private static DBConnectionPool instance = new DBConnectionPool();

	private DBConnectionPool() {
		ResourceBundle bundle = ResourceBundle.getBundle(CONFIG_NAME);
		maxConnectionNum = Integer
				.valueOf(bundle.getString(MAX_CONNECTION_NUM));
		mysqlIP = bundle.getString(MYSQL_IP);
		mysqlPort = bundle.getString(MYSQL_PORT);
		mysqlSchemaName = bundle.getString(MYSQL_SCHEMA_NAME);
		mysqlUserName = bundle.getString(MYSQL_USER_NAME);
		mysqlPassword = bundle.getString(MYSQL_PASSWORD);
		idleConnectionPool = new Vector<Connection>(maxConnectionNum);
		inUsedConnectionPool = new Vector<Connection>(maxConnectionNum);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO fatal error!
			e.printStackTrace();
		}
	}

	public static DBConnectionPool getInstance() {
		return instance;
	}

	public synchronized Connection getConnection() {
		if (idleConnectionPool.size() + inUsedConnectionPool.size() < maxConnectionNum) {
			try {
				String mysqlUri = "jdbc:mysql://" + mysqlIP + ":" + mysqlPort
						+ "/" + mysqlSchemaName;
				Connection con = DriverManager.getConnection(mysqlUri,
						mysqlUserName, mysqlPassword);
				inUsedConnectionPool.add(con);
				return con;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		while (idleConnectionPool.size() <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}

		Connection con = idleConnectionPool.get(0);
		idleConnectionPool.remove(0);
		inUsedConnectionPool.add(con);
		return con;
	}

	public synchronized void releaseConnection(Connection con) {
		inUsedConnectionPool.remove(con);
		idleConnectionPool.add(con);
		notify();
	}

	public void closeConnection() {
		try {
			for (Connection con : idleConnectionPool) {
				if (con != null)
					con.close();
			}
			for (Connection con : inUsedConnectionPool) {
				if (con != null)
					con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
