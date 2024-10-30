package com.ecommerce.auth_service.controller;

import com.ecommerce.auth_service.dto.APIResponse;
import com.ecommerce.auth_service.dto.request.PermissionRequest;
import com.ecommerce.auth_service.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission", description = "Permission Endpoints")
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ResponseEntity<APIResponse> getPermissions() {
        return ResponseEntity.ok(APIResponse.success(permissionService.getPermissions()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<APIResponse> getPermission(@PathVariable String name) {
        return ResponseEntity.ok(APIResponse.success(permissionService.getPermission(name)));
    }

    @PostMapping
    public ResponseEntity<APIResponse> createPermission(@RequestBody PermissionRequest createRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(permissionService.createPermission(createRequest)));
    }

    @PutMapping("/{name}")
    public ResponseEntity<APIResponse> updatePermission(@RequestBody PermissionRequest updateRequest) {
        return ResponseEntity.ok(APIResponse.success(permissionService.updatePermission(updateRequest)));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<APIResponse> deletePermission(@PathVariable String name) {
        return ResponseEntity.ok(APIResponse.success(permissionService.deletePermission(name)));
    }
}
