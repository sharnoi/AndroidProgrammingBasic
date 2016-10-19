package com.example.pjt_student;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.pjt_student.R.id.read_score_list;

public class ReadStudentActivity extends AppCompatActivity {

    ImageView studentImage;
    TextView nameView;
    TextView phoneView;
    TextView emailView;

    TabHost host;

    int student_id = 1;

    TextView addScoreView;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnBack, btnAdd;

    MyView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_student_activity);

        initData();
        initTab();

        initAddScore();
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

        String photo = cursor.getString(4);

        scoreView = (MyView)findViewById(R.id.read_score);
        Cursor cursor1 = db.rawQuery("select score from tb_score where student_id = ? order by date desc limit 1",
                new String[]{String.valueOf(student_id)});

        int score=0;
        while(cursor1.moveToNext()){
            score = cursor1.getInt(0);
        }

        scoreView.setScore(score);

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
        tabSpec.setContent(read_score_list);
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

    private void initAddScore() {
        btn0 = (Button) findViewById(R.id.key_0);
        btn1 = (Button) findViewById(R.id.key_1);
        btn2 = (Button) findViewById(R.id.key_2);
        btn3 = (Button) findViewById(R.id.key_3);
        btn4 = (Button) findViewById(R.id.key_4);
        btn5 = (Button) findViewById(R.id.key_5);
        btn6 = (Button) findViewById(R.id.key_6);
        btn7 = (Button) findViewById(R.id.key_7);
        btn8 = (Button) findViewById(R.id.key_8);
        btn9 = (Button) findViewById(R.id.key_9);
        btnBack = (Button) findViewById(R.id.key_back);
        btnAdd = (Button) findViewById(R.id.key_add);

        addScoreView = (TextView) findViewById(R.id.key_edit);

        btn0.setOnClickListener(addScoreListener);
        btn1.setOnClickListener(addScoreListener);
        btn2.setOnClickListener(addScoreListener);
        btn3.setOnClickListener(addScoreListener);
        btn4.setOnClickListener(addScoreListener);
        btn5.setOnClickListener(addScoreListener);
        btn6.setOnClickListener(addScoreListener);
        btn7.setOnClickListener(addScoreListener);
        btn8.setOnClickListener(addScoreListener);
        btn9.setOnClickListener(addScoreListener);
        btnBack.setOnClickListener(addScoreListener);
        btnAdd.setOnClickListener(addScoreListener);
    }

    View.OnClickListener addScoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==btnAdd){
                long date = System.currentTimeMillis();
                String score = addScoreView.getText().toString();

                DBHelper helper = new DBHelper(ReadStudentActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("insert into tb_score (student_id, date, score) values (?,?,?)",
                        new String[]{String.valueOf(student_id), String.valueOf(date), score});
                db.close();
                // 화면은??
                initData();
                host.setCurrentTab(0);
                addScoreView.setText("0");

                scoreView.setScore(Integer.parseInt(score));

            }else if(v==btnBack){
                String score = addScoreView.getText().toString();
                if(score.length() == 1) {
                    addScoreView.setText("0");
                }else{
                    addScoreView.setText(score.substring(0, score.length()-1));
                }
            }else{
                String score = addScoreView.getText().toString();
                Button btn = (Button)v;
                String txt = btn.getText().toString();
                if(score.equals("0")){
                    addScoreView.setText(txt);
                }else{
                    String newTxt = score + txt;
                    if(Integer.valueOf(newTxt) > 100){
                        Toast t = Toast.makeText(ReadStudentActivity.this, R.string.read_add_score_over_score, Toast.LENGTH_SHORT);
                        t.show();
                    }else {
                        addScoreView.setText(newTxt);
                    }
                }

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }
}
