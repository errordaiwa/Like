package cn.com.sina.like.DAO;

public abstract class BaseDAO {
	private DBConnectionPool pool;

	public BaseDAO() {
		pool = DBConnectionPool.getInstance();
	}

}
