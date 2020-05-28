package com.itechart.jooq.service;

import com.itechart.jooq.generated.model.*;

public interface AuthService {

    RestUser signUp(RestSignUpRequest signUpRequest);

    RestTokenResponse signIn(RestSignInRequest signInRequest);

    RestTokenResponse refresh(RestTokenRequest tokenRequest);

}
