package kr.ac.cnu.cse.termproject;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemActivity extends Activity {
    /*ActionBar abar;*/
    Button itemAddButton, fileAddButton;
    ImageView addItemImage;
    TextView fileAddText;
    EditText addMemoEditText, addItemNameEditText;

    Uri imageItem = null;
    Uri fileItem = null;

    static final int REQUEST_CODE_IMAGE = 110;
    static final int REQUEST_CODE_FILE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getWindow().setStatusBarColor(Color.BLACK);
        /*abar = this.getSupportActionBar();
        abar.show();
        abar.setTitle("아이템 추가");
*/
        itemAddButton = (Button)findViewById(R.id.itemAddButton);
        fileAddButton = (Button)findViewById(R.id.fileAddButton);
        addItemImage = (ImageView)findViewById(R.id.addItemImage);
        fileAddText = (TextView)findViewById(R.id.fileAddText);
        addMemoEditText = (EditText)findViewById(R.id.addMemoEditText);
        addItemNameEditText = (EditText)findViewById(R.id.addItemNameEditText);

        itemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                if (addItemNameEditText.getText().toString().equals("")){
                    intent.putExtra("name", "없음");
                }else{
                    intent.putExtra("name", addItemNameEditText.getText().toString());
                }

                if (addMemoEditText.getText().toString().equals("")){
                    intent.putExtra("memo","없음");
                }else{
                    intent.putExtra("memo",addMemoEditText.getText().toString());
                }

                if (imageItem == null){
                    intent.putExtra("image","없음");
                }else{
                    intent.putExtra("image",imageItem.toString());
                }

                if (fileItem == null){
                    intent.putExtra("file","없음");
                }else{
                    intent.putExtra("file",fileItem.toString());
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        fileAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                i.setType("*/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(i.createChooser(i,"Open"),REQUEST_CODE_FILE);
            }
        });

        addItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                i.setType("image/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(i.createChooser(i,"Open"),REQUEST_CODE_IMAGE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_IMAGE){
                if (data != null){

                    Uri uri = data.getData();
                    imageItem = uri;

                    addItemImage.setImageURI(uri);
                }
            }

            else if (requestCode == REQUEST_CODE_FILE){
                if (data != null){
                    Uri uri = data.getData();

                    String[] temp = getPathFromUri(this, uri).split("/");
                    fileAddText.setText(temp[temp.length-1]);
                    fileItem = uri;
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
