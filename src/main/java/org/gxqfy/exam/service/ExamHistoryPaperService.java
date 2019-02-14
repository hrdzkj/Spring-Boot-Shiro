package org.gxqfy.exam.service;

import java.util.List;
import java.util.Map;

import org.gxqfy.exam.po.ExamHistoryInfo;
import org.gxqfy.exam.po.ExamHistoryPaper;
import org.springframework.stereotype.Repository;



/**
  *
  * <p>Title: ExamHistoryPaperService</p>
  * <p>Description: </p>
  * @author: taohan
  * @date: 2018-8-29
  * @time: 下午4:45:27
  * @version: 1.0
  */

@Repository
public interface ExamHistoryPaperService {

	//查询考试历史信息，针对前台职工查询
	public List<ExamHistoryPaper> getExamHistoryToStudent(int studentId);
	
	public int isAddExamHistory(Map<String, Object> map);
	
	public int getHistoryInfoWithIds(Map<String, Object> map);
	
	public List<ExamHistoryInfo> getExamHistoryToTeacher();
}
