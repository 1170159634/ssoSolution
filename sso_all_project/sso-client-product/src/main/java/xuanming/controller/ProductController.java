package xuanming.controller;

import lombok.extern.slf4j.Slf4j;
import me.xuanming.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xuanming.pojo.ProductEntity;
import xuanming.service.ProductService;

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
@RequestMapping("/api/product")
@Slf4j
public class ProductController {


    @Autowired
    private ProductService productService;

    /**
     * 获取所有商品
     *
     * @Author: xingxuanming
     * @Date: 2022/2/14
     */
    @RequestMapping("/getAllProduct")
    public ResponseEntity<Object> getAllProduct() {
        log.info(" getAllProduct start handle ");
        List<ProductEntity> info = productService.queryAllProduct();
        return new ResponseEntity<>(info, HttpStatus.OK);
    }
}
