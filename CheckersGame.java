public class CheckersGame {

    private static int player1Pieces;  // 32-bit bitboard for Player 1 pieces
    private static int player2Pieces;  // 32-bit bitboard for Player 2 pieces
    private static int kingPieces;      // 32-bit bitboard for kinged pieces

    // Method to initialize the bitboard with Player 1 and Player 2 pieces
    public static void initializeBoard() {
        player1Pieces = 0x00000FFF;  // Player 1 starts with pieces in the bottom rows
        player2Pieces = 0xFFF00000;  // Player 2 starts with pieces in the top rows
        kingPieces = 0;               // No kings at the start
    }

    // Method to print the current state of the checkers board
    public static void printBoard() {
        for (int row = 7; row >= 0; row--) {  // Iterate through rows 8 to 1
            for (int col = 0; col < 8; col++) {
                int pos = row * 4 + col / 2;  // Calculate bit position
                if ((col + row) % 2 != 0) {  // Only print playable squares
                    if (BitManipulationUtil.getBit(player1Pieces, pos)) {
                        System.out.print(" 1 ");  // Player 1 piece
                    } else if (BitManipulationUtil.getBit(player2Pieces, pos)) {
                        System.out.print(" 2 ");  // Player 2 piece
                    } else if (BitManipulationUtil.getBit(kingPieces, pos)) {
                        System.out.print(" K ");  // King piece
                    } else {
                        System.out.print(" . ");  // Empty playable square
                    }
                } else {
                    System.out.print("   ");  // Non-playable square
                }
            }
            System.out.println();  // Move to the next row
        }

        // Print bitboard states in binary and hexadecimal formats
        System.out.println("\nPlayer 1 Pieces (Binary): " + BitManipulationUtil.toBinaryString(player1Pieces));
        System.out.println("Player 1 Pieces (Hex): " + BitManipulationUtil.toHexString(player1Pieces));
        System.out.println("Player 2 Pieces (Binary): " + BitManipulationUtil.toBinaryString(player2Pieces));
        System.out.println("Player 2 Pieces (Hex): " + BitManipulationUtil.toHexString(player2Pieces));
        System.out.println("Kings (Binary): " + BitManipulationUtil.toBinaryString(kingPieces));
        System.out.println("Kings (Hex): " + BitManipulationUtil.toHexString(kingPieces));
    }

    // Method to capture an opponent's piece
    public static void capturePiece(int player, int position) {
        if (player == 1) {
            player2Pieces = BitManipulationUtil.clearBit(player2Pieces, position); // Clear opponent's piece
            System.out.println("Player 1 captured a piece at position " + position);
        } else {
            player1Pieces = BitManipulationUtil.clearBit(player1Pieces, position); // Clear opponent's piece
            System.out.println("Player 2 captured a piece at position " + position);
        }
    }

    // Method to move a piece, handling normal moves and jumps
    public static void movePiece(int start, int end) {
        System.out.println("Attempting to move from " + start + " to " + end);
        boolean isJumpMove = Math.abs(start - end) == 14 || Math.abs(start - end) == 18; // Jump distances for diagonal moves
        boolean isValidMove = false;

        // Check if Player 1's piece is at the start position
        if (BitManipulationUtil.getBit(player1Pieces, start)) {
            System.out.println("Player 1 piece found at start position: " + start);
            // Handle normal move
            if (!isJumpMove && isValidMove(player1Pieces, player2Pieces, start, end)) {
                isValidMove = true; // Valid normal move
            }
            // Handle jump move (over Player 2's piece)
            else if (isJumpMove && isValidJump(player1Pieces, player2Pieces, start, end)) {
                isValidMove = true; // Valid jump move
                // Remove opponent's piece being jumped over
                int midPosition = (start + end) / 2; // Calculate mid position for capture
                capturePiece(2, midPosition); // Capture Player 2's piece
            }
        }
        // Check if Player 2's piece is at the start position
        else if (BitManipulationUtil.getBit(player2Pieces, start)) {
            System.out.println("Player 2 piece found at start position: " + start);
            // Handle normal move
            if (!isJumpMove && isValidMove(player2Pieces, player1Pieces, start, end)) {
                isValidMove = true; // Valid normal move
            }
            // Handle jump move (over Player 1's piece)
            else if (isJumpMove && isValidJump(player2Pieces, player1Pieces, start, end)) {
                isValidMove = true; // Valid jump move
                // Remove opponent's piece being jumped over
                int midPosition = (start + end) / 2; // Calculate mid position for capture
                capturePiece(1, midPosition); // Capture Player 1's piece
            }
        } else {
            System.out.println("No piece at start position: " + start);
        }

        // Execute move if valid
        if (isValidMove) {
            if (BitManipulationUtil.getBit(player1Pieces, start)) {
                player1Pieces = BitManipulationUtil.clearBit(player1Pieces, start); // Clear starting position
                player1Pieces = BitManipulationUtil.setBit(player1Pieces, end); // Set destination position
            } else if (BitManipulationUtil.getBit(player2Pieces, start)) {
                player2Pieces = BitManipulationUtil.clearBit(player2Pieces, start); // Clear starting position
                player2Pieces = BitManipulationUtil.setBit(player2Pieces, end); // Set destination position
            }
            System.out.println("Move executed successfully from " + start + " to " + end);
        } else {
            System.out.println("Illegal move: Either the destination is occupied or you're trying to jump incorrectly.");
        }
    }

    // Method to check if the move is valid
    public static boolean isValidMove(int currentPlayerPieces, int opponentPlayerPieces, int start, int end) {
        return !BitManipulationUtil.getBit(currentPlayerPieces, end) && !BitManipulationUtil.getBit(opponentPlayerPieces, end);
    }

    // Method to validate if a jump is valid
    public static boolean isValidJump(int currentPlayerPieces, int opponentPlayerPieces, int start, int end) {
        int midPosition = (start + end) / 2; // Calculate the mid position

        // Ensure the piece is jumping over an opponent's piece and landing on an empty spot
        if (BitManipulationUtil.getBit(opponentPlayerPieces, midPosition) &&  // Opponent piece in the middle
                !BitManipulationUtil.getBit(currentPlayerPieces, end) &&        // Destination is empty
                !BitManipulationUtil.getBit(opponentPlayerPieces, end)) {       // No opponent piece at destination
            System.out.println("Valid jump: Mid-position " + midPosition + " contains opponent's piece.");
            return true;
        } else {
            System.out.println("Invalid jump: No opponent's piece at mid-position or destination is occupied.");
            return false;
        }
    }

    public static void main(String[] args) {
        // Initialize the board and test various scenarios
        System.out.println("Test: Initial Board Setup");
        CheckersGame.initializeBoard();
        CheckersGame.printBoard();

        // Test Cases
        System.out.println("\nTest Case 1.1: Player 1 moves from position 10 to 14");
        CheckersGame.movePiece(10, 14); // Valid move
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.2: Player 2 moves from position 21 to 17");
        CheckersGame.movePiece(21, 17); // Valid move
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.3: Player 1 jumps over Player 2 from position 14 to 19");
        CheckersGame.movePiece(14, 19); // Valid jump
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.4: Player 1 attempts an illegal move from position 12 to 16");
        CheckersGame.movePiece(12, 16); // Invalid move (not diagonal)
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.5: Player 2 jumps Player 1 from position 21 to 14");
        CheckersGame.movePiece(21, 14); // Valid jump
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.6: Player 2 attempts to jump Player 1's piece at position 17 to 12 (illegal)");
        CheckersGame.movePiece(17, 12); // Invalid jump (no opponent piece to jump over)
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.7: Player 1 jumps over Player 2 from position 19 to 25");
        CheckersGame.movePiece(19, 25); // Valid jump
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.8: Player 1 attempts to jump over Player 2 from position 10 to 19 (illegal)");
        CheckersGame.movePiece(10, 19); // Invalid jump (cannot jump two spaces)
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.9: Player 2 captures Player 1's piece at position 16");
        CheckersGame.capturePiece(1, 16); // Capture piece
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.10: Player 1 moves from position 14 to 10");
        CheckersGame.movePiece(14, 10); // Valid move
        CheckersGame.printBoard();

        System.out.println("\nTest Case 1.11: Player 2 tries to move from position 25 to 27 (illegal - blocked)");
        CheckersGame.movePiece(25, 27); // Invalid move (destination occupied)
        CheckersGame.printBoard();
    }
}
