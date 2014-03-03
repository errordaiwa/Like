package cn.com.sina.like.service;

import java.util.List;

public interface ILikeService {
	void setLike(long userId, long feedId);
	
	void cancelLike(long userId, long feedId);

	long getLikeUsersCount(long feedId);

	List<Long> getLikeUsersList(long userId, long feedId, int num, int startNum);
}
