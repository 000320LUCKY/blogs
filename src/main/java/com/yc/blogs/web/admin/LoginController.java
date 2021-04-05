package com.yc.blogs.web.admin;

import com.yc.blogs.po.User;
import com.yc.blogs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String loginPage() {
        return "admin/login";
    }


    /**
     *
     * @param username
     * @param password
     * @param session
     * @param attributes 给前端页面错误提示方法
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username,password);
        System.out.println("-------login-------");
        System.out.println("登录！");
        if(user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            System.out.println("登录成功！");
            return "admin/index";
        }else {
            attributes.addFlashAttribute("message","用户名和密码错误");
            System.out.println("登录1！");
            return "redirect:/admin";

        }

    }

    /**
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        System.out.println("注销成功！");
        return "redirect:/admin";
    }

}
