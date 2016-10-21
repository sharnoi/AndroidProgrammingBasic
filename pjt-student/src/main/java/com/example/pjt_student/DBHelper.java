package com.example.pjt_student;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by student on 2016-10-18.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;

    public DBHelper(Context context) {
        super(context, "studentdb", null, DATABASE_VERSION);
    }

    // app install 후 helper 클래스가 최초로 사용되는 순간 딱 한번 실행
    // app을 위한 db 준비
    @Override
    public void onCreate(SQLiteDatabase db) {
        String studentSql = "create table tb_student (" +
                                "_id integer primary key autoincrement," +
                                "name not null," +
                                "email," +
                                "phone," +
                                "photo," +
                                "memo)";
        String scoreSql = "create table tb_score (" +
                            "_id integer primary key autoincrement," +
                            "student_id not null," +
                            "date," +
                            "score)";

        db.execSQL(studentSql);
        db.execSQL(scoreSql);
    }

    // 생성자에서 상위 클래스로 db version 정보가 넘어간다
    // 그 버전이 변경될 때 마다 => db schme 변경
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table tb_student");
            db.execSQL("drop table tb_score");
            onCreate(db);
        }
    }
}
