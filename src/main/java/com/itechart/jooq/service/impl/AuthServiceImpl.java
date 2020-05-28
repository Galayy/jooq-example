package com.itechart.jooq.service.impl;

import com.itechart.jooq.exception.EntityAlreadyProcessedException;
import com.itechart.jooq.generated.model.*;
import com.itechart.jooq.security.JwtTokenProvider;
import com.itechart.jooq.security.UserDetailsImpl;
import com.itechart.jooq.service.AuthService;
import com.itechart.jooq.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.itechart.jooq.mapper.UserMapper.USER_MAPPER;
import static com.itechart.jooq.utils.SecurityUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public RestUser signUp(final RestSignUpRequest signUpRequest) {
        isSignedIn();
        isNotExist(signUpRequest.getEmail());
        return userService.create(signUpRequest);
    }

    @Override
    public RestTokenResponse signIn(final RestSignInRequest signInRequest) {
        var user = userService.validateCredentials(signInRequest);

        var userDetails = new UserDetailsImpl();
        userDetails.setId(user.getId());
        userDetails.setRole(USER_MAPPER.toEntity(user.getRole()));
        return jwtTokenProvider.createToken(userDetails);
    }

    @Override
    public RestTokenResponse refresh(final RestTokenRequest tokenRequest) {
        return jwtTokenProvider.refreshTokens(tokenRequest.getToken());
    }

    private void isSignedIn() {
        if (getCurrentUser() != null) {
            throw new EntityAlreadyProcessedException("You can't sign up until logout");
        }
    }

    private void isNotExist(final String email) {
        userService.existsByLogin(email);
    }

}
