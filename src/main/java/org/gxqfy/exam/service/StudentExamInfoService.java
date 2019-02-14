package org.gxqfy.exam.service;

import java.util.List;

import org.gxqfy.exam.po.StudentExamInfo;
import org.springframework.stereotype.Repository;


/**
  *
  * <p>Title: StudentExamInfoService</p>
  * <p>Description: </p>
  * @author: taohan
  * @date: 2018-9-19
  * @time: 上午10:08:46
  * @version: 1.0
  */

@Repository
public interface StudentExamInfoService {

	//后台带教根据查看某一层级下所有职工考试数量
	public List<StudentExamInfo> getStudentExamCountByClassId(int classId);
	
	//后台带教查看某一职工的考试信息
	public List<StudentExamInfo> getStudentExamInfo(int studentId);
	
	//后台带教查看指定层级中所有职工的平均成绩以及考试次数
	public List<StudentExamInfo> getAllStudentAvgScoreCount(int classId);
}
