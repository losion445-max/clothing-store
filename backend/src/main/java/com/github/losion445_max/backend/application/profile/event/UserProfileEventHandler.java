package com.github.losion445_max.backend.application.profile.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.losion445_max.backend.application.profile.UserProfileService;
import com.github.losion445_max.backend.domain.account.UserRegisteredEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileEventHandler {

    private final UserProfileService profileService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Creating user profile for user {}", event.userId());

        profileService.create(event.userId());
    }
}