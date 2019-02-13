package org.inlighting.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.inlighting.entity.UserEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
	@Autowired
	private UserMapper UserMapper;

	@Test
	public void testInsert() {
		System.out.println("-------------->hello world");
		try {
			UserMapper.insert(new UserEntity("aa", "a123456"));
			UserMapper.insert(new UserEntity("bb", "b123456"));
			UserMapper.insert(new UserEntity("cc", "b123456"));
			Assert.assertEquals(3, UserMapper.getAll().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQuery() throws Exception {
		List<UserEntity> users = UserMapper.getAll();
		if (users == null || users.size() == 0) {
			System.out.println("is null");
		} else {
			System.out.println(users.toString());
		}
	}

	@Test
	public void testUpdate() throws Exception {
		UserEntity user = UserMapper.getOne(6l);
		if (user != null) {
			System.out.println(user.toString());
			user.setNickName("neo");
			UserMapper.update(user);
			Assert.assertTrue(("neo".equals(UserMapper.getOne(6l).getNickName())));
		}
	}

}