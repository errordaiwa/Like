package cn.com.sina.like.WebService;

import java.util.List;

public interface ILikeService {
	void setLike(long userId, long feedId, boolean like);
	long getLikeUsersCount(long feedId);
	List<Long> getLikeUsersList(long userId, long feedId);
}
