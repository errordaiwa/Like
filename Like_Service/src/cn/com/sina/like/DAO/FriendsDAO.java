package cn.com.sina.like.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.com.sina.like.Utils.Log;

public class FriendsDAO extends BaseDAO {
	private static final String LOG_TAG = FriendsDAO.class.getSimpleName();
	private static final String TABLE_NAME = "friends";
	private static final String COLUMN_FROM_UID = "from_uid";
	private static final String COLUMN_TO_UID = "to_uid";
//	private static final String COLUMN_CTIME = "ctime";

	public static void main(String[] args) {
		new FriendsDAO().insert(1, 2);
		Log.i(LOG_TAG, new FriendsDAO().select(1).get(0).toString());
	}
	
	private static FriendsDAO instance = new FriendsDAO();
	
	private FriendsDAO(){
		super();
	}
	
	public static FriendsDAO getInstance(){
		return instance;
	}

	public List<Long> select(long uid) {
		String sql = "select * from " + TABLE_NAME + " where "
				+ COLUMN_FROM_UID + "=" + uid + " and " + COLUMN_TO_UID
				+ " in (select " + COLUMN_FROM_UID + " from " + TABLE_NAME
				+ " where " + COLUMN_TO_UID + "=" + uid + ")";
		// System.out.println(sql);
		final ArrayList<Long> friendsList = new ArrayList<Long>();
		excuteSql(sql, true, new ResultParser() {

			@Override
			public void parseResult(ResultSet resultSet) throws SQLException {

				while (resultSet.next()) {
					friendsList.add(resultSet.getLong(COLUMN_TO_UID));
				}
			}
		});
		return friendsList;
	}

	public void insert(long fromUid, long toUid) {
		String sql = "insert into " + TABLE_NAME + " (" + COLUMN_FROM_UID
				+ ", " + COLUMN_TO_UID + ") values(" + fromUid + "," + toUid
				+ ")";
		excuteSql(sql, false, null);
	}

	public void delete(long fromUid) {
		// TODO useless method
	}

	public void update(long fromUid, long toUid) {
		// TODO useless method
	}
}
