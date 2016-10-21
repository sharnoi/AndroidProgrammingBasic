package com.example.pjt_student;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.pjt_student.R.id.read_score_list;

// 유저가 tab 화면 조정 순간을 감지하기 위해서
public class ReadStudentActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, View.OnClickListener {

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

        Intent intent = getIntent();
        student_id = intent.getIntExtra("id", 1);

        initData();
        initTab();

        initAddScore();

        initSpannable();

        initList();

        initWebView();
    }

    private void initData() {
        studentImage = (ImageView) findViewById(R.id.read_student_image);
        nameView = (TextView) findViewById(R.id.read_name);
        phoneView = (TextView) findViewById(R.id.read_phone);
        emailView = (TextView) findViewById(R.id.read_email);

        studentImage.setOnClickListener(this);

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
        initStudentImage(photo);

        scoreView = (MyView) findViewById(R.id.read_score);
        Cursor cursor1 = db.rawQuery("select score from tb_score where student_id = ? order by date desc limit 1",
                new String[]{String.valueOf(student_id)});

        int score = 0;
        while (cursor1.moveToNext()) {
            score = cursor1.getInt(0);
        }

        scoreView.setScore(score);

        db.close();
    }

    private void initTab() {
        host = (TabHost) findViewById(R.id.host);
        host.setup(); // 너네 하위에 TabWidget과 FrameLayout 선언되어 있을 거다
        // 선언되어 있는데로 ui 준비하라
        // tab 하나가 TabSpec으로 표현되고 Tab Spec에 button과 button을 눌렀을 때 나오는 view가 연결되어 표현
        // 그 버튼 누르면 무조건 그 view가 출력
        // 매게변수는 개발자가 주는 tab 식별자 문자열
        // 필요없을 수도 있고 코드에서 유저가 어느 tab 화면을 보고 있는건지 판단할 때 이용
        TabHost.TabSpec tabSpec = host.newTabSpec("tab1");
        tabSpec.setIndicator("SCORE");//button
        tabSpec.setContent(read_score_list);
        host.addTab(tabSpec);

        tabSpec = host.newTabSpec("tab2");
        tabSpec.setIndicator("CHART");//button
        tabSpec.setContent(R.id.read_score_chart);
        host.addTab(tabSpec);

        tabSpec = host.newTabSpec("tab3");
        tabSpec.setIndicator("ADD");//button
        tabSpec.setContent(R.id.read_score_add);
        host.addTab(tabSpec);

        tabSpec = host.newTabSpec("tab4");
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
            if (v == btnAdd) {
                String score = addScoreView.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadStudentActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setTitle("Add Score");
                builder.setMessage(score + getResources().getString(R.string.read_add_score_dialog_message));
                builder.setPositiveButton("OK", dialogListener);
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (v == btnBack) {
                String score = addScoreView.getText().toString();
                if (score.length() == 1) {
                    addScoreView.setText("0");
                } else {
                    addScoreView.setText(score.substring(0, score.length() - 1));
                }
            } else {
                String score = addScoreView.getText().toString();
                Button btn = (Button) v;
                String txt = btn.getText().toString();
                if (score.equals("0")) {
                    addScoreView.setText(txt);
                } else {
                    String newTxt = score + txt;
                    if (Integer.valueOf(newTxt) > 100) {
                        Toast t = Toast.makeText(ReadStudentActivity.this, R.string.read_add_score_over_score, Toast.LENGTH_SHORT);
                        t.show();
                    } else {
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
        TextView spanView = (TextView) findViewById(R.id.spanView);
        TextView htmlView = (TextView) findViewById(R.id.htmlView);

        // ForegroundColorSpan, StyleSpan 등은 이 span이 투여되어 ui가 그렇게 나오면 끝
        // URLSpan은 이 span이 적용되어 ui가 링크 모양으로 나오는게 끝이 아니라 유저 이벤트를 신경써야함
        // URLSpan 상속으로 이벤트 코드 내장된 클래스를 적용한다
        URLSpan urlSpan = new URLSpan("") {
            @Override
            public void onClick(View widget) {
                Toast.makeText(ReadStudentActivity.this, "more click...", Toast.LENGTH_SHORT).show();
            }
        };

        Spannable spannable = (Spannable) spanView.getText();
        String data = spanView.getText().toString();

        int pos = data.indexOf("EXID");
        while (pos > -1) {
            spannable.setSpan(new ForegroundColorSpan(Color.RED), pos, pos + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            pos = data.indexOf("EXID", pos + 4);
        }

        pos = data.indexOf("more");
        spannable.setSpan(urlSpan, pos, pos + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            if (source.equals("myImage")) {
                Drawable dr = getResources().getDrawable(R.drawable.hani_1);
                //아래의 정보(bound) 설정 안되면 화면에 출력 안됨
                dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                return dr;
            }
            return null; // 이미지 없다(적용만 안된다)
        }
    }

    ListView listView;
    ArrayList<HashMap<String, String>> scoreList;
    SimpleAdapter sa;

    private void initList() {
        listView = (ListView) findViewById(R.id.read_score_list);
        scoreList = new ArrayList<>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select score, date from tb_score where student_id=? order by date desc",
                new String[]{String.valueOf(student_id)});

        while (cursor.moveToNext()) {
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
        webView = (WebView) findViewById(R.id.read_score_chart);

        // android webview의 js engine 기본 꺼져있다
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        // js에서는 java에서 공개한 객체의 함수만 호출이 가능하다.
        // android라는 단어는 개발자 임의 단어. js에서 사용하는 객체명
        webView.addJavascriptInterface(new JavascriptTest(), "android");
    }

    public class JavascriptTest {
        // 아래의 annotation이 추가된 함수만 호출 가능
        @JavascriptInterface
        public String getWebData() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            if(scoreList.size() <= 10){
                int j=0;
                for(int i=scoreList.size(); i>0; i--){
                    sb.append("[" + j + ",");
                    sb.append(scoreList.get(i-1).get("score"));
                    sb.append("]");
                    if(i>1){
                        sb.append(",");
                        j++;
                    }
                }
            }else{
                int j=0;
                for(int i=10; i>0; i--){
                    sb.append("[" + j + ",");
                    sb.append(scoreList.get(i-1).get("score"));
                    sb.append("]");
                    if(i>1){
                        sb.append(",");
                        j++;
                    }
                }
            }
            sb.append("]");
            Log.d("Jackie", sb.toString());
            return sb.toString();
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("tab2")){
            webView.loadUrl("file:///android_asset/test.html");
        }
    }

    // dialog의 버튼 이벤트
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        // dialog : 이벤트 발생 dialog 객체
        // which : 버튼의 종류 (ok, cancel)
        @Override
        public void onClick(DialogInterface dialog, int which) {
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
        }
    };

    // 메뉴 선택 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // ListView의 첫번째 항목에서 데이터 추출
        View root = listView.getChildAt(0);
        TextView tv1 = (TextView)root.findViewById(R.id.read_list_score);
        TextView tv2 = (TextView)root.findViewById(R.id.read_list_date);

        String message = "score : " + tv1.getText().toString() + " " + tv2.getText().toString();

        int id = item.getItemId();
        if(id == R.id.menu_read_sms){
            String number = phoneView.getText().toString();
            if(number != null && number.isEmpty() == false){
                // SMS APP의 발신 activity를 실행
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + number));
                intent.putExtra("sms_body", message);

                startActivity(intent);
            }
        }else if(id == R.id.menu_read_email){
            String email = emailView.getText().toString();
            if(email != null && email.isEmpty() == false){
                // Email APP의 발신 activity를 실행
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "score");
                intent.putExtra(Intent.EXTRA_TEXT, message);

                // phone에 gmail app이 없다면 에러 발생
                // exception 발생
                try {
                    startActivity(intent);

                }catch (Exception e){
                    Toast.makeText(this, "no email app...", Toast.LENGTH_SHORT).show();
                }
            }
        }


        return super.onOptionsItemSelected(item);
    }

    // 매게변수로 넘어온 db 사진 경로의 이미지를 출력
    private void initStudentImage(String photo){
        if(photo != null && !"".equals(photo)){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            Bitmap bitmap = BitmapFactory.decodeFile(photo, options);
            if(bitmap != null){
                studentImage.setImageBitmap(bitmap);
            }
        }
    }

    File imageFile;
    @Override
    public void onClick(View v) {
        if(v == studentImage){
            try{
                // camera app 실행 사진 찍고 결과 되돌려 받기, camera app 연동 후 사진데이터를 되돌려 받기
                // ==> 사진 사이즈가 상당히 작게 리턴턴
                // file 정보를 주면 camera app이 사진 찍고 그 파일에 file write 후 성공 실패만 반환
                // ==> 원본 사진 사이즈 그대로 유지
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                imageFile = File.createTempFile("student", ".jpg",
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

                if(!imageFile.exists()){
                    imageFile.createNewFile();
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(intent, 10);
           }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // startActivityForResult에 의한 요청이 되돌아 올 때 자동 호출
    // requestCode : intent를 발생시킨 곳에서 intent를 구분하기 위해서 설정한 개발자 임의 숫자값
    // resultCode : intent에 의해 실행된 곳에서 되돌리기 전에
    // 요청 업무 처리를 어떻게 처리해서 되돌린 것인지 지정

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){
            // db update
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("update tb_student set photo = ? where _id = ?",
                    new String[]{imageFile.getAbsolutePath(), String.valueOf(student_id)});
            db.close();

            // 화면 출력
            initStudentImage(imageFile.getAbsolutePath());
        }
    }

    // camera app을 실행하면 camera app이 화면전환을 시키면서
    // 우리의 app까지 같이 화면전환되어 file 정보가 날아간다다
    // activity 종료 가능성을 대비해서 데이터 저장 복원

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // 데이터 저장
        outState.putString("filePath", imageFile.getAbsolutePath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            // 뭔가 저장된게 있다면
            String filePath = savedInstanceState.getString("filePath");
            if(filePath != null){
                imageFile = new File(filePath);
            }
        }
    }
}
