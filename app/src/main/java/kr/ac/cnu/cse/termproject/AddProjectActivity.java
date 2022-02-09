package kr.ac.cnu.cse.termproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AddProjectActivity extends Activity {
    /*ActionBar abar;*/
    Button posterAddButton, addProjectButton;
    TextView posterAddText,startDate,finishDate;
    public static final int REQUEST_CODE_POSTER = 101;
    EditText addProjectName, addHost;

    String projectName,host;
    String start = "";
    String finish = "";
    Uri poster = null;

    private String mImgPath = null;
    private String mImgTitle = null;
    private String mImgOrient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        getWindow().setStatusBarColor(Color.BLACK);
        /*

        abar = this.getSupportActionBar();
        abar.show();
        abar.setTitle("프로젝트 등록");
*/

        posterAddButton = (Button)findViewById(R.id.posterAddButton);
        posterAddText = (TextView)findViewById(R.id.posterAddText);
        addProjectButton = (Button)findViewById(R.id.addProjectButton);
        startDate = (TextView)findViewById(R.id.startDate);
        finishDate = (TextView)findViewById(R.id.finishDate);
        addProjectName = (EditText)findViewById(R.id.addProjectName);
        addHost = (EditText)findViewById(R.id.addHost);

        posterAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                i.setType("image/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(i.createChooser(i,"Open"),REQUEST_CODE_POSTER);
            }
        });

        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addProjectName.getText().toString().equals("")||addHost.getText().toString().equals("")||start.equals("")||finish.equals("")||poster==null){
                    Toast.makeText(getApplicationContext(),"공란이 있습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();

                    AddProjectData data = new AddProjectData(addProjectName.getText().toString(),addHost.getText().toString(),start,finish, poster.toString());
                    intent.putExtra("AddProjectData",data);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        final DatePickerDialog startDialog = new DatePickerDialog(this, startListener, 2017,10,10);
        final DatePickerDialog finishDidalog = new DatePickerDialog(this, finishListener, 2017,10,10);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog.show();
            }
        });

        finishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishDidalog.show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_POSTER){
                if (data != null){
/*
                    getImageNameToUri(uri);
*/

                    Uri uri = data.getData();
                    poster = uri;

                    String[] temp = getPathFromUri(this, uri).split("/");
                    posterAddText.setText(temp[temp.length-1]);
                    /*
                    try{
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                        String folder_name = "/podal/";
                        String file_name = "asd.jpg";
                        String string_path = ex_storage + folder_name;

                        File file_path;
                        file_path = new File(string_path);
                        if (!file_path.isDirectory()){
                            file_path.mkdirs();
                        }
                        FileOutputStream outputStream = new FileOutputStream(string_path+file_name);
                        bm.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        outputStream.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                }
            }
        }
    }

    public String getRealPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null,null,null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    private void getImageNameToUri(Uri data){
        String[] proj = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.ORIENTATION
        };

        Cursor cursor = this.getContentResolver().query(data,proj,null,null,null);
        cursor.moveToFirst();

        int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        int column_orientation = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

        mImgPath = cursor.getString(column_data);
        mImgTitle = cursor.getString(column_title);
        mImgOrient = cursor.getString(column_orientation);
    }

    private DatePickerDialog.OnDateSetListener startListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            int month = i1+1;
            startDate.setText( i +"년 " + month +"월 " + i2+"일");
            start = i +"년 " + month +"월 " + i2+"일";
        }
    };

    private DatePickerDialog.OnDateSetListener finishListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            int month = i1+1;
            finishDate.setText( i +"년 " + month +"월 " + i2+"일");
            finish = i +"년 " + month +"월 " + i2+"일";
        }
    };

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
