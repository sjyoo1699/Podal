package kr.ac.cnu.cse.termproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PortfolioGenerateActivity extends Activity {
    /*ActionBar abar;*/
    Button generation;
    ArrayList<String> finishedList;
    ListView listView;
    CustomChoiceListViewAdapter adapter;
    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/podal/";
    ArrayList<String> isCheckedList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_generate);
        getWindow().setStatusBarColor(Color.BLACK);
        /*
        abar = this.getSupportActionBar();
        abar.show();
        abar.setTitle("포트폴리오 생성");
*/
        Intent intent = getIntent();
        finishedList = intent.getStringArrayListExtra("finishedList");

        adapter = new CustomChoiceListViewAdapter();

        for (int i =0; i<finishedList.size();i++){
            adapter.addItem(finishedList.get(i));
            isCheckedList.add("unChecked");
        }

        listView = (ListView)findViewById(R.id.checkboxContainer);
        listView.setAdapter(adapter);

        generation = (Button)findViewById(R.id.generation);
        generation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generate();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isCheckedList.get(i).equals("unChecked")){
                    isCheckedList.set(i,"Checked");
                }else{
                    isCheckedList.set(i,"unChecked");
                }
            }
        });
    }

    public void generate(){
        ArrayList<String> toGenerateProjectName = new ArrayList<String>();

        for (int i=0; i<finishedList.size();i++){
            if (isCheckedList.get(i).equals("Checked")){
                toGenerateProjectName.add(finishedList.get(i));
            }
        }


        String fullStr = "";

        File inFile1 = new File(dirPath+"UserInfo.txt");

        ArrayList<String> introduction = new ArrayList<String>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile1));
            String line;
            while((line = br.readLine()) != null) {
                introduction.add(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        String inst="";

        for (int i=0;i<introduction.size();i++){
            switch (i){
                case 0:
                    inst += "이름 : ";
                    inst += introduction.get(i) +"<br>";
                    break;

                case 1:
                    inst += "생년월일 : ";
                    inst += introduction.get(i) +"<br>";
                    break;

                case 2:
                    inst += "학교 : ";
                    inst += introduction.get(i) +"<br>";
                    break;

                case 3:
                    inst += "전공 : ";
                    inst += introduction.get(i) +"<br>";
                    break;

                case 4:
                    inst += "번호 : ";
                    inst += introduction.get(i) +"<br>";
                    break;

                case 5:
                    inst += "이메일 : ";
                    inst += introduction.get(i) +"<br>";
                    break;

                case 7:
                    inst += "블로그 : ";
                    inst += introduction.get(i) +"<br>";
                    break;

                default:
                    break;
            }
        }

        String str1 = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <title>Personal</title>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "  <meta name=\"description\" content=\"Free HTML5 Template plus Photoshop design\">\n" +
                "  <meta name=\"keywords\" content=\"free html template, portfolio, html5, sass, jquery\">\n" +
                "  <meta name=\"author\" content=\"Bucky Maler\">\n" +
                "  <link rel=\"stylesheet\" href=\"assets/css/main.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\t<!-- SECTION: Splashscreen -->\n" +
                "\t<div class=\"splashscreen\">\n" +
                "\t\t<div class=\"preloader\">\n" +
                "\t\t\t<span class=\"preloader__text\">Loading</span>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "\t<!-- END SECTION: Splashscreen -->\n" +
                "\t<!-- SECTION: Navigation -->\n" +
                "\t<div id=\"top\" class=\"navigation\">\n" +
                "\t\t<div class=\"navigation__wrapper wrapper--large\">\n" +
                "\t\t\t<a class=\"navigation__logo logo\" href=\"#0\" style=\"color:#165FB1\">PODAL</a>\n" +
                "\t\t\t<nav class=\"navigation__container\">\n" +
                "\t\t\t\t<ul class=\"navigation__list\">\n" ;

        for (int i=0; i<toGenerateProjectName.size();i++){
            str1+= "\t\t\t\t\t<li><a href =\"#about\">"+toGenerateProjectName.get(i)+"</a></li>\n";
        }

        str1 += "\t\t\t\t</ul>\n" +
                "\t\t\t</nav>\n" +
                "\t\t\t<a class=\"navigation__cta button\" href=\"#0\">Get A Quote</a>\n" +
                "\t\t\t<div class=\"navigation__burger\">\n" +
                "\t\t\t\t<span class=\"navigation__burger-el navigation__burger-el--top\"></span>\n" +
                "\t\t\t\t<span class=\"navigation__burger-el navigation__burger-el--middle\"></span>\n" +
                "\t\t\t\t<span class=\"navigation__burger-el navigation__burger-el--bottom\"></span>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "\t<!-- END SECTION: Navigation -->\n" +
                "\t<main role=\"main\">\n" +
                "\t\t<!-- SECTION: About -->\n" +
                "\t\t<section class=\"about\">\n" +
                "\t\t\t<div class=\"about__wrapper wrapper--small\">\n" +
                "                <div class=\"about__content\">\n" +
                "\t\t\t\t\t<div class=\"about__content-blurb blurb blurb--framed\">\n" +
                "                        <h2 class=\"blurb__heading\" style=\"margin-top:30px;\">Self<br>introduction</h2>\n" +
                "\t\t\t\t\t\t<p class=\"blurb__copy no-margin\">";

        fullStr += str1;
        fullStr += inst;

        fullStr +="</p>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<img class=\"about__visual\" src=\"";

        fullStr += dirPath+"/UserInfo_image.jpg";
        fullStr += "\" width=\"300\" height=\"270\" alt=\"image\">\n" +
                "\t\t\t</div>\n" +
                "\n" +
                "\t\t</section>";

        for (int i=0; i< toGenerateProjectName.size();i++){
            fullStr += eachProjectHtml(toGenerateProjectName.get(i));
        }


        fullStr +="\t</main>\n" +
                "\t<!-- SECTION: Footer -->\n" +
                "\t<footer class=\"footer\">\n" +
                "\t\t<div class=\"footer__wrapper wrapper--large\">\n" +
                "\t\t\t<p class=\"footer__copyright no-margin\">&copy; 포트폴리오를 달자꾸나</p>\n" +
                "\t\t\t<a class=\"footer__logo logo\" href=\"#0\" style=\"color:#165FB1\">PODAL</a>\n" +
                "\t\t\t<a class=\"footer__arrow\" href=\"#top\">\n" +
                "\t\t\t\t<svg class=\"footer__arrow-el\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" id=\"Layer_1\" x=\"0\" y=\"0\" viewBox=\"0 0 130 65\" xml:space=\"preserve\" enable-background=\"new 0 0 130 65\"><g transform=\"translate(0.000000,65.000000) scale(0.100000,-0.100000)\"><path d=\"M315.4 365c-336-305-345-315-290-351 18-12 32-14 45-8 10 5 145 123 300 262l281 253 196-178c108-98 237-215 287-260 50-46 98-83 107-83s27 9 40 19c20 16 22 23 14 46 -12 33-623 585-647 585C638.4 650 489.4 522 315.4 365z\"/></g></svg>\n" +
                "\t\t\t</a>\n" +
                "\t\t</div>\n" +
                "\t</footer>\n" +
                "\t<!-- END SECTION: Footer -->\n" +
                "\n" +
                "\t<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js\"></script>\n" +
                "\t<!-- jQuery local fallback -->\n" +
                "\t<script>window.jQuery || document.write('<script src=\"assets/js/vendor/jquery-2.2.4.min.js\"><\\/script>')</script>\n" +
                "\t<script src=\"assets/js/functions-min.js\"></script>\n" +
                "\t<!-- Google Analytics: change UA-XXXXX-X to be your site's ID and uncomment -->\n" +
                "\t<!--\n" +
                "\t<script>\n" +
                "\t(function(b, o, i, l, e, r) {\n" +
                "\t\tb.GoogleAnalyticsObject = l;\n" +
                "\t\tb[l] || (b[l] =\n" +
                "\t\t\tfunction() {\n" +
                "\t\t\t\t(b[l].q = b[l].q || []).push(arguments)\n" +
                "\t\t\t});\n" +
                "\t\tb[l].l = +new Date;\n" +
                "\t\te = o.createElement(i);\n" +
                "\t\tr = o.getElementsByTagName(i)[0];\n" +
                "\t\te.src = '//www.google-analytics.com/analytics.js';\n" +
                "\t\tr.parentNode.insertBefore(e, r)\n" +
                "\t}(window, document, 'script', 'ga'));\n" +
                "\tga('create', 'UA-XXXXX-X', 'auto');\n" +
                "\tga('send', 'pageview');\n" +
                "\t</script>\n" +
                "\t-->\t\n" +
                "</body>\n" +
                "</html>\n";

        try {
            File fos_htmlFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/PodalTemplate/index.html");
            FileOutputStream fos_html = new FileOutputStream(fos_htmlFile);
            fos_html.write(fullStr.getBytes());
            fos_html.close();
        }catch (IOException e){

        }

        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        startActivity(intent);
    }

    public String eachProjectHtml(String projectName){
        String html="\t\t<section class=\"app-design\">\n" +
                "\t\t\t<div class=\"app-design__wrapper wrapper--large\">\n" +
                "\t\t\t\t<div class=\"app-design__content blurb\">\n" +
                "\t\t\t\t\t<h2 class=\"blurb__heading\">"+projectName+"</h2>\n" +
                "\t\t\t\t\t<p class=\"blurb__copy blurb__copy--tight\">";

        File inFile1 = new File(dirPath+projectName+"/projectInfo.txt");

        ArrayList<String> projectInfoList = new ArrayList<String>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile1));
            String line;
            while((line = br.readLine()) != null) {
                projectInfoList.add(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        String projectInfo="";

        for (int i=0;i<projectInfoList.size();i++){
            switch (i){
                case 1:
                    projectInfo += "주최 : ";
                    projectInfo += projectInfoList.get(i) +"<br>";
                    break;

                case 2:
                    projectInfo += "기간 : ";
                    projectInfo += projectInfoList.get(i) +" ~ ";
                    break;

                case 3:
                    projectInfo += projectInfoList.get(i) +"<br>";
                    break;

                default:
                    break;
            }
        }

        html += projectInfo +"</p>\n" +
                "                    <img src=\"";

        html += dirPath+projectName+"/"+projectName+"_poster.jpg";
        html += "\" style=\"width:100%; height:100%;\" alt=\"Man viewing work plans\">\n" +
                "                </div>\n" +
                "\t\t\t</div>\n" +
                "\t\t</section>";

        html += ItemHtml(projectName);

        return html;
    }

    public String ItemHtml(String projectName){
        String html = "\t\t<section id=\"blog\" class=\"blog\">\n" +
                "\t\t\t<h6 class=\"blog__heading title\">프로젝트 진행과정</h6>\n" +
                "\t\t\t<div class=\"blog__wrapper wrapper--large\">\n";

        File inFile1 = new File(dirPath+projectName+"/itemList.txt");

        ArrayList<String> itemList = new ArrayList<String>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile1));
            String line;
            while((line = br.readLine()) != null) {
                itemList.add(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        for (int i=0; i<itemList.size(); i++){
            String[] arr;
            arr = itemList.get(i).split(" ");
            if (arr.length > 2){
                String temp = arr[0];
                for (int j=1;j<arr.length-1;j++){
                    temp += " " + arr[j];
                }

                if (arr[arr.length-1].equals("item")){
                    html += eachItemHtml(projectName,temp);
                }else{
                    html += eachLittleTitleHtml(temp);
                }
            }else{
                if (arr[1].equals("item")){
                    html += eachItemHtml(projectName,arr[0]);
                }else{
                    html += eachLittleTitleHtml(arr[0]);
                }
            }
        }

        html += "\t\t\t</div>\n" +
                "\t\t\t<div class=\"blog__view-more\">\n" +
                "\t\t\t</div>\n" +
                "\t\t</section>";

        return html;
    }

    public String eachLittleTitleHtml(String title){
        String html = "                <h3>";
        html += title + "</h3>";
        return html;
    }

    public String eachItemHtml(String projectName, String itemName){
        String html = "\t\t\t\t<article class=\"blog__post\">\n" +
                "\t\t\t\t\t<div class=\"blog__post-thumbnail\">\n" +
                "\t\t\t\t\t\t<img src=\"";

        File inFile1 = new File(dirPath+projectName+"/"+itemName+"/"+itemName+"_memo.txt");

        ArrayList<String> itemMemo = new ArrayList<String>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile1));
            String line;
            while((line = br.readLine()) != null) {
                itemMemo.add(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        String memo = "";

        for (int i=0; i<itemMemo.size();i++){
            memo += itemMemo.get(i) + "<br>";
        }

        html += dirPath+projectName+"/"+itemName+"/"+itemName+"_image.jpg";

        html += "\" alt=\"Man viewing work plans\">\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"blog__post-excerpt\">\n" +
                "\t\t\t\t\t\t<h3>";
        html += itemName + "</h3>\n" +
                "\t\t\t\t\t\t<p>";

        html += memo + "</p>\n";

        html += "\t\t\t\t\t</div>\n" +
                "\t\t\t\t</article>\n";

        File file = new File(dirPath+projectName+"/"+itemName+"/"+itemName+"_file.pdf");

        if (file.exists()){
            html += "\t\t<section class=\"web-design\">\n" +
                    "\t\t\t<div class=\"web-design__wrapper wrapper--small\">\n" +
                    "\t\t\t\t<div class=\"web-design__content blurb\">\n" +
                    "                    <iframe style=\"width:100%; height:100%;\" src=\"http://drive.google.com/viewerng/viewer?embedded=true&url="+dirPath+projectName+"/"+itemName+"/"+itemName+"_file.pdf"+
                    "\"></iframe>\t\n" +
                    "                </div>\n" +
                    "\t\t\t</div>\n" +
                    "\t\t</section>\n";
        }

        return html;
    }
}