package org.gxqfy.exam.database;

import org.apache.ibatis.jdbc.Null;
import org.gxqfy.exam.mapper.UserMapper;
import org.gxqfy.exam.po.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//构建一个UserService来模拟数据库查询 并且把结果放到UserBean之中
@Component
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
    private UserEntity getStudentByName(String studentName) {
    	UserEntity user=userMapper.getStudentByName(studentName);
        return user;
    }
    
    public UserBean getUser(String studentName) {
    	UserEntity userEntity = getStudentByName(studentName);
    	if (userEntity==null) {
    		return null;
    	}
    	
        UserBean user = new UserBean();
        user.setUsername(studentName);
        user.setPassword(userEntity.getPassWord());
        return user;
        
        // 没有此用户直接返回null
    	/*
        if (! DataSource.getData().containsKey(username))
            return null;

        UserBean user = new UserBean();
        Map<String, String> detail = DataSource.getData().get(username);

        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        user.setPermission(detail.get("permission"));
        return user;
        */
    }
}
