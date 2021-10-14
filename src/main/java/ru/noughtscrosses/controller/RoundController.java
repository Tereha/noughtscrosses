package ru.noughtscrosses.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noughtscrosses.constant.RoundType;
import ru.noughtscrosses.constant.Status;
import ru.noughtscrosses.db.model.Round;
import ru.noughtscrosses.db.model.RoundStep;
import ru.noughtscrosses.dto.AuthResponse;
import ru.noughtscrosses.dto.RoundDto;
import ru.noughtscrosses.dto.StepRequest;
import ru.noughtscrosses.dto.TokenRequest;
import ru.noughtscrosses.service.AuthService;
import ru.noughtscrosses.service.RoundService;

import java.util.List;
import java.util.UUID;

@RestController
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService=roundService;
    }

    @GetMapping("/rounds/history")
    public List<Round> getHistory() {
        return roundService.getHistory();
    }

    @GetMapping("/rounds/public")
    public List<Round> getPublic() {
        return roundService.getPublic();
    }

    @GetMapping("/rounds/{id}")
    public RoundDto getById(@PathVariable UUID id) {
        return roundService.getById(id);
    }

    @PostMapping("/rounds")
    public Round create(@RequestBody Round round) {
        return roundService.create(round);
    }

    @PostMapping("/rounds/{id}/join")
    public void join(@PathVariable UUID id) {
        roundService.join(id);
    }

    @PostMapping("/rounds/{id}/step")
    public Round step(@PathVariable UUID id, @RequestBody StepRequest stepRequest) {
        return roundService.step(id, stepRequest.getLocationType());
    }

}
