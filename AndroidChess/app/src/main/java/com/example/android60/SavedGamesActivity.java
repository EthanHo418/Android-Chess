package com.example.android60;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android60.chess.model.ChessApp;
import com.example.android60.chess.model.CompletedChessGame;

import java.util.ArrayList;
import java.util.Comparator;

public class SavedGamesActivity extends AppCompatActivity {
    private ChessApp chessApp;
    private ArrayList<CompletedChessGame> previousChessGames;
    ListView gameList;
    private ArrayAdapter<CompletedChessGame> adapter;
    private ArrayAdapter<CompletedChessGame> sortedNameAdapter;
    private Button backButton;
    private Button sortNameButton;
    private Button sortDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chessApp = ChessAppContainer.chessApp;
        previousChessGames = chessApp.getPreviousChessGames();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1,
                new ArrayList<>(chessApp.getPreviousChessGames()));
        sortedNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1,
                new ArrayList<>(chessApp.getPreviousChessGames()));
        sortedNameAdapter.sort(new Comparator<CompletedChessGame>() {
            @Override
            public int compare(CompletedChessGame completedChessGame, CompletedChessGame t1) {
                return completedChessGame.getName().compareTo(t1.name);
            }
        });
        setContentView(R.layout.activity_saved_games);
        gameList = findViewById(R.id.game_list);
        gameList.setAdapter(adapter);
        sortDateButton = findViewById(R.id.sort_date_button);
        sortDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameList.setAdapter(adapter);
            }
        });
        sortNameButton = findViewById(R.id.sort_name_button);
        sortNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameList.setAdapter(sortedNameAdapter);
            }
        });
        gameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CompletedChessGame game = (CompletedChessGame) adapterView.getItemAtPosition(i);
                ActivitySwitcher.switchToChessReplay(SavedGamesActivity.this, game);
            }
        });
        backButton = findViewById(R.id.saved_games_button);
        backButton.setOnClickListener(view -> { ActivitySwitcher.switchToHomeScreen(this, ""); });
    }
}