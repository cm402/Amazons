import java.io.Serializable;
import java.util.ArrayList;

// didn't use interface as it can't extend serializable
public class Player implements Serializable {

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

    // this will be overridden by AI or human player
    public Move getMove(Board board, String gameStatus){

        Move move = null;
        return move;
    }

    // this will be overridden by AI or human player
    public Move getPartitionMove(BoardPartition board, String gameStatus){

        Move move = null;
        return move;
    }

    public ArrayList<Move> getValidMoves(Board board){

        ArrayList<Move> validMoves = new ArrayList<Move>();

        // looping through amazon pieces
        for(Piece piece: pieces){

            ArrayList<Square> validSquares = board.getValidSquares(piece.getPosition());

            // looping through the squares they can move to
            for(Square endSquare: validSquares){

                ArrayList<Square> validShotSquares = board.getValidSquares(endSquare);

                // can shoot back to the square where the amazon came from, but saved in
                // board as having the piece so must add manually
                validShotSquares.add(piece.getPosition());

                // looping through the squares they can then shoot at
                for(Square validShotSquare: validShotSquares){

                    validMoves.add(new Move(this, piece.getPosition(), endSquare, validShotSquare));
                }
            }

        }
        return validMoves;
    }

    public ArrayList<Move> getPartitionValidMoves(BoardPartition board){

        ArrayList<Move> validMoves = new ArrayList<Move>();

        // looping through amazon pieces
        for(Piece piece: pieces){

            ArrayList<Square> validSquares = board.getValidSquares(piece.getPosition());

            // looping through the squares they can move to
            for(Square endSquare: validSquares){

                ArrayList<Square> validShotSquares = board.getValidSquares(endSquare);

                // can shoot back to the square where the amazon came from, but saved in
                // board as having the piece so must add manually
                validShotSquares.add(piece.getPosition());

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