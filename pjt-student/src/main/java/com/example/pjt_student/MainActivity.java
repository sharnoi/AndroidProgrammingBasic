package com.example.pjt_student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    ImageView addBtn;
    ListView listView;
    ArrayList<StudentVO> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = (ImageView)findViewById(R.id.main_btn);
        listView = (ListView) findViewById(R.id.main_list);

        addBtn.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_student order by name", null);

        datas = new ArrayList<>();
        while(cursor.moveToNext()){
            StudentVO vo = new StudentVO();
            vo.id = cursor.getInt(0);
            vo.name = cursor.getString(1);
            vo.email = cursor.getString(2);
            vo.phone = cursor.getString(3);
            vo.photo = cursor.getString(4);
            vo.memo = cursor.getString(5);

            Cursor cursor1 = db.rawQuery("select score from tb_score where student_id = ? order by date desc limit 1",
                    new String[]{String.valueOf(vo.id)});
            while(cursor1.moveToNext()){
                vo.score = cursor1.getInt(0);
            }
            datas.add(vo);
        }

        db.close();

        MainListAdapter adapter = new MainListAdapter(this, R.layout.main_list_item, datas);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == addBtn){
            Intent intent = new Intent(this, AddStudentActivity.class);
            startActivity(intent);
        }
    }

    double initTime;
    Toast t;
    // 만약 back button처리만을 목적으로 한다면 onBackPressed(){} 이용
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - initTime > 3000){
                t = Toast.makeText(this, R.string.main_back_end, Toast.LENGTH_LONG);
                t.show();
            }else{
                // 3초 이내에 back이 두번 눌린경우 activity 종료
                t.cancel();
                finish();
            }
            initTime=System.currentTimeMillis();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // SearchView 획득,munu기법,menuItem 획득,그 안의 searchview
        MenuItem menuItem = menu.findItem(R.id.menu_main_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint(getResources().getString(R.string.main_search_hint));
        searchView.setIconifiedByDefault(true); // 초기 icon으로만;

        // search event 등록
        searchView.setOnQueryTextListener(queryListenter);
        return true;
    }

    SearchView.OnQueryTextListener queryListenter = new SearchView.OnQueryTextListener() {
        // 키보드에서 검색버튼 누른 순간
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
            Log.d("Jackie", query);
            return false;
        }

        // 검색어 한자 입력할 때마다
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ReadStudentActivity.class);
        intent.putExtra("id", datas.get(position).id);
        startActivity(intent);
    }
}
