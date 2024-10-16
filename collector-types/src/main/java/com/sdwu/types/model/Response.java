package com.sdwu.types.model;

import com.sdwu.types.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    private Integer code;
    private String info;
    private T data;

    //返回成功
    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }
    //返回失败
    public static <T> Response<T> fail(T data) {
        return Response.<T>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .data(data)
                .build();
    }


    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    public Response<T> put(String key, Object value) {
        if (data == null) {
            data = (T) new HashMap<String, Object>();
        }
        ((HashMap<String, Object>) data).put(key, value);
        return this;
    }
}
