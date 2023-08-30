package com.example.android60;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.android60.chess.model.ChessApp;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ChessAppContainer.chessApp == null) {
            ChessAppContainer.chessApp = new ChessApp(this);
        }

        Button start_button = findViewById(R.id.main_page_start_game_button);
        start_button.setOnClickListener(view -> { ActivitySwitcher.switchToChessGame(this); });

        Button replay_button = findViewById(R.id.main_page_go_to_replays_button);
        replay_button.setOnClickListener(view -> { ActivitySwitcher.switchToSavedGames(this); });
    }
}