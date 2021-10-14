package ru.noughtscrosses.dto;

import ru.noughtscrosses.db.model.Round;
import ru.noughtscrosses.db.model.RoundStep;

import java.util.List;
import java.util.Objects;

public class RoundDto extends Round {

    private List<RoundStep> steps;

    public RoundDto(Round round) {
        this.id = round.getId();
        this.type = round.getType();
        this.status = round.getStatus();
        this.winnerUserId = round.getWinnerUserId();
    }

    public List<RoundStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RoundStep> steps) {
        this.steps = steps;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        RoundDto roundDto = (RoundDto) o;
        return Objects.equals(steps, roundDto.steps);
    }

    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), steps);
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("RoundDto{");
        sb.append("steps=").append(steps);
        sb.append('}');
        return sb.toString();
    }
}
