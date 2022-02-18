package me.xuanming.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单
 *
 * @author Ming
 * @email 1170159634@qq.com
 * @date 2022-01-22 15:20:42
 */

@Data
@ToString
@AllArgsConstructor
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * member_id
     */
    @NotEmpty
    private String userId;
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * create_time
     */
    private LocalDateTime createTime;
    /**
     * 用户名
     */
    private String username;


}
