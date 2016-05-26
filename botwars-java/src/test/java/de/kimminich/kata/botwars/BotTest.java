package de.kimminich.kata.botwars;

import org.junit.gen5.api.Test;

import static de.kimminich.kata.botwars.BotBuilder.aBot;
import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.assertFalse;
import static org.junit.gen5.api.Assertions.assertTrue;

public class BotTest {

    private Bot bot;

    @Test
    void damageTakenIsReducedByArmor() {
        bot = aBot().withIntegrity(100).withArmor(10).build();

        bot.takeDamage(20);
        assertEquals(90, bot.getIntegrity());

        bot.takeDamage(60);
        assertEquals(40, bot.getIntegrity());

        bot.takeDamage(5);
        assertEquals(40, bot.getIntegrity(), "Armor will not reduce damage below zero");

    }

    @Test
    void randomDamageIsBetweenHalfAndFullPowerOfAttacker() {
        bot = aBot().withPower(100).build();

        for (int i = 0; i < 1000; i++) {
            Bot opponent = aBot().withIntegrity(200).withArmor(0).build();
            bot.causeDamage(opponent);
            assertTrue(opponent.getIntegrity() <= 150);
            assertTrue(opponent.getIntegrity() >= 100);
        }
    }

    @Test
    void botWithZeroIntegrityIsDestroyed() {
        bot = aBot().withIntegrity(100).withArmor(0).build();

        bot.takeDamage(90);
        assertFalse(bot.isDestroyed());

        bot.takeDamage(10);
        assertTrue(bot.isDestroyed());
    }

    @Test
    void integrityCannotDropBelowZero() {
        bot = aBot().withIntegrity(100).build();

        bot.takeDamage(9999);
        assertEquals(0, bot.getIntegrity());
    }

}
