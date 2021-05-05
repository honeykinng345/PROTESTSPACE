package protestspacec.om;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPostActivity extends AppCompatActivity {

    @BindView(R.id.postTitle)
    EditText postTitle;
    @BindView(R.id.postImage1)
    ImageView postImage1;
    @BindView(R.id.postDescription)
    EditText postDescription;
    //image pick uri
    Uri image_uri;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ");
    }


    @OnClick({R.id.RbackBtn, R.id.UploadPostBtn,R.id.postImage1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.RbackBtn:
                onBackPressed();
                break;
            case R.id.UploadPostBtn:

                inputdata();
                break;

            case R.id.postImage1:
                Helper.getFileFromStorage(AddPostActivity.this, Helper.BP_REQUEST_CODE);
                break;
        }
    }
String  title , description;
    private void inputdata() {

         title= postTitle.getText().toString().trim();
       description= postDescription.getText().toString().trim();


        if (TextUtils.isEmpty(title)){
            Toast.makeText(AddPostActivity.this,"Feild Empty",Toast.LENGTH_SHORT).show();


            return;
        }
        if (TextUtils.isEmpty(description)){
            Toast.makeText(AddPostActivity.this,"Feild Empty",Toast.LENGTH_SHORT).show();
            return;
        }
      AddPost();

    }

    private void AddPost() {

        progressDialog.setMessage("Product Added Please wait ");
        progressDialog.show();


        final String timeStamp = ""+System.currentTimeMillis();

        if (image_uri==null){

            //save info without Imge

            //setup date to sve

            HashMap<String,Object> hashMap = new HashMap<>();

            hashMap.put("postid",""+timeStamp);
            hashMap.put("posttTitle",""+title);
            hashMap.put("postDescription",""+description);
            hashMap.put("name",""+Helper.fetchUserId(AddPostActivity.this));

            hashMap.put("postImage","");

            hashMap.put("timeStamp",""+timeStamp);
            hashMap.put("uid",""+firebaseAuth.getUid());

            //save to db

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(aVoid -> {


                progressDialog.dismiss();
                Toast.makeText(AddPostActivity.this,"Infromation Is Updated Succful",Toast.LENGTH_SHORT).show();
                clearData();


            }).addOnFailureListener(e -> {

                //failed Updating Db
                progressDialog.dismiss();
                Toast.makeText(AddPostActivity.this,""+e,Toast.LENGTH_SHORT).show();
                clearData();

            });

        }else {
//save info With Image


            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Uploads");
            StorageReference fileReference = storageReference.child(timeStamp);

            fileReference.putFile(image_uri).addOnSuccessListener(taskSnapshot ->
                    Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                //addRoomToDB(new Room(id, beds, wifi, rent, status, uri.toString()));
                                //saving video url
                                //Prefrence.saveVideoLink(uri.toString(), getContext());

                             //   teacher.setVideo(uri.toString());
                                sendApplicationData(uri.toString());
                               progressDialog.dismiss();
                                //pd.dismiss();
                                //Helper.Toast(getContext(), uri.toString());
                            }))
                    .addOnFailureListener(e -> Helper.message(AddPostActivity.this, e.toString()))
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploading " + (int) progress + "%");
                        //Helper.Toast(getContext(), "Uploading... "+ progress + "%");
                    });


        }
    }

    private void sendApplicationData(String imageuri1) {

        final String timeStamp = ""+System.currentTimeMillis();


        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("postid",""+timeStamp);
        hashMap.put("posttTitle",""+title);
        hashMap.put("postDescription",""+description);
        hashMap.put("name",""+Helper.fetchUserId(AddPostActivity.this));

        hashMap.put("postImage",imageuri1);

        hashMap.put("timeStamp",""+timeStamp);
        hashMap.put("uid",""+firebaseAuth.getUid());

        //save to db

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(aVoid -> {

            progressDialog.dismiss();
            Toast.makeText(AddPostActivity.this,"Infromation Is Updated Succful",Toast.LENGTH_SHORT).show();
            clearData();


        }).addOnFailureListener(e -> {

            //failed Updating Db
            progressDialog.dismiss();
            Toast.makeText(AddPostActivity.this,""+e,Toast.LENGTH_SHORT).show();
            clearData();

        });
    }

    private void clearData() {

        postTitle.setText("");
        postDescription.setText("");
        image_uri = null;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case Helper.BP_REQUEST_CODE:
                image_uri = data.getData();
                postImage1.setImageURI(image_uri);


            }
        }

    }
}