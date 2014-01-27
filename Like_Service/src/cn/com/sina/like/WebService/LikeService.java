package cn.com.sina.like.WebService;

import java.util.List;

import cn.com.sina.like.Cache.RedisClient;
import cn.com.sina.like.DAO.FeedDAO;
import cn.com.sina.like.DAO.FriendsDAO;

public class LikeService implements ILikeService {
	private RedisClient redisClient = RedisClient.INSTANCE;
	private FriendsDAO friendDao = FriendsDAO.getInstance();
	private FeedDAO feedDAO = FeedDAO.getInstance();

	private static final String USER_FRIENDS_SUFFIX = "_friends";
	private static final String USER_FEED_SUFFIX = "_feed";
	private static final String FEED_OTHERS_SUFFIX = "_other";

	@Override
	public void setLike(long userId, long feedId, boolean like) {
		String urerIdString = Long.toString(userId);
		String feedIdString = Long.toString(feedId);
		if(like){
			feedDAO.insert(feedId, userId);
			redisClient.setList(urerIdString + USER_FEED_SUFFIX, feedIdString);
			List<Long> friendsList = FriendsDAO.getInstance().select(userId);
			if(friendsList!=null){
				for(long friendUid: friendsList){
					
				}
			}
		}
	}

	@Override
	public long getLikeUsersCount(long feedId) {
		return 0;
	}

	@Override
	public List<Long> getLikeUsersList(long userId, long feedId) {
		return null;
	}

}
