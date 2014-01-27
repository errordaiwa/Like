package cn.com.sina.like.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.com.sina.like.Utils.Log;

public abstract class BaseDAO {
	private static final String LOG_TAG = BaseDAO.class.getSimpleName();
	protected DBConnectionPool pool;

	protected BaseDAO() {
		pool = DBConnectionPool.getInstance();
	}

	protected void excuteSql(String sql, boolean hasResult, ResultParser parser) {
		Connection con = pool.getConnection();
		if (con == null) {
			Log.w(LOG_TAG, "Failed to get Connection!");
			return;
		}
		try {
			Statement statement = null;
			try {
				statement = con.createStatement();
				if (hasResult) {
					ResultSet result = statement.executeQuery(sql);
					if (parser != null)
						parser.parseResult(result);
				} else {
					statement.execute(sql);
				}
			} catch (SQLException e) {
				Log.e(LOG_TAG, "Failed to excute SQL!");
				Log.e(LOG_TAG, Log.getExceptionStackTrace(e));
			} finally {
				try {
					if (statement != null)
						statement.close();
				} catch (SQLException e) {
					Log.e(LOG_TAG, "Failed to close statement!");
					Log.e(LOG_TAG, Log.getExceptionStackTrace(e));
				}
			}
		} finally {
			pool.releaseConnection(con);
		}
	}
}
