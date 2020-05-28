package com.itechart.jooq.service;

import com.itechart.jooq.generated.model.RestSignInRequest;
import com.itechart.jooq.generated.model.RestSignUpRequest;
import com.itechart.jooq.generated.model.RestUpdateUserRequest;
import com.itechart.jooq.generated.model.RestUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    Page<RestUser> getPage(Pageable pageable);

    RestUser create(RestSignUpRequest signUpRequest);

    RestUser getCurrent();

    RestUser getById(UUID id);

    RestUser update(UUID id, RestUpdateUserRequest newUser);

    void deleteById(UUID id);

    void existsByLogin(String email);

    RestUser validateCredentials(RestSignInRequest signInRequest);

}
