package personal.chencs.learn.dbmidware;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行映射接口：将ResultSet结果集的每行记录映射成实体类。 具体实现与实体类相关
 * 
 * @author chencs
 * 
 * @param <T>具体实体类
 */
public interface RowMapper<T> {
	public abstract T mapRow(ResultSet rs) throws SQLException;
}
