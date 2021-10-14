package ru.noughtscrosses.db.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.noughtscrosses.db.model.User;
import ru.noughtscrosses.db.repository.helpers.RowParameterMapper;
import ru.noughtscrosses.db.repository.helpers.RowParameterSource;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.DbResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository extends AbstractRepository<User> {
  private UserMapper userMapper = new UserMapper();

  @Override
  public User add(User data) throws ApiException {
    try {
      data.setId(UUID.randomUUID());
      performUpdate(INSERT, data);
      return data;
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new ApiException(DbResponse.DATA_UPDATE_ERROR.withDescription(ex.getMessage()));
    }
  }

  @Override
  public Optional<User> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(SELECT + assembleWhere(COL_ID), UserParams.paramsById(id), userMapper));
    } catch (EmptyResultDataAccessException noResult) {
      return Optional.empty();
    }
  }

  public Optional<User> findByName(String name) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(SELECT + assembleWhere(COL_NAME), UserParams.paramsByName(name), userMapper));
    } catch (EmptyResultDataAccessException noResult) {
      return Optional.empty();
    }
  }

  private GeneratedKeyHolder performUpdate(String sql, User data) {
    SqlParameterSource params = new UserParams(data);
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    int rowAffected = jdbcTemplate.update(sql, params, keyHolder);
    if (rowAffected == 0) {
      logger.warn("0 row affected");
    }
    return keyHolder;
  }
  /*
   * parameter mappings
   */

  public static final String TBL_NAME = "user";

  private static final String COL_ID = "id";
  private static final String COL_NAME = "name";

  private static final String INSERT =
      assembleInsert(
          TBL_NAME,
          List.of(
              COL_ID,
              COL_NAME));

  private static final String SELECT =
      assembleSelect(
          TBL_NAME,
          List.of(
              COL_ID,
              COL_NAME));

  static class UserParams extends RowParameterSource<User> {
    UserParams(User data) {
      super(data);
    }

    static UserParams paramsById(UUID id) {
      User data = new User();
      data.setId(id);
      return new UserParams(data);
    }

    static UserParams paramsByName(String name) {
      User data = new User();
      data.setName(name);
      return new UserParams(data);
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_ID, User::getId);
      setStringValue(COL_NAME, User::getName);
    }
  }

  static class UserMapper extends RowParameterMapper<User> {

    @Override
    protected User getInstance() {
      return new User();
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_ID, User::setId);
      setStringValue(COL_NAME, User::setName);
    }
  }

}
