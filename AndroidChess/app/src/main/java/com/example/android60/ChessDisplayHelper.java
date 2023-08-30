package com.example.android60;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.core.content.ContextCompat;

import com.example.android60.chess.model.ChessApp;
import com.example.android60.chess.model.ChessBoard;
import com.example.android60.chess.model.ChessPiece;

public class ChessDisplayHelper {

    public static ImageView[][] chessBoardViewSetup(TableLayout tableLayout, Context context ) {
        ImageView[][] chessBoardView = new ImageView[8][8];
        int viewHeight = tableLayout.getLayoutParams().height/8;
        int viewWidth = tableLayout.getLayoutParams().width/8;
        for(int r = 0; r < 8; r++) {
            //not sure why table row needs context
            TableRow tableRow = new TableRow(context);
            for (int c = 0; c < 8; c++) {
                ImageView imageView = new ImageView(context);
                chessBoardView[r][c] = imageView;
                setChessSquareBackground(imageView, r, c, context);
                tableRow.addView(imageView);
                //imageView.setImageIcon(Icon.createWithResource(this, R.drawable.black_bishop));
                imageView.getLayoutParams().height = viewHeight;
                imageView.getLayoutParams().width = viewWidth;
                //imageView.setOnClickListener(view -> { processSelected((ImageView) view); });
            }
            tableLayout.addView(tableRow);
        }
        return chessBoardView;
    }

    private static void setChessSquareBackground(View view, int r, int c, Context context) {
        if ((r + c + 1) % 2 == 0) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.red_brown));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    public static void updateChessBoardDisplay(ImageView[][] chessBoardView, ChessApp chessApp, Context context ) {
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ImageView imageView = chessBoardView[r][c];
                ChessPiece chessPiece = chessApp.getPiece(r,c);
                if(chessPiece != null) {
                    int resourceId = getResourceIdFromPieceName(chessPiece.getName());
                    imageView.setImageIcon(Icon.createWithResource(context, resourceId));
                } else {
                    imageView.setImageIcon(null);
                }
            }
        }
    }

    public static void updateChessBoardDisplay(ImageView[][] chessBoardView, ChessBoard chessBoard, Context context ) {
        for(int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ImageView imageView = chessBoardView[r][c];
                ChessPiece chessPiece = chessBoard.getPiece(r,c);
                if(chessPiece != null) {
                    int resourceId = getResourceIdFromPieceName(chessPiece.getName());
                    imageView.setImageIcon(Icon.createWithResource(context, resourceId));
                } else {
                    imageView.setImageIcon(null);
                }
            }
        }
    }

    private static int getResourceIdFromPieceName(String name) {
        switch (name) {
            case "whitep": return R.drawable.white_pawn;
            case "whiteK": return R.drawable.white_king;
            case "whiteQ": return R.drawable.white_queen;
            case "whiteB": return R.drawable.white_bishop;
            case "whiteN": return R.drawable.white_knight;
            case "whiteR": return R.drawable.white_rook;

            case "blackp": return R.drawable.black_pawn;
            case "blackK": return R.drawable.black_king;
            case "blackQ": return R.drawable.black_queen;
            case "blackB": return R.drawable.black_bishop;
            case "blackN": return R.drawable.black_knight;
            case "blackR": return R.drawable.black_rook;

            default: return R.drawable.black_rook;
        }
    }

}
