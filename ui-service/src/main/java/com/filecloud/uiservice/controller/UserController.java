package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {
        UserSession currentUser = getCurrentUser(session);

        if (isAdmin(currentUser))
            return "redirect:/admin/home";

        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "user/home";
    }

}
