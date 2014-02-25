package cn.com.sina.like.WebService;

import java.util.List;

public interface ILikeService {
	void setLike(String userId, String feedId);
	
	void cancelLike(String userId, String feedId);

	long getLikeUsersCount(String feedId);

	List<String> getLikeUsersList(String userId, String feedId, int num, int startNum);
}
