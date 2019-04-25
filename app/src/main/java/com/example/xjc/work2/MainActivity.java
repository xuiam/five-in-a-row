package com.example.xjc.work2;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Chess chess_f2;
    private Button reset_btn;
    private TextView curr_f2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("游戏结束")
                .setPositiveButton("确定", null);
        chess_f2.setOnOver(new Chess.CallBack() {
            @Override
            public void over(int type) {
                if (type == 1) {
                    dialog.setMessage("黑子胜利！！！").show();
                } else {
                    dialog.setMessage("白子胜利！！！").show();
                }
            }

            @Override
            public void curr(boolean curr) {
                if (curr) {
                    curr_f2.setText("黑子下子");
                } else {
                    curr_f2.setText("白子下子");
                }
            }
        });
    }
    private void initView() {
        chess_f2 = findViewById(R.id.chess_f2);
        curr_f2 = findViewById(R.id.curr_f2);
        reset_btn =  findViewById(R.id.reset_btn);

        reset_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_btn:
                chess_f2.clearChess();
                break;
        }
    }
}
