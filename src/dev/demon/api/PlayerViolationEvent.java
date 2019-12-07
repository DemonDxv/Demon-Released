package dev.demon.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerViolationEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player p;
    private String checkName, checkType;
    private int violation;

    public PlayerViolationEvent(Player p, String checkName, String checkType, int violation) {
        this.p = p;
        this.checkName = checkName;
        this.checkType = checkType;
        this.violation = violation;
    }

    public Player getPlayer() {
        return p;
    }

    public String getCheckName() {
        return checkName;
    }

    public String getCheckType() {
        return checkType;
    }

    public int getViolation() {
        return violation;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}