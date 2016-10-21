package com.example.pjt_student;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.pjt_student.R.id.read_score_list;

// 유저가 tab 화면 조정 순간을 감지하기 위해서
public class ReadStudentActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{

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

        initSpannable();

        initList();

        initWebView();
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

        host.setOnTabChangedListener(this);
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

                // 동적 항목 추가
                HashMap<String, String> map = new HashMap<>();
                map.put("score", score);

                Date d = new Date(date);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                map.put("date", sd.format(d));
                scoreList.add(0, map);
                // 동적 항목 추가 제거는 데이터 집합에 데이터 작업후 반영명령
                sa.notifyDataSetChanged();
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

    private void initSpannable() {
        TextView spanView = (TextView)findViewById(R.id.spanView);
        TextView htmlView = (TextView)findViewById(R.id.htmlView);

        // ForegroundColorSpan, StyleSpan 등은 이 span이 투여되어 ui가 그렇게 나오면 끝
        // URLSpan은 이 span이 적용되어 ui가 링크 모양으로 나오는게 끝이 아니라 유저 이벤트를 신경써야함
        // URLSpan 상속으로 이벤트 코드 내장된 클래스를 적용한다
        URLSpan urlSpan = new URLSpan(""){
            @Override
            public void onClick(View widget) {
                Toast.makeText(ReadStudentActivity.this, "more click...", Toast.LENGTH_SHORT).show();
            }
        };

        Spannable spannable = (Spannable)spanView.getText();
        String data = spanView.getText().toString();

        int pos = data.indexOf("EXID");
        while(pos > -1){
            spannable.setSpan(new ForegroundColorSpan(Color.RED), pos, pos+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            pos = data.indexOf("EXID", pos+4);
        }

        pos = data.indexOf("more");
        spannable.setSpan(urlSpan, pos, pos+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanView.setMovementMethod(LinkMovementMethod.getInstance());

        htmlView.setText(Html.fromHtml("<font color='blue'>HANI</font><br/><img src='myImage'",
                new MyImageGetter(), // 이미지는 개발자가 직접 가져와야 함
                null)); // TagHandler
    }

    class MyImageGetter implements Html.ImageGetter {
        // fromHtml 함수가 문자열에 <img>태그 하나당 한번씩 호출
        // 최종 리턴되는 이미지를 <img>영역에 출력
        // 매게변수가 img 태그의 src 문자열
        @Override
        public Drawable getDrawable(String source) {
            if(source.equals("myImage")){
                Drawable dr = getResources().getDrawable(R.drawable.hani_1);
                //아래의 정보(bound) 설정 안되면 화면에 출력 안됨
                dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                return dr;
            }
            return null; // 이미지 없다(적용만 안된다)
        }
    }

    ListView listView;
    ArrayList<HashMap<String,String>> scoreList;
    SimpleAdapter sa;
    private void initList() {
        listView = (ListView)findViewById(R.id.read_score_list);
        scoreList = new ArrayList<>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select score, date from tb_score where student_id=? order by date desc",
                new String[]{String.valueOf(student_id)});

        while(cursor.moveToNext()){
            HashMap<String, String> map = new HashMap<>();
            map.put("score", cursor.getString(0));

            Date d = new Date(Long.parseLong(cursor.getString(1)));
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            map.put("date", sd.format(d));
            scoreList.add(map);
        }
        db.close();

        sa = new SimpleAdapter(this,
                scoreList, // 항목구성 집합 데이터
                R.layout.read_list_item, // 항목 하나 layout xml
                new String[]{"score", "date"}, // 항목 데이터 획득을 위한 key
                new int[]{R.id.read_list_score, R.id.read_list_date}); // layout xml에 view가 여러개다. 어느 View인지 View id 표시

        listView.setAdapter(sa);
    }

    WebView webView;
    private void initWebView() {
        webView = (WebView)findViewById(R.id.read_score_chart);
        // android webview의 js engine 기본 꺼져있다
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    public void onTabChanged(String tabId) {

    }
}
