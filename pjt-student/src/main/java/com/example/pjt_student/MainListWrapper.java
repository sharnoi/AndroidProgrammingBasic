package com.example.pjt_student;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by student on 2016-10-20.
 * findViewById의 성능이슈를 해결하기 위해
 * 한번은 find 해야하지만 한번 find한 view를 그 다음에 다시 이용된다면
 * 성능을 고려해서 저장했다가 find없이 그대로 획득해서 사용
 *
 * 하나의 항목에 find 대상이 되는 view가 여러개라면 하나하나 따로 저장 획득이 힘들기 때문에
 * wrapper 객체로 묶어서 한꺼번에 저장 획득
 */

public class MainListWrapper {
    // 한번은 find해야하고 find는 view 계층구조의 root 객체가 있어야 한다.
    // Adapter가 전달해주는 객체
    View root;

    // 화면에 보이는 모든 view를 선언할 필요 없이 find 대상이 되는 애들만 선언언
    ImageView studentImageView;
    TextView nameView;
    TextView scoreView;
    ImageView contactView;

    public MainListWrapper(View root) {
        this.root = root;
    }

    // Adapter에서 view가 필요할 때 직접 find 하지 않고 아래의 함수를 호출해서 획득
    // 이 Wrapper 객체를 Adapter에서 지속된다며 최초 한번만 find가 된다.
    public ImageView getStudentImageView() {
        if(studentImageView == null){
            studentImageView = (ImageView)root.findViewById(R.id.main_item_student_image);
        }
        return studentImageView;
    }

    public TextView getNameView() {
        if(nameView == null){
            nameView = (TextView)root.findViewById(R.id.main_item_name);
        }
        return nameView;
    }

    public TextView getScoreView() {
        if(scoreView == null){
            scoreView = (TextView)root.findViewById(R.id.main_item_score);
        }
        return scoreView;
    }

    public ImageView getContactView() {
        if(contactView == null){
            contactView = (ImageView)root.findViewById(R.id.main_item_contact);
        }
        return contactView;
    }
}
