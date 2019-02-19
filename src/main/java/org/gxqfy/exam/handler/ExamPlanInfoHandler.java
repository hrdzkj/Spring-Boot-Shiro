package org.gxqfy.exam.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gxqfy.exam.po.ClassInfo;
import org.gxqfy.exam.po.CourseInfo;
import org.gxqfy.exam.po.ExamPaperInfo;
import org.gxqfy.exam.po.ExamPlanInfo;
import org.gxqfy.exam.po.GradeInfo;
import org.gxqfy.exam.po.ResponseBean;
import org.gxqfy.exam.service.ClassInfoService;
import org.gxqfy.exam.service.CourseInfoService;
import org.gxqfy.exam.service.ExamPaperInfoService;
import org.gxqfy.exam.service.ExamPlanInfoService;
import org.gxqfy.exam.util.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



/**
  *
  * <p>Title: ExamPlanInfoHandler</p>
  * <p>Description: 考试安排</p>
  * @author: taohan
  * @date: 2018-8-22
  * @time: 下午4:06:51
  * @version: 1.0
  */

@Controller
@SuppressWarnings("all")
public class ExamPlanInfoHandler {

	@Autowired
	private ExamPlanInfoService examPlanInfoService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private CourseInfoService courseInfoService;
	@Autowired
	private ExamPaperInfoService examPaperInfoService;
	@Autowired
	private ExamPaperInfo examPaper;
	@Autowired
	private GradeInfo grade;
	
	private Logger logger = LoggerFactory.getLogger(ExamPlanInfoHandler.class);
	
	
	/**
	 * 获取所有待考记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/api/examPlans",method=RequestMethod.POST)
	public ResponseBean getExamPlans() {
		List<ExamPlanInfo> examPlans = examPlanInfoService.getExamPlans(null);
		String message = (examPlans==null||examPlans.isEmpty())?"暂无开始安排":"";
		return new ResponseBean(ResultCode.CODE_OK, message, examPlans);
	}
	
	

	/**
	 * 查询职工待考信息
	 * @param classId 职工所在层级编号
	 * @param gradeId 职工所在科室百年好
	 * @param studentId 职工编号  实际上这个参数可以不要
	 * @return
	 */
	@RequestMapping("/api/willexams")
	@ResponseBody
	public ResponseBean getStudentWillExam(
			@RequestParam("classId") Integer classId,
			@RequestParam("gradeId") Integer gradeId,
			@RequestParam(value="studentId", required=false) Integer studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classId", classId);
		map.put("gradeId", gradeId);
		List<ExamPlanInfo> examPlans = examPlanInfoService.getStudentWillExam(map);
				String msg = (examPlans==null||examPlans.isEmpty())?"占无待考记录":"";
		return new ResponseBean(ResultCode.CODE_OK, msg, examPlans);
	}
	
	
	
	/**
	 * 定时刷新考试安排记录，将过期考试移除
	 * 周一至周五 每隔15分钟刷新一次
	 */
	@Scheduled(cron="0 */15 * * * MON-FRI")
	public void refreshExamPlan() {
		List<ExamPlanInfo> examPlans = examPlanInfoService.getExamPlans(null);
		logger.info("刷新待考记录, SIZE "+examPlans.size());
		if (examPlans.size() > 0) {
			for (ExamPlanInfo examPlanInfo : examPlans) {
				String beginTime = examPlanInfo.getBeginTime();
				int examPaperTime = examPlanInfo.getExamPaper().getExamPaperTime();
				/** 验证是否可移除 */
				if (validateExamPaerBeOverdue(beginTime, examPaperTime)) {
					logger.info("待考试卷 "+examPlanInfo.getExamPaper().getExamPaperId()+" 已经过期，即将移除");
					//移除过期考试安排
					int row = examPlanInfoService.isRemoveExamPlan(examPlanInfo.getExamPlanId());
				} else {
					logger.info("待考试卷 "+examPlanInfo.getExamPaper().getExamPaperId()+" 暂未过期，无法移除");
					continue;
				}
			} 
		}
	}
	
	
	/**
	 * 验证试卷是否过期
	 * @param beginTime 考试开始时间
	 * @param examTime 考试时间
	 * @return
	 */
	private boolean validateExamPaerBeOverdue(String beginTime, int examTime) {
		boolean flag = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date beginTimeDate = sdf.parse(beginTime);
			Long beginTimeTime = beginTimeDate.getTime();
			
			/** 转换考试时间为毫秒单位 */
			int examTimeSecond = examTime * 60 * 1000;
			
			Date nowDate = new Date();
			Long nowDateTime = nowDate.getTime();
			
			/** 当前时间超过了 考试结束时间，即为过期记录 */
			if(nowDateTime > (beginTimeTime+examTimeSecond)) {
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
}
