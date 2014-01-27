package cn.com.sina.like.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedDAO extends BaseDAO {
	private static final String LOG_TAG = FeedDAO.class.getSimpleName();
	private static final String TABLE_NAME = "feed";
	private static final String COLUMN_FEED_ID = "feed_id";
	private static final String COLUMN_LIKE_ID = "like_id";

	private static FeedDAO instance = new FeedDAO();
	
	public static void main(String[] ags){
		FeedDAO.getInstance().insert(1, 1);
		FeedDAO.getInstance().insert(1, 2);
		FeedDAO.getInstance().insert(1, 3);
	}

	private FeedDAO() {
		super();
	}

	public static FeedDAO getInstance() {
		return instance;
	}

	public List<Long> selectLikeUsers(long feedId) {
		String sql = "select * from " + TABLE_NAME + " where " + COLUMN_FEED_ID
				+ "=" + feedId;
		final ArrayList<Long> likeUsersList = new ArrayList<Long>();
		excuteSql(sql, true, new ResultParser() {

			@Override
			public void parseResult(ResultSet resultSet) throws SQLException {

				while (resultSet.next()) {
					likeUsersList.add(resultSet.getLong(COLUMN_LIKE_ID));
				}
			}
		});
		return likeUsersList;
	}

	public void insert(long feedId, long likeId) {
		String sql = "insert into " + TABLE_NAME + " (" + COLUMN_FEED_ID + ", "
				+ COLUMN_LIKE_ID + ") values(" + feedId + "," + likeId + ")";
		excuteSql(sql, false, null);
	}

	public void delete(long feedId, long likeId) {
		String sql = "delete from " + TABLE_NAME + " where " + COLUMN_FEED_ID
				+ "=" + feedId + " and " + COLUMN_LIKE_ID + "=" + likeId;
		excuteSql(sql, false, null);
	}

	public void update(long fromUid, long toUid) {
		// TODO useless method
	}
}
