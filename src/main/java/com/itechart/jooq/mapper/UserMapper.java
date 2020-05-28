package com.itechart.jooq.mapper;

import com.itechart.jooq.generated.entity.enums.Role;
import com.itechart.jooq.generated.entity.tables.records.UserRecord;
import com.itechart.jooq.generated.model.RestUser;
import com.itechart.jooq.generated.model.RestUserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "email", source = "login")
    @Mapping(target = "createdAt", expression = "java(record.getCreatedAt().toInstant())")
    @Mapping(target = "updatedAt", expression = "java(record.getUpdatedAt() != null ? record.getUpdatedAt().toInstant()"
            + " : null)")
    @Mapping(target = "deletedAt", expression = "java(record.getDeletedAt() != null ? record.getDeletedAt().toInstant()"
            + " : null)")
    RestUser toModel(UserRecord record);

    @ValueMappings({
            @ValueMapping(source = "ROLE_ADMIN", target = "ADMIN"),
            @ValueMapping(source = "ROLE_USER", target = "USER")
    })
    RestUserRole toModel(Role role);

    @ValueMappings({
            @ValueMapping(source = "ADMIN", target = "ROLE_ADMIN"),
            @ValueMapping(source = "USER", target = "ROLE_USER")
    })
    Role toEntity(RestUserRole role);

}
