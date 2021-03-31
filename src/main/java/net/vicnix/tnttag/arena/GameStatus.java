package net.vicnix.tnttag.arena;

public enum GameStatus {

    WAITING(0),
    IN_GAME(1),
    RESTARTING(2);

    private final Integer status;

    GameStatus(int status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}