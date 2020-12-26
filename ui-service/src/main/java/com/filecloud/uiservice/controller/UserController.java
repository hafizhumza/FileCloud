package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.client.request.ChangePasswordRequest;
import com.filecloud.uiservice.client.request.UpdateUserRequest;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.service.DocumentService;
import com.filecloud.uiservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    private final DocumentService documentService;

    @Autowired
    public UserController(UserService userService, DocumentService documentService) {
        this.userService = userService;
        this.documentService = documentService;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {
        UserSession currentUser = getCurrentUser(session);

        if (isAdmin(currentUser))
            return "redirect:/admin/home";

        model.addAttribute("documentCount", documentService.count(getBearerToken(currentUser)));
        model.addAttribute("usedSpace", documentService.spaceInfo(getBearerToken(currentUser)).getUsedSpace());
        model.addAttribute("userCount", userService.activeUserCount(getBearerToken(currentUser)));
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "user/home";
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) String mode, HttpSession session, Model model, @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String resultMessage, @ModelAttribute(UiConst.KEY_ERROR) String errorMessage) {
        UserSession user = getVerifiedUser(session);
        model.addAttribute(UiConst.KEY_USER, user);
        model.addAttribute("mode", mode);
        model.addAttribute("changePassword", new ChangePasswordRequest());
        model.addAttribute("updateProfile", new UpdateUserRequest(user.getName(), user.getEmail()));
        model.addAttribute(UiConst.KEY_ERROR, (errorMessage != null && errorMessage.equals("")) ? null : errorMessage);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (resultMessage != null && resultMessage.equals("")) ? null : resultMessage);
        return "user/profile";
    }

}
