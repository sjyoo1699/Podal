package kr.ac.cnu.cse.termproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Usung on 2017-12-05.
 */

public class CustomChoiceListViewAdapter extends BaseAdapter {

    private ArrayList<GenerateItem> listViewItemList = new ArrayList<GenerateItem>();

    public CustomChoiceListViewAdapter(){

    }

    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.generate_item, parent, false);
        }

        TextView textTextView = (TextView)convertView.findViewById(R.id.checkboxItem);

        GenerateItem listViewItem = listViewItemList.get(position);

        textTextView.setText(listViewItem.getText());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    public void addItem(String text){
        GenerateItem item = new GenerateItem();

        item.setText(text);

        listViewItemList.add(item);
    }
}
