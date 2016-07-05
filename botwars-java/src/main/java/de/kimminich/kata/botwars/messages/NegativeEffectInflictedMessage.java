package de.kimminich.kata.botwars.messages;

import de.kimminich.kata.botwars.Bot;
import de.kimminich.kata.botwars.effects.Effect;

public class NegativeEffectInflictedMessage implements Message {

    private StringBuilder text = new StringBuilder();

    public NegativeEffectInflictedMessage(Bot target, Effect effect) {
        text.append("\n");
        text.append(effect.getClass().getSimpleName()).append(" was inflicted on ").append(target).append("!");
    }

    @Override
    public String getMessage() {
        return text.toString();
    }

    @Override
    public String toString() {
        return getMessage();
    }

}
