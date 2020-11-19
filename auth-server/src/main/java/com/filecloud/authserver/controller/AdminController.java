package com.filecloud.authserver.controller;

import com.filecloud.authserver.model.dto.RequestUserDto;
import com.filecloud.authserver.security.role.Admin;
import com.filecloud.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Admin
@Transactional(readOnly = true)
@RequestMapping("api/v1/admin")
@RestController
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list-users")
    public ResponseEntity<?> users() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/enable-user")
    public ResponseEntity<?> enableUser(@RequestBody RequestUserDto dto) {
        userService.enableUser(dto);
        return new ResponseEntity<>("User enabled successfully!", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/disable-user")
    public ResponseEntity<?> disableUser(@RequestBody RequestUserDto dto) {
        userService.disableUser(dto);
        return new ResponseEntity<>("User disabled successfully!", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody RequestUserDto dto) {
        userService.deleteUser(dto);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}
