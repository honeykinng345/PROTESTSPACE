package protestspacec.om;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.emailET)
    EditText emailET;
    @BindView(R.id.password)
    EditText password;
    SQLiteHandler myDbAdapter;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

/*        if (firebaseUser == null){
            startActivity(new Intent(LoginActivity.this, LoginActivity.class));

        }else{
            //user Is Logerr In// Check User Type
           CheckUserType();


        }*/
        myDbAdapter = new SQLiteHandler(LoginActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ");
        preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = preferences.edit();


    }


    @OnClick({R.id.loginBtn, R.id.noAccountTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                logUser();
                break;
            case R.id.noAccountTv:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
        }
    }

    private void logUser() {

        String email, password1;

        email = emailET.getText().toString().trim();
        password1 = password.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Inavlid Email", Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "Password Feild Empty", Toast.LENGTH_SHORT).show();
        }


        progressDialog.setMessage("Logging In");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                //sussec full login

                makeMeOnline();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(LoginActivity.this, "" + e, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void makeMeOnline() {

        progressDialog.setMessage("Checking User...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "true");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //update Succfuull

                CheckUserType();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //failed  to update

                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + e, Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void CheckUserType() {


        //if user is seller start saller screen
        //if user is buyer start buyer screen

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String accountType = "" + ds.child("accountType").getValue();
                    String userEmail = "" + ds.child("email").getValue();
                    String name = "" + ds.child("name").getValue();

                    boolean id = myDbAdapter.insertData(name, userEmail);
                    if (id) {
                        Helper.message(LoginActivity.this, "Done" + id);

                    } else {
                        Helper.message(LoginActivity.this, "no" + id);

                    }

                    if (accountType.equals("User")) {

                        //user is seller

                        progressDialog.dismiss();
                        editor.putString("AccountType", "User");
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();


                    } else {
                        //user is buyer
                        progressDialog.dismiss();
                        editor.putString("AccountType", "Lawyer");
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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