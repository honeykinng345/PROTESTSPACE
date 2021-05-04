package protestspacec.om;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

public class Helper {

    public static final int BP_REQUEST_CODE = 103;
    public static void getFileFromStorage(Activity context, int requestCode){
        Dexter.withContext(context).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                switch (requestCode){

                    case BP_REQUEST_CODE:
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        if (context instanceof Activity) {
                            ((Activity) context).startActivityForResult(intent,requestCode);
                        }
                     /*   context.startactivityfo;
                        fragment.startActivityForResult(intent, requestCode);*/
                        break;


                }
                /* */
            }


            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).withErrorListener(dexterError -> {
            Toast.makeText(context, dexterError.toString(), Toast.LENGTH_SHORT).show();
        }).check();
    }

    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String fetchUserId(Context context) {
        SQLiteHandler sqLiteHandler;
        sqLiteHandler = new SQLiteHandler(context);
        Cursor res = sqLiteHandler.getAllData();
        String name= null;
        String email = null;
        while (res.moveToNext()) {
            name = res.getString(1);
            //email = res.getString(2);
        }
        return name;
    }


 public static void makemeOffline(Context context) {
     ProgressDialog progressDialog;
     FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Logging Out...");
        progressDialog.show();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("online","false");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //update Succfuull
                progressDialog.dismiss();

                firebaseAuth.signOut();


                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null){
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity) context).finish();

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //failed  to update

                progressDialog.dismiss();
                Toast.makeText(context,""+e.toString(),Toast.LENGTH_SHORT).show();




            }
        });




    }

}
