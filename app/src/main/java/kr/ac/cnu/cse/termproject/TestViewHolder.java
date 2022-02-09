package kr.ac.cnu.cse.termproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Usung on 2017-12-10.
 */

public class TestViewHolder extends RecyclerView.ViewHolder {
    public TextView mItemNameText;

    public TestViewHolder(View itemView){
        super(itemView);
        mItemNameText = (TextView) itemView.findViewById(R.id.manageChild);
    }
}
