package ru.noughtscrosses.db.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.noughtscrosses.constant.RoundType;
import ru.noughtscrosses.constant.Status;
import ru.noughtscrosses.db.model.Round;
import ru.noughtscrosses.db.repository.helpers.RowParameterMapper;
import ru.noughtscrosses.db.repository.helpers.RowParameterSource;
import ru.noughtscrosses.db.repository.helpers.constant.JoinType;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.DbResponse;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoundRepository extends AbstractRepository<Round> {
  private RoundMapper roundMapper = new RoundMapper();

  @Override
  public Round add(Round data) throws ApiException {
    try {
      data.setId(UUID.randomUUID());
      data.setStatus(Status.ACTIVE);
      performUpdate(INSERT, data);
      return data;
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new ApiException(DbResponse.DATA_UPDATE_ERROR.withDescription(ex.getMessage()));
    }
  }

  @Override
  public Optional<Round> findById(UUID id) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(SELECT + assembleWhere(COL_ID), RoundParams.paramsById(id), roundMapper));
    } catch (EmptyResultDataAccessException noResult) {
      return Optional.empty();
    }
  }

  public List<Round> findByUserIdAndStatus(UUID userId, Status status) {
    StringBuilder sql = new StringBuilder();
    sql.append(SELECT).append(AS_OPER).append("r ");

    sql.append(
        assembleJoin(
            "r",
            COL_ID,
            JoinType.RIGHT_JOIN,
            RoundUserRepository.TBL_NAME,
            "ru",
            RoundUserRepository.COL_ROUND_ID));

    sql.append(assembleWhere(List.of(COL_STATUS, RoundUserRepository.COL_USER_ID)));

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(COL_STATUS, status.name());
    params.addValue(RoundUserRepository.COL_USER_ID, userId.toString());

    return jdbcTemplate.query(sql.toString(), params, roundMapper);
  }

  public List<Round> findByTypeAndStatus(RoundType type, Status status) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(COL_TYPE, type.name());
    params.addValue(COL_STATUS, status.name());
    return jdbcTemplate.query(SELECT + assembleWhere(List.of(COL_TYPE, COL_STATUS)), params, roundMapper);
  }

  public void updateStatusAndWinnerUserId(UUID roundId, UUID userId) {
    Round data = new Round();
    data.setId(roundId);
    data.setWinnerUserId(userId);
    data.setStatus(Status.INACTIVE);
    try {
      jdbcTemplate.update(
          assembleUpdate(TBL_NAME, List.of(COL_ID, COL_WINNER_USER_ID, COL_STATUS)),
          new RoundParams(data));
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new ApiException(DbResponse.DATA_UPDATE_ERROR);
    }
  }

  private GeneratedKeyHolder performUpdate(String sql, Round data) {
    SqlParameterSource params = new RoundParams(data);
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

  public static final String TBL_NAME = "round";

  private static final String COL_ID = "id";
  private static final String COL_TYPE = "type";
  private static final String COL_STATUS = "status";
  private static final String COL_WINNER_USER_ID = "winner_user_id";

  private static final String INSERT =
      assembleInsert(
          TBL_NAME,
          List.of(
              COL_ID, COL_TYPE,
              COL_STATUS, COL_WINNER_USER_ID));

  private static final String SELECT =
      assembleSelect(
          TBL_NAME,
          List.of(
              COL_ID, COL_TYPE,
              COL_STATUS, COL_WINNER_USER_ID));

  static class RoundParams extends RowParameterSource<Round> {
    RoundParams(Round data) {
      super(data);
    }

    static RoundParams paramsById(UUID id) {
      Round data = new Round();
      data.setId(id);
      return new RoundParams(data);
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_ID, Round::getId);
      setStringValue(COL_TYPE, (d) -> d.getType() != null ? d.getType().name() : RoundType.BOT.name());
      setStringValue(COL_STATUS, (d) -> d.getStatus() != null ? d.getStatus().name() : Status.ACTIVE.name());
      setUUIDValue(COL_WINNER_USER_ID, Round::getWinnerUserId);
    }
  }

  static class RoundMapper extends RowParameterMapper<Round> {

    @Override
    protected Round getInstance() {
      return new Round();
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_ID, Round::setId);
      setStringValue(COL_TYPE, (d, v) -> d.setType(v != null ? RoundType.valueOf(v) : null));
      setStringValue(COL_STATUS, (d, v) -> d.setStatus(v != null ? Status.valueOf(v) : null));
      setUUIDValue(COL_WINNER_USER_ID, Round::setWinnerUserId);
    }
  }

}
