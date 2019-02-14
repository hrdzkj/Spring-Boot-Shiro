https://github.com/hrdzkj/Spring-Boot-Shiro
访问链接：http://localhost:8080/article/
jwt 缓存：https://www.jianshu.com/p/f37f8c295057

如何集成mybatis:
http://www.ityouknow.com/springboot/2016/11/06/spring-boo-mybatis.html

Invalid bound statement (not found)错误的可能原因:
https://www.cnblogs.com/liaojie970/p/8034525.html

理解了分成3层：
接口层：@Controller/@RestController/@ResponseBody/@RestControllerAdvice  @RequestMapping/@RequestParam
业务层：@Component/@service
数据层：@Autowired
持久层：@Repository
整个应用层：@MapperScan


todo:
1.先连接登录的正常
DataSource.java--获取数据库信息

POJO POJO（Plain Ordinary Java Object）简单的Java对象，实际就是普通JavaBeans


转移了所有的：
持久层：dao包下的数据库对象，xxxMap.xml。
属于基础数据的POJO。
属于业务层的service/service.impl包下的所有对象。

todo：
1.）所有接口层handler下的接口也转移过来了，只要根据一个个具体接口，调整接口返回json。
2.）实现生产token的算法，及登录拦截
3.）手机填空题，采用富文本SpannableString,弹窗输入的方式解决。
https://www.cnblogs.com/zimengfang/p/5527259.html
4)ios端，估计开发时间也是比较久。 先混属性后端spring boot 再开发苹果吧。



