package cn.com.sina.like.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.sina.like.Utils.Log;

public class FriendsDAO extends AbstractDAO {
	private static final String LOG_TAG = FriendsDAO.class.getSimpleName();
	private static final String TABLE_NAME = "friends";
	private static final String COLUMN_FROM_UID = "from_uid";
	private static final String COLUMN_TO_UID = "to_uid";

	// private static final String COLUMN_CTIME = "ctime";

	private static FriendsDAO instance = new FriendsDAO();

	private FriendsDAO() {
		super();
	}

	public static FriendsDAO getInstance() {
		return instance;
	}

	public ArrayList<String> selectFriendsList(String uid) {
		String sql = "select * from " + TABLE_NAME + " where "
				+ COLUMN_FROM_UID + "=" + uid + " and " + COLUMN_TO_UID
				+ " in (select " + COLUMN_FROM_UID + " from " + TABLE_NAME
				+ " where " + COLUMN_TO_UID + "=" + uid + ")";
		final ArrayList<String> friendsList = new ArrayList<String>();
		excuteSql(sql, new ResultParser() {

			@Override
			public void parseResult(ResultSet resultSet) throws SQLException {

				while (resultSet.next()) {
					friendsList.add(Long.toString(resultSet.getLong(COLUMN_TO_UID)));
				}
			}
		});
		return friendsList;
	}

	public boolean insert(String fromUid, String toUid) {
		String sql = "insert into " + TABLE_NAME + " (" + COLUMN_FROM_UID
				+ ", " + COLUMN_TO_UID + ") values(" + fromUid + "," + toUid
				+ ")";
		return excuteSql(sql, null);
	}

	public void delete(String fromUid) {
		// TODO useless method
	}

	public void update(String fromUid, String toUid) {
		// TODO useless method
	}
}
