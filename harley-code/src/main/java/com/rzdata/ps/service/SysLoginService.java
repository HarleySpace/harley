package com.rzdata.ps.service;

import com.rzdata.common.constant.Constants;
import com.rzdata.system.model.SysUser;
import com.rzdata.core.domain.model.LoginUser;
import com.rzdata.exception.ServiceException;
import com.rzdata.exception.user.CaptchaException;
import com.rzdata.exception.user.CaptchaExpireException;
import com.rzdata.exception.user.UserPasswordNotMatchException;
import com.rzdata.utils.DateUtils;
import com.rzdata.utils.MessageUtils;
import com.rzdata.utils.RedisUtils;
import com.rzdata.utils.ServletUtils;
import com.rzdata.system.service.ISysConfigService;
import com.rzdata.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

	@Autowired
    private ISysUserService userService;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private AsyncService asyncService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
		HttpServletRequest request = ServletUtils.getRequest();
		boolean captchaOnOff = false;
        // 验证码开关
        if (captchaOnOff)
        {
            validateCaptcha(username, code, uuid, request);
        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
				asyncService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"), request);
                throw new UserPasswordNotMatchException();
            }
            else
            {
				asyncService.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage(), request);
                throw new ServiceException(e.getMessage());
            }
        }
		//asyncService.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"), request);
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //recordLoginInfo(loginUser.getUser());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid, HttpServletRequest request) {
		String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
		String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
		if (captcha == null) {
			asyncService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"), request);
			throw new CaptchaExpireException();
		}
		if (!code.equalsIgnoreCase(captcha)) {
			asyncService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"), request);
			throw new CaptchaException();
		}
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user)
    {
        user.setLoginIp(ServletUtils.getClientIP());
        user.setLoginDate(DateUtils.getNowDate());
		user.setUpdateBy(user.getUserName());
        userService.updateUserProfile(user);
    }
}
