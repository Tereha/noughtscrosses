package ru.noughtscrosses.db.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.noughtscrosses.db.model.RoundUser;
import ru.noughtscrosses.db.repository.helpers.RowParameterMapper;
import ru.noughtscrosses.db.repository.helpers.RowParameterSource;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.DbResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoundUserRepository extends AbstractRepository<RoundUser> {
  private RoundUserMapper roundUserMapper = new RoundUserMapper();

  public RoundUser add(RoundUser data) throws ApiException {
    try {
      performUpdate(INSERT, data);
      return data;
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new ApiException(DbResponse.DATA_UPDATE_ERROR.withDescription(ex.getMessage()));
    }
  }

  public Optional<RoundUser> findById(UUID id) {
    throw new RuntimeException("No implement method");
  }

  private GeneratedKeyHolder performUpdate(String sql, RoundUser data) {
    SqlParameterSource params = new RoundUserParams(data);
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
  public static final String TBL_NAME = "round_user";

  public static final String COL_USER_ID = "user_id";
  public static final String COL_ROUND_ID = "round_id";

  private static final String INSERT =
      assembleInsert(
          TBL_NAME,
          List.of(
              COL_USER_ID, COL_ROUND_ID));

  static class RoundUserParams extends RowParameterSource<RoundUser> {
    RoundUserParams(RoundUser data) {
      super(data);
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_USER_ID, RoundUser::getUserId);
      setUUIDValue(COL_ROUND_ID, RoundUser::getRoundId);
    }
  }

  static class RoundUserMapper extends RowParameterMapper<RoundUser> {

    @Override
    protected RoundUser getInstance() {
      return new RoundUser();
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_USER_ID, RoundUser::setUserId);
      setUUIDValue(COL_ROUND_ID, RoundUser::setRoundId);
    }
  }

}
