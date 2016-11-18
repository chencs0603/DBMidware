package personal.chencs.learn.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import personal.chencs.learn.dbmidware.DBUtils;
import personal.chencs.learn.dbmidware.RowMapper;
import personal.chencs.learn.entity.User;


/**
 * 对User表的增删改查操作的封装
 * @author chencs
 *
 */
public class UserDao {

	private DBUtils dbUtlis = new DBUtils();

	/**
	 * 添加记录
	 * @param user
	 * @return
	 */
	public boolean add(User user) {
		String sql = "insert into User(`id`, `name`, `password`, `age`) values(?, ?, ?, ?)";
		Object[] params = new Object[4];
		params[0] = user.getId();
		params[1] = user.getName();
		params[2] = user.getPassword();
		params[3] = user.getAge();

		try {
			int affects = dbUtlis.execute(sql, params);
			if (1 == affects) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除记录
	 * @param name
	 * @return
	 */
	public boolean deleteByName(String name) {
		String sql = "delete from User where `name`=?";
		Object[] params = new Object[1];
		params[0] = name;

		try {
			int affects = dbUtlis.execute(sql, params);
			if (1 == affects) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 更新记录
	 * @param name
	 * @param user
	 * @return
	 */
	public boolean updateByName(String name, User user){
		String sql = "update user set `password` = ?, `age` = ?  where `name` = ?";
		Object[] params = new Object[3];
		params[0] = user.getPassword();
		params[1] = user.getAge();
		params[2] = name;
		
		try {
			int affects = dbUtlis.execute(sql, params);
			if (1 == affects) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 查询单个记录
	 * @param name
	 * @return
	 */
	public User queryByName(String name) {
		String sql = "select * from user where name = ?";
		Object[] params = new Object[1];
		params[0] = name;
		try {
			@SuppressWarnings("unchecked")
			List<User> list = (List<User>) dbUtlis.queryForBean(sql, params, new RowMapper<User>() {
				public User mapRow(ResultSet rs) throws SQLException {
					User user = new User();
					
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					user.setAge(rs.getInt("age"));
					
					return user;
				}
			});

			if (1 == list.size()) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询所有记录
	 * @return
	 */
	public List<User> queryAll() {
		String sql = "select * from User";

		try {
			@SuppressWarnings("unchecked")
			List<User> list = (List<User>) dbUtlis.queryForBean(sql, new RowMapper<User>() {
				public User mapRow(ResultSet rs) throws SQLException {
					User user = new User();

					user.setId(rs.getInt("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					user.setAge(rs.getInt("age"));
					
					return user;
				}
			});
			if (0 < list.size()) {
				return list;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}