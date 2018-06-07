package com.hklk.oplatform.controller.local;

import com.hklk.oplatform.comm.LoginUser;
import com.hklk.oplatform.comm.TokenManager;
import com.hklk.oplatform.controller.BaseController;
import com.hklk.oplatform.entity.table.PPage;
import com.hklk.oplatform.entity.table.User;
import com.hklk.oplatform.provider.IdProvider;
import com.hklk.oplatform.service.UserService;
import com.hklk.oplatform.util.CookieUtils;
import com.hklk.oplatform.util.StatusCode;
import com.hklk.oplatform.util.StringUtils;
import com.hklk.oplatform.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/loginUser")
@Controller
public class LoginUserController extends BaseController {
    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping("/login")
    public String loginUser(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "password", required = false) String password, HttpServletRequest request,
                            HttpServletResponse response, HttpSession session) {
        User user = userService.loginUser(username, password);
        if (user != null && user.getState() == 1) {
            List<PPage> pPages = userService.queryUserPages(user.getId());
            LoginUser loginUser = new LoginUser(user.getId(), user.getUsername(), user.getNickname(), "");
            if (pPages.get(0) != null) {
                StringBuffer rolePage = new StringBuffer();
                for (PPage pPage : pPages) {
                    rolePage.append(pPage.getPageSrc());
                }
                loginUser.setRolePage(rolePage.toString());
            }
            String token = CookieUtils.getCookie(request, TokenManager.TOKEN);
            if (StringUtils.isBlank(token) || tokenManager.validate(tokenManager.userTokenKey, token) == null) {// 没有登录的情况
                token = createToken(loginUser);
                addTokenInCookie(token, request, response);
            }
            return ToolUtil.buildResultStr(StatusCode.SUCCESS, StatusCode.getStatusMsg(StatusCode.SUCCESS), token);
        } else if (user != null && user.getState() != 1) {
            return ToolUtil.buildResultStr(StatusCode.LOGIN_DISABLE, StatusCode.getStatusMsg(StatusCode.LOGIN_DISABLE));
        } else {
            return ToolUtil.buildResultStr(StatusCode.LOGIN_NAME_OR_PWD_ERROR, StatusCode.getStatusMsg(StatusCode.LOGIN_NAME_OR_PWD_ERROR));
        }
    }

    private String createToken(LoginUser loginUser) {
        // 生成token
        String token = IdProvider.createUUIDId();
        // 缓存中添加token对应User
        tokenManager.addToken(tokenManager.userTokenKey, token, loginUser);
        return token;
    }

    private void addTokenInCookie(String token, HttpServletRequest request, HttpServletResponse response) {
        // Cookie添加token
        Cookie cookie = new Cookie(TokenManager.TOKEN, token);
        cookie.setPath("/");
        if ("https".equals(request.getScheme())) {
            cookie.setSecure(true);
        }
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @ResponseBody
    @RequestMapping("/loginOut")
    public String loginOut(HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) {
        String token = request.getHeader("Access-Toke");
        tokenManager.remove(tokenManager.userTokenKey, token);
        return ToolUtil.buildResultStr(StatusCode.SUCCESS, StatusCode.getStatusMsg(StatusCode.SUCCESS));
    }


}
