package me.xuanming.service;

import me.xuanming.pojo.OrderEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/14 4:28 下午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/14 4:28 下午
 * @updateRemark :   修改内容
 **/
@Service
public class OrderService {


    public static final List<OrderEntity> ORDER_LIST = Collections.synchronizedList(new ArrayList<>(8));

    static {
        ORDER_LIST.add(new OrderEntity(128712747L, "25001", "167266251771", LocalDateTime.now().plus(10, ChronoUnit.MINUTES), "张三"));
        ORDER_LIST.add(new OrderEntity(128712757L, "25001", "267266251771", LocalDateTime.now().plus(11, ChronoUnit.MINUTES), "张三"));
        ORDER_LIST.add(new OrderEntity(128712767L, "25001", "367266251771", LocalDateTime.now().plus(12, ChronoUnit.MINUTES), "张三"));
        ORDER_LIST.add(new OrderEntity(128712777L, "25001", "467266251771", LocalDateTime.now().plus(13, ChronoUnit.MINUTES), "张三"));
        ORDER_LIST.add(new OrderEntity(128712787L, "25001", "567266251771", LocalDateTime.now().plus(14, ChronoUnit.MINUTES), "张三"));
        ORDER_LIST.add(new OrderEntity(128712718L, "25002", "767266251772", LocalDateTime.now().plus(30, ChronoUnit.MINUTES), "李四"));
        ORDER_LIST.add(new OrderEntity(128712719L, "25003", "767266251773", LocalDateTime.now().plus(60, ChronoUnit.MINUTES), "王五"));
        ORDER_LIST.add(new OrderEntity(128712720L, "25004", "767266251774", LocalDateTime.now().plus(90, ChronoUnit.MINUTES), "赵六"));
        ORDER_LIST.add(new OrderEntity(128712721L, "25005", "767266251775", LocalDateTime.now().plus(120, ChronoUnit.MINUTES), "史七"));
    }

    public List<OrderEntity> getOrderInfo(OrderEntity orderEntity) {
        return ORDER_LIST.stream().filter((data) -> orderEntity.getUserId().equals(data.getUserId())).collect(Collectors.toList());
    }
}
