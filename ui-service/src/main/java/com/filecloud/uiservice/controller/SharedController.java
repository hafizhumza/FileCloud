package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.client.request.ChangeForgotPasswordRequest;
import com.filecloud.uiservice.client.request.ChangePasswordRequest;
import com.filecloud.uiservice.client.request.ForgotPasswordRequest;
import com.filecloud.uiservice.client.response.ForgotPasswordVerifiedResponse;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.client.request.UpdateUserRequest;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.service.UserService;
import feign.FeignException.BadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class SharedController extends BaseController {

    private final UserService userService;

    @Autowired
    public SharedController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model, @ModelAttribute(UiConst.KEY_RESULT_MESSAGE) String result, @ModelAttribute(UiConst.KEY_ERROR) String error) {
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
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, bindingResult.getFieldErrors());
            return "forgot";
        }

        Result<String> result = userService.forgotPassword(request);

        if (!result.isSuccess()) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, result.getMessage());
            return "redirect:/forgot-password";
        }

        redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, result.getMessage());
        return "redirect:/login";
    }

    @GetMapping("/forgot-password/{token}")
    public String verifyForgotPasswordToken(@PathVariable String token, @ModelAttribute(UiConst.KEY_ERROR) String error, Model model) {
        Result<ForgotPasswordVerifiedResponse> result = userService.verifyForgotPasswordToken(token);

        ChangeForgotPasswordRequest request = new ChangeForgotPasswordRequest();
        request.setId(result.getData().getUserId());
        request.setToken(result.getData().getToken());

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

        Result<String> result = userService.changeForgotPassword(request);

        if (!result.isSuccess()) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, result.getMessage());
            return "redirect:/forgot-password/" + request.getToken();
        }

        redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, result.getMessage());
        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePassword(@ModelAttribute(UiConst.KEY_ERROR) String error, Model model) {
        model.addAttribute("changePassword", new ChangePasswordRequest());
        model.addAttribute(UiConst.KEY_ERROR, (error != null && error.equals("")) ? null : error);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestHeader(value = "referer") final String referer, HttpServletRequest servletRequest, HttpSession session, @Valid ChangePasswordRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        UserSession user = getCurrentUser(session);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, bindingResult.getFieldErrors());
            return "redirect:" + referer;
        }

        Result<String> result = userService.changePassword(user.getAccessToken(), request);

        if (!result.isSuccess()) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, result.getMessage());
            return "redirect:" + referer;
        }

        try {
            UserSession userSession = userService.login(new LoginModel(user.getEmail(), request.getNewPassword()));
            persistSession(servletRequest, userSession);
            redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, "Password successfully changed");
            return "redirect:" + referer;
        } catch (BadRequest e) {
            model.addAttribute(UiConst.KEY_ERROR, UiConst.MESSAGE_INVALID_LOGIN);
            return "login";
        }
    }

    @PostMapping("update-profile")
    public String updateProfile(@RequestHeader(value = "referer") final String referer, HttpServletRequest request, @Valid UpdateUserRequest updateUserRequest, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        UserSession currentUser = getCurrentUser(session);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, bindingResult.getFieldErrors());
            return "redirect:" + referer;
        }

        Result<String> result = userService.updateProfile(currentUser.getAccessToken(), updateUserRequest);

        if (!result.isSuccess())
            redirectAttributes.addFlashAttribute(UiConst.KEY_ERROR, result.getMessage());
        else {
            currentUser.setName(updateUserRequest.getName());
            currentUser.setEmail(updateUserRequest.getEmail());
            persistSession(request, currentUser);
            redirectAttributes.addFlashAttribute(UiConst.KEY_RESULT_MESSAGE, result.getMessage());
        }

        return "redirect:" + referer;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // TODO: Should logout from AuthServer as well
        invalidateSession(request);
        return "redirect:/login";
    }

}
