package com.app.controller;

import com.app.payloads.requests.CreateWorkerAccountPayload;
import com.app.payloads.responses.ApiPayload;
import com.app.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Api(value = "Admin REST API")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(
            value = "Register worker account",
            response = ResponseEntity.class
    )
    @PostMapping("/create")
    public ResponseEntity createWorkerAccount(@Valid @RequestBody CreateWorkerAccountPayload createWorkerAccountPayload) {
        adminService.createWorkerAccount(createWorkerAccountPayload);
        return ResponseEntity.ok().body(new ApiPayload(true, "User registered successfully"));
    }
}
