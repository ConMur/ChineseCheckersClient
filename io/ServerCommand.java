package io;

/**
 * Commands the server can send to this Chinese Checkers client
 * @author Connor Murphy
 *
 */
public enum ServerCommand {
	MOVE, NEW_GAME, PLACE_PIECE, YOUR_TURN, INVALID_MOVE, TIMEOUT, WIN
}
