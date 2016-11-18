package personal.chencs.learn.dao;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import personal.chencs.learn.entity.User;


public class UserDaoTest {
	
	private UserDao userDao;

	@Before
	public void setUp() throws Exception {
		userDao = new UserDao();
	}

	@Test
	public void testAdd() {
		User user = new User();

		user.setName("chencs");
		user.setPassword("123456");
		user.setAge(27);
		
		boolean result = userDao.add(user);
		Assert.assertTrue(result);
	}
	/**
	 * 无法指定测试方法的顺序，即无法保证这个方法最后执行，所以暂时注释
	 */
//	@Test
//	public void testDelete() {
//		String name = "chencs";
//		boolean result = userDao.deleteByName(name);
//		Assert.assertTrue(result);
//	}
	
	@Test
	public void testUpdate(){
		User user = new User();

		user.setPassword("ccs");
		user.setAge(27);
		String name = "chencs";
		
		boolean result = userDao.updateByName(name, user);
		Assert.assertTrue(result);
	}

	@Test
	public void testQueryByName() {
		String name = "chencs";
		User user = userDao.queryByName(name);
		
		System.out.println(user);
		Assert.assertNotNull(user);
	}

	@Test
	public void testQueryAll() {
		ArrayList<User> list = (ArrayList<User>) userDao.queryAll();
		Assert.assertTrue(list.size() > 0);
	}

}
