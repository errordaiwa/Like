package cn.com.sina.like.WebService;

import java.util.List;

public interface ILikeService {
	void setLike(String userId, String tarId, boolean like);
	long getLikeCount(String tarId);
	List<String> getLikeList(String userId, String tarId);
}
