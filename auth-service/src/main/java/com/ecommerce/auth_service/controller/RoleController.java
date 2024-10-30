package com.ecommerce.auth_service.controller;

import com.ecommerce.auth_service.dto.APIResponse;
import com.ecommerce.auth_service.dto.request.RoleRequest;
import com.ecommerce.auth_service.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Role", description = "Role Endpoints")
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ResponseEntity<APIResponse> getRoles() {
        return ResponseEntity.ok(APIResponse.success(roleService.getRoles()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<APIResponse> getRole(@PathVariable String name) {
        return ResponseEntity.ok(APIResponse.success(roleService.getRole(name)));
    }

    @PostMapping
    public ResponseEntity<APIResponse> createRole(@RequestBody RoleRequest createRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(roleService.createRole(createRequest)));
    }

    @PutMapping("/{name}")
    public ResponseEntity<APIResponse> updateRole(@RequestBody RoleRequest updateRequest) {
        return ResponseEntity.ok(APIResponse.success(roleService.updateRole(updateRequest)));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<APIResponse> deleteRole(@PathVariable String name) {
        return ResponseEntity.ok(APIResponse.success(roleService.deleteRole(name)));
    }
}
