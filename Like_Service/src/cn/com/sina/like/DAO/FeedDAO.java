package cn.com.sina.like.DAO;

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

	public ArrayList<String> selectLikeUsers(String feedId) {
		String sql = "select * from " + TABLE_NAME + " where " + COLUMN_FEED_ID
				+ "=" + feedId;
		final ArrayList<String> likeUsersList = new ArrayList<String>();
		excuteSql(sql, new ResultParser() {

			@Override
			public void parseResult(ResultSet resultSet) throws SQLException {

				while (resultSet.next()) {
					likeUsersList.add(Long.toString(resultSet.getLong(COLUMN_LIKE_ID)));
				}
			}
		});
		return likeUsersList;
	}

	public boolean insert(String feedId, String likeId) {
		String sql = "insert into " + TABLE_NAME + " (" + COLUMN_FEED_ID + ", "
				+ COLUMN_LIKE_ID + ") values(" + feedId + "," + likeId + ")";
		return excuteSql(sql, null);
	}

	public boolean delete(String feedId, String likeId) {
		String sql = "delete from " + TABLE_NAME + " where " + COLUMN_FEED_ID
				+ "=" + feedId + " and " + COLUMN_LIKE_ID + "=" + likeId;
		return excuteSql(sql, null);
	}

	public void update(String fromUid, String toUid) {
		// TODO useless method
	}
}
