package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/admin/")
@Controller
public class AdminController extends BaseController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("home")
    public String home(HttpServletRequest request, HttpSession session, Model model) {
        UserSession currentUser = getVerifiedAdminUser(session);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        model.addAttribute(UiConst.KEY_ACTIVE_USERS_COUNT, adminService.activeUserCount(getBearerToken(currentUser)));
        model.addAttribute(UiConst.KEY_IN_ACTIVE_USERS_COUNT, adminService.inActiveUserCount(getBearerToken(currentUser)));
        model.addAttribute(UiConst.KEY_ALL_USERS, adminService.allUsersCount(getBearerToken(currentUser)));
        return "home";
    }

}
