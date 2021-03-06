package de.kimminich.kata.botwars;

import de.kimminich.extensions.MockitoExtension;
import de.kimminich.kata.botwars.ui.UserInterface;
import de.kimminich.kata.botwars.ui.answers.FirstBotFromOpponentTeam;
import de.kimminich.kata.botwars.ui.answers.TeamOfUpToThreeBotsFromRoster;
import de.kimminich.kata.botwars.ui.answers.UniquePlayerName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static de.kimminich.kata.botwars.builders.BotBuilder.aBot;
import static de.kimminich.kata.botwars.builders.BotBuilder.anyBot;
import static de.kimminich.kata.botwars.builders.PlayerBuilder.aPlayer;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.expectThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.ArgumentMatchers.anySetOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("A game")
public class GameTest {

    private Game game;

    @BeforeEach
    void initUserInterface(@Mock UserInterface ui) {
        when(ui.enterName()).thenAnswer(new UniquePlayerName());
        when(ui.selectTeam(anySetOf(Bot.class))).thenAnswer(new TeamOfUpToThreeBotsFromRoster());
        when(ui.selectTarget(any(Bot.class), anyListOf(Bot.class))).thenAnswer(new FirstBotFromOpponentTeam());
    }

    @Nested
    @DisplayName("ends with")
    class GameOver {

        @Test()
        @DisplayName("a winner")
        void gameEndsWithAWinner(@Mock UserInterface ui) {
            game = new Game(ui);
            game.loop();
            assertTrue(game.getWinner().isPresent());
        }

        @Test()
        @DisplayName("the considerably stronger player winning")
        void strongerPlayerWinsGame(@Mock UserInterface ui) {
            Player strongPlayer = aPlayer().withTeam(
                    aBot().withPower(1000).build(), aBot().withPower(1000).build(), aBot().withPower(1000).build())
                    .build();
            Player weakPlayer = aPlayer().withTeam(
                    aBot().withIntegrity(1).build(), aBot().withIntegrity(1).build(), aBot().withIntegrity(1).build())
                    .build();

            game = new Game(ui, strongPlayer, weakPlayer);
            game.loop();
            assertEquals(strongPlayer, game.getWinner().orElseThrow(IllegalStateException::new));
        }

        @Test()
        @DisplayName("the considerably faster player winning")
        void fasterPlayerWinsGame(@Mock UserInterface ui) {
            Player fastPlayer = aPlayer().withTeam(
                    aBot().withSpeed(200).build(), aBot().withSpeed(300).build(), aBot().withSpeed(400).build())
                    .build();
            Player slowPlayer = aPlayer().withTeam(
                    aBot().withSpeed(20).build(), aBot().withSpeed(30).build(), aBot().withSpeed(40).build())
                    .build();

            game = new Game(ui, slowPlayer, fastPlayer);
            game.loop();
            assertEquals(fastPlayer, game.getWinner().orElseThrow(IllegalStateException::new));
        }

    }

    @Nested
    @DisplayName("raises an error")
    class ErrorCases {

        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        @Test
        @DisplayName("when a player has a team of less than 3 bots")
        void cannotCreateGameWithIncompleteTeamSetup(@Mock UserInterface ui) {
            Player playerWithCompleteTeam = aPlayer().withTeam(anyBot(), anyBot(), anyBot()).build();
            Player playerWithIncompleteTeam = aPlayer().withTeam(anyBot(), anyBot()).build();

            Throwable exception = expectThrows(IllegalArgumentException.class,
                    () -> new Game(ui, playerWithCompleteTeam, playerWithIncompleteTeam));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains(playerWithIncompleteTeam.toString())),
                    () -> assertFalse(exception.getMessage().contains(playerWithCompleteTeam.toString()))
            );

        }

        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        @Test
        @DisplayName("when a player has the same bot twice in his team")
        void cannotCreateGameWithDuplicateBotInTeam(@Mock UserInterface ui) {
            Bot duplicateBot = anyBot();
            Player playerWithDuplicateBotInTeam = aPlayer().withTeam(duplicateBot, duplicateBot, anyBot()).build();
            Player playerWithValidTeam = aPlayer().withTeam(anyBot(), anyBot(), anyBot()).build();

            Throwable exception = expectThrows(IllegalArgumentException.class,
                    () -> new Game(ui, playerWithValidTeam, playerWithDuplicateBotInTeam));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains(playerWithDuplicateBotInTeam.toString())),
                    () -> assertTrue(exception.getMessage().contains(duplicateBot.toString())),
                    () -> assertFalse(exception.getMessage().contains(playerWithValidTeam.toString()))
            );
        }

        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        @Test
        @DisplayName("when both players chose the same name")
        void playersCannotHaveSameName(@Mock UserInterface ui) {
            Player horst = aPlayer().withName("Horst").build();
            Player theOtherHorst = aPlayer().withName("Horst").build();

            Throwable exception = expectThrows(IllegalArgumentException.class,
                    () -> new Game(ui, horst, theOtherHorst));

            assertTrue(exception.getMessage().contains("Horst"));
        }

    }

}
