package me.xuanming.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/15 3:45 下午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/15 3:45 下午
 * @updateRemark :   修改内容
 **/
@Configuration
@ConfigurationProperties(prefix = "sso.client.user")
@Data
public class SsoLoginConfig {
    private String userLoginUrl;
    private String userLoginApiUrl;
    private Integer retryLoginTime;
    private Integer retryLoginPollingTime;
    private Integer userLoginSynTime;
}
