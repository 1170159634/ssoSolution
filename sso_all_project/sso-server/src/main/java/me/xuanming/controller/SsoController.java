package me.xuanming.controller;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import me.xuanming.pojo.SsoEntity;
import me.xuanming.service.SsoService;
import me.xuanming.utils.R;
import okhttp3.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/15 11:00 上午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/15 11:00 上午
 * @updateRemark :   修改内容
 **/
@RestController
@RequestMapping("/api/sso")
@Slf4j
public class SsoController {

    @Autowired
    private SsoService ssoService;

    @PostMapping("/getUserInfoByToken")
    public ResponseEntity<Object> getUserInfoByToken(@Validated({SsoEntity.getUserInfoGroup.class}) @RequestBody SsoEntity ssoEntity) {
        log.info("handle getUserInfoByToken method request body:{}", ssoEntity);
        R uerInfo = ssoService.getUserInfoByToken(ssoEntity);
        return new ResponseEntity<>(uerInfo, HttpStatus.OK);
    }


    @PostMapping("/ssoLogin")
    public ResponseEntity<Object> ssologin(@Validated({SsoEntity.ssologin.class}) @RequestBody SsoEntity.UserVaildInfo userVaildInfo) {
        log.info("handle ssologin method ");
        R uerInfo = ssoService.ssoLogin(userVaildInfo);
        return new ResponseEntity<>(uerInfo, HttpStatus.OK);


    }


    @PostMapping("/checkLogin")
    public ResponseEntity<Object> checkLogin(@Validated({SsoEntity.checkLogin.class}) @RequestBody SsoEntity ssoEntity) {
        log.info("handle checkLogin method  request body:{}",ssoEntity);
        R uerInfo = ssoService.checkLogin(ssoEntity);
        return new ResponseEntity<>(uerInfo, HttpStatus.OK);
    }
}
