package org.gxqfy.exam.component;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Null;

import org.gxqfy.exam.po.StudentInfo;
import org.springframework.stereotype.Component;

@Component
public class UserComponent {
	private static Map<String, StudentInfo> mLoginStudentList = new HashMap<>(); // 已经登录的学生信息
	
	public StudentInfo getStudent(String token) {
		return mLoginStudentList.get(token);
	}
	
	public void putStudent(StudentInfo studentInfo) {
		if (studentInfo!=null) {
			mLoginStudentList.put(studentInfo.getStudentAccount(), studentInfo);
		}
	}
	
	public void removeStudent(StudentInfo studentInfo) {
		if (studentInfo!=null) {
			mLoginStudentList.remove(studentInfo.getStudentAccount());
		}
	}
	
}
