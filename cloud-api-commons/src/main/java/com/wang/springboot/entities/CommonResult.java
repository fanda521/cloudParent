package com.wang.springboot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-08-17 23:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult <T> {

    private Integer code;
    private String message;
    private T date;

    public CommonResult(Integer code,String message){
        this(code,message,null);
    }

}
