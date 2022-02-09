package kr.ac.cnu.cse.termproject;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManageProjectActivity extends Activity {
    /*ActionBar abar;*/
    String addTitleName = null;
    Button addItemButton, addTitleButton;
    TextView projcetNameView;
    static final int REQUEST_CODE_ADD_ITEM = 96;
    String projectName;
    RecyclerView itemList;
    TestAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    ItemTouchHelper mItemTouchHelper;
    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/podal/";
    ArrayList<AddItemData> item_List;
    Button cancelProjectManageButton, applyProjectManageButton, finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_project);
        getWindow().setStatusBarColor(Color.BLACK);
        /*

        abar = this.getSupportActionBar();
        abar.show();
        abar.setTitle("프로젝트 관리");

*/
        projectName = getIntent().getStringExtra("index");

        addItemButton = (Button)findViewById(R.id.addItemButton);
        addTitleButton = (Button)findViewById(R.id.addTitleButton);
        projcetNameView = (TextView)findViewById(R.id.projectName);
        cancelProjectManageButton = (Button)findViewById(R.id.cancelProjectManageButton);
        applyProjectManageButton = (Button)findViewById(R.id.applyProjectManageButton);
        finishButton = (Button)findViewById(R.id.finishButton);

        projcetNameView.setText(projectName);

        final AlertDialog.Builder ad = new AlertDialog.Builder(ManageProjectActivity.this);

        ad.setTitle("추가할 소제목을 입력하세요.");

        final EditText et = new EditText(ManageProjectActivity.this);
        ad.setView(et);

        ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = et.getText().toString();

                addTitleName = value;

                try{
                    File fos_itemList = new File(dirPath+projectName+"/itemList.txt");
                    String temp="";
                    for (int j=0; j<mAdapter.items.size();j++){
                        temp += mAdapter.items.get(j).getName()+" "+mAdapter.items.get(j).getClassify()+ "\n";
                    }
                    String Str = temp+addTitleName+ " title";
                    FileOutputStream fos_processing = new FileOutputStream(fos_itemList);
                    fos_processing.write(Str.getBytes());
                    fos_processing.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                initialize();
                dialogInterface.dismiss();
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        addTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD_ITEM);
            }
        });

        cancelProjectManageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        applyProjectManageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    File fos_itemList = new File(dirPath+projectName+"/itemList.txt");
                    String temp="";
                    for (int j=0; j<mAdapter.items.size();j++){
                        temp += mAdapter.items.get(j).getName()+" "+mAdapter.items.get(j).getClassify()+ "\n";
                    }
                    FileOutputStream fos_processing = new FileOutputStream(fos_itemList);
                    fos_processing.write(temp.getBytes());
                    fos_processing.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                finish();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("projectName",projectName);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        mAdapter = new TestAdapter();

        initialize();
    }

    public void initialize(){

        itemList = (RecyclerView)findViewById(R.id.itemList);
        mLayoutManager = new LinearLayoutManager(this);
        itemList.setLayoutManager(mLayoutManager);
        AddItemDataTouchHelperCallBack mCallback = new AddItemDataTouchHelperCallBack(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(itemList);

        File inFile = new File(dirPath+projectName+"/itemList.txt");

        item_List = new ArrayList<AddItemData>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile));
            String line;
            String[] arr;
            while((line = br.readLine()) != null) {
                //한 라인 씩 " " 으로 스플릿 하고, 그렇게 나눈 스트링을
                //node로 만들어서 node 배열에 삽입
                arr = line.split(" ");
                if (arr.length > 2){
                    String temp = arr[0];
                    for (int i=1;i<arr.length-1;i++){
                        temp += " "+arr[i];
                    }
                    item_List.add(new AddItemData(temp,arr[arr.length-1]));
                }else{
                    item_List.add(new AddItemData(arr[0],arr[1]));
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        mAdapter.deleteAll();
        for (int i=0;i<item_List.size();i++){
            mAdapter.add(item_List.get(i));
        }

        itemList.setAdapter(mAdapter);
    }

    public class TestAdapter extends RecyclerView.Adapter<TestViewHolder> implements AddItemDataTouchHelperCallBack.OnItemMoveListener{
        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.manage_item_child, parent, false);
            return new TestViewHolder(v);
        }

        List<AddItemData> items = new ArrayList<>();
        public void add(AddItemData data){
            items.add(data);
            notifyDataSetChanged();
        }

        public void deleteAll(){
            while(!items.isEmpty()){
                items.remove(0);
            }
        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            AddItemData item = items.get(position);
            if (item.getClassify().equals("title")){
                holder.mItemNameText.setText("[ "+item.getName()+" ]");
            }else{
                holder.mItemNameText.setText(item.getName());
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(items,fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_ADD_ITEM){
                if (data != null){
                    try{
                        String name = data.getStringExtra("name");
                        String memo = data.getStringExtra("memo");
                        String image = data.getStringExtra("image");
                        String file =  data.getStringExtra("file");

                        File fos_itemList = new File(dirPath+projectName+"/itemList.txt");
                        String temp="";
                        for (int j=0; j<mAdapter.items.size();j++){
                            temp += mAdapter.items.get(j).getName()+" "+mAdapter.items.get(j).getClassify()+ "\n";
                        }
                        String Str = temp+name+ " item";
                        FileOutputStream fos_processing = new FileOutputStream(fos_itemList);
                        fos_processing.write(Str.getBytes());
                        fos_processing.close();

                        File file_path = new File(dirPath+projectName+"/"+name+"/");
                        if (!file_path.isDirectory()){
                            file_path.mkdirs();
                        }

                        if (!memo.equals("없음")){
                            File itemMemo = new File(dirPath+projectName+"/"+name+"/"+name+"_memo.txt");
                            FileOutputStream fos_itemMemo = new FileOutputStream(itemMemo);
                            fos_itemMemo.write(memo.getBytes());
                            fos_itemMemo.close();
                        }

                        if (!image.equals("없음")){
                            File itemImage = new File(dirPath+projectName+"/"+name+"/"+name+"_image.jpg");
                            FileOutputStream fos_itemImage = new FileOutputStream(itemImage);
                            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image));
                            bm.compress(Bitmap.CompressFormat.JPEG,100,fos_itemImage);
                            fos_itemImage.close();
                        }

                        if (!file.equals("없음")){
                            Uri fileUri = Uri.parse(file);
                            File inFile = new File(getPathFromUri(this,fileUri));
                            String[] arr1 = getPathFromUri(this,fileUri).split("/");
                            String fullFileName = arr1[arr1.length-1];
                            String[] tempSplit = fullFileName.split("\\.");
                            String type = tempSplit[1];
                            RandomAccessFile f = new RandomAccessFile(inFile, "r");
                            byte[] bytes = new byte[(int)f.length()];
                            f.readFully(bytes);
                            File itemFile = new File(dirPath+projectName+"/"+name+"/"+name+"_file."+type);
                            FileOutputStream fos_itemFile = new FileOutputStream(itemFile);
                            fos_itemFile.write(bytes);
                            fos_itemFile.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    initialize();
                }
            }
        }
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
