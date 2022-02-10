package org.godfather.blocksumo.manager.game;

import org.godfather.blocksumo.manager.runnables.Countdown;

public class GameManager {

    private GamePhases phase;
    private Countdown countdown;

    public GameManager() {
        setPhase(GamePhases.LOADING);
        this.countdown = new Countdown(this);
    }

    public Countdown getCountdown() {
        return this.countdown;
    }

    public GamePhases getPhase() {
        return this.phase;
    }

    public void setPhase(GamePhases phase) {
        this.phase = phase;

        switch (getPhase()) {
            case LOADING:
                break;
            case WAITING:
                break;
            case STARTING:
                countdown.start();
                break;
            case INGAME:
                break;
            case END:
                break;
        }
    }
}
