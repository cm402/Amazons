import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a player for the Amazons game, acting as a
 * superclass that both the AI and human players inherit from.
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 4L;

    private boolean white;
    private boolean human;
    private ArrayList<Piece> pieces;


    public Player(boolean white, boolean human){
        this.white = white;
        this.human = human;
    }

    public void addPieces(ArrayList<Piece> pieces){
        this.pieces = pieces;
    }

    public ArrayList<Piece> getPieces(){
        return this.pieces;
    }

    /**
     * Returns a move, overridden by any player class
     * that inherits from this class.
     * @param board
     * @return
     */
    public Move getMove(Board board){

        Move move = null;
        return move;
    }

    /**
     * Getting a list of all the possible moves "this" player can
     * make, on the specified Board object.
     * @param board The board we are checking moves in
     * @return List of "valid" moves
     */
    public ArrayList<Move> getValidMoves(Board board){

        ArrayList<Move> validMoves = new ArrayList<Move>();

        // looping through amazon pieces
        for(Piece piece: pieces){

            ArrayList<Square> validSquares = board.getValidSquares(piece.getPosition(), -1, -1);

            // looping through the squares they can move to
            for(Square endSquare: validSquares){

                ArrayList<Square> validShotSquares = board.getValidSquares(endSquare, piece.getPosition().getX(), piece.getPosition().getY());

                // looping through the squares they can then shoot at
                for(Square validShotSquare: validShotSquares){

                    validMoves.add(new Move(this, piece.getPosition(), endSquare, validShotSquare));
                }
            }
        }
        return validMoves;
    }

    public boolean isWhite(){
        return this.white;
    }

    public boolean isHuman(){
        return this.human;
    }



}