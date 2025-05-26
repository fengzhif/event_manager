package com.fengzhi.event_manager.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;//业务状态码，0 成功，-1失败
    private String msg;//提示信息
    private T data;//响应数据

    public static <T> Result<T> success(T data) {
        return new Result<T>(0, "success", data);
    }

    public static Result success() {
        return new Result(0, "success", null);
    }

    public static Result error(String msg) {
        return new Result(1, msg, null);
    }
}
