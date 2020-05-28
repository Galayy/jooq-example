package com.itechart.jooq.repository.impl;

import com.itechart.jooq.exception.EntityNotFoundException;
import com.itechart.jooq.generated.entity.tables.User;
import com.itechart.jooq.generated.entity.tables.records.UserRecord;
import com.itechart.jooq.repository.CrudRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository implements CrudRepository<UserRecord> {

    private final DSLContext dsl;

    @Override
    public UserRecord create(final UserRecord user) {
        return dsl.insertInto(User.USER)
                .set(user)
                .set(User.USER.CREATED_AT, Timestamp.from(Instant.now()))
                .returning()
                .fetchOne();
    }

    @Override
    public UserRecord update(final UserRecord userRecord) {
        return dsl.update(User.USER)
                .set(dsl.newRecord(User.USER, userRecord))
                .where(User.USER.ID.eq(userRecord.getId()))
                .returning()
                .fetchOne();
    }

    @Override
    public UserRecord findById(final UUID id) {
        return dsl.selectFrom(User.USER)
                .where(User.USER.ID.eq(id).and(User.USER.DELETED_AT.isNull()))
                .fetchOptional()
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s doesn't exist", id)));
    }

    @Override
    public Page<UserRecord> search(final Condition condition, final Pageable pageable) {
        var records = dsl.selectFrom(User.USER)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().into(UserRecord.class);

        return new PageImpl<>(records);
    }

    @Override
    public Boolean deleteById(final UUID id) {
        return dsl.update(User.USER)
                .set(User.USER.DELETED_AT, Timestamp.from(Instant.now()))
                .where(User.USER.ID.eq(id))
                .execute() == SUCCESS_CODE;
    }

    public UserRecord findByLogin(final String email) {
        return dsl.selectFrom(User.USER)
                .where(User.USER.LOGIN.eq(email).and(User.USER.DELETED_AT.isNull()))
                .fetchOptional()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Admin with username %s doesn't exist",
                        email)));
    }

    public Boolean existsByLogin(final String email) {
        return dsl.selectFrom(User.USER)
                .where(User.USER.LOGIN.eq(email).and(User.USER.DELETED_AT.isNull()))
                .execute() == SUCCESS_CODE;
    }

}
