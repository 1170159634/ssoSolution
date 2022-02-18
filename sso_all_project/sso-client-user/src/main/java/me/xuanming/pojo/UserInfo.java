package me.xuanming.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/14 5:27 下午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/14 5:27 下午
 * @updateRemark :   修改内容
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {


    private String userId;

    /**
     * 用户姓名
     */
    @NotEmpty
    private String userName;

    /**
     * 用户密码
     */
    @NotEmpty
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 性别
     */
    private String gender;

    /**
     * 头像地址
     */
    private String avatarPath;


    /**
     * 用户session信息
     *
     *
     * @Author: xingxuanming
     * @Date: 2022/2/14
     */
    @Data
    @ToString
    public static class UserDTO {


        private String userId;

        /**
         * 用户姓名
         */
        private String userName;
        /**
         * 性别
         */
        private String gender;

        /**
         * 头像地址
         */
        private String avatarPath;
        /**
         * 用户邮箱
         */
        private String email;
    }
}
