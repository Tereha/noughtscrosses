package ru.noughtscrosses.db.repository;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.noughtscrosses.constant.PositionType;
import ru.noughtscrosses.constant.StepType;
import ru.noughtscrosses.db.model.RoundStep;
import ru.noughtscrosses.db.repository.helpers.RowParameterMapper;
import ru.noughtscrosses.db.repository.helpers.RowParameterSource;
import ru.noughtscrosses.db.repository.helpers.constant.OrderByType;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.DbResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoundStepRepository extends AbstractRepository<RoundStep> {
  private RoundStepMapper roundStepMapper = new RoundStepMapper();

  public RoundStep add(RoundStep data) throws ApiException {
    try {
      performUpdate(INSERT, data);
      return data;
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new ApiException(DbResponse.DATA_UPDATE_ERROR.withDescription(ex.getMessage()));
    }
  }

  @Override
  public Optional<RoundStep> findById(UUID id) {
    throw new RuntimeException("No implement method");
  }

  public List<RoundStep> findByRoundId(UUID id) {
    StringBuilder sql = new StringBuilder(SELECT);
    sql.append(assembleWhere(COL_ROUND_ID));
    sql.append(assembleOrderBy(COL_ORDER, OrderByType.ASC));

    return jdbcTemplate.query(sql.toString(), RoundStepParams.paramsByRoundId(id), roundStepMapper);
  }

  private GeneratedKeyHolder performUpdate(String sql, RoundStep data) {
    SqlParameterSource params = new RoundStepParams(data);
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
  public static final String TBL_NAME = "round_step";

  private static final String COL_USER_ID = "user_id";
  private static final String COL_ROUND_ID = "round_id";
  private static final String COL_TYPE = "type";
  private static final String COL_POSITION = "position";
  private static final String COL_ORDER = "order";

  private static final String INSERT =
      assembleInsert(
          TBL_NAME,
          List.of(
              COL_USER_ID, COL_ROUND_ID, COL_TYPE, COL_POSITION, COL_ORDER));

  private static final String SELECT =
      assembleSelect(
          TBL_NAME,
          List.of(
              COL_USER_ID, COL_ROUND_ID, COL_TYPE, COL_POSITION, COL_ORDER));

  static class RoundStepParams extends RowParameterSource<RoundStep> {
    RoundStepParams(RoundStep data) {
      super(data);
    }

    static RoundStepParams paramsByRoundId(UUID id) {
      RoundStep data = new RoundStep();
      data.setRoundId(id);
      return new RoundStepParams(data);
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_USER_ID, RoundStep::getUserId);
      setUUIDValue(COL_ROUND_ID, RoundStep::getRoundId);
      setStringValue(COL_TYPE, (d) -> d.getType().name());
      setStringValue(COL_POSITION, (d) -> d.getPosition().name());
      setIntValue(COL_ORDER, RoundStep::getOrder);
    }
  }

  static class RoundStepMapper extends RowParameterMapper<RoundStep> {

    @Override
    protected RoundStep getInstance() {
      return new RoundStep();
    }

    @Override
    public void setMapping() {
      setUUIDValue(COL_USER_ID, RoundStep::setUserId);
      setUUIDValue(COL_ROUND_ID, RoundStep::setRoundId);
      setStringValue(COL_TYPE, (d, v) -> d.setType(StepType.valueOf(v)));
      setStringValue(COL_POSITION, (d, v) -> d.setPosition(PositionType.valueOf(v)));
      setIntValue(COL_ORDER, RoundStep::setOrder);
    }
  }

}
