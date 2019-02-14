package org.gxqfy.exam.service;

import java.util.List;
import java.util.Map;

import org.gxqfy.exam.po.ClassInfo;
import org.springframework.stereotype.Repository;


/**
  *
  * <p>Title: ClassInfoService</p>
  * <p>Description: 层级信息 Service</p>
  * @author: taohan
  * @date: 2018-8-13
  * @time: 下午2:08:29
  * @version: 1.0
  */

@Repository
public interface ClassInfoService {

	// 获取层级集合
	public List<ClassInfo> getClasses(ClassInfo classInfo);

	// 添加层级
	public int isAddClass(ClassInfo classInfo);

	// 删除层级
	public int isDelClass(int classId);

	// 根据编号获取层级信息
	public ClassInfo getClassById(int classId);
	
	//根据当前层级带教职工编号获取当前层级信息
	public ClassInfo getClassByTeacherId(int teacherId);

	// 修改层级信息
	public int isUpdateClass(ClassInfo classInfo);
	
	//获取指定科室下的层级集合
	public List<ClassInfo> getClassByGradeId(int gradeId);
	
	public Map<String, Object> getStudentCountForClass(Integer gradeId);
}
