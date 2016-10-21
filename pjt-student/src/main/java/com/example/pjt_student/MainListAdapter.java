package com.example.pjt_student;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by student on 2016-10-20.
 */

public class MainListAdapter extends ArrayAdapter<StudentVO> {
    Context context;
    ArrayList<StudentVO> datas;
    int resId;

    public MainListAdapter(Context context, int resId, ArrayList<StudentVO> datas) {
        super(context, resId);

        this.context = context;
        this.resId = resId;
        this.datas = datas;
    }

    // 항목 갯수를 판단하기 위해서 자동 호출
    @Override
    public int getCount() {
        return this.datas.size();
    }

    /**
     * 항목 하나가 화면에 보일때마다 호출
     * => 항목하나를 구성하는 알고리즘
     * => 성능이슈 고려
     *
     * @param position 항목 index 값 (몇번째 항목인가)
     * @param convertView 해당 항목이 최초로 화면에 나오는 순간이라면 null, 아니면 리턴시킨 view의 root 객체
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            // android
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resId, null);

            MainListWrapper wrapper = new MainListWrapper(convertView);

            // android의 모든 view 객체에 개발자 임의 데이터(non-visible) 저장 가능
            // 그 view가 메모리에 지속된다면 언제든지 획득 가능
            convertView.setTag(wrapper); // setTag(key, value)
        }

        MainListWrapper wrapper = (MainListWrapper)convertView.getTag();

        // view 획득
        ImageView studentImageView = wrapper.getStudentImageView();
        TextView nameView = wrapper.getNameView();
        TextView scoreView = wrapper.getScoreView();
        final ImageView contactView = wrapper.getContactView();

        // 항목 데이터 획득
        final StudentVO vo = datas.get(position);

        nameView.setText(vo.name);
        scoreView.setText(String.valueOf(vo.score));

        // 유저 프사가 있으면 그 이미지로 출력하고 없으면 기본 이미지 출력
        // 우리의 이미지는 유저 카메라로 찍은 이미지이고 사이즈가 너무 크다
        // 화면에 출력하려면 메모리에 올리며 이때 OutOfMemory 문제가 발생한다.
        // 이미지 데이터 사이즈 자체를 줄여서 로딩
        if(vo.photo != null && !vo.photo.equals("")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            Bitmap bitmap = BitmapFactory.decodeFile(vo.photo);
            if (bitmap != null) {
                studentImageView.setImageBitmap(bitmap);
            }
        }else{
            studentImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_student_small));
        }

        // 프사 클릭 이벤트 => dialog
        studentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog를 위한 layout xml 초기화
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View root = inflater.inflate(R.layout.dialog_student_image, null);
                ImageView dialogImage = (ImageView) root.findViewById(R.id.dialog_image);

                // 유저 프사가 있으면 그 이미지로 출력하고 없으면 기본 이미지 출력
                // 우리의 이미지는 유저 카메라로 찍은 이미지이고 사이즈가 너무 크다
                // 화면에 출력하려면 메모리에 올리며 이때 OutOfMemory 문제가 발생한다.
                // 이미지 데이터 사이즈 자체를 줄여서 로딩
                if(vo.photo != null && !vo.photo.equals("")) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap bitmap = BitmapFactory.decodeFile(vo.photo);
                    if (bitmap != null) {
                        dialogImage.setImageBitmap(bitmap);
                    }
                }else{
                    dialogImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_student_large));
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(root);
                builder.create().show();
            }
        });

        contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vo.phone != null && !"".equals(vo.phone)){
                    // CallApp 연동으로 전화
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + vo.phone));
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, R.string.main_list_phone_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
