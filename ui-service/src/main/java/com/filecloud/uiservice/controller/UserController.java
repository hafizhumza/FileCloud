package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.service.UserService;
import feign.FeignException.BadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute(UiConst.KEY_LOGIN_MODEL, new LoginModel());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(HttpServletRequest request, @Valid LoginModel loginModel, BindingResult result, Model model) {
        if (result.hasErrors())
            return "login";

        try {
            UserSession userSession = userService.login(loginModel);
            persistSession(request, userSession);

            if (isAdmin(userSession))
                return "redirect:/admin/home";
            else
                return "redirect:/home";
        } catch (BadRequest e) {
            model.addAttribute(UiConst.KEY_ERROR, UiConst.MESSAGE_INVALID_LOGIN);
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request, HttpSession session, Model model) {
        UserSession currentUser = getCurrentUser(session);

        if (isAdmin(currentUser))
            return "redirect:/admin/home";

        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "home";
    }

}
