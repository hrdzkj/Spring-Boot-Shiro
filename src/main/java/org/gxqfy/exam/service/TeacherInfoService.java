package org.gxqfy.exam.service;

import java.util.List;
import java.util.Map;

import org.gxqfy.exam.po.TeacherInfo;
import org.springframework.stereotype.Repository;


/**
  *
  * <p>Title: TeacherInfoService</p>
  * <p>Description: </p>
  * @author: taohan
  * @date: 2018-8-13
  * @time: 下午3:15:35
  * @version: 1.0
  */

@Repository
public interface TeacherInfoService {

	//获取带教集合
	//public List<TeacherInfo> getTeachers(TeacherInfo teacher);
	public List<TeacherInfo> getTeachers(Map<String, Object> map);
	
	//根据带教账户获取带教信息
	public TeacherInfo getTeacherByAccount(String teacherAccount);
	
	//获取带教数据总量
	public int getTeacherTotal();
	
	//修改带教带教职工状态
	public int updateTeacherIsWork(TeacherInfo teacherInfo);
	
	//根据带教编号获取带教信息
	public TeacherInfo getTeacherById(int teacherId);
	
	//修改带教信息
	public int isUpdateTeacherInfo(TeacherInfo teacher);
	
	//添加带教信息
	public int isAddTeacherInfo(TeacherInfo teacher);
	
	//删除带教信息
	public int isDelTeacherInfo(int teacherId);
}
