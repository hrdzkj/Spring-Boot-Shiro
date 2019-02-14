package org.gxqfy.exam.dao;

import java.util.List;
import java.util.Map;

import org.gxqfy.exam.po.ExamHistoryInfo;
import org.gxqfy.exam.po.ExamHistoryPaper;
import org.springframework.stereotype.Repository;

/**
  *
  * <p>Title: ExamHistoryPaperMapper</p>
  * <p>Description: </p>
  * @author: taohan
  * @date: 2018-8-29
  * @time: 下午4:44:07
  * @version: 1.0
  */

@Repository
public interface ExamHistoryPaperMapper {

	//查询考试历史信息，针对前台职工查询
	public List<ExamHistoryPaper> getExamHistoryToStudent(int studentId);
	
	public int isAddExamHistory(Map<String, Object> map);
	
	public int getHistoryInfoWithIds(Map<String, Object> map);
	
	//查询考试历史信息，针对后台带教查询
	public List<ExamHistoryInfo> getExamHistoryToTeacher();
}
