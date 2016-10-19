package com.example.pjt_student;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudentActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nameView;
    EditText emailView;
    EditText phoneView;
    EditText memoView;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        nameView = (EditText)findViewById(R.id.add_name);
        emailView = (EditText)findViewById(R.id.add_email);
        phoneView = (EditText)findViewById(R.id.add_email);
        memoView = (EditText)findViewById(R.id.add_memo);
        addBtn = (Button)findViewById(R.id.add_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = nameView.getText().toString();
        String email = emailView.getText().toString();
        String phone = phoneView.getText().toString();
        String memo = memoView.getText().toString();

        if(name == null || "".equals(name)){
            Toast.makeText(this, R.string.add_name_null, Toast.LENGTH_SHORT);
//            Toast t = Toast.makeText(this, R.string.add_name_null, Toast.LENGTH_SHORT);
//            t.show();
        }else{
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("insert into tb_student (name, email, phone, memo) values(?,?,?,?)",
                    new String[]{name, email, phone, memo});
            db.close();

            // 화면은??
            // code 적으로 activity를 죽여서 시스템에서 알아서 이전화면으로 이동
            finish();
        }
    }
}
