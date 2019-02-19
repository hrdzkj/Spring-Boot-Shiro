package org.gxqfy.exam.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.gxqfy.exam.po.ExamChooseInfo;
import org.gxqfy.exam.po.ResponseBean;
import org.gxqfy.exam.service.ExamChooseInfoService;
import org.gxqfy.exam.util.ResultCode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 * <p>
 * Title: ExamChooseInfoHandler
 * </p>
 * <p>
 * Description: 试卷试题答案选择
 * </p>
 * 
 * @author: taohan
 * @date: 2018-8-25
 * @time: 上午10:36:41
 * @version: 1.0
 */

@Controller
public class ExamChooseInfoHandler {

	@Autowired
	private ExamChooseInfoService examChooseInfoService;
	@Autowired
	private ExamChooseInfo examChoose;

	private Logger logger = LoggerFactory.getLogger(ExamChooseInfoHandler.class);

	/**
	 * 记录考生考试选择答案
	 * 
	 * @param studentId
	 *            考生编号
	 * @param examPaperId
	 *            考试试卷编号
	 * @param subjectId
	 *            当前选择试题编号
	 * @param index
	 *            前台控制索引
	 * @param chooseAswer
	 *            选择答案
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/api/choose", method = RequestMethod.POST)
	@ResponseBody
	public ResponseBean examChooseHandler(@RequestParam("studentId") Integer studentId,
			@RequestParam("examPaperId") Integer examPaperId, @RequestParam("subjectId") Integer subjectId,
			@RequestParam(value = "index", required = false) Integer index,
			@RequestParam("chooseAswer") String chooseAswer, HttpServletResponse response) throws IOException {
		// todo 判断考试是否已经结束
		
		// 判断该考生是否已经选择过该试题
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		map.put("examPaperId", examPaperId);
		map.put("subjectId", subjectId);
		examChoose = examChooseInfoService.getChooseWithIds(map);
		if (examChoose == null) {
			logger.info("考生 " + studentId + " 尚未选择试题 " + subjectId + " 添加选择记录 答案 " + chooseAswer);
			map.put("chooseResult", chooseAswer);
			examChooseInfoService.addChoose(map);
			return new ResponseBean(ResultCode.CODE_OK, "答题卡已经记录提交答案", null);
		} else if (examChoose.getChooseId() != null && examChoose != null) {
			if (!chooseAswer.equals(examChoose.getChooseResult())) {
				examChoose.setChooseResult(chooseAswer);
				examChooseInfoService.updateChooseWithIds(examChoose);
			}
			return new ResponseBean(ResultCode.CODE_OK, "答题卡已经更新", null);
		} else {
			return new ResponseBean(ResultCode.CODE_1, "更新答题卡有误", null);
		}
	}
}
