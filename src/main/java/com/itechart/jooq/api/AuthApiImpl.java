package com.itechart.jooq.api;

import com.itechart.jooq.generated.api.AuthApi;
import com.itechart.jooq.generated.model.*;
import com.itechart.jooq.service.AuthService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Api(tags = "auth")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthApiImpl implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<RestUser> signUp(@Valid @RequestBody final RestSignUpRequest signUpRequest) {
        var user = authService.signUp(signUpRequest);
        return new ResponseEntity<>(user, CREATED);
    }

    @Override
    public ResponseEntity<RestTokenResponse> signIn(@Valid @RequestBody final RestSignInRequest signInRequest) {
        var tokenResponse = authService.signIn(signInRequest);
        return new ResponseEntity<>(tokenResponse, CREATED);
    }

    @Override
    public ResponseEntity<RestTokenResponse> refresh(@RequestBody final RestTokenRequest tokenRequest) {
        var tokenResponse = authService.refresh(tokenRequest);
        return new ResponseEntity<>(tokenResponse, CREATED);
    }

}
