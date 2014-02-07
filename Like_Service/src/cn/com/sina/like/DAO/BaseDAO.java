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

	protected boolean excuteSql(String sql, ResultParser parser) {
		Connection con = pool.getConnection();
		if (con == null) {
			Log.w(LOG_TAG, "Failed to get Connection!");
			return false;
		}
		try {
			Statement statement = null;
			try {
				statement = con.createStatement();
				if (statement.execute(sql)) {
					ResultSet result = statement.getResultSet();
					if (parser != null)
						parser.parseResult(result);
				}
				return true;
			} catch (SQLException e) {
				Log.e(LOG_TAG, "Failed to excute SQL!");
				Log.e(LOG_TAG, Log.getExceptionStackTrace(e));
				return false;
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
