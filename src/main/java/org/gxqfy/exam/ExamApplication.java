package org.gxqfy.exam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("org.gxqfy.exam.mapper")
@MapperScan("org.gxqfy.exam.dao")
public class ExamApplication {

    public static void main(String[] args) {
       SpringApplication.run(ExamApplication.class);
    }
}
