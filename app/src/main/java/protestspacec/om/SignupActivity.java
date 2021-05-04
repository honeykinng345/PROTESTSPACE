package protestspacec.om;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @BindView(R.id.nameEt)
    EditText nameEt;
    @BindView(R.id.phoneET)
    EditText phoneET;
    @BindView(R.id.RemailET)
    EditText RemailET;
    @BindView(R.id.Rpassword)
    EditText Rpassword;
    @BindView(R.id.Cpassword)
    EditText Cpassword;
    @BindView(R.id.snackbar_action)
    RelativeLayout snackbarAction;

    RadioGroup radioGroup;
String UserType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        radioGroup = findViewById(R.id.radioGroup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.user:
                        UserType = "User";
                        break;

                    case R.id.lawyer:
                       UserType = "Lawyer";
                        break;
                }
            }
        });



    }


    @OnClick({R.id.RbackBtn, R.id.regsiterBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.RbackBtn:
                onBackPressed();
                break;
            case R.id.regsiterBtn:
                inputData();
                Toast.makeText(this,"Name Feild Is Empty",Toast.LENGTH_SHORT).show();
                break;


        }
    }

    String fullName, Phone, email, password, confrimPassword;

    private void inputData() {
        fullName = nameEt.getText().toString().trim();
        Phone = phoneET.getText().toString().trim();
        email = RemailET.getText().toString().trim();
        password = Rpassword.getText().toString().trim();
        confrimPassword = Cpassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)){
            Toast.makeText(this,"Name Feild Is Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Phone)){
            Toast.makeText(this,"phone Feild Is Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email  Feild Is Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Email not proper Format",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"password Feild Is Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confrimPassword)){
            Toast.makeText(this,"Password Not Match",Toast.LENGTH_SHORT).show();
            return;
        }

        CreateAccount();


    }

    private void CreateAccount() {

        progressDialog.setMessage("Create Account");
        progressDialog.show();

        //create Accountt

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {

            //SaveUserDataa In DatabAse
           SaverFirebaseUser();


        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(SignupActivity.this,"Message"+e.toString(),Toast.LENGTH_SHORT).show();


        });
    }

    private void SaverFirebaseUser() {

        progressDialog.setMessage("Saving Account");


        final String timeStamp = ""+System.currentTimeMillis();



        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("email",""+email);
        hashMap.put("name",""+fullName);
        hashMap.put("phone",""+Phone);
        hashMap.put("timeStamp",""+timeStamp);
        hashMap.put("accountType",""+UserType);
        hashMap.put("online",""+"true");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                progressDialog.dismiss();
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //failed Updating Db
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this,""+e.toString(),Toast.LENGTH_SHORT).show();


            }
        });

    }

}