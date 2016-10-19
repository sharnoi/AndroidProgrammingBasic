package com.example.pjt_student;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class ReadStudentActivity extends AppCompatActivity {

    ImageView studentImage;
    TextView nameView;
    TextView phoneView;
    TextView emailView;

    TabHost host;

    int student_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_student_activity);

        initData();
        initTab();
    }

    private void initData() {
        studentImage = (ImageView)findViewById(R.id.read_student_image);
        nameView = (TextView)findViewById(R.id.read_name);
        phoneView = (TextView)findViewById(R.id.read_phone);
        emailView = (TextView)findViewById(R.id.read_email);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select괸 row의 집합객체 (like resultSet)
        // cursor를 움직여서 row 하나를 선택하고 선택된 row의 column data 추출
        Cursor cursor = db.rawQuery("select * from tb_student where _id = " + student_id, null);
        cursor.moveToFirst();

        nameView.setText(cursor.getString(1));
        emailView.setText(cursor.getString(2));
        phoneView.setText(cursor.getString(3));

        String photo = cursor.getString (4);

        db.close();
    }

    private void initTab() {
        host = (TabHost)findViewById(R.id.host);
        host.setup(); // 너네 하위에 TabWidget과 FrameLayout 선언되어 있을 거다
                      // 선언되어 있는데로 ui 준비하라
        // tab 하나가 TabSpec으로 표현되고 Tab Spec에 button과 button을 눌렀을 때 나오는 view가 연결되어 표현
        // 그 버튼 누르면 무조건 그 view가 출력
        // 매게변수는 개발자가 주는 tab 식별자 문자열
        // 필요없을 수도 있고 코드에서 유저가 어느 tab 화면을 보고 있는건지 판단할 때 이용
        TabHost.TabSpec tabSpec =host.newTabSpec("tab1");
        tabSpec.setIndicator("SCORE");//button
        tabSpec.setContent(R.id.read_score_list);
        host.addTab(tabSpec);

        tabSpec =host.newTabSpec("tab2");
        tabSpec.setIndicator("CHART");//button
        tabSpec.setContent(R.id.read_score_chart);
        host.addTab(tabSpec);

        tabSpec =host.newTabSpec("tab3");
        tabSpec.setIndicator("ADD");//button
        tabSpec.setContent(R.id.read_score_add);
        host.addTab(tabSpec);

        tabSpec =host.newTabSpec("tab4");
        tabSpec.setIndicator("MEMO");//button
        tabSpec.setContent(R.id.read_memo);
        host.addTab(tabSpec);
    }
}
