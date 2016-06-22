package de.kimminich.kata.botwars.ui;

import de.kimminich.kata.botwars.Bot;
import de.kimminich.kata.botwars.Player;
import de.kimminich.kata.botwars.reports.AttackReport;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserInterface {

    Optional<Bot> selectTarget(Bot attacker, List<Bot> opponentTeam);

    List<Bot> selectTeam(Set<Bot> roster);

    String enterName();

    void gameOver(Player winner);

    void attackReport(AttackReport report);

    void botDestruction(Bot target);
}
