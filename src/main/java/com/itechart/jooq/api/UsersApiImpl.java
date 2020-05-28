package com.itechart.jooq.api;

import com.itechart.jooq.generated.api.UsersApi;
import com.itechart.jooq.generated.model.RestUpdateUserRequest;
import com.itechart.jooq.generated.model.RestUser;
import com.itechart.jooq.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.itechart.jooq.utils.HeaderUtils.generatePaginationHeaders;
import static com.itechart.jooq.utils.PagingUtils.toPageable;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Api(tags = "users")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class UsersApiImpl implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<List<RestUser>> getUsers(@RequestParam(defaultValue = "1") final Integer page,
                                               @RequestParam(defaultValue = "100") final Integer size) {
        var users = userService.getPage(toPageable(page, size));
        var headers = generatePaginationHeaders(users);
        return new ResponseEntity<>(users.getContent(), headers, OK);
    }

    @Override
    public ResponseEntity<RestUser> getCurrentUser() {
        var user = userService.getCurrent();
        return new ResponseEntity<>(user, OK);
    }

    @Override
    public ResponseEntity<RestUser> getUser(@PathVariable final UUID id) {
        var user = userService.getById(id);
        return new ResponseEntity<>(user, OK);
    }

    @Override
    public ResponseEntity<RestUser> updateUser(@PathVariable final UUID id,
                                           @Valid @RequestBody final RestUpdateUserRequest newUser) {
        var updatedUser = userService.update(id, newUser);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable final UUID id) {
        userService.deleteById(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

}
