package org.gxqfy.exam.service;

import java.util.List;

import org.gxqfy.exam.po.CourseInfo;
import org.springframework.stereotype.Repository;


/**
  *
  * <p>Title: CourseInfoService</p>
  * <p>Description: </p>
  * @author: taohan
  * @date: 2018-8-15
  * @time: 下午5:30:21
  * @version: 1.0
  */

@Repository
public interface CourseInfoService {

	public List<CourseInfo> getCourses(CourseInfo course);

	public int isUpdateCourse(CourseInfo course);

	public int isAddCourse(CourseInfo course);

	public int isDelCourse(int courseId);
	
	public CourseInfo getCourseById(int courseId);
}
