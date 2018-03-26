package com.github.chanming2015.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public String login(Model model) {

        String loginName = "admin";
        String password = "123";

        Subject subject = SecurityUtils.getSubject();
        try {

            subject.login(new UsernamePasswordToken(loginName, password));
            if(subject.getSession(false).getAttribute("user") == null){
                subject.logout();
                subject.login(new UsernamePasswordToken(loginName, password));
            }
        } catch (UnknownAccountException e) {
            logger.info("not exist userName or disabled: "+loginName);
            model.addAttribute("error", "用户不存在或已被禁用");
        } catch(IncorrectCredentialsException e) {
            logger.info("incorrect password for userName: " + loginName);
            model.addAttribute("error", "密码错误！");
        } catch(ExcessiveAttemptsException e) {
            logger.info("excessive attempts for userName: "+loginName+", times="+e.getMessage());
            model.addAttribute("error", "密码连续错误"+e.getMessage()+"次，请5分钟后再试！");
        } catch(Exception e) {
            logger.info("unknown exception for userName: " + loginName + ", error : " + e.getMessage());
            model.addAttribute("error", "未知登录错误 : " + e.getMessage());
        }

        // 当前仅作身份认证，并未验证权限
        if(subject.isAuthenticated()){
            return "redirect:home.html";
        }else{
            return "redirect:login.html";
        }
    }

    @RequestMapping("404")
    public String pageNotFound(Model model) {
        model.addAttribute("error", "Error hints");
        return "404";
    }

    @RequestMapping("500")
    public String internalError(Model model) {
        model.addAttribute("error", "Error hints");
        return "500";
    }
}
