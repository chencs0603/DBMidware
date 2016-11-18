package personal.chencs.learn.dbmidware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作工具类
 * @author chencs
 *
 */
public class DBUtils {
	
	// 设置连接数据库的相关常量
	private static final String driverName = "com.mysql.jdbc.Driver";
	private static final String serverIp = "localhost";
	private static final String serverPort = "3306";
	private static final String databaseName = "demo";
	private static final String url = "jdbc:mysql:" + "//" + serverIp + ":" + serverPort + "/" + databaseName;
	private static final String username = "chencs";
	private static final String password = "ccs";
	
	static{
		try {
			Class.forName(driverName);// 加载驱动
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * 执行SQL语句
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int execute(String sql, Object[] params) throws SQLException {
		// 要支持事物回滚必须将autoCommit设置为false，即不能自动提交
		Connection conn = getConnection(false);
		PreparedStatement stmt = null;
		int affectRows = -1;
		try {
			stmt = createPreparedStatement(conn, sql, params);
			affectRows = stmt.executeUpdate();
			/**
			 * bug:当update表中数据时，然后立即select的结果不是update之后的数据，需要连续执行select两次才行（
			 * 如频繁地挂失解挂会出现这种现象）
			 * 解决方法：MySQL的默认隔离级别是REPEATABLE_READ(可重复读)，需要改成READ_COMMITTED
			 * （已提交读），可以读取提交后的数据
			 */
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			free(stmt);
			free(conn);
		}
		return affectRows;
	}
	
	/**
	 * 查询操作，返回bean列表
	 * @param sql
	 * @param params
	 * @param mapper
	 * @return
	 * @throws SQLException
	 */
	public List<?> queryForBean(String sql, Object[] params,
			RowMapper<?> mapper) throws SQLException {
		Connection conn = getConnection(false);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object> list = null;
		try {
			stmt = createPreparedStatement(conn, sql, params);
			rs = stmt.executeQuery();
			list = new ArrayList<Object>();
			while (rs.next()) {
				list.add(mapper.mapRow(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			free(rs);
			free(stmt);
			free(conn);
		}
		return list;
	}
 
	/**
	 * 查询操作，返回bean列表
	 * @param sql
	 * @param mapper
	 * @return
	 * @throws SQLException
	 */
	public List<?> queryForBean(String sql, RowMapper<?> mapper)
			throws SQLException {
		return queryForBean(sql, new Object[] {}, mapper);
	}

	/**
	 * 查询操作，返回Map
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryForMap(String sql, Object[] params)
			throws SQLException {
		Connection conn = getConnection(false);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = createPreparedStatement(conn, sql, params);
			rs = stmt.executeQuery();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			while (rs.next()) {
				map = new HashMap<String, Object>(columnCount);
				for (int i = 0; i < columnCount; i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getObject(i + 1));// ResultSetMetaData的序列号是从1开始的而不是从0开始的
				}
				list.add(map);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			free(rs);
			free(stmt);
			free(conn);
		}
		return null;
	}

	/**
	 * 查询操作，返回Map
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryForMap(String sql)
			throws SQLException {
		return queryForMap(sql, new Object[] {});
	}

	/**
	 * 构造PreparedStatement
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement createPreparedStatement(Connection conn,
			String sql, Object[] params) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(sql);
		for (int i = 0; i < params.length; i++)
			stmt.setObject(i + 1, params[i]);
		return stmt;
	}
	
	/**
	 * 获取连接
	 * @param autoCommit
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection(boolean autoCommit) throws SQLException{
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			if (!autoCommit)
				conn.setAutoCommit(autoCommit);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 关闭ResultSet
	 * @param x
	 */
	public void free(ResultSet x) {
		if (x != null)
			try {
				x.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 关闭Connection
	 * @param conn
	 */
	public void free(Connection conn) {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 关闭PreparedStatement
	 * @param x
	 */
	public void free(PreparedStatement x) {
		if (x != null)
			try {
				x.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}
