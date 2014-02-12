package cn.com.sina.like.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultParser {
	void parseResult(ResultSet resultSet) throws SQLException;
}
