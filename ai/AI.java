package ai;

import java.awt.Point;
import java.util.LinkedList;

import board.Board;
import board.BoardSpot;
import board.Move;

public class AI {

	static boolean[][] visited;
	static int startLocation;
	static int winLocation;
	static int[][] weightedBoard;
	static Point[] pieces;
	static int turn;

	public static Move getMove() {
		turn++;

		if (turn == 1) {
			if (startLocation == 1) {
				return new Move(13, 9, 12, 9);
			} else if (startLocation == 2) {
				return new Move(12, 13, 11, 12);
			} else if (startLocation == 3) {
				return new Move(4, 9, 5, 9);
			} else if (startLocation == 4) {
				return new Move(3, 4, 4, 5);
			} else if (startLocation == 5) {
				return new Move(4, 3, 5, 4);
			} else {
				return new Move(9, 4, 9, 5);
			}
		} else if (turn == 2) {
			if (startLocation == 1) {
				return new Move(13, 12, 12, 11);
			} else if (startLocation == 2) {
				return new Move(9, 13, 9, 12);
			} else if (startLocation == 3) {
				return new Move(7, 12, 7, 11);
			} else if (startLocation == 4) {
				return new Move(3, 7, 4, 7);
			} else if (startLocation == 5) {
				return new Move(7, 3, 7, 4);
			} else {
				return new Move(12, 7, 11, 7);
			}
		} else {

			//cleanup later
			visited = new boolean[17][17];

			Point bestMovePiece = move(pieces[0].x, pieces[0].y, false);

			int bestPiece = 0;

			for (int i = 0; i < 10; i++) {
				visited = new boolean[17][17];
				Point tempMove = move(pieces[i].x, pieces[i].y, false);

				if (tempMove != null && weightedBoard[pieces[bestPiece].y][pieces[bestPiece].x]
						- weightedBoard[bestMovePiece.y][bestMovePiece.x] < weightedBoard[pieces[i].y][pieces[i].x]
								- weightedBoard[tempMove.y][tempMove.x]) {
					bestPiece = i;
					bestMovePiece = tempMove;
				}
			}
			return new Move(pieces[bestPiece].y, pieces[bestPiece].x, bestMovePiece.y, bestMovePiece.x);
		}

	}

	public static void start(int startingLocation) {
		turn = 0;
		startLocation = startingLocation;
		pieces = new Point[10];
		if (startLocation > 3)
			winLocation = startLocation - 3;
		else
			winLocation = startLocation + 3;
		weightedBoard = new int[17][17];
		if (startingLocation == 1) {
			setWeight(4, 0);
			for (int i = 0; i < 4; ++i) {
				pieces[i] = new Point(9 + i, 13);
			}
			for (int i = 0; i < 3; ++i) {
				pieces[i + 4] = new Point(10 + i, 14);
			}
			for (int i = 0; i < 2; ++i) {
				pieces[i + 7] = new Point(11 + i, 15);
			}
			pieces[9] = new Point(12, 16);
		} else if (startingLocation == 2) {
			setWeight(0, 4);
			for (int i = 0; i < 4; ++i) {
				pieces[i] = new Point(13 + i, 12);
			}
			for (int i = 0; i < 3; ++i) {
				pieces[i + 4] = new Point(13 + i, 11);
			}
			for (int i = 0; i < 2; ++i) {
				pieces[i + 7] = new Point(13 + i, 10);
			}
			pieces[9] = new Point(13, 9);
		} else if (startingLocation == 3) {
			setWeight(4, 12);
			for (int i = 0; i < 4; ++i) {
				pieces[i] = new Point(9 + i, 4);
			}
			for (int i = 0; i < 3; ++i) {
				pieces[i + 4] = new Point(10 + i, 5);
			}
			for (int i = 0; i < 2; ++i) {
				pieces[i + 7] = new Point(11 + i, 6);
			}
			pieces[9] = new Point(12, 7);
		} else if (startingLocation == 4) {
			setWeight(12, 16);
			for (int i = 0; i < 4; ++i) {
				pieces[i] = new Point(4 + i, 3);
			}
			for (int i = 0; i < 3; ++i) {
				pieces[i + 4] = new Point(4 + i, 2);
			}
			for (int i = 0; i < 2; ++i) {
				pieces[i + 7] = new Point(4 + i, 1);
			}
			pieces[9] = new Point(0, 4);
		} else if (startingLocation == 5) {
			setWeight(16, 12);
			for (int i = 0; i < 4; ++i) {
				pieces[i] = new Point(0 + i, 4);
			}
			for (int i = 0; i < 3; ++i) {
				pieces[i + 4] = new Point(1 + i, 5);
			}
			for (int i = 0; i < 2; ++i) {
				pieces[i + 7] = new Point(2 + i, 6);
			}
			pieces[9] = new Point(3, 7);
		} else if (startingLocation == 6) {
			setWeight(12, 4);
			for (int i = 0; i < 4; ++i) {
				pieces[i] = new Point(4 + i, 12);
			}
			for (int i = 0; i < 3; ++i) {
				pieces[i + 4] = new Point(4 + i, 11);
			}
			for (int i = 0; i < 2; ++i) {
				pieces[i + 7] = new Point(4 + i, 10);
			}
			pieces[9] = new Point(4, 9);
		}
	}

	public static void movePiece(Move move) {
		int i = 0;
		while (pieces[i].x != move.getOldCol() && pieces[i].y != move.getOldRow()) {
			i++;
		}
		//BoardSpot temp = Board.getSpot(pieces[i].y, pieces[i].x);
		//Board.set(move.getNewRow(), move.getNewCol(), temp.getColor());
		//Board.set(move.getOldRow(), move.getOldCol(), null);
		pieces[i].x = move.getNewCol();
		pieces[i].y = move.getNewRow();
	}

	/**
	 * bfs sets weight of board based on how many moves is required to reach win
	 * state
	 */

/*	public static void main(String[] args) {
		Board.init();
		AI.start(1);
		for (int i = 0; i < 17; i++) {
			for (int j = 0; j < 17; j++) {
				System.out.printf("%3d", weightedBoard[i][j]);
			}
			System.out.println();
		}

		Move m = getMove();
		movePiece(m);
		System.out.println("Move 1: " + m.toString());
		m = getMove();
		movePiece(m);
		System.out.println("Move 2: " + m.toString());
		m = getMove();
		movePiece(m);
		System.out.println("Move 3: " + m.toString());

	}*/

	private static void setWeight(int x, int y) {
		visited = new boolean[17][17];
		LinkedList<Point> l = new LinkedList<Point>();
		l.add(new Point(x, y));
		visited[y][x] = true;
		while (!l.isEmpty()) {
			Point current = l.removeFirst();
			if (Board.isValidSpot(current.y, current.x)) {
				for (int i = -1; i < 2; i++)
					for (int j = -1; j < 2; j++) {
						// checking 6 surrounding spots
						if (!((i == 0 && j == 0) || (i * j < 0))) {
							if (Board.isValidSpot(current.y + i, current.x + j)
									&& !visited[current.y + i][current.x + j]) {
								weightedBoard[current.y + i][current.x + j] = weightedBoard[current.y][current.x] + 1;
								visited[current.y + i][current.x + j] = true;
								l.addLast(new Point(current.x + j, current.y + i));
							}
						}
					}
			}
		}
	}

	private static Point move(int x, int y, boolean jumped) {
		visited[y][x] = true;
		if (!Board.isValidSpot(y, x)) {
			return null;
		}
		Point bestMove = new Point(x, y);
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!((i == 0 && j == 0) || (i * j < 0))) {
					Point tempMove = bestMove;
					int moveX = j;
					int moveY = i;
					// jump
					if (Board.isValidSpot(y + moveY, x + moveX) && !visited[y + moveY][x + moveX]) {
						if (Board.isValidSpot(y + moveY * 2, x + moveX * 2) && Board.isTaken(y + moveY, x + moveX)
								&& !Board.isTaken(y + moveY * 2, x + moveX * 2)) {
							visited[y + moveY][x + moveX] = true;
							moveX *= 2;
							moveY *= 2;
							tempMove = move(x + moveX, y + moveY, true);
						} else if (!jumped && !Board.isTaken(y + moveY, x + moveX)) {
							tempMove = new Point(x + moveX, y + moveY);
						}
					}
					if (tempMove != null
							&& weightedBoard[bestMove.y][bestMove.x] > weightedBoard[tempMove.y][tempMove.x]) {
						bestMove = tempMove;

					}
				}
			}
		}
		return bestMove;
	}

}
