package cn.com.sina.like.WebService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import cn.com.sina.like.Cache.RedisClientManager;
import cn.com.sina.like.DAO.FeedDAO;
import cn.com.sina.like.DAO.FriendsDAO;

public class LikeService implements ILikeService {
	private static final String LOG_TAG = LikeService.class.getSimpleName();

	private RedisClientManager redisClientMgr = RedisClientManager
			.getInstance();
	private FriendsDAO friendDao = FriendsDAO.getInstance();
	private FeedDAO feedDAO = FeedDAO.getInstance();

	private static final String USER_PREFIX = "user_";
	private static final String FEED_PREFIX = "feed_";

	public static void main(String[] args) {
		System.out
				.println(new LikeService().getLikeUsersList("2209", "100", 20, 0));
	}

	@Override
	public void setLike(String userId, String feedId) {
		String feedIdString = FEED_PREFIX + feedId;
		if (feedDAO.insert(feedId, userId)) {
			if (!redisClientMgr.getMaster().addToList(feedIdString, userId)) {
				copyFeedToCache(feedId);
			}
		}

	}

	@Override
	public void cancelLike(String userId, String feedId) {
		String feedIdString = FEED_PREFIX + feedId;
		if (feedDAO.delete(feedId, userId)) {
			if (redisClientMgr.getMaster().deleteFromList(feedIdString, userId) == 0L) {
				copyFeedToCache(feedId);
			}
		}

	}

	@Override
	public long getLikeUsersCount(String feedId) {
		String feedIdString = FEED_PREFIX + feedId;
		long count = redisClientMgr.getOneClient().getListLen(feedIdString);
		if (count == 0) {
			// count = copyFeedToCache(feedId).size();
		} else {
			// 减去哨兵的计数
			count--;
		}
		return count;
	}

	private ArrayList<String> copyFeedToCache(String feedId) {
		ArrayList<String> likeUsers = feedDAO.selectLikeUsers(feedId);
		redisClientMgr.getMaster().setList(FEED_PREFIX + feedId, likeUsers);
		return likeUsers;
	}

	@Override
	public List<String> getLikeUsersList(String userId, String feedId, int num,
			int startNum) {
		String userIdString = USER_PREFIX + userId;
		String feedIdString = FEED_PREFIX + feedId;
		List<String> userFriends = redisClientMgr.getOneClient().getList(
				userIdString);

		if (userFriends != null) {
			if (userFriends.size() == 0) {
				// 没有哨兵，cache中不存在该key
				// userFriends = copyFriendToCache(userId);
			} else {
				// 移除哨兵
				userFriends.remove(new Long(-1L));
			}
		}
		List<String> likeUsers = redisClientMgr.getOneClient().getList(
				feedIdString);
		if (likeUsers != null) {
			if (likeUsers.size() == 0) {
				// 没有哨兵，cache中不存在该key
				// likeUsers = copyFeedToCache(userId);
			} else {
				// 移除哨兵
				likeUsers.remove(new Long(-1L));
			}
		}
		LinkedList<String> result = new LinkedList<String>();

		HashSet<String> userSet = new HashSet<String>();
		for (String user : likeUsers) {
			userSet.add(user);
		}
		for (String friend : userFriends) {
			if (userSet.contains(friend)) {
				result.addFirst(friend);
				userSet.remove(friend);
			}
		}
		for (String user : userSet) {
			result.addLast(user);
		}

		// if (likeUsers.removeAll(userFriends)) {
		// userFriends.retainAll(likeUsers);
		// userFriends.addAll(likeUsers);
		// if (userFriends.size() > startNum) {
		// result = userFriends.subList(startNum,
		// startNum + num < userFriends.size() ? startNum + num
		// : userFriends.size());
		// }
		// } else {
		// if (likeUsers.size() > startNum) {
		// result = likeUsers.subList(startNum, startNum + num < likeUsers
		// .size() ? startNum + num : likeUsers.size());
		// }
		// }
		return result.subList(
				startNum < result.size() ? startNum : result.size(), startNum
						+ num < result.size() ? startNum + num : result.size());
		// return null;
	}

	private ArrayList<String> copyFriendToCache(String userId) {
		ArrayList<String> friends = friendDao.selectFriendsList(userId);
		redisClientMgr.getMaster().setList(USER_PREFIX + userId, friends);
		return friends;
	}
}
