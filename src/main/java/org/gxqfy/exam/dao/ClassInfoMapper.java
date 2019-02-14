package org.gxqfy.exam.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.gxqfy.exam.po.ClassInfo;
import org.springframework.stereotype.Repository;


/**
  *
  * <p>Title: ClassInfoMapper</p>
  * <p>Description: 层级信息代理接口</p>
  * @author: taohan
  * @date: 2018-8-13
  * @time: 下午2:08:29
  * @version: 1.0
  */

@Repository
public interface ClassInfoMapper {

	//获取层级信息集合
	public List<ClassInfo> getClasses(ClassInfo classInfo);
	
	//添加层级
	public int isAddClass(ClassInfo classInfo);
	
	//删除层级
	public int isDelClass(int classId);
	
	//根据层级编号获取层级信息
	public ClassInfo getClassById(int classId);
	
	//根据当前层级带教职工编号获取当前层级信息
	public ClassInfo getClassByTeacherId(int teacherId);
	
	//修改层级信息
	public int isUpdateClass(ClassInfo classInfo);
	
	//获取指定科室下的层级集合
	public List<ClassInfo> getClassByGradeId(int gradeId);
	
	//获取各(指定科室下)层级下的职工总量
	//指定某一列的值作为 Map 的键
	@MapKey("className")
	public Map<String, Object> getStudentCountForClass(Integer gradeId);
}
