package org.gxqfy.exam.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysql.fabric.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
  *
  * <p>Title: StudentInfoHandler</p>
  * <p>Description: </p>
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
	private CommonStudentMapper mCommonStudentMapper;
	
	private UserComponent mUserComponent;
	private Logger logger = LoggerFactory.getLogger(StudentInfoHandler.class);
    @Autowired
    public void setService(UserComponent userComponent) {
    	mUserComponent = userComponent;
    }

    
	@ResponseBody
	@RequestMapping(value="/common/401",method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseBean response401() {	
		return new ResponseBean(401, "Unauthorized", null);
	}
	
	
	/**
	 * 获取职工集合
	 * @param studentId 职工编号
	 * @param classId 层级编号
	 * @param gradeId 科室编号
	 * @param startPage 起始页 default=1
	 * @param pageShow 页容量 default=10
	 * @return
	 */
	@RequestMapping("/students")
	public ModelAndView getCourses(@RequestParam(value = "studentId", required = false) Integer studentId,
			@RequestParam(value = "classId", required = false) Integer classId,
			@RequestParam(value = "gradeId", required = false) Integer gradeId,
			@RequestParam(value="startPage", required=false, defaultValue="1") Integer startPage,
			@RequestParam(value="pageShow", required=false, defaultValue="10") Integer pageShow ) {
		//logger.info("获取职工集合  classId="+classId+", gradeId="+gradeId+", startPage="+startPage+", pageShow="+pageShow);
		ModelAndView model = new ModelAndView();
		model.setViewName("/admin/students");

		//查询条件处理
		StudentInfo student = new StudentInfo();
		if (studentId != null)
			student.setStudentId(studentId);
		if (classId != null) {
			classInfo.setClassId(classId);
			student.setClassInfo(classInfo);
		}
		if (gradeId != null) {
			grade.setGradeId(gradeId);
			student.setGrade(grade);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		//计算当前查询起始数据索引
		int startIndex = (startPage-1) * pageShow;
		map.put("student", student);
		map.put("startIndex", startIndex);
		map.put("pageShow", pageShow);
		List<StudentInfo> students = studentInfoService.getStudents(map);
		model.addObject("students", students);
		
		//获取职工总量
		int studentTotal = studentInfoService.getStudentTotal();
		//计算总页数
		int pageTotal = 1;
		if (studentTotal % pageShow == 0)
			pageTotal = studentTotal / pageShow;
		else
			pageTotal = studentTotal / pageShow + 1;			
		model.addObject("pageTotal", pageTotal);
		model.addObject("pageNow", startPage);

		return model;
	}

	/**
	 * 根据编号获取职工信息
	 * @param studentId
	 * @return
	 */
	@RequestMapping("/student/{studentId}")
	public ModelAndView getCourseById(@PathVariable("studentId") Integer studentId) {
		logger.info("获取职工 " + studentId);
		ModelAndView model = new ModelAndView();
		model.setViewName("/admin/studentedit");

		StudentInfo student = studentInfoService.getStudentById(studentId);
		model.addObject("student", student);
		List<ClassInfo> classes = classInfoService.getClasses(null);
		model.addObject("classes", classes);

		return model;
	}

	/**
	 * 添加/修改职工信息
	 * @param studentId
	 * @param isUpdate 操作标识
	 * @param studentName
	 * @param studentAccount
	 * @param studentPwd
	 * @param classId
	 * @return
	 */
	@RequestMapping(value = "/student/student", method = RequestMethod.POST)
	public String isUpdateOrAddCourse(
			@RequestParam(value = "studentId", required = false) Integer studentId,
			@RequestParam(value = "isupdate", required = false) Integer isUpdate,
			@RequestParam(value = "studentName", required = false) String studentName,
			@RequestParam("studentAccount") String studentAccount,
			@RequestParam("studentPwd") String studentPwd,
			@RequestParam("classId") Integer classId) {

		StudentInfo student = new StudentInfo();
			student.setStudentId(studentId);
			student.setStudentName(studentName);
			student.setStudentAccount(studentAccount);
			student.setStudentPwd(studentPwd);
			classInfo.setClassId(classId);
			student.setClassInfo(classInfo);

		if (isUpdate != null) {
			logger.info("修改职工 " + student + " 的信息");
			int row = studentInfoService.isUpdateStudent(student);
		} else {
			logger.info("添加职工 " + student + " 的信息");
			int row = studentInfoService.isAddStudent(student);
		}

		return "redirect:/students";
	}

	/**
	 * 删除职工
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/student/{studentId}", method = RequestMethod.DELETE)
	public String isDelTeacher(@PathVariable("studentId") Integer studentId) {
		logger.info("删除职工 " + studentId);

		int row = studentInfoService.isDelStudent(studentId);

		return "redirect:/students";
	}

	/**
	 * 预添加职工
	 * @return
	 */
	@RequestMapping("/preAddStudent")
	public ModelAndView preAddStudent() {
		logger.info("预添加职工信息");
		ModelAndView model = new ModelAndView();
		model.setViewName("/admin/studentedit");
		List<ClassInfo> classes = classInfoService.getClasses(null);
		model.addObject("classes", classes);

		return model;
	}
	
	/**
	 * 职工考试登录验证
	 * 
	 * 此处验证并不合理 登录验证实现如下：
	 *   前台职工登录传入账户，后台根据账户获取职工密码
	 *   返回职工密码，前台登录焦点离开密码框使用 JavaScript 判断
	 * 
	 * @param studentAccount 职工登录账户
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/validateLoginStudent")
	public void validateLoginStudent(@RequestParam("studentAccount") String studentAccount,
			HttpServletResponse response) throws IOException {
		logger.info("职工账户 "+studentAccount+"，尝试登录考试");
		
		//获取需要登录的职工对象
		StudentInfo student = studentInfoService.getStudentByAccountAndPwd(studentAccount);
		
		if (student == null) {
			logger.error("登录职工账户 "+studentAccount+" 不存在");
			response.getWriter().print("n");
		} else {
			logger.error("登录职工账户 "+studentAccount+" 存在");
			response.getWriter().print(student.getStudentPwd());
		}
	}
	
	/**
	 * liuyi modify
	 * 职工登录考试
	 * @param student 登录职工
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/common/studentLogin", method=RequestMethod.POST)
	public ResponseBean studentLogin(@RequestParam("studentAccount") String studentAccount,@RequestParam("studentPwd") String studentPwd) {	
		StudentInfo loginStudent = studentInfoService.getStudentByAccountAndPwd(studentAccount);
		if(loginStudent != null && loginStudent.getStudentPwd().equals(studentPwd)){
			String token =mJwtUtil.generateToken(loginStudent);
			mUserComponent.putStudent(loginStudent);
			return new ResponseBean(ResultCode.CODE_OK,"登录成功",token);
		}else {
			return new ResponseBean(ResultCode.CODE_1,"用户名或密码错误",null);
		    
		}
	}
	
	/**
	 * 退出登录
	 * @param session
	 * @return
	 */
	@RequestMapping("/exit")
	public String studentClearLogin(HttpSession session) {
		StudentInfo studnet = (StudentInfo) session.getAttribute("loginStudent");
		logger.info("职工 "+studnet.getStudentName()+", 编号 "+studnet.getStudentId()+" 退出登录");
		session.removeAttribute("loginStudent");
		
		return "redirect:index.jsp";
	}
	
	/**
	 * 职工注册 验证当前账户是否被占用
	 * @param studentAccount 注册账户
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/validateAccount")
	public void validateRegisterAccount(@RequestParam("studentAccount") String studentAccount,
			HttpServletResponse response) throws IOException {
		logger.info("验证职工账户 "+studentAccount+"，是否已被注册");
		
		StudentInfo student = studentInfoService.getStudentByAccountAndPwd(studentAccount);
		
		if (student == null) {
			logger.error("注册职工账户 "+studentAccount+" 可以注册");
			response.getWriter().print("t");
		} else {
			logger.error("注册职工账户 "+studentAccount+" 已被注册");
			response.getWriter().print("f");
		}
	}
	
	/**
	 * 学生注册
	 */
	@ResponseBody
	@RequestMapping(value="/common/studentReg", method=RequestMethod.POST)
	public ResponseBean studentRegister(
			@RequestParam("account") String studentAccount,
			@RequestParam("pwd") String studentPwd,
			@RequestParam("classId") Integer classId,
			@RequestParam("email") String email) throws IOException {
		//拦截空
		if(TextUtil.isEmpty(studentAccount)) {
			return new ResponseBean(ResultCode.CODE_1, "用户不能为空", null);
		}
	
		if(TextUtil.isEmpty(studentPwd)) {
			return new ResponseBean(ResultCode.CODE_1, "密码不能为空", null);
		}
		
		if(TextUtil.isEmpty(email)) {
			return new ResponseBean(ResultCode.CODE_1, "邮箱不能为空", null);
		}
		
		//检查是用户名和邮箱是否已经注册
		StudentInfo student = mCommonStudentMapper.selectStudentBystudentName(studentAccount);
		if (student!=null) {
			return new ResponseBean(ResultCode.CODE_1, "用户名已经被注册", null);	
		}
		
		student = mCommonStudentMapper.selectStudentBystudentEmail(email);
		if (student!=null) {
			return new ResponseBean(ResultCode.CODE_1, "邮箱已经被注册", null);	
		}	
			
		//写库
		student = new StudentInfo();
		student.setStudentName(studentAccount);
		student.setStudentAccount(studentAccount);
		student.setEmail(email);
		student.setStudentPwd(studentPwd);
		classInfo.setClassId(classId);
		student.setClassInfo(classInfo);
	
		int row = studentInfoService.isAddStudent(student);
		if (row>0) {
		    return new ResponseBean(ResultCode.CODE_OK, "注册成功", null);
		}else {
			return new ResponseBean(ResultCode.CODE_OK, "注册失败", null);
		}

	}
	
	/**
	 * 预注册
	 * @return
	 */
	@RequestMapping("/preStudentReg")
	public ModelAndView preStudentReg() {
		ModelAndView model = new ModelAndView();
		model.setViewName("reception/register");
		model.addObject("classs", classInfoService.getClasses(null));
		return model;
	}
	
	/**
	 * 职工进入考试
	 * @param classId 层级编号
	 * @param examPaperId 试卷编号
	 * @param studentId 考生编号
	 * @param examTime 考试时间
	 * @param beginTime 考试开始时间
	 * @param gradeId 科室编号
	 * @param session
	 * @return
	 */
	@RequestMapping("/begin")
	public ModelAndView beginExam(
			@RequestParam("classId") Integer classId,
			@RequestParam("examPaperId") Integer examPaperId,
			@RequestParam(value="studentId", required=false) Integer studentId,
			@RequestParam("examTime") Integer examTime,
			@RequestParam("beginTime") String beginTime,
			@RequestParam("gradeId") Integer gradeId,
			HttpSession session) {
		ModelAndView model = new ModelAndView();
		
		/*
		 * 查询该考试当前进入的试卷是否已经在历史记录中存在
		 * 如果存在，则不能再次进入考试； 反之进入考试
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("examPaperId", examPaperId);
		int count = examHistoryPaperService.getHistoryInfoWithIds(map);
		if(session.getAttribute("loginStudent") == null) {
			model.addObject("error", "请先登录后再操作");
			model.setViewName("error");
			return model;
		} else if (count >= 1) {
			model.addObject("error", "你已经考试过了");
			model.setViewName("error");
			return model;
		} else {			
			logger.info("职工 "+studentId+" 进入考试 层级 "+classId+" 试卷 "+examPaperId);
			model.setViewName("/reception/exam");
			
			ExamPaperInfo examPaper = new ExamPaperInfo();
			examPaper.setExamPaperId(examPaperId);
			esm.setExamPaper(examPaper);
			//获取试卷 试题集合
			List<ExamSubjectMiddleInfo> esms = examSubjectMiddleInfoService.getExamPaperWithSubject(esm);
			logger.info("考试试题总量 "+esms.size());
			
			//获取当前考生在当前试卷中已选答案记录
			Map<String, Object> choosedMap = new HashMap<String, Object>();
			choosedMap.put("studentId", studentId);
			choosedMap.put("examPaperId", examPaperId);
			List<ExamChooseInfo> chooses = examChooseInfoService.getChooseInfoWithSumScore(choosedMap); 
			if (chooses == null || chooses.size() == 0) {
				model.addObject("chooses", null);
			} else {
				model.addObject("chooses", chooses);				
			}
			
			
			model.addObject("esms", esms);
			model.addObject("sumSubject", esms.size());
			model.addObject("examPaperId", examPaperId);
			model.addObject("examTime", examTime);
			model.addObject("beginTime", beginTime);
			model.addObject("classId", classId);
			model.addObject("gradeId", gradeId);
			
			return model;
		}
	}
	
	
	/**
	 * 获取职工历史考试记录
	 * @param studentId 职工编号
	 * @return
	 */
	@RequestMapping("/history/{studentId}")
	public ModelAndView getExamHistoryInfo(@PathVariable("studentId") Integer studentId) {
		ModelAndView model = new ModelAndView();
		
		if (studentId == null) {
			logger.error("职工编号 为空");
			model.setViewName("error");
			return model;
		}
		logger.info("职工 "+studentId+" 获取考试历史记录");
		//获取历史考试信息记录集合
		List<ExamHistoryPaper> ehps = examHistoryPaperService.getExamHistoryToStudent(studentId);
		model.addObject("ehps", ehps);
		model.setViewName("/reception/examHistory");
		
		return model;
	}
	
	
	/**
	 * 考生提交考试
	 * @param studentId
	 * @param examPaperId
	 * @param classId
	 * @param gradeId
	 * @return
	 */
	@RequestMapping(value="/submit", method={RequestMethod.POST, RequestMethod.GET})
	public String examSubmit(
			@RequestParam("studentId") Integer studentId,
			@RequestParam("examPaperId") Integer examPaperId,
			@RequestParam("classId") Integer classId,
			@RequestParam("gradeId") Integer gradeId) {
		logger.info("职工 "+studentId+" 提交了试卷 "+examPaperId);
		
		//获取当前职工当前试卷所选择的全部答案
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("examPaperId", examPaperId);
		List<ExamChooseInfo> chooses = examChooseInfoService.getChooseInfoWithSumScore(map);
		logger.info("职工 "+studentId+" 共选择了 "+chooses.size()+" 道题");
		
		//总分记录
		double sumScore = 0;
		for (ExamChooseInfo choose : chooses) {
			SubjectInfo subject = choose.getSubject();
			String chooseResult = choose.getChooseResult();
			String rightResult = subject.getRightResult();
			if(subject.getSubjectType()==3) { // 填空题统计分数
				String[] rightResultArray=rightResult.split(",|，");
				String[] chooseResultArray=chooseResult.split(",");
				int rightBlank=0; 
				for(int i=0;i<chooseResultArray.length;i++) {
				   if (i<rightResultArray.length && chooseResultArray[i].equalsIgnoreCase(rightResultArray[i])) { //答案正确
					   rightBlank++;   
				   }
				}
				sumScore += (subject.getSubjectScore()/(double)rightResultArray.length)*rightBlank;	
			}else { //单选,多选题统计分数
				if (chooseResult.equals(rightResult)) {	//答案正确
					sumScore += subject.getSubjectScore();	
				}
			}
		}
		
		/*
		 * 首先判断当前记录是否已经添加过
		 * 防止当前职工点击提交后，系统倒计时再次进行提交
		 */
		int count = examHistoryPaperService.getHistoryInfoWithIds(map);
		
		if (count == 0) {
			//添加到历史记录
			DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
			map.put("examScore",decimalFormat.format(sumScore));
			int row = examHistoryPaperService.isAddExamHistory(map);
			logger.info("职工 "+studentId+" 提交的试卷 "+examPaperId+" 已成功处理，并添加到历史记录中");
		}
		
		return "redirect:willexams?gradeId="+gradeId+"&classId="+classId+"&studentId="+studentId;
	}
	
	
	/**
	 * 职工回顾试卷  --  后台带教查看也调用此方法
	 * @param studentId
	 * @param examPaperId
	 * @param score
	 * @param examPaperName
	 * @param studentName  后台带教查看需传入职工姓名
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/review")
	public ModelAndView reViewExam(
			@RequestParam("studentId") Integer studentId,
			@RequestParam("examPaperId") Integer examPaperId,
			@RequestParam("score") Integer score,
			@RequestParam("examPaperName") String examPaperName,
			@RequestParam(value="studentName", required=false) String studentName) throws UnsupportedEncodingException {
		ModelAndView model = new ModelAndView();
		if (studentId == null) {
			model.addObject("error", "请先登录后再操作");
			model.setViewName("error");
			return model;
		} else {
			//获取当前试卷的试题集合  -- 前台判断需要
			examPaper.setExamPaperId(examPaperId);
			esm.setExamPaper(examPaper);
			List<ExamSubjectMiddleInfo> esms = examSubjectMiddleInfoService.getExamPaperWithSubject(esm);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("studentId", studentId);
			map.put("examPaperId", examPaperId);
			
			//获取当前回顾试卷 试题、选择答案 信息
			List<ExamChooseInfo> reviews = examChooseInfoService.getChooseInfoWithExamSubject(map);
			logger.info("职工 "+studentId+" 回顾试卷 "+examPaperId+" 试题数量 "+reviews.size());
			//设置试卷名称、试卷总分
			model.addObject("examPaperName", examPaperName);
			model.addObject("score", score);
			
			model.setViewName("reception/review");
			model.addObject("views", reviews);
			
			model.addObject("esms", esms);
			if (studentName != null) model.addObject("studentName", studentName);
			
			model.addObject("ExamedPaper", examPaperInfoService.getExamPaper(examPaperId));
			
			return model;
		}
	}
	
	/**
	 * 职工查看自己信息
	 * @param studentId
	 * @return
	 */
	@RequestMapping("/self/{studentId}")
	public ModelAndView selfInfo(@PathVariable("studentId") Integer studentId) {
		StudentInfo stu = studentInfoService.getStudentById(studentId);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("/reception/self");
		model.addObject("self", stu);
		
		
		return model;
	}
	
	
	/**
	 * 职工修改密码
	 */
	@ResponseBody
	@RequestMapping("/api/resetPassword")
	public ResponseBean isResetPwd(
			@RequestParam("oldPwd") String oldPwd,
			@RequestParam("newPwd") String newPwd,
			@RequestParam("studentId") Integer studentId) throws IOException {
		StudentInfo student= studentInfoService.getStudentById(studentId);
		if (student==null) {
			return new ResponseBean(ResultCode.CODE_1, "查找用户失败，请联系系统管理员", null);
		}
		
		if (!oldPwd.equals(student.getStudentPwd())) {
			return new ResponseBean(ResultCode.CODE_1, "旧密码不正确", null);
		}
		
		student.setStudentId(studentId);
		student.setStudentPwd(newPwd);
		int row = studentInfoService.isResetPwdWithStu(student);
		if (row > 0) {
			mUserComponent.removeStudent(student);
			return new ResponseBean(ResultCode.CODE_OK, "修改密码成功，请重新登录", null);
		} else {
			return new ResponseBean(ResultCode.CODE_OK, "修改密码失败", null);			
		}
	}
}