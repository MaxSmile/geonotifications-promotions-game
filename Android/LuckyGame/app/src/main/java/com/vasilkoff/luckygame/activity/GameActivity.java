package com.vasilkoff.luckygame.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.vasilkoff.luckygame.R;

public class GameActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        parentLayout = (RelativeLayout) findViewById(R.id.activity_game);
    }

    private void checkAccount() {
        if (AccessToken.getCurrentAccessToken() != null) {
            playGame();
        } else {
            startActivity(new Intent(this, ChooseAccountActivity.class));
        }
    }

    public void onClickGame(View view) {
        switch (view.getId()) {
            case R.id.gamePlay:
                checkAccount();
                break;
            case R.id.gameWin:
                startActivity(new Intent(this, ShareActivity.class));
                finish();
                break;
            case R.id.gameLose:
                chooseAction();
                break;
        }
    }

    private void playGame() {

    }

    private void chooseAction() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.choose_action, null);

        popupWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        popupWindow.showAtLocation(parentLayout, Gravity.CENTER,0, 0);
    }

    private void shareDialog() {

    }

}
