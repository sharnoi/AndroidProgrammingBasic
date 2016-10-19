package com.example.pjt_student;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by student on 2016-10-19.
 */

public class MyView extends View {
    int score;
    int color;
    Context context;

    // CustomView를 이용하는 Activity 소스코드에서 직접 생성해서 이용 => 생성자는 하나만
    // Customview를 layout xml에 등록해서 사용한다면 생성시 호출되는 생성자는 상황에 따라 틀리다
    // layout xml 등록을 허용할려면 곡 생성자 3개 모두 정의해줘야 한다

    public MyView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs != null){
            // activity의 layout xml 에 등록된 속성값 획득
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AAA);
            color = a.getColor(R.styleable.AAA_customColor, Color.YELLOW);
        }
    }

    // activity에서 score 전달 목적으로 호출
    public void setScore(int score){
        this.score = score;

        // 화면 갱신
        invalidate(); // onDraw 다시 호출
    }

    // activity에 의해 view가 출력될때 view의 화면 내용을 그리기 위해 자동호출
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.alpha(Color.CYAN)); // 화면 지우기

        // 원의 사각형 정보
        RectF rectF = new RectF(15, 15, 70, 70);

        // 그리기 옵션
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(15);
        // 기본 색으로 360도 원 그리기
        canvas.drawArc(rectF, 0, 360, false, paint);

        // score에 해당되는 각도 계산
        float endAngle = (360*score)/100;
        paint.setColor(color);
        // -90 : 0도가 동쪽, 북쪽부터 그리기
        canvas.drawArc(rectF, -90, endAngle, false, paint);
    }
}
