package protestspacec.om;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
FirebaseAuth firebaseAuth;
    SQLiteHandler myDbAdapter;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for making full screen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        firebaseAuth = FirebaseAuth.getInstance();
        myDbAdapter = new SQLiteHandler(SplashActivity.this);


        preferences = getApplicationContext().getSharedPreferences("MyPref",0);
        editor = preferences.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser == null){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();

                }else{
                    //user Is Logerr In// Check User Type
                    CheckUserType();


                }


            }
        },1000);

    }

    private void CheckUserType() {


        //if user is seller start saller screen
        //if user is buyer start buyer screen

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String accountType = ""+ds.child("accountType").getValue();
                    String userEmail = ""+ds.child("email").getValue();
                    String name = ""+ds.child("name").getValue();

                    boolean id =  myDbAdapter.insertData(name,userEmail);
                    if (id){
                        Helper.message(SplashActivity.this,"Done"+id);

                    }else{
                        Helper.message(SplashActivity.this,"no"+id);

                    }

                    if (accountType.equals("User")){

                        //user is seller


                        editor.putString("AccountType","User");
                        editor.commit();

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();


                    }else {
                        //user is buyer  editor.putString("AccountType","Lawyer");
                        editor.commit();
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}