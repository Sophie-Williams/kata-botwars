package de.kimminich.kata.botwars.effects;

import de.kimminich.kata.botwars.messages.EmptyMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.kimminich.kata.botwars.builders.BotBuilder.anyBot;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("The 'No Effect' effect")
public class NoEffectTest {

    @Test
    @DisplayName("returns an empty message when applied")
    void returnEmptyMessageWhenApplied() {
        Effect noEffect = new NoEffect(anyBot(), 1);
        assertTrue(noEffect.apply(anyBot()) instanceof EmptyMessage);
    }

    @Test
    @DisplayName("returns an empty message when expiring")
    void returnEmptyMessageWhenExpiring() {
        Effect noEffect = new NoEffect(anyBot(), 1);
        assertTrue(noEffect.revoke(anyBot()) instanceof EmptyMessage);
    }

}
