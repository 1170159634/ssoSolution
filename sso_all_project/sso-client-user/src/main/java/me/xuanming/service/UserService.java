package me.xuanming.service;

import lombok.extern.slf4j.Slf4j;
import me.xuanming.pojo.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/15 10:07 上午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/15 10:07 上午
 * @updateRemark :   修改内容
 **/
@Service
@Slf4j
public class UserService {


    public static final List<UserInfo> USER_LIST = Collections.synchronizedList(new ArrayList<>(5));

    static {
        USER_LIST.add(new UserInfo("25001", "张三", "123456", "121@qq.com", "1218282821", "男", null));
        USER_LIST.add(new UserInfo("25002", "李四", "qwer1234", "1231@qq.com", "1218282822", "男", "http://images//2.png"));
        USER_LIST.add(new UserInfo("25003", "王五", "zzxxc", "567@qq.com", "1218282823", "男", "http://images//3.png"));
        USER_LIST.add(new UserInfo("25004", "赵六", "321sda", "712@qq.com", "1218282824", "男", "http://images//4.png"));
        USER_LIST.add(new UserInfo("25005", "史七", "admin", "234@qq.com", "1218282825", "女", "http://images//5.png"));

    }


    public UserInfo.UserDTO login(UserInfo userInfo) {
        Optional<UserInfo.UserDTO> optional = USER_LIST.stream().filter((info) -> info.getUserName().equals(userInfo.getUserName()) && info.getPassword().equals(userInfo.getPassword())).map((info) -> {
            UserInfo.UserDTO userDTO = new UserInfo.UserDTO();
            BeanUtils.copyProperties(info, userDTO);
            return userDTO;
        }).findFirst();
        return optional.orElse(null);
    }


}
