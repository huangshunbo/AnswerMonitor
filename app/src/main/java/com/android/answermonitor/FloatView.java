package com.android.answermonitor;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FloatView extends FrameLayout {
    ImageView ivIcon;
    ImageView ivVisiable;
    TextView tvAnswer;
    public FloatView(@NonNull Context context, final Service service) {
        super(context);
        inflate(context, R.layout.view_float,this);
        ivIcon = (ImageView) findViewById(R.id.icon);
        ivVisiable = findViewById(R.id.visible);
        tvAnswer = (TextView) findViewById(R.id.answer);
        ivVisiable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvAnswer.getVisibility() == VISIBLE){
                    tvAnswer.setVisibility(GONE);
                }else{
                    tvAnswer.setVisibility(VISIBLE);
                }
            }
        });
    }

    public void setText(Spanned result){
        tvAnswer.setText(result);
        tvAnswer.setVisibility(VISIBLE);
    }

    public ImageView getIvIcon() {
         return ivIcon;
    }
}
