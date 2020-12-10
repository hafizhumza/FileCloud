package com.filecloud.adminservice.controller;

import com.filecloud.adminservice.model.dto.SingleIdRequestDto;
import com.filecloud.adminservice.response.Result;
import com.filecloud.adminservice.security.role.Admin;
import com.filecloud.adminservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Admin
@RequestMapping("api/v1/")
@RestController
public class AdminController extends BaseController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("list-users")
    public Result<?> listUsers() {
        return adminService.listUsers();
    }

    @GetMapping("get-user/{userId}")
    public Result<?> getUser(@PathVariable long userId) {
        return adminService.getUser(userId);
    }

    @PostMapping("enable-user")
    public Result<?> enableUser(@RequestBody SingleIdRequestDto dto) {
        return adminService.enableUser(dto);
    }

    @PostMapping("disable-user")
    public Result<?> disableUser(@RequestBody SingleIdRequestDto dto) {
        return adminService.disableUser(dto);
    }

    @PostMapping("delete-user")
    public Result<?> deleteUser(@RequestBody SingleIdRequestDto dto) {
        return adminService.deleteUser(dto);
    }

}
