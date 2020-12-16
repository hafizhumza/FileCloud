package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginModel", new LoginModel());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid LoginModel loginModel, BindingResult result, Model model) {
        if (result.hasErrors())
            return "login";

        if (userService.login(loginModel)) {
            return "home";
        } else {
            model.addAttribute(UiConst.KEY_ERROR, UiConst.MESSAGE_INVALID_LOGIN);
            return "login";
        }

    }

}
