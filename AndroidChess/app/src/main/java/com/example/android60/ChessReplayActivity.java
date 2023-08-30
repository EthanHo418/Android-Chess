package com.example.android60;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android60.chess.model.ChessApp;
import com.example.android60.chess.model.ChessBoard;
import com.example.android60.chess.model.CompletedChessGame;

import java.util.ArrayList;

public class ChessReplayActivity extends AppCompatActivity {
    private ChessApp chessApp;
    private TextView gameTitle;
    private ImageView[][] chessBoardView;
    private TableLayout tableLayout;
    private ChessBoard currentBoard;
    ArrayList<ChessBoard> boards;
    private Button nextButton;
    String gameName;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_replay);
        chessApp = ChessAppContainer.chessApp;
        gameTitle = findViewById(R.id.replay_game_title);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        tableLayout = findViewById(R.id.replay_chess_board);
        chessBoardView = ChessDisplayHelper.chessBoardViewSetup(tableLayout, this);
        assert b != null;
        CompletedChessGame game = (CompletedChessGame) b.getSerializable("game");
        gameName = game.getName();
        boards = game.getAllBoards();
        gameTitle.setText(gameName);
        updateChessBoardDisplay(boards.get(counter));
        counter ++;

        nextButton = findViewById(R.id.next_move_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChessBoardDisplay(boards.get(counter));
                if (counter < boards.size() - 1){
                    counter++;
                } else {
                    Toast.makeText(view.getContext(), "game over", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void updateChessBoardDisplay(ChessBoard currentBoard) {
        ChessDisplayHelper.updateChessBoardDisplay(chessBoardView, currentBoard, this);
    }
}