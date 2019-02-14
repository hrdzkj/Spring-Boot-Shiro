package org.gxqfy.exam.service;

import java.util.List;

import org.gxqfy.exam.po.GradeInfo;
import org.springframework.stereotype.Repository;


/**
  *
  * <p>Title: GradeInfoService</p>
  * <p>Description: </p>
  * @author: taohan
  * @date: 2018-8-14
  * @time: 上午9:56:49
  * @version: 1.0
  */

@Repository
public interface GradeInfoService {

	//获取所有科室
	public List<GradeInfo> getGrades();
	
	//根据编号获取科室
	public GradeInfo getGradeById(int gradeId);
	
	//添加科室
	public int isAddGrade(GradeInfo grade);
	
	//删除科室
	public int isDelGrade(int gradeId);
	
	//修改科室
	public int isUpdateGrade(GradeInfo grade);
}
