package com.wang.sprinbboot.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2020-09-05 12:04
 */
@Service
public class PaymentService {
    /**
     * 正常的返回
     * @param id
     * @return
     */
    public String paymentInfo_OK(Integer id){
        return "线程池： "+Thread.currentThread().getName()+" paymentInfo_OK,ID:"+id+" O(∩_∩)O哈哈~";
    }

    /**
     * 访问超时
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
        }
    )
    public String paymentInfo_TimeOut(Integer id){
        /**
         * 超时和报错都会走兜底的方法
         */
        int timeNumber=3;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池： "+Thread.currentThread().getName()+" paymentInfo_TimeOut,ID:"+id+"~~~~(>_<)~~~~ 超时了(秒)！"+timeNumber;
       /* int i=10/0;
        return "线程池： "+Thread.currentThread().getName()+" paymentInfo_TimeOut,ID:"+id+"~~~~(>_<)~~~~ 出错了！";
*/
    }

    /**
     * 兜底的方法
     * @return
     */
    public String paymentInfo_TimeOutHandler(Integer id){
        //超时时
        /*return "线程池： "+Thread.currentThread().getName()+" paymentInfo_TimeOutHandler,ID:"+id+"~~~~(>_<)~~~~ 超时了！";*/

        //报错时
        return "线程池： "+Thread.currentThread().getName()+" paymentInfo_TimeOutHandler,ID:"+id+"~~~~(>_<)~~~~ 出错了！";

    }

    /**
     *========== 服务熔断
     */
    //HystrixCommandProperties 具体的参数文件在改文件里查看
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "6"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "6000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "40")//失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if(id<0){
            throw  new RuntimeException("******** id 不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t"+"调用成功，流水号："+serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能为负数，请稍后再试，~~~~(>_<)~~~~  id: "+id;
    }

}
