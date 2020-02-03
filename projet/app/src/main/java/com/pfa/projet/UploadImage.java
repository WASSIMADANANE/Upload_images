package com.pfa.projet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadImage extends AppCompatActivity {
private Button btnChoose,btnUpload;
private ImageView imageView;
private Uri filePath;
private final int PICK_IMAGE_REQUEST=71;
FirebaseStorage storage;
StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
storage=FirebaseStorage.getInstance();
storageReference=storage.getReference();




        btnChoose=(Button)findViewById(R.id.btnChoose);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        imageView=(ImageView)findViewById(R.id.imgView);
btnChoose.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onClick(View view) {
        chooseImage();
    }
});
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });


    }

    private void  uploadImage(){
if(filePath != null)
{
 final ProgressDialog progressDialog=new ProgressDialog(this);
 progressDialog.setTitle("Uploading...");
 progressDialog.show();
 StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
 ref.putFile(filePath)
         .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 progressDialog.dismiss();
                 Toast.makeText(UploadImage.this,"Upload",Toast.LENGTH_SHORT).show();
             }
         })
         .addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 progressDialog.dismiss();
                 Toast.makeText(UploadImage.this,"Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();

             }
         })
         .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                 double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                 progressDialog.setMessage("Uploaded"+(int)progress+"%");

             }
         });



}

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void chooseImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    protected void onActivityResult(int requestCode,int resutCode,Intent data)
    {
        super.onActivityResult(requestCode,resutCode,data);
        if(requestCode==PICK_IMAGE_REQUEST && resutCode == RESULT_OK
        && data != null && data.getData()!= null)
        {
            filePath= data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
