package xuanming.service;

import org.springframework.stereotype.Service;
import xuanming.pojo.ProductEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
public class ProductService {


    public static final List<ProductEntity> PRODUCT_LIST = Collections.synchronizedList(new ArrayList<>(5));

    static {
        PRODUCT_LIST.add(new ProductEntity(128712747L, "玩具车", 121.1d, LocalTime.now()));
        PRODUCT_LIST.add(new ProductEntity(128712748L, "喷水枪", 23.4d, LocalTime.now()));
        PRODUCT_LIST.add(new ProductEntity(128712717L, "变形金刚", 98.3d, LocalTime.now()));
        PRODUCT_LIST.add(new ProductEntity(128712727L, "溜溜球", 275.2d, LocalTime.now()));
        PRODUCT_LIST.add(new ProductEntity(128712737L, "冰墩墩", 180.0d, LocalTime.now()));
        PRODUCT_LIST.add(new ProductEntity(128712797L, "雪容融", 99.4d, LocalTime.now()));


    }

    public List<ProductEntity> queryAllProduct() {
        return PRODUCT_LIST;

    }
}
