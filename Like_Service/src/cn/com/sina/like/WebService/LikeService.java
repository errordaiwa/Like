package cn.com.sina.like.WebService;

import java.util.ArrayList;
import java.util.List;

import cn.com.sina.like.Cache.LikeRedisClient;
import cn.com.sina.like.DAO.FeedDAO;
import cn.com.sina.like.DAO.FriendsDAO;

public class LikeService implements ILikeService {
	private static final String LOG_TAG = LikeService.class.getSimpleName();
	private LikeRedisClient redisClient = LikeRedisClient.getInstance();
	private FriendsDAO friendDao = FriendsDAO.getInstance();
	private FeedDAO feedDAO = FeedDAO.getInstance();

	private static final String USER_PREFIX = "user_";
	private static final String FEED_PREFIX = "feed_";

	@Override
	public void setLike(long userId, long feedId) {
		String feedIdString = FEED_PREFIX + feedId;
		if (feedDAO.insert(feedId, userId)) {
			if (!redisClient.addLongToList(feedIdString, userId)) {
				copyFeedToCache(feedId);
			}
		}

	}

	@Override
	public void cancelLike(long userId, long feedId) {
		String feedIdString = FEED_PREFIX + feedId;
		if (feedDAO.delete(feedId, userId)) {
			if (redisClient.deleteLongFromList(feedIdString, userId) == 0L) {
				copyFeedToCache(feedId);
			}
		}

	}

	@Override
	public long getLikeUsersCount(long feedId) {
		String feedIdString = FEED_PREFIX + feedId;
		long count = redisClient.getListLen(feedIdString);
		if (count == 0) {
			count = copyFeedToCache(feedId).size();
		} else {
			// 减去哨兵的计数
			count--;
		}
		return count;
	}

	private ArrayList<Long> copyFeedToCache(long feedId) {
		ArrayList<Long> likeUsers = feedDAO.selectLikeUsers(feedId);
		redisClient.setListLong(FEED_PREFIX + feedId, likeUsers);
		return likeUsers;
	}

	@Override
	public List<Long> getLikeUsersList(long userId, long feedId, int num,
			int startNum) {
		String userIdString = USER_PREFIX + userId;
		String feedIdString = FEED_PREFIX + feedId;
		ArrayList<Long> userFriends = null;
		ArrayList<Long> likeUsers = null;
		userFriends = redisClient.getListLong(userIdString);
		if (userFriends != null) {
			if (userFriends.size() == 0) {
				// 没有哨兵，cache中不存在该key
				userFriends = copyFriendToCache(userId);
			} else {
				// 移除哨兵
				userFriends.remove(new Long(-1L));
			}
		}
		likeUsers = redisClient.getListLong(feedIdString);
		if (likeUsers != null) {
			if (likeUsers.size() == 0) {
				// 没有哨兵，cache中不存在该key
				likeUsers = copyFeedToCache(userId);
			} else {
				// 移除哨兵
				likeUsers.remove(new Long(-1L));
			}
		}
		List<Long> result = null;
		if (likeUsers.removeAll(userFriends)) {
			userFriends.retainAll(likeUsers);
			userFriends.addAll(likeUsers);
			if (userFriends.size() > startNum) {
				result = userFriends.subList(startNum,
						startNum + num < userFriends.size() ? startNum + num
								: userFriends.size());
			}
		} else {
			if (likeUsers.size() > startNum) {
				result = likeUsers.subList(startNum, startNum + num < likeUsers
						.size() ? startNum + num : likeUsers.size());
			}
		}
		return result;
	}

	private ArrayList<Long> copyFriendToCache(long userId) {
		ArrayList<Long> friends = friendDao.selectFriendsList(userId);
		redisClient.setListLong(USER_PREFIX + userId, friends);
		return friends;
	}
}
