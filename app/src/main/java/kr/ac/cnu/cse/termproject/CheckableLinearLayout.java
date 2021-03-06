package kr.ac.cnu.cse.termproject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by Usung on 2017-12-05.
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable{

    public CheckableLinearLayout(Context context, AttributeSet arrts){
        super(context,arrts);
    }

    @Override
    public boolean isChecked(){
        CheckBox cb = (CheckBox)findViewById(R.id.checkbox);

        return cb.isChecked();
    }

    @Override
    public void toggle(){
        CheckBox cb = (CheckBox)findViewById(R.id.checkbox);
        setChecked(cb.isChecked() ? false : true);
    }

    @Override
    public void setChecked(boolean checked){
        CheckBox cb = (CheckBox) findViewById(R.id.checkbox);

        if (cb.isChecked() != checked){
            cb.setChecked(checked);
        }
    }
}
