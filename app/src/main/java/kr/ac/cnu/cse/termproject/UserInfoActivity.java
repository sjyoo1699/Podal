package kr.ac.cnu.cse.termproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UserInfoActivity extends Activity {
    /*ActionBar abar;*/
    Button addUserInfoButton, addFaceButton;
    /*TextView faceAddText;*/
    ImageView faceImage;

    EditText name, birth, university, major, phone, address_Email, address_Blog ;
    String birth_Data;

    ArrayList<String> userInfoArr;

    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/podal/";
    static final int REQUEST_CODE_FACE = 102;

    Uri temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getWindow().setStatusBarColor(Color.BLACK);
        /*
        abar = this.getSupportActionBar();
        abar.show();
        abar.setTitle("유저정보");
*/
        addUserInfoButton = (Button)findViewById(R.id.addUserInfoButton);
        addFaceButton = (Button) findViewById(R.id.faceAddButton);
        /*faceAddText = (TextView)findViewById(R.id.faceAddText);
        */
        faceImage = (ImageView)findViewById(R.id.faceImage);

        name = (EditText)findViewById(R.id.name);
        birth = (EditText)findViewById(R.id.birth);
        university = (EditText)findViewById(R.id.university);
        major = (EditText)findViewById(R.id.major);
        phone = (EditText)findViewById(R.id.phone);
        address_Email = (EditText)findViewById(R.id.address_Email);
        address_Blog = (EditText)findViewById(R.id.address_Blog);

        File inFile = new File(dirPath+"UserInfo.txt");

        userInfoArr = new ArrayList<String>();

        // 파일 읽기
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile));
            String line;
            while((line = br.readLine()) != null) {
                //한 라인 씩 " " 으로 스플릿 하고, 그렇게 나눈 스트링을
                //node로 만들어서 node 배열에 삽입
                userInfoArr.add(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) try {br.close(); } catch(IOException e) {}
        }

        if(!(userInfoArr.size() == 0)) {
            if (!userInfoArr.get(0).isEmpty()) {
                name.setText(userInfoArr.get(0));
            }
            if (!userInfoArr.get(1).isEmpty()) {
                birth_Data = userInfoArr.get(1);
                birth.setText(birth_Data);
            }
            if (!userInfoArr.get(2).isEmpty()) {
                university.setText(userInfoArr.get(2));
            }
            if (!userInfoArr.get(3).isEmpty()) {
                major.setText(userInfoArr.get(3));
            }
            if (!userInfoArr.get(4).isEmpty()) {
                phone.setText(userInfoArr.get(4));
            }
            if (!userInfoArr.get(5).isEmpty()) {
                address_Email.setText(userInfoArr.get(5));
            }
            if (!userInfoArr.get(6).isEmpty()){
                temp = Uri.parse(userInfoArr.get(6));
                faceImage.setImageURI(temp);
            }
            if (userInfoArr.size() == 8) {
                address_Blog.setText(userInfoArr.get(7));
            }
        }

        addFaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                i.setType("image/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(i.createChooser(i,"Open"),REQUEST_CODE_FACE);
            }
        });

        addUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("")||birth_Data.equals("")||university.getText().toString().equals("")|| major.getText().toString().equals("")|| phone.getText().toString().equals("")|| address_Email.getText().toString().equals("")|| address_Blog.getText().toString().equals("")|| temp==null){
                    Toast.makeText(getApplicationContext(),"공란이 있습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();

                    UserInfoData UserInfoData = new UserInfoData(name.getText().toString(), birth_Data, university.getText().toString(), major.getText().toString(), phone.getText().toString(), address_Email.getText().toString(), address_Blog.getText().toString(), temp.toString());

                    setResult(RESULT_OK, intent);

                    try{
                        String userInfo = UserInfoData.getName() + "\n" +
                                UserInfoData.getBirth() + "\n" +
                                UserInfoData.getUniversity() + "\n" +
                                UserInfoData.getMajor() + "\n" +
                                UserInfoData.getPhone() + "\n" +
                                UserInfoData.getAddress_email() + "\n" +
                                UserInfoData.getUri() + "\n"+
                                UserInfoData.getAddress_blog();
                        FileOutputStream fos_UserInfo = new FileOutputStream(dirPath + "UserInfo.txt");
                        fos_UserInfo.write(userInfo.getBytes());
                        fos_UserInfo.close();

                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(UserInfoData.getUri()));
                        File file_path;
                        file_path = new File(dirPath+"UserInfo_image.jpg");
                        FileOutputStream outputStream = new FileOutputStream(file_path);
                        bm.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        outputStream.close();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });

        Calendar c = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(UserInfoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                int month = i1+1;
                birth.setText( i +"년 " + month +"월 " + i2+"일");
                birth_Data = i +"년 " + month +"월 " + i2+"일";
            }
        }, c.get(Calendar.YEAR)-24,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        birth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_FACE){
                if (data != null){
                    Bundle bundle = data.getExtras();
                    String path = data.getData().getPath();
                    String name = data.getData().getLastPathSegment();
                    Uri uri = data.getData();
                    temp = uri;
                    faceImage.setImageURI(uri);
                    String[] arr = getPathFromUri(this,uri).split("/");
                   /* faceAddText.setText(arr[arr.length-1]);*/
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