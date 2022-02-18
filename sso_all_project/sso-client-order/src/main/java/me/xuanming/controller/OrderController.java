package me.xuanming.controller;

import lombok.extern.slf4j.Slf4j;
import me.xuanming.pojo.OrderEntity;
import me.xuanming.service.OrderService;
import me.xuanming.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/14 4:24 下午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/14 4:24 下午
 * @updateRemark :   修改内容
 **/
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {


    @Autowired
    private OrderService orderService;

    /**
     * 获取用户所有订单
     *
     * @Author: xingxuanming
     * @Date: 2022/2/14
     */
    @PostMapping("/getOrderInfo")
    public ResponseEntity<Object> getOrderInfo(@RequestBody OrderEntity orderEntity) {
        log.info(" getOrderInfo start handle request body:{}", orderEntity);
        List<OrderEntity> info = orderService.getOrderInfo(orderEntity);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }
}
