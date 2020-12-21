package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.client.response.UserDto;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/admin/")
@Controller
public class AdminController extends BaseController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("home")
    public String home(HttpSession session, Model model) {
        UserSession currentUser = verifyAdmin(session);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        model.addAttribute(UiConst.KEY_ACTIVE_USERS_COUNT, adminService.activeUserCount(getBearerToken(currentUser)));
        model.addAttribute(UiConst.KEY_IN_ACTIVE_USERS_COUNT, adminService.inActiveUserCount(getBearerToken(currentUser)));
        model.addAttribute(UiConst.KEY_ALL_USERS, adminService.allUsersCount(getBearerToken(currentUser)));
        return "admin/home";
    }

    @GetMapping("users")
    public String users(@RequestParam(value = "mode", required = false, defaultValue = "all") String mode,
                        @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String result,
                        HttpSession session, Model model) {
        UserSession currentUser = verifyAdmin(session);
        model.addAttribute(UiConst.KEY_USERS, adminService.listUsers(currentUser.getAccessToken(), mode));
        model.addAttribute(UiConst.KEY_USER, currentUser);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (result != null && result.equals("")) ? null : result);
        return "admin/users";
    }

    @GetMapping("edit-user")
    public String editUser(@RequestParam long id, HttpSession session, Model model) {
        UserSession currentUser = verifyAdmin(session);
        model.addAttribute(UiConst.KEY_PROCESS_USER, adminService.getUser(currentUser.getAccessToken(), id));
        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "admin/edit_user";
    }

    @PostMapping("update-user")
    public String updateUser(@Valid UserDto userDto, HttpSession session, RedirectAttributes redirectAttributes) {
        UserSession currentUser = verifyAdmin(session);
        String result;

        if (userDto.getAccountNonLocked() == null || !userDto.getAccountNonLocked()) {
            result = adminService.disableUser(currentUser.getAccessToken(), userDto.getId());
        } else {
            result = adminService.enableUser(currentUser.getAccessToken(), userDto.getId());
        }

        redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, result);
        return "redirect:/admin/users";
    }

    @GetMapping("delete-user")
    public String deleteUser(@RequestParam long id, HttpSession session, RedirectAttributes redirectAttributes) {
        UserSession currentUser = verifyAdmin(session);
        String result = adminService.deleteUser(currentUser.getAccessToken(), id);
        redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, result);
        return "redirect:/admin/users";
    }

}
