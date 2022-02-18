package xuanming.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class ProductEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    private String productName;

    private Double productPrice;

    private LocalTime createTime;


}
