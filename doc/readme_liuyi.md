https://github.com/hrdzkj/Spring-Boot-Shiro
访问链接：http://localhost:8080/article/
jwt 缓存：https://www.jianshu.com/p/f37f8c295057

如何集成mybatis:
http://www.ityouknow.com/springboot/2016/11/06/spring-boo-mybatis.html

Invalid bound statement (not found)错误的可能原因:
https://www.cnblogs.com/liaojie970/p/8034525.html

理解了分成3层：
接口层：@Controller/@RestController/@ResponseBody/@RestControllerAdvice  @RequestMapping/@RequestParam/@PathVariable
业务层：@Component/@service
数据层：@Autowired
持久层：@Repository
整个应用层：@MapperScan

在Spring中的属性名称首字母一定要小写
todo:
1.先连接登录的正常
DataSource.java--获取数据库信息

POJO POJO（Plain Ordinary Java Object）简单的Java对象，实际就是普通JavaBeans


转移了所有的：
持久层：dao包下的数据库对象，xxxMap.xml。
属于基础数据的POJO。
属于业务层的service/service.impl包下的所有对象。

todo：。所有业务层都也转移过来了，只要根据一个个具体接口，调整接口返回json。一个一个来
实现生产token的算法，及登录拦截，过期验证
3.）手机填空题，采用富文本SpannableString,弹窗输入的方式解决。
https://www.cnblogs.com/zimengfang/p/5527259.html
4)ios端，估计开发时间也是比较久。 先混属性后端spring boot 再开发苹果吧。
5）需要的接口：
用户接口：登录、注册，修改密码；找回密码,（找回密码，通过发送一个随机生成的密码到注册邮箱，登录之后修改密码）
考试接口：考试列表，题目列表，选项提交，考试提交；考试成绩； 考试历史浏览

里程碑：
1.用户管理
2.考试列表
3.考试 调用这个方法的地方getExamPaperWithSubject，就是和考试有关的地方。 "/begin" 进入考试
4.考试历史


