package kr.ac.cnu.cse.termproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    private AnimatedExpandableListView mainExpandableListView;
    private ExampleAdapter adapter;
    Button generateButton;
    ImageView profile;
    public static final int REQUEST_CODE_ADD_PROJECT = 100;
    public static final int REQUEST_CODE_MANAGE_PROJECT = 99;
    public static final int REQUEST_CODE_GENERATE = 98;
    public static final int REQUEST_CODE_USERINFO = 97;
    /*ActionBar abar;*/
    private BackPressCloseHandler backPressCloseHandler;
    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/podal/";

    ArrayList<String> processingList;
    ArrayList<String> finishedList;

    int deleteIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<GroupItem> items = new ArrayList<GroupItem>();

        deleteIndex = 0;

        getWindow().setStatusBarColor(Color.BLACK);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }

/*
        abar = this.getSupportActionBar();
        abar.show();
        abar.setTitle("포 달");
*/

        backPressCloseHandler = new BackPressCloseHandler(this);

        File file = new File(dirPath);

        if(! file.exists()){
            file.mkdirs();
            String testStr = "";
            try {
                File fos_finish = new File(dirPath + "finished.txt");
                File fos_process = new File(dirPath + "processing.txt");
                File fos_userInfo = new File(dirPath + "UserInfo.txt");
                FileOutputStream fos_finished = new FileOutputStream(fos_finish);
                FileOutputStream fos_processing = new FileOutputStream(fos_process);
                FileOutputStream fos_userInformation = new FileOutputStream(fos_userInfo);
                fos_finished.write(testStr.getBytes());
                fos_finished.close();
                fos_processing.write(testStr.getBytes());
                fos_processing.close();
                fos_userInformation.write(testStr.getBytes());
                fos_userInformation.close();
            }catch (IOException e){}
        }

        File inFile = new File(dirPath+"processing.txt");


        processingList = new ArrayList<String>();
        finishedList = new ArrayList<String>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile));
            String line;
            while((line = br.readLine()) != null) {
                //한 라인 씩 " " 으로 스플릿 하고, 그렇게 나눈 스트링을
                //node로 만들어서 node 배열에 삽입
                processingList.add(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        // Populate our list with groups and it's children
        final GroupItem processing = new GroupItem();
        processing.title = "진행중인 프로젝트";
        for (int i=0;i<processingList.size();i++){
            ChildItem child = new ChildItem();
            child.title = processingList.get(i);

            processing.items.add(child);
        }
        ChildItem addButton = new ChildItem();
        addButton.title = "+";
        processing.items.add(addButton);
        items.add(processing);

        File inFile2 = new File(dirPath+"finished.txt");

        // 파일 읽기
        BufferedReader br2 = null;
        try {
            br2 = new BufferedReader(new FileReader(inFile2));
            String line;
            while((line = br2.readLine()) != null) {
                //한 라인 씩 " " 으로 스플릿 하고, 그렇게 나눈 스트링을
                //node로 만들어서 node 배열에 삽입
                finishedList.add(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        // Populate our list with groups and it's children
        GroupItem finished = new GroupItem();
        finished.title = "완료된 프로젝트";
        for (int i=0;i<finishedList.size();i++){
            ChildItem child = new ChildItem();
            child.title = finishedList.get(i);

            finished.items.add(child);
        }
        items.add(finished);

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        mainExpandableListView = (AnimatedExpandableListView) findViewById(R.id.mainExpandableListView);
        mainExpandableListView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        mainExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.

                if (mainExpandableListView.isGroupExpanded(groupPosition)) {
                    mainExpandableListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    if (groupPosition == 1){
                        if (mainExpandableListView.isGroupExpanded(0)){
                            mainExpandableListView.collapseGroupWithAnimation(0);
                        }
                    }else{
                        if (mainExpandableListView.isGroupExpanded(1)){
                            mainExpandableListView.collapseGroupWithAnimation(1);
                        }
                    }
                    mainExpandableListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        mainExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (i==0 && i1 ==processingList.size()){
                    Intent intent = new Intent(getApplicationContext(), AddProjectActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_ADD_PROJECT);
                }else if(i == 0){
                    Intent intent = new Intent(getApplicationContext(), ManageProjectActivity.class);
                    intent.putExtra("index",processingList.get(i1));
                    startActivityForResult(intent,REQUEST_CODE_MANAGE_PROJECT);
                }else{
                    Intent intent = new Intent(getApplicationContext(), ManageProjectActivity.class);
                    intent.putExtra("index",finishedList.get(i1));
                    startActivityForResult(intent,REQUEST_CODE_MANAGE_PROJECT);
                }
                return true;
            }
        });

        final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);

        ad.setTitle("삭제하시겠습니까?");

        ad.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                try{
                    File fos_delete = new File(dirPath + "processing.txt");
                    String temp="";
                    for (int j=0; j<processingList.size();j++){
                        if (deleteIndex-1 != j) {
                            if (j == processingList.size()-1) temp += processingList.get(j);
                            else temp += processingList.get(j) + "\n";
                        }
                    }
                    FileOutputStream fos_processing = new FileOutputStream(fos_delete);
                    fos_processing.write(temp.getBytes());
                    fos_processing.close();
                    Thread.sleep(1000);

                    onResume();
                }catch (Exception e){
                    e.printStackTrace();
                }
                dialogInterface.dismiss();
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        mainExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i-1 < processingList.size()){
                    deleteIndex = i;
                    ad.show();
                }
                return true;
            }
        });

        generateButton = (Button)findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PortfolioGenerateActivity.class);
                intent.putStringArrayListExtra("finishedList",finishedList);
                startActivityForResult(intent,REQUEST_CODE_GENERATE);
            }
        });

        profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivityForResult(intent,REQUEST_CODE_USERINFO);
            }
        });
    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
    }

    private static class ChildHolder {
        TextView title;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.main_item_child, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textChild);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.main_item_group, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textGroup);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
        startActivityForResult(intent,REQUEST_CODE_USERINFO);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_ADD_PROJECT){
                if (data != null){
                    AddProjectData addProjectData = (AddProjectData)data.getSerializableExtra("AddProjectData");

                    try{
                        String projectInfo = addProjectData.getProjectName()+"\n"+addProjectData.getHost()+"\n"+addProjectData.getStartDate()+"\n"+addProjectData.getFinishDate();
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(addProjectData.getPoster()));
                        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/podal/";
                        String folder_name = "/"+addProjectData.projectName+"/";
                        String file_name = addProjectData.projectName+"_poster.jpg";
                        String string_path = ex_storage + folder_name;

                        File file_path;
                        file_path = new File(string_path);
                        if (!file_path.isDirectory()){
                            file_path.mkdirs();
                        }
                        FileOutputStream outputStream = new FileOutputStream(string_path+file_name);
                        bm.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        outputStream.close();

                        FileOutputStream fos_projectInfoStream = new FileOutputStream(string_path+"projectInfo.txt");
                        fos_projectInfoStream.write(projectInfo.getBytes());
                        fos_projectInfoStream.close();

                        String str = "";
                        FileOutputStream fos_ItemListStream = new FileOutputStream(string_path+"itemList.txt");
                        fos_ItemListStream.write(str.getBytes());
                        fos_ItemListStream.close();

                        File fos_process = new File(dirPath + "processing.txt");
                        String temp="";
                        for (int i=0; i<processingList.size();i++){
                            temp += processingList.get(i) + "\n";
                        }
                        String Str = temp+addProjectData.getProjectName();
                        FileOutputStream fos_processing = new FileOutputStream(fos_process);
                        fos_processing.write(Str.getBytes());
                        fos_processing.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            else if (requestCode == REQUEST_CODE_MANAGE_PROJECT){
                if (data != null){
                    if (data.hasExtra("projectName")){
                        String tempProjectName = data.getStringExtra("projectName");

                        try{
                            File fos_process = new File(dirPath + "processing.txt");
                            String temp="";
                            for (int i=0; i<processingList.size();i++){
                                if (!processingList.get(i).equals(tempProjectName)){
                                    temp += processingList.get(i) + "\n";
                                }
                            }
                            FileOutputStream fos_processing = new FileOutputStream(fos_process);
                            fos_processing.write(temp.getBytes());
                            fos_processing.close();

                            File fos_finish = new File(dirPath + "finished.txt");
                            String temp2="";
                            for (int i=0; i<finishedList.size();i++){
                                if (!finishedList.get(i).equals(tempProjectName)){
                                    temp2 += finishedList.get(i) + "\n";
                                }
                            }
                            String Str = temp2+tempProjectName;
                            FileOutputStream fos_finished = new FileOutputStream(fos_finish);
                            fos_finished.write(Str.getBytes());
                            fos_finished.close();
                            onResume();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<GroupItem> items = new ArrayList<GroupItem>();
        getWindow().setStatusBarColor(Color.BLACK);
        deleteIndex = 0;

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

/*
        abar = this.getSupportActionBar();
        abar.show();
        abar.setTitle("포 달");
*/

        backPressCloseHandler = new BackPressCloseHandler(this);

        File file = new File(dirPath);

        if (!file.exists()) {
            file.mkdirs();
            String testStr = "";
            try {
                File fos_finish = new File(dirPath + "finished.txt");
                File fos_process = new File(dirPath + "processing.txt");
                FileOutputStream fos_finished = new FileOutputStream(fos_finish);
                FileOutputStream fos_processing = new FileOutputStream(fos_process);
                fos_finished.write(testStr.getBytes());
                fos_finished.close();
                fos_processing.write(testStr.getBytes());
                fos_processing.close();
            } catch (IOException e) {
            }
        }

        File inFile = new File(dirPath + "processing.txt");


        processingList = new ArrayList<String>();
        finishedList = new ArrayList<String>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile));
            String line;
            while ((line = br.readLine()) != null) {
                //한 라인 씩 " " 으로 스플릿 하고, 그렇게 나눈 스트링을
                //node로 만들어서 node 배열에 삽입
                processingList.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
            }
        }

        // Populate our list with groups and it's children
        final GroupItem processing = new GroupItem();
        processing.title = "진행중인 프로젝트";
        for (int i = 0; i < processingList.size(); i++) {
            ChildItem child = new ChildItem();
            child.title = processingList.get(i);

            processing.items.add(child);
        }
        ChildItem addButton = new ChildItem();
        addButton.title = "+";
        processing.items.add(addButton);
        items.add(processing);

        File inFile2 = new File(dirPath + "finished.txt");

        // 파일 읽기
        BufferedReader br2 = null;
        try {
            br2 = new BufferedReader(new FileReader(inFile2));
            String line;
            while ((line = br2.readLine()) != null) {
                //한 라인 씩 " " 으로 스플릿 하고, 그렇게 나눈 스트링을
                //node로 만들어서 node 배열에 삽입
                finishedList.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
            }
        }

        // Populate our list with groups and it's children
        final GroupItem finished = new GroupItem();
        finished.title = "완료된 프로젝트";
        for (int i = 0; i < finishedList.size(); i++) {
            ChildItem child = new ChildItem();
            child.title = finishedList.get(i);

            finished.items.add(child);
        }
        items.add(finished);

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        mainExpandableListView = (AnimatedExpandableListView) findViewById(R.id.mainExpandableListView);
        mainExpandableListView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        mainExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.

                if (mainExpandableListView.isGroupExpanded(groupPosition)) {
                    mainExpandableListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    if (groupPosition == 1) {
                        if (mainExpandableListView.isGroupExpanded(0)) {
                            mainExpandableListView.collapseGroupWithAnimation(0);
                        }
                    } else {
                        if (mainExpandableListView.isGroupExpanded(1)) {
                            mainExpandableListView.collapseGroupWithAnimation(1);
                        }
                    }
                    mainExpandableListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        mainExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (i==0 && i1 ==processingList.size()){
                    Intent intent = new Intent(getApplicationContext(), AddProjectActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_ADD_PROJECT);
                }else if(i == 0){
                    Intent intent = new Intent(getApplicationContext(), ManageProjectActivity.class);
                    intent.putExtra("index",processingList.get(i1));
                    startActivityForResult(intent,REQUEST_CODE_MANAGE_PROJECT);
                }else{
                    Intent intent = new Intent(getApplicationContext(), ManageProjectActivity.class);
                    intent.putExtra("index",finishedList.get(i1));
                    startActivityForResult(intent,REQUEST_CODE_MANAGE_PROJECT);
                }
                return true;
            }
        });

        final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);

        ad.setTitle("삭제하시겠습니까?");

        ad.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                try{
                    File fos_delete = new File(dirPath + "processing.txt");
                    String temp="";
                    for (int j=0; j<processingList.size();j++){
                        if (deleteIndex-1 != j) {
                            if (j == processingList.size()-1) temp += processingList.get(j);
                            else temp += processingList.get(j) + "\n";
                        }
                    }
                    FileOutputStream fos_processing = new FileOutputStream(fos_delete);
                    fos_processing.write(temp.getBytes());
                    fos_processing.close();
                    Thread.sleep(1000);

                    onResume();
                }catch (Exception e){
                    e.printStackTrace();
                }

                dialogInterface.dismiss();
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mainExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i-1 < processingList.size()){
                    deleteIndex = i;
                    ad.show();
                }
                return true;
            }
        });

        generateButton = (Button) findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PortfolioGenerateActivity.class);
                intent.putStringArrayListExtra("finishedList",finishedList);
                startActivityForResult(intent,REQUEST_CODE_GENERATE);
            }
        });

        profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivityForResult(intent,REQUEST_CODE_USERINFO);
            }
        });
    }
}
