import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/17 10:41 上午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/17 10:41 上午
 * @updateRemark :   修改内容
 **/
public class Base64Test {
    public static void main(String[] args) {
        String s = null;
        try {
            s = new String(Base64.getDecoder().decode("eyJlbWFpbCI6IjEyMUBxcS5jb20iLCJnZW5kZXIiOiLnlLciLCJ1c2VySWQiOiIyNTAwMSIsInVzZXJOYW1lIjoi5byg5LiJIn0=".replace("\r\n", "")),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(s);
    }
}
