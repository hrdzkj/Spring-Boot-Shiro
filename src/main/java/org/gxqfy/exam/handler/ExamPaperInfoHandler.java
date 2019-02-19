package org.gxqfy.exam.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gxqfy.exam.po.ExamPaperInfo;
import org.gxqfy.exam.po.GradeInfo;
import org.gxqfy.exam.po.ResponseBean;
import org.gxqfy.exam.service.ExamPaperInfoService;
import org.gxqfy.exam.service.GradeInfoService;
import org.gxqfy.exam.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  *
  * <p>Title: ExamPaperInfoHandler</p>
  * <p>Description: 试卷</p>
  * @author: taohan
  * @date: 2018-8-16
  * @time: 下午4:35:43
  * @version: 1.0
  */

@Controller
@SuppressWarnings("all")
public class ExamPaperInfoHandler {

	@Autowired
	private ExamPaperInfoService examPaperInfoService;
	@Autowired
	private GradeInfoService gradeInfoService;
	@Autowired
	private GradeInfo grade;
	@Autowired
	private ExamPaperInfo examPaper;
	
	private Logger logger = LoggerFactory.getLogger(ExamPaperInfoHandler.class);
	
	
	/**
	 * 获取试卷信息
	 * @param gradeId 科室编号
	 * @param startPage 起始页 默认第一页
	 * @param pageShow 页容量 默认10
	 * @return 
	 */
	@RequestMapping("/api/examPapers") //尚未测试该接口
	public ResponseBean getCourses(@RequestParam(value = "gradeId", required = false) Integer gradeId,
			@RequestParam(value="startPage", required=false, defaultValue="1") Integer startPage,
			@RequestParam(value="pageShow", required=false, defaultValue="10") Integer pageShow ) {
		
		if (gradeId != null) {
			grade.setGradeId(gradeId);
			examPaper.setGrade(grade);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		//计算当前查询起始数据索引
		int startIndex = (startPage-1) * pageShow;
		map.put("examPaper", examPaper);
		map.put("startIndex", startIndex);
		map.put("pageShow", pageShow);
		List<ExamPaperInfo> examPapers = examPaperInfoService.getExamPapers(map);
		
		//获取试卷总量
		int examPaperTotal = examPaperInfoService.getExamPpaerTotal();
		
		//计算总页数
		int pageTotal = 1;
		if (examPaperTotal % pageShow == 0)
			pageTotal = examPaperTotal / pageShow;
		else
			pageTotal = examPaperTotal / pageShow + 1;	
		
		//拼接返回的json
		Map<String, Object> resultMap= new HashMap<>();
		resultMap.put("examPapers", examPapers);
		resultMap.put("pageTotal", pageTotal);
		resultMap.put("pageNow", startPage);

		return new ResponseBean(ResultCode.CODE_OK, "", resultMap);
	}

	/**
	 * 根据编号获取试卷信息
	 * @param examPaperId 试卷编号
	 * @return
	 */
	@RequestMapping("/api/examPaper/{examPaperId}")
	@ResponseBody
	public ResponseBean getCourseById(@PathVariable("examPaperId") Integer examPaperId) {
		ExamPaperInfo paper = examPaperInfoService.getExamPaper(examPaperId);
		String msg = paper==null?"无试卷信息":"";
		return new ResponseBean(ResultCode.CODE_OK, msg, paper);
	}


}
