package de.kimminich.kata.botwars.effects.negative;

import de.kimminich.kata.botwars.Bot;
import de.kimminich.kata.botwars.effects.Effect;
import org.junit.gen5.api.DisplayName;
import org.junit.gen5.api.Test;

import static de.kimminich.kata.botwars.builders.BotBuilder.aBot;
import static de.kimminich.kata.botwars.builders.BotBuilder.anyBot;
import static de.kimminich.kata.botwars.effects.EffectFactory.createEffectFactoryFor;
import static org.junit.gen5.api.Assertions.assertEquals;

@DisplayName("The Speed Down negative status effect")
public class SpeedDownTest {

    @Test
    @DisplayName("reduces speed by 25% during its duration")
    void reducesSpeedBy25Percent() {
        Effect effect = createEffectFactoryFor(anyBot(),
                1, SpeedDown.class).newInstance();
        Bot bot = aBot().withSpeed(100).withStatusEffects(effect).build();

        bot.applyEffects();
        assertEquals(75, bot.getSpeed());
        bot.expireEffects();
        assertEquals(100, bot.getSpeed(), "Speed should have been restored after effect expired");
        assertEquals(0, bot.getEffects().size());

    }

}
