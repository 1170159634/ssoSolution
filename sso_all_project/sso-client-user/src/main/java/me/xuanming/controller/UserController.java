package me.xuanming.controller;

import lombok.extern.slf4j.Slf4j;
import me.xuanming.pojo.UserInfo;
import me.xuanming.service.UserService;
import me.xuanming.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/14 5:25 下午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/14 5:25 下午
 * @updateRemark :   修改内容
 **/
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserInfo userInfo) {


        log.info(" getUserInfo start handle ");
        UserInfo.UserDTO info = userService.login(userInfo);
        if (Objects.isNull(info)) {
            return new ResponseEntity<>(R.loginFail(), HttpStatus.OK);
        }
        return new ResponseEntity<>(R.loginSuccess(info), HttpStatus.OK);


    }

}
