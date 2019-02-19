package org.gxqfy.exam.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.javassist.expr.NewArray;
import org.gxqfy.exam.component.JWTComponent;
import org.gxqfy.exam.component.UserComponent;
import org.gxqfy.exam.dao.common.CommonStudentMapper;
import org.gxqfy.exam.po.ClassInfo;
import org.gxqfy.exam.po.ExamChooseInfo;
import org.gxqfy.exam.po.ExamHistoryPaper;
import org.gxqfy.exam.po.ExamPaperInfo;
import org.gxqfy.exam.po.ExamSubjectMiddleInfo;
import org.gxqfy.exam.po.GradeInfo;
import org.gxqfy.exam.po.ResponseBean;
import org.gxqfy.exam.po.StudentInfo;
import org.gxqfy.exam.po.SubjectInfo;
import org.gxqfy.exam.service.ClassInfoService;
import org.gxqfy.exam.service.ExamChooseInfoService;
import org.gxqfy.exam.service.ExamHistoryPaperService;
import org.gxqfy.exam.service.ExamPaperInfoService;
import org.gxqfy.exam.service.ExamSubjectMiddleInfoService;
import org.gxqfy.exam.service.StudentInfoService;
import org.gxqfy.exam.util.ResultCode;
import org.gxqfy.exam.util.TextUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.fabric.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * <p>
 * Title: StudentInfoHandler
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author: taohan
 * @date: 2018-8-16
 * @time: 上午10:22:22
 * @version: 1.0
 */

@Controller
@SuppressWarnings("all")
public class StudentInfoHandler {

	@Autowired
	private StudentInfoService studentInfoService;
	@Autowired
	private ClassInfoService classInfoService;
	@Autowired
	private ExamSubjectMiddleInfoService examSubjectMiddleInfoService;
	@Autowired
	private ExamHistoryPaperService examHistoryPaperService;
	@Autowired
	private ExamChooseInfoService examChooseInfoService;
	@Autowired
	private ExamSubjectMiddleInfo esm;
	@Autowired
	private ClassInfo classInfo;
	@Autowired
	private ExamPaperInfo examPaper;
	@Autowired
	private GradeInfo grade;
	@Autowired
	private StudentInfo student;
	@Autowired
	private ExamPaperInfoService examPaperInfoService;
	@Autowired
	private JWTComponent mJwtUtil;
	@Autowired
	private CommonStudentMapper commonStudentMapper;
	@Autowired
	private UserComponent userComponent;
	private Logger logger = LoggerFactory.getLogger(StudentInfoHandler.class);


	
	/**
	 * 根据编号获取职工信息
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping("/student/{studentId}")
	@ResponseBody
	public ResponseBean getCourseById(@PathVariable("studentId") Integer studentId) {
		StudentInfo student = studentInfoService.getStudentById(studentId);
		List<ClassInfo> classes = classInfoService.getClasses(null);
		Map<String,Object> result = new HashMap<>();
		result.put("student", student);
		result.put("classes", classes);
		return new ResponseBean(ResultCode.CODE_OK, "", result);
	}



	/**
	 * 
	 * @param student 登录职工
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/common/studentLogin", method = RequestMethod.POST)
	public ResponseBean studentLogin(@RequestParam("studentAccount") String studentAccount,
			@RequestParam("studentPwd") String studentPwd) {
		StudentInfo loginStudent = studentInfoService.getStudentByAccountAndPwd(studentAccount);
		if (loginStudent != null && loginStudent.getStudentPwd().equals(studentPwd)) {
			String token = mJwtUtil.generateToken(loginStudent);
			userComponent.putStudent(loginStudent);
			return new ResponseBean(ResultCode.CODE_OK, "登录成功", token);
		} else {
			return new ResponseBean(ResultCode.CODE_1, "用户名或密码错误", null);

		}
	}


	/**
	 * 注册
	 */
	@ResponseBody
	@RequestMapping(value = "/common/studentReg", method = RequestMethod.POST)
	public ResponseBean studentRegister(@RequestParam("account") String studentAccount,
			@RequestParam("pwd") String studentPwd, @RequestParam("classId") Integer classId,
			@RequestParam("email") String email) throws IOException {
		// 拦截空
		if (TextUtil.isEmpty(studentAccount)) {
			return new ResponseBean(ResultCode.CODE_1, "用户不能为空", null);
		}

		if (TextUtil.isEmpty(studentPwd)) {
			return new ResponseBean(ResultCode.CODE_1, "密码不能为空", null);
		}

		if (TextUtil.isEmpty(email)) {
			return new ResponseBean(ResultCode.CODE_1, "邮箱不能为空", null);
		}

		// 检查是用户名和邮箱是否已经注册
		StudentInfo student = commonStudentMapper.selectStudentBystudentName(studentAccount);
		if (student != null) {
			return new ResponseBean(ResultCode.CODE_1, "用户名已经被注册", null);
		}

		student = commonStudentMapper.selectStudentBystudentEmail(email);
		if (student != null) {
			return new ResponseBean(ResultCode.CODE_1, "邮箱已经被注册", null);
		}

		// 写库
		student = new StudentInfo();
		student.setStudentName(studentAccount);
		student.setStudentAccount(studentAccount);
		student.setEmail(email);
		student.setStudentPwd(studentPwd);
		classInfo.setClassId(classId);
		student.setClassInfo(classInfo);

		int row = studentInfoService.isAddStudent(student);
		if (row > 0) {
			return new ResponseBean(ResultCode.CODE_OK, "注册成功", null);
		} else {
			return new ResponseBean(ResultCode.CODE_OK, "注册失败", null);
		}

	}


	/**
	 * 职工进入考试
	 * 
	 * @param classId
	 *            层级编号
	 * @param examPaperId
	 *            试卷编号
	 * @param studentId
	 *            考生编号
	 * @param examTime
	 *            考试时间
	 * @param beginTime
	 *            考试开始时间
	 * @param gradeId
	 *            科室编号
	 * @param session
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping("/api/begin")
	@ResponseBody
	public ResponseBean beginExam(@RequestParam("classId") Integer classId,
			@RequestParam("examPaperId") Integer examPaperId,
			@RequestParam(value = "studentId", required = false) Integer studentId,
			@RequestParam("examTime") Integer examTime, @RequestParam("beginTime") String beginTime,
			@RequestParam("gradeId") Integer gradeId, HttpSession session){

		
		/*
		 * 查询该考试当前进入的试卷是否已经在历史记录中存在 如果存在，则不能再次进入考试； 反之进入考试
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("examPaperId", examPaperId);
		int count = examHistoryPaperService.getHistoryInfoWithIds(map);
		if (count >= 1) {
			return new ResponseBean(ResultCode.CODE_1, "你已经考试过了", null);
		}
		
		
		logger.info("职工 " + studentId + " 进入考试 层级 " + classId + " 试卷 " + examPaperId);
		ExamPaperInfo examPaper = new ExamPaperInfo();
		examPaper.setExamPaperId(examPaperId);
		esm.setExamPaper(examPaper);
		// 获取试卷 试题集合
		List<ExamSubjectMiddleInfo> esms = examSubjectMiddleInfoService.getExamPaperWithSubject(esm);
		List<SubjectInfo> subjectInfos = new ArrayList<>();
		for (ExamSubjectMiddleInfo examSubjectMiddleInfo : esms) {
			subjectInfos.add(examSubjectMiddleInfo.getSubject());
			examPaper = examSubjectMiddleInfo.getExamPaper();
		}
		examPaper.setSubjectNum(subjectInfos.size());
		logger.info("考试试题总量 " + esms.size());

		// 获取当前考生在当前试卷中已选答案记录
		Map<String, Object> choosedMap = new HashMap<String, Object>();
		choosedMap.put("studentId", studentId);
		choosedMap.put("examPaperId", examPaperId);
		List<ExamChooseInfo> chooses = examChooseInfoService.getChooseInfoWithSumScore(choosedMap);
		
		//拼接json信息
		Map<String, Object> model = new HashMap<>();
		if (chooses == null || chooses.size() == 0) {
			model.put("chooses", null);
		} else {
			model.put("chooses", chooses);
		}

		model.put("examPaperInfo", examPaper);
		model.put("subjectList", subjectInfos);
		model.put("beginTime", beginTime);
		model.put("classId", classId);
		model.put("gradeId", gradeId);
		return new ResponseBean(ResultCode.CODE_OK, "", model);
	}

	/**
	 * 获取职工历史考试记录
	 * @param studentId职工编号
	 * @return
	 */
	@RequestMapping("/api/history/{studentId}")
	@ResponseBody
	public ResponseBean getExamHistoryInfo(@PathVariable("studentId") Integer studentId) {
		// 获取历史考试信息记录集合
		List<ExamHistoryPaper> ehps = examHistoryPaperService.getExamHistoryToStudent(studentId);
		return new ResponseBean(ResultCode.CODE_OK, "", ehps);
	}

	/**
	 * 考生提交考试
	 * 
	 * @param studentId
	 * @param examPaperId
	 * @param classId
	 * @param gradeId
	 * @return
	 */
	@RequestMapping(value = "/api/submit", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseBean examSubmit(@RequestParam("studentId") Integer studentId,
			@RequestParam("examPaperId") Integer examPaperId, @RequestParam("classId") Integer classId,
			@RequestParam("gradeId") Integer gradeId) {
		logger.info("职工 " + studentId + " 提交了试卷 " + examPaperId);

		// 获取当前职工当前试卷所选择的全部答案
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("examPaperId", examPaperId);
		List<ExamChooseInfo> chooses = examChooseInfoService.getChooseInfoWithSumScore(map);
		logger.info("职工 " + studentId + " 共选择了 " + chooses.size() + " 道题");

		// 总分记录
		double sumScore = 0;
		for (ExamChooseInfo choose : chooses) {
			SubjectInfo subject = choose.getSubject();
			String chooseResult = choose.getChooseResult();
			String rightResult = subject.getRightResult();
			if (subject.getSubjectType() == 3) { // 填空题统计分数
				String[] rightResultArray = rightResult.split(",|，");
				String[] chooseResultArray = chooseResult.split(",");
				int rightBlank = 0;
				for (int i = 0; i < chooseResultArray.length; i++) {
					if (i < rightResultArray.length && chooseResultArray[i].equalsIgnoreCase(rightResultArray[i])) { // 答案正确
						rightBlank++;
					}
				}
				sumScore += (subject.getSubjectScore() / (double) rightResultArray.length) * rightBlank;
			} else { // 单选,多选题统计分数
				if (chooseResult.equals(rightResult)) { // 答案正确
					sumScore += subject.getSubjectScore();
				}
			}
		}

		/*
		 * 首先判断当前记录是否已经添加过 防止当前职工点击提交后，系统倒计时再次进行提交
		 */
		int count = examHistoryPaperService.getHistoryInfoWithIds(map);
		if (count == 0) {
			// 添加到历史记录
			DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
			map.put("examScore", decimalFormat.format(sumScore));
			int row = examHistoryPaperService.isAddExamHistory(map);
			logger.info("职工 " + studentId + " 提交的试卷 " + examPaperId + " 已成功处理，并添加到历史记录中");
		}

		Map result = new HashMap<>();
		result.put("sumScore", sumScore);
		result.put("classId", classId);
		result.put("studentId", studentId);
		return new ResponseBean(ResultCode.CODE_OK, "", result);
	}

	/**
	 * 职工回顾试卷 -- 后台带教查看也调用此方法
	 * 
	 * @param studentId
	 * @param examPaperId
	 * @param score
	 * @param examPaperName
	 * @param studentName
	 *            后台带教查看需传入职工姓名
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/api/review")
	@ResponseBody
	public ResponseBean reViewExam(@RequestParam("studentId") Integer studentId,
			@RequestParam("examPaperId") Integer examPaperId, @RequestParam("score") Integer score,
			@RequestParam("examPaperName") String examPaperName,
			@RequestParam(value = "studentName", required = false) String studentName)
			throws UnsupportedEncodingException {
	
			// 获取当前试卷的试题集合 -- 前台判断需要
			examPaper.setExamPaperId(examPaperId);
			esm.setExamPaper(examPaper);
			List<ExamSubjectMiddleInfo> esms = examSubjectMiddleInfoService.getExamPaperWithSubject(esm);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("studentId", studentId);
			map.put("examPaperId", examPaperId);

			// 获取当前回顾试卷 试题、选择答案 信息
			List<ExamChooseInfo> reviews = examChooseInfoService.getChooseInfoWithExamSubject(map);
	
			// 设置试卷名称、试卷总分
			Map<String, Object> result = new HashMap<>();
			result.put("examPaperName", examPaperName);
			result.put("score", score);;
			result.put("views", reviews);
			result.put("esms", esms);
			if (studentName != null) {
				result.put("studentName", studentName);
			}
			result.put("ExamedPaper", examPaperInfoService.getExamPaper(examPaperId));
			return new ResponseBean(ResultCode.CODE_OK, "", result);
	}

	/**
	 * 职工查看自己信息
	 * @param studentId
	 * @return
	 */
	@RequestMapping("/api/self/{studentId}")
	@ResponseBody
	public ResponseBean selfInfo(@PathVariable("studentId") Integer studentId) {
		StudentInfo studentInfo = studentInfoService.getStudentById(studentId);
		return new ResponseBean(ResultCode.CODE_OK, "", studentInfo);
	}

	
	/**
	 * 职工修改密码
	 */
	@ResponseBody
	@RequestMapping("/api/resetPassword")
	public ResponseBean isResetPwd(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd,
			@RequestParam("studentId") Integer studentId) throws IOException {
		StudentInfo student = studentInfoService.getStudentById(studentId);
		if (student == null) {
			return new ResponseBean(ResultCode.CODE_1, "查找用户失败，请联系系统管理员", null);
		}

		if (!oldPwd.equals(student.getStudentPwd())) {
			return new ResponseBean(ResultCode.CODE_1, "旧密码不正确", null);
		}

		student.setStudentId(studentId);
		student.setStudentPwd(newPwd);
		int row = studentInfoService.isResetPwdWithStu(student);
		if (row > 0) {
		   userComponent.removeStudent(student);
			return new ResponseBean(ResultCode.CODE_OK, "修改密码成功，请重新登录", null);
		} else {
			return new ResponseBean(ResultCode.CODE_OK, "修改密码失败", null);
		}
	}
}