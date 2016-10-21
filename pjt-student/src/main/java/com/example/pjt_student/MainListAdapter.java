package com.example.pjt_student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        return convertView;
    }
}
