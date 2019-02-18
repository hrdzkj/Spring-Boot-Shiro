package org.gxqfy.exam.dao.common;

import org.apache.ibatis.annotations.Select;
import org.gxqfy.exam.po.StudentInfo;
import tk.mybatis.mapper.common.Mapper;

public interface CommonStudentMapper extends Mapper<StudentInfo>{
	@Select("select * from studentinfo where studentName = #{studentName}")
	StudentInfo selectStudentBystudentName(String studentName);
	
	@Select("select * from studentinfo where email = #{email}")
	StudentInfo selectStudentBystudentEmail(String email);	
}
