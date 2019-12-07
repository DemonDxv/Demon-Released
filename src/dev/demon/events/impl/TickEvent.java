package dev.demon.events.impl;

import dev.demon.events.BitDefenderEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TickEvent extends BitDefenderEvent {
    private int currentTick;
}
