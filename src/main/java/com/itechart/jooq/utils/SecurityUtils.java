package com.itechart.jooq.utils;

import com.itechart.jooq.exception.ForbiddenAccessException;
import com.itechart.jooq.generated.entity.tables.records.UserRecord;
import com.itechart.jooq.generated.model.RestUser;
import com.itechart.jooq.security.UserDetailsImpl;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

import static com.itechart.jooq.generated.entity.enums.Role.ROLE_ADMIN;
import static com.itechart.jooq.generated.entity.enums.Role.ROLE_USER;
import static com.itechart.jooq.generated.model.RestUserRole.ADMIN;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@UtilityClass
public class SecurityUtils {

    public static UserDetailsImpl getCurrentUser() {
        var principal = getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetailsImpl) principal;
        }
        return null;
    }

    public static boolean hasAdminAccessLevel() {
        return getCurrentUser() != null && getCurrentUser().getRole().equals(ROLE_ADMIN);
    }

    public static void isEnoughPermissions(final RestUser currentUser, final UUID requestedId) {
        if (!(currentUser.getId().equals(requestedId) || currentUser.getRole().equals(ADMIN))) {
            throw new ForbiddenAccessException("Low access level");
        }
    }

    public static void isEnoughPermissions(final RestUser currentUser, final UserRecord requestedUser) {
        if (!(currentUser.getId().equals(requestedUser.getId()) || requestedUser.getRole().equals(ROLE_USER) &&
                currentUser.getRole().equals(ADMIN))) {
            throw new ForbiddenAccessException(String.format("You cannot delete user with id %s", requestedUser.getId()));
        }
    }

}
