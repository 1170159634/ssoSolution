package me.xuanming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/14 4:18 下午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/14 4:18 下午
 * @updateRemark :   修改内容
 **/
@RestController
@SpringBootApplication
public class orderApplication {
    public static void main(String[] args) {
        SpringApplication.run(orderApplication.class, args);
    }

}
