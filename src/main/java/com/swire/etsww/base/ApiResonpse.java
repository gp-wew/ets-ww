package com.swire.etsww.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@ApiModel(description = "接口返回结果")
public class ApiResonpse<T> {

    public final static ApiResonpse SUCCESS=new ApiResonpse();

    @ApiModelProperty("状态码，00：成功")
    private String status="00";

    @ApiModelProperty("状态说明")
    private String message="success";

    @ApiModelProperty("结果内容")
    private T content;

    public ApiResonpse(T content) {
        this.content=content;
    }

    public static ApiResonpse badRequest(String msg){
        ApiResonpse<Object> resonpse = new ApiResonpse<>();
        resonpse.setStatus("400");
        resonpse.setMessage(msg);
        return resonpse;
    }

    @ApiModelProperty("请求是否成功")
    public boolean isSuccess(){
        return "00".equals(status);
    }
}
