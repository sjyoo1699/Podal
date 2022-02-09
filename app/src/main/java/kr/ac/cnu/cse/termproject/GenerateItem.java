package kr.ac.cnu.cse.termproject;

/**
 * Created by Usung on 2017-12-05.
 */

public class GenerateItem {
    private String text;

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public GenerateItem(){}

    public GenerateItem(String text){
        this.text = text;
    }
}
