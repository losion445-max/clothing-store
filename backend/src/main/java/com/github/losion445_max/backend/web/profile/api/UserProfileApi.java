package com.github.losion445_max.backend.web.profile.api;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.github.losion445_max.backend.application.profile.command.AddAddressCommand;
import com.github.losion445_max.backend.application.profile.command.UpdateUserProfileCommand;
import com.github.losion445_max.backend.application.profile.result.AddAddressResult;
import com.github.losion445_max.backend.application.profile.result.GetFullUserProfileResult;
import com.github.losion445_max.backend.application.profile.result.UpdateUserProfileResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Profile", description = "Manage user profiles")
@RequestMapping("/api/profile")
public interface UserProfileApi {

    @Operation(summary = "Get full user profile")
    @GetMapping
    ResponseEntity<GetFullUserProfileResult> getProfile(@AuthenticationPrincipal String userId);

    @Operation(summary = "Update user profile")
    @PutMapping
    ResponseEntity<UpdateUserProfileResult> updateProfile(@AuthenticationPrincipal String userId, @RequestBody UpdateUserProfileCommand command);

    @Operation(summary = "Add address to user profile")
    @PostMapping("/addresses")
    ResponseEntity<AddAddressResult> addAddress(@AuthenticationPrincipal String userId, @RequestBody AddAddressCommand command);
}