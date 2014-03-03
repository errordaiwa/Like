package cn.com.sina.like.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeedDAO extends AbstractDAO {
	private static final String LOG_TAG = FeedDAO.class.getSimpleName();
	private static final String TABLE_NAME = "feed";
	private static final String COLUMN_FEED_ID = "feed_id";
	private static final String COLUMN_LIKE_ID = "like_id";

	private static FeedDAO instance = new FeedDAO();
	
	private FeedDAO() {
		super();
	}

	public static FeedDAO getInstance() {
		return instance;
	}

	public ArrayList<Long> selectLikeUsers(long feedId) {
		String sql = "select * from " + TABLE_NAME + " where " + COLUMN_FEED_ID
				+ "=" + feedId;
		final ArrayList<Long> likeUsersList = new ArrayList<Long>();
		excuteSql(sql, new ResultParser() {

			@Override
			public void parseResult(ResultSet resultSet) throws SQLException {

				while (resultSet.next()) {
					likeUsersList.add(resultSet.getLong(COLUMN_LIKE_ID));
				}
			}
		});
		return likeUsersList;
	}

	public boolean insert(long feedId, long likeId) {
		String sql = "insert into " + TABLE_NAME + " (" + COLUMN_FEED_ID + ", "
				+ COLUMN_LIKE_ID + ") values(" + feedId + "," + likeId + ")";
		return excuteSql(sql, null);
	}

	public boolean delete(long feedId, long likeId) {
		String sql = "delete from " + TABLE_NAME + " where " + COLUMN_FEED_ID
				+ "=" + feedId + " and " + COLUMN_LIKE_ID + "=" + likeId;
		return excuteSql(sql, null);
	}

	public void update(long fromUid, long toUid) {
		// TODO useless method
	}
}
