package com.rzdata.common.model;

import com.rzdata.core.domain.model.LoginUser;
import com.rzdata.ps.exception.ErrorInfo;
import com.rzdata.utils.SecurityUtils;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 基础rest类
 * @author ouandyang
 * @date 2021年3月31日
 */
@ApiResponses({
        @ApiResponse(code = 200, message = "接口调用成功"),
        @ApiResponse(code = 400, message = "非法请求", response = ErrorInfo.class),
        @ApiResponse(code = 401, message = "未授权", response = ErrorInfo.class),
        @ApiResponse(code = 403, message = "无法执行此操作", response = ErrorInfo.class),
        @ApiResponse(code = 404, message = "资源错误", response = ErrorInfo.class),
        @ApiResponse(code = 500, message = "内部错误", response = ErrorInfo.class)
})
public abstract class BaseRest {

    /**
     * 获取当前登录用户
     * @return 用户ID
     */
    protected String getUserId() {
        LoginUser loginUser;
        try {
            loginUser = SecurityUtils.getLoginUser();
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息异常，请联系管理员。");
        }
        return loginUser.getUserId();
    }

}
