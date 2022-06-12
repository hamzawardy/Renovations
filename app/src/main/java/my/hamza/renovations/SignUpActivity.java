package my.hamza.renovations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etfullname,etphone,etEmail,etPassword,etRepassword;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etfullname=findViewById(R.id.etfullname);
        etphone=findViewById(R.id.etphone);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etRepassword=findViewById(R.id.etRepassword);
        btnSave=findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renovationUp();
            }
        });

    }

    private void renovationUp() {

        boolean isOk=true;//if all the fields filled well
        String fname=etfullname.getText().toString();
        String phone=etphone.getText().toString();
        String email=etEmail.getText().toString();
        String passw1=etPassword.getText().toString();
        String passw2=etRepassword.getText().toString();

        if(email.length()<4 || email.indexOf('@')<0 || email.indexOf('.')<0)
        {
            etEmail.setError("Wrong Eamil");
            isOk=false;
        }
        if(passw1.length()<8 || passw1.equals(passw2)==false)
        {
            etPassword.setError("Have to be at least 8 char and the same password");
            etRepassword.setError("Have to be at least 8 char and the same password");
            isOk=false;
        }
        if(fname.length()==0)
        {
            etfullname.setError("enter name");
            isOk=false;
        }
        if(phone.length()==0||phone.length()<8)
        {
            etphone.setError("enter your phone");
            isOk=false;
        }
        if(isOk)
        {
            createAcount(fname,phone,email,passw1);
        }
    }

    private void createAcount(String fname, String phone, String email, String passw1) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,passw1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Sign Up successful",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SignUpActivity.this,CommunityActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    etEmail.setError("Sign Up failed");
                }
            }
        });

    }
}