package de.minestar.SinCity.Exceptions;

public class PlayerOfflineException extends RuntimeException {

    private static final long serialVersionUID = -1134077131156224579L;

    public PlayerOfflineException(String playerName) {
        super("Could not update player '" + playerName + "'. He seems to be offline!");
    }
}
