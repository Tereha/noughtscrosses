package ru.noughtscrosses.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.noughtscrosses.constant.PositionType;
import ru.noughtscrosses.constant.RoundType;
import ru.noughtscrosses.constant.Status;
import ru.noughtscrosses.constant.StepType;
import ru.noughtscrosses.db.model.Round;
import ru.noughtscrosses.db.model.RoundStep;
import ru.noughtscrosses.db.model.RoundUser;
import ru.noughtscrosses.db.repository.RoundRepository;
import ru.noughtscrosses.db.repository.RoundStepRepository;
import ru.noughtscrosses.db.repository.RoundUserRepository;
import ru.noughtscrosses.db.repository.UserRepository;
import ru.noughtscrosses.dto.RoundDto;
import ru.noughtscrosses.exception.ApiException;
import ru.noughtscrosses.exception.code.DataNotFoundResponse;
import ru.noughtscrosses.exception.code.DataValidationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoundService {
    private static final Logger log = LoggerFactory.getLogger(RoundService.class);

    private final UUID botId = UUID.randomUUID();

    private final AuthService authService;
    private final RoundRepository roundRepository;
    private final RoundUserRepository roundUserRepository;
    private final RoundStepRepository roundStepRepository;
    private final RoundMutexFactory roundMutexFactory;

    public RoundService(AuthService authService,
        RoundRepository roundRepository, RoundUserRepository roundUserRepository,
        RoundStepRepository roundStepRepository, RoundMutexFactory roundMutexFactory) {
        this.authService = authService;
        this.roundRepository = roundRepository;
        this.roundUserRepository = roundUserRepository;
        this.roundStepRepository = roundStepRepository;
        this.roundMutexFactory=roundMutexFactory;
    }

    public RoundDto getById(UUID id) {
        RoundDto round = new RoundDto(roundRepository.findById(id).orElseThrow(()
            -> new ApiException(DataNotFoundResponse.ROUND_NOT_FOUND)));
        round.setSteps(roundStepRepository.findByRoundId(id));
        return round;
    }

    public List<Round> getHistory() {
        return roundRepository.findByUserIdAndStatus(authService.getCurrentUserId(), Status.INACTIVE);
    }

    public List<Round> getPublic() {
        return roundRepository.findByTypeAndStatus(RoundType.PVP, Status.INACTIVE);
    }

    public Round create(Round round) {
        roundRepository.add(round);
        roundUserRepository.add(new RoundUser(authService.getCurrentUserId(), round.getId()));
        return round;
    }

    public void join(UUID id) {
        roundUserRepository.add(new RoundUser(authService.getCurrentUserId(), id));
    }

    public Round step(UUID roundId, PositionType position) {
        synchronized (roundMutexFactory.getMutex(roundId)) {
            Round round = roundRepository.findById(roundId).orElseThrow(()
                -> new ApiException(DataNotFoundResponse.ROUND_NOT_FOUND));

            if (Status.INACTIVE == round.getStatus()) {
                throw new ApiException(DataValidationResponse.ROUND_IS_INACTIVE);
            }

            UUID userId = authService.getCurrentUserId();
            List<RoundStep> steps = roundStepRepository.findByRoundId(roundId);

            createStepAndCheckVictory(round, userId, position, steps);

            if (RoundType.PVP == round.getType() || Status.INACTIVE == round.getStatus()) {
                return round;
            } else {
                return createStepAndCheckVictory(round, botId, randomPosition(steps), steps);
            }
        }
    }

    private Round createStepAndCheckVictory(Round round, UUID userId, PositionType position, List<RoundStep> steps) {
        RoundStep step = new RoundStep();
        step.setUserId(userId);
        step.setRoundId(round.getId());
        step.setType(detectCurrentStepType(steps));
        step.setPosition(position);
        step.setOrder(steps.size());
        roundStepRepository.add(step);
        steps.add(step);

        if (isVictory(steps.stream().filter(s -> s.getUserId().equals(userId)).collect(
            Collectors.toList()))) {
            roundRepository.updateStatusAndWinnerUserId(round.getId(), userId);
            round.setStatus(Status.INACTIVE);
            round.setWinnerUserId(userId);
        }
        return round;
    }

    private StepType detectCurrentStepType(List<RoundStep> steps) {
        if (CollectionUtils.isEmpty(steps)) {
            return StepType.NOUGHTS;
        }
        return steps.get(steps.size() - 1).getType() == StepType.NOUGHTS ? StepType.CROSSES : StepType.NOUGHTS;
    }

    private static boolean isVictory(List<RoundStep> steps) {
        return PositionType.victoryScheme.stream().anyMatch(v -> steps.stream()
            .filter(s -> v.contains(s.getPosition())).count() == v.size());
    }

    private PositionType randomPosition(List<RoundStep> steps) {
        List<PositionType> busy = steps.stream().map(RoundStep::getPosition).collect(Collectors.toList());
        return Stream.of(PositionType.values()).filter(p -> !busy.contains(p)).findFirst().orElse(null);
    }

}
