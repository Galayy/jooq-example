package com.itechart.jooq.service.impl;

import com.itechart.jooq.exception.EntityAlreadyProcessedException;
import com.itechart.jooq.exception.ForbiddenAccessException;
import com.itechart.jooq.exception.IncorrectJwtAuthenticationException;
import com.itechart.jooq.generated.entity.tables.User;
import com.itechart.jooq.generated.entity.tables.records.UserRecord;
import com.itechart.jooq.generated.model.RestSignInRequest;
import com.itechart.jooq.generated.model.RestSignUpRequest;
import com.itechart.jooq.generated.model.RestUpdateUserRequest;
import com.itechart.jooq.generated.model.RestUser;
import com.itechart.jooq.repository.impl.UserRepository;
import com.itechart.jooq.security.UserDetailsImpl;
import com.itechart.jooq.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

import static com.itechart.jooq.generated.entity.enums.Role.ROLE_USER;
import static com.itechart.jooq.mapper.UserMapper.USER_MAPPER;
import static com.itechart.jooq.utils.SecurityUtils.*;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<RestUser> getPage(final Pageable pageable) {
        var condition = User.USER.DELETED_AT.isNull().and(User.USER.ROLE.eq(ROLE_USER));
        var preparedCondition = hasAdminAccessLevel() ? DSL.noCondition() : condition;
        return userRepository.search(preparedCondition, pageable).map(USER_MAPPER::toModel);
    }

    @Override
    @Transactional
    public RestUser create(final RestSignUpRequest signUpRequest) {
        existsByLogin(signUpRequest.getEmail());
        var encryptedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        var record = new UserRecord();
        record.setPhone(signUpRequest.getPhone());
        record.setPassword(encryptedPassword);
        record.setLogin(signUpRequest.getEmail());
        record.setFirstName(signUpRequest.getFirstName());
        record.setLastName(signUpRequest.getLastName());
        record.setRole(ROLE_USER);

        return USER_MAPPER.toModel(userRepository.create(record));
    }

    @Override
    @Transactional(readOnly = true)
    public RestUser getCurrent() {
        return userRepository.findById(requireNonNull(getCurrentUser()).getId())
                .map(record -> USER_MAPPER.toModel((UserRecord) record));
    }

    @Override
    @Transactional(readOnly = true)
    public RestUser getById(final UUID id) {
        return userRepository.findById(id).map(record -> USER_MAPPER.toModel((UserRecord) record));
    }

    @Override
    @Transactional
    public RestUser update(final UUID id, final RestUpdateUserRequest userToUpdate) {
        var recordToUpdate = userRepository.findById(id);
        var currentUser = getCurrent();
        isEnoughPermissions(currentUser, id);

        recordToUpdate.setPhone(userToUpdate.getNewPhone());
        recordToUpdate.setFirstName(userToUpdate.getNewFirstName());
        recordToUpdate.setLastName(userToUpdate.getNewLastName());

        return USER_MAPPER.toModel(userRepository.update(recordToUpdate));
    }

    @Override
    @Transactional
    public void deleteById(final UUID id) {
        var entity = userRepository.findById(id);
        var currentUser = getCurrent();
        isEnoughPermissions(currentUser, entity);

        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public void existsByLogin(final String email) {
        if (userRepository.existsByLogin(email)) {
            throw new EntityAlreadyProcessedException(format("User with email %s already exists", email));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RestUser validateCredentials(RestSignInRequest signInRequest) {
        var user = userRepository.findByLogin(signInRequest.getEmail());
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new IncorrectJwtAuthenticationException("Invalid password");
        }
        return USER_MAPPER.toModel(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String id) throws UsernameNotFoundException {
        var entity = userRepository.findById(UUID.fromString(id));
        isUserDeleted(entity.getDeletedAt());

        var userDetails = new UserDetailsImpl();
        userDetails.setId(entity.getId());
        userDetails.setRole(entity.getRole());
        return userDetails;
    }

    private void isUserDeleted(final Timestamp deletedAt) {
        if (deletedAt != null) {
            throw new ForbiddenAccessException(format("User was deleted at %s", deletedAt));
        }
    }

}
