package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.client.request.ChangeForgotPasswordRequest;
import com.filecloud.uiservice.client.request.ForgotPasswordRequest;
import com.filecloud.uiservice.client.response.ForgotPasswordVerifiedResponse;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Locale;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model,
                        @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String result,
                        @ModelAttribute(UiConst.KEY_ERROR) String error) {
        if (session != null) {
            UserSession userSession = (UserSession) session.getAttribute(UiConst.SESSION_CURRENT_USER);

            if (userSession != null) {
                if (isAdmin(userSession))
                    return "redirect:/admin/home";
                else
                    return "redirect:/home";
            }
        }

        model.addAttribute(UiConst.KEY_LOGIN_MODEL, new LoginModel());
        model.addAttribute(UiConst.KEY_ERROR, (error != null && error.equals("")) ? null : error);
        model.addAttribute(UiConst.KEY_RESULT_MESSAGE, (result != null && result.equals("")) ? null : result);
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

    @GetMapping("/forgot-password")
    public String forgotPassword(@ModelAttribute(UiConst.KEY_ERROR) String error, Model model) {
        model.addAttribute("forgotPassword", new ForgotPasswordRequest());
        model.addAttribute(UiConst.KEY_ERROR, (error != null && error.equals("")) ? null : error);
        return "forgot";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid ForgotPasswordRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors())
            return "forgot";

        String message = userService.forgotPassword(request);

        if (message.toLowerCase().contains("no account found")) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, message);
            return "redirect:/forgot-password";
        }

        redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, message);
        return "redirect:/login";
    }

    @GetMapping("/forgot-password/{token}")
    public String verifyForgotPasswordToken(@PathVariable String token, @ModelAttribute(UiConst.KEY_ERROR) String error, Model model) {
        ForgotPasswordVerifiedResponse response = userService.verifyForgotPasswordToken(token);

        ChangeForgotPasswordRequest request = new ChangeForgotPasswordRequest();
        request.setId(response.getUserId());
        request.setToken(response.getToken());

        model.addAttribute("changeForgotPassword", request);
        model.addAttribute(UiConst.KEY_ERROR, (error != null && error.equals("")) ? null : error);
        return "reset-password";
    }

    @PostMapping("/change-forgot-password")
    public String changeForgotPassword(@Valid ChangeForgotPasswordRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, bindingResult.getFieldErrors());
            return "redirect:/forgot-password/" + request.getToken();
        }

        String message = userService.changeForgotPassword(request);

        if (message.toLowerCase().contains("not match")) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, message);
            return "redirect:/forgot-password/" + request.getToken();
        }

        redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, message);
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        UserSession currentUser = getCurrentUser(session);

        if (isAdmin(currentUser))
            return "redirect:/admin/home";

        model.addAttribute(UiConst.KEY_USER, currentUser);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // TODO: Should logout from AuthServer as well
        invalidateSession(request);
        return "redirect:/login";
    }

}
