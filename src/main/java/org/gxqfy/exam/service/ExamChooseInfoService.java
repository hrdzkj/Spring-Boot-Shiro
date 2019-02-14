package org.gxqfy.exam.service;

import java.util.List;
import java.util.Map;

import org.gxqfy.exam.po.ExamChooseInfo;
import org.springframework.stereotype.Repository;


/**
  *
  * <p>Title: ExamChooseInfoService</p>
  * <p>Description: </p>
  * @author: taohan
  * @date: 2018-8-25
  * @time: 上午10:34:29
  * @version: 1.0
  * @Repository用于标注数据访问组件,Spring2.5为我们引入了组件自动扫描机制，他在类路径下寻找标注了上述注解的类，并把这些类纳入进spring容器中管理。
  */

@Repository
public interface ExamChooseInfoService {

	public ExamChooseInfo getChooseWithIds(Map<String, Object> map);

	public int updateChooseWithIds(ExamChooseInfo examChoose);
	
	public int addChoose(Map<String, Object> map);
	
	public List<ExamChooseInfo> getChooseInfoWithSumScore(Map<String, Object> map);
	
	public List<ExamChooseInfo> getChooseInfoWithExamSubject(Map<String, Object> map);
}
