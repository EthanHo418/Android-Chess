package com.example.android60;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android60.chess.model.ChessApp;
import com.example.android60.chess.model.ChessPiece;
import com.example.android60.chess.model.InvalidMoveException;
import com.example.android60.chess.model.Move;

public class ChessGameActivity extends AppCompatActivity {

    private ImageView[][] chessBoardView;
    private Location currentSelectedLocation;
    private ChessApp chessApp;

    private TextView gameStatusTextView;

    private static final String GAME_IN_PROGRESS = "game in progress";
    private static final String GAME_OVER = "game is over";
    private String gameStatus;
    private boolean hasUndoOneTurn;
    private EditText input;

    public class Location {
        public Location(int _row, int _col) {
            row = _row;
            col = _col;
        }
        public int row;
        public int col;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_game);
        TableLayout tableLayout = findViewById(R.id.chess_board);
        tableLayout.removeAllViews();

        //TODO: what is left in this class
        //3.display a popup for winning the game, with a field to save the game

        gameStatusTextView = findViewById(R.id.game_status_text_view);

        chessApp = ChessAppContainer.chessApp;
        chessApp.startNewGame();
        gameStatusTextView.setText("Current turn: " + chessApp.currentTurn);
        gameStatus = GAME_IN_PROGRESS;
        hasUndoOneTurn = false;

        Button buttonResign = findViewById(R.id.resign_button);
        buttonResign.setOnClickListener( (view) -> {handleResign();});

        Button buttonUndo = findViewById(R.id.undo_button);
        buttonUndo.setOnClickListener( (view) -> {handleUndo();});

        Button buttonDraw = findViewById(R.id.draw_button);
        buttonDraw.setOnClickListener( (view) -> {handleDraw();});

        Button aiMove = findViewById(R.id.ai_move_button);
        aiMove.setOnClickListener( (view) -> {handleAiMove();});

        chessBoardView = ChessDisplayHelper.chessBoardViewSetup(tableLayout, this);
        for (int r = 0; r < 8; r++) {
            for(int c = 0; c <8; c++) {
                chessBoardView[r][c].setOnClickListener(view -> { processSelected((ImageView) view); });
            }
        }
        updateChessBoardDisplay();
    }

    private void handleAiMove() {
        if(gameStatus.equals(GAME_OVER)) {
            return;
        }
        chessApp.makeAIMove();
        gameStatusTextView.setText("Current turn: " + chessApp.currentTurn);
        hasUndoOneTurn = false;
        updateChessBoardDisplay();
    }

    private void handleDraw() {
        if(gameStatus.equals(GAME_OVER)) {
            return;
        }
        chessApp.endGame("draw");
        gameStatus = GAME_OVER;
        gameStatusTextView.setText("draw");
        showAlertDialogResign();

        //TODO: display a dialog for save game
    }

    private void handleResign() {
        if(gameStatus.equals(GAME_OVER)) {
            return;
        }
        gameStatusTextView.setText(chessApp.handleResign());
        gameStatus = GAME_OVER;
        showAlertDialogResign();
    }

    private void processSelected(ImageView imageView) {
        if(!gameStatus.equals((GAME_IN_PROGRESS))) {
            //game is over, just exit
            return;
        }
        Location imageViewLocation = getImageViewLocation(imageView);

        ChessPiece chessPiece = chessApp.getPiece(imageViewLocation.row, imageViewLocation.col);
        if(chessPiece == null || !(chessPiece.getColor().equals(chessApp.currentTurn)) ) {
            //if the square is empty or contains an enemy piece, we will attempt to move there
            if(currentSelectedLocation == null) {
                //no piece is selected to move there, we must return
                return;
            } else {
                if(chessApp.getPiece(currentSelectedLocation.row, currentSelectedLocation.col) != null &&
                        !chessApp.currentTurn.equals(
                        chessApp.getPiece(currentSelectedLocation.row, currentSelectedLocation.col)
                                .getColor())) {
                    //we somehow have a piece selected of a different color, lets fix that
                    currentSelectedLocation = null;
                    return;
                }
            }

            try {
                chessApp.makeMove(new Move(currentSelectedLocation.row, currentSelectedLocation.col,
                        imageViewLocation.row, imageViewLocation.col));
            } catch (InvalidMoveException e){
                //I guess that was not a valid move!
                //we just exit
                return;
            }
            //the move was valid!
            //clear selection for the next player
            //check conditions for checkmate here
            if(chessApp.isInCheckMate(chessApp.currentTurn)) {
                //can't make a move if you are in checkmate!
                //update chess board display before resign
                updateChessBoardDisplay();
                //force a resign
                handleResign();
            } else {
                gameStatusTextView.setText("Current turn: " + chessApp.currentTurn);
                hasUndoOneTurn = false;
                updateChessBoardDisplay();
            }
        } else {
            //System.out.println("changed selection!");
            //we must be selecting a same color piece, we will update the selection
            currentSelectedLocation = imageViewLocation;
        }
    }

    private void handleUndo() {
        if(gameStatus.equals(GAME_OVER)) {
            return;
        }
        if(hasUndoOneTurn) {
            return;
        }
        hasUndoOneTurn = true;
        chessApp.undo();
        updateChessBoardDisplay();
        gameStatusTextView.setText("Current turn: " + chessApp.currentTurn);

    }

    private Location getImageViewLocation(ImageView imageView) {
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if(chessBoardView[r][c] == imageView) {
                    return new Location(r,c);
                }
            }
        }
        //should not be possible to get here
        return null;
    }

    private void updateChessBoardDisplay() {
        ChessDisplayHelper.updateChessBoardDisplay(chessBoardView, chessApp, this);
    }

    private void showAlertDialogResign() {
        input = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save game?");
        builder.setMessage("Title Game");
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String txt = input.getText().toString();

                //prompt user for name and save
                if (txt.equals("")) {
                    Toast.makeText(ChessGameActivity.this, "game name cannot be empty", (Toast.LENGTH_SHORT)).show();
                }
                else {
                    chessApp.saveGame(txt);
                    Toast.makeText(ChessGameActivity.this, "game saved", (Toast.LENGTH_SHORT)).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ChessGameActivity.this,"game not saved", (Toast.LENGTH_SHORT)).show();
            }
        });

        builder.create().show();
    }

}