package cn.com.sina.like.WebService;

import java.util.List;

public class LikeService implements ILikeService {

	@Override
	public void setLike(String userId, String tarId, boolean like) {
		
	}

	@Override
	public long getLikeCount(String tarId) {
		return 0L;
	}

	@Override
	public List<String> getLikeList(String userId, String tarId) {
		return null;
	}

}
