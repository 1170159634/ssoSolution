package me.xuanming.handler;

import lombok.extern.slf4j.Slf4j;
import me.xuanming.utils.HttpStatusEnum;
import me.xuanming.utils.JsonUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * SpringMVC统一异常处理
 *
 * @author JustryDeng
 * @date 2019/10/12 16:28
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Map<String, Object> handleParameterVerificationException(Exception e) {
        log.error(" handleParameterVerificationException has been invoked", e);
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("code", HttpStatusEnum.VAILD_FIELD_ERROR.getCode());
        HashMap<String, String> data = new HashMap<>();
        // 获取数据校验的错误结果
        BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            String message = fieldError.getDefaultMessage();
            String field = fieldError.getField();
            data.put(field, message);
        });


        resultMap.put("msg", HttpStatusEnum.VAILD_FIELD_ERROR.getMsg());
        resultMap.put("data", data);
        log.error("数据校验出现问题{}", JsonUtil.objectToJson(resultMap), e);
        return resultMap;
    }


    /**
     * 处理任何 未捕捉的异常
     *
     * @Author: xingxuanming
     * @Date: 2022/1/24
     */
    @ExceptionHandler(value = Throwable.class)
    public Map<String, Object> handleUnKnwonError(Throwable e) {
        log.error(" handleUnknownException has been invoked", e);
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("code", HttpStatusEnum.UNKNOWN_ERROR.getCode());
        resultMap.put("msg", HttpStatusEnum.UNKNOWN_ERROR.getMsg()+e.getMessage());
        log.error("数据校验出现问题{}", JsonUtil.objectToJson(resultMap), e);
        return resultMap;

    }

}
