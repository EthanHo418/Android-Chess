package com.example.android60;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android60.chess.model.CompletedChessGame;

public class ActivitySwitcher {
    //ok so basically we put strings in the bundle,
    //then we put the bundle in the intent
    //and that is how information is passed between activities.

    public static void switchToHomeScreen(AppCompatActivity appCompatActivity, String exampleArg) {
        Bundle bundle = new Bundle();
        bundle.putString("ButtonName", exampleArg);
        Intent intent = new Intent(appCompatActivity, HomeScreenActivity.class);
        intent.putExtras(bundle);
        appCompatActivity.startActivity(intent);
    }

    public static void switchToSavedGames(AppCompatActivity appCompatActivity) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(appCompatActivity, SavedGamesActivity.class);
        intent.putExtras(bundle);
        appCompatActivity.startActivity(intent);
    }

    public static void switchToChessGame(AppCompatActivity appCompatActivity) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(appCompatActivity, ChessGameActivity.class);
        intent.putExtras(bundle);
        appCompatActivity.startActivity(intent);
    }

    public static void switchToChessReplay(AppCompatActivity appCompatActivity, CompletedChessGame game) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(appCompatActivity, ChessReplayActivity.class);
        bundle.putSerializable("game", game);
        intent.putExtras(bundle);
        appCompatActivity.startActivity(intent);
    }
}
