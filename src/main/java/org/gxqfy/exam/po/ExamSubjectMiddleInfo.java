package org.gxqfy.exam.po;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
  *
  * <p>Title: ExamSubjectMiddleInfo</p>
  * <p>Description: 试卷、试题中间关联表： 描述一个试卷有那些题目</p>
  * @author: taohan
  * @date: 2018-8-20
  * @time: 下午1:29:29
  * @version: 1.0
  */

@Component
@Scope("prototype")
public class ExamSubjectMiddleInfo {

	private Integer esmId;
	private ExamPaperInfo examPaper;//试卷信息
	private SubjectInfo subject; //试题信息

	public Integer getEsmId() {
		return esmId;
	}

	public void setEsmId(Integer esmId) {
		this.esmId = esmId;
	}

	public ExamPaperInfo getExamPaper() {
		return examPaper;
	}

	public void setExamPaper(ExamPaperInfo examPaper) {
		this.examPaper = examPaper;
	}

	public SubjectInfo getSubject() {
		return subject;
	}

	public void setSubject(SubjectInfo subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "ExamSubjectMiddleInfo [esmId=" + esmId + ", examPaper="
				+ examPaper + ", subject=" + subject + "]";
	}
}
