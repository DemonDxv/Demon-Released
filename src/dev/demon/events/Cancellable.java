package dev.demon.events;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean var1);
}

