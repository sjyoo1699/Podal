package kr.ac.cnu.cse.termproject;

/**
 * Created by Usung on 2017-12-10.
 */

public class AddItemData {
    private String name, classify;

    public AddItemData(String name, String classify){
        this.name = name;
        this.classify = classify;
    }

    public String getName(){return name;}
    public void SetName(String name){this.name = name;}
    public String getClassify(){return classify;}
    public void setClassify(String classify){this.classify = classify;}
}
