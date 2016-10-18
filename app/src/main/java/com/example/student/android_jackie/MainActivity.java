package com.example.student.android_jackie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 화면출력..
        setContentView(R.layout.activity_main);
        // view 객체 획득..
        btn = (Button)findViewById(R.id.btn);
        editText = (EditText)findViewById(R.id.edit);
        // view에 이벤트 등록
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String data = editText.getText().toString();
        Log.d("jackie", data);
    }
}
