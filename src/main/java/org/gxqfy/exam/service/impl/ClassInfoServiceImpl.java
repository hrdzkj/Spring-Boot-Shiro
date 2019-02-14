package org.gxqfy.exam.service.impl;

import java.util.List;
import java.util.Map;

import org.gxqfy.exam.dao.ClassInfoMapper;
import org.gxqfy.exam.po.ClassInfo;
import org.gxqfy.exam.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
  *
  * <p>Title: ClassInfoServiceImpl</p>
  * <p>Description: 层级信息 Service 实现类</p>
  * @author: taohan
  * @date: 2018-8-13
  * @time: 下午2:16:32
  * @version: 1.0
  */

@Service
public class ClassInfoServiceImpl implements ClassInfoService {

	@Autowired
	private ClassInfoMapper classInfoMapper;
	
	//获取层级集合
	public List<ClassInfo> getClasses(ClassInfo classInfo) {
		return classInfoMapper.getClasses(classInfo);
	}

	//添加层级
	public int isAddClass(ClassInfo classInfo) {
		return classInfoMapper.isAddClass(classInfo);
	}

	//删除层级
	public int isDelClass(int classId) {
		return classInfoMapper.isDelClass(classId);
	}

	public ClassInfo getClassById(int classId) {
		return classInfoMapper.getClassById(classId);
	}

	public int isUpdateClass(ClassInfo classInfo) {
		return classInfoMapper.isUpdateClass(classInfo);
	}

	//获取指定科室下的层级
	public List<ClassInfo> getClassByGradeId(int gradeId) {
		return classInfoMapper.getClassByGradeId(gradeId);
	}

	//获取各(指定科室下)层级下的职工总量
	public Map<String, Object> getStudentCountForClass(Integer gradeId) {
		return classInfoMapper.getStudentCountForClass(gradeId);
	}

	//根据当前层级带教职工编号获取当前层级信息
	public ClassInfo getClassByTeacherId(int teacherId) {
		return classInfoMapper.getClassByTeacherId(teacherId);
	}

}
