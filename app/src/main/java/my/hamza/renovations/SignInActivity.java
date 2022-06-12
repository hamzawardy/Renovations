package my.hamza.renovations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText etEmail,etPassword;
    private Button btnSignin,btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null && auth.getCurrentUser().getEmail()!=null)
        {

            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }



        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnSignin=findViewById(R.id.btnSignin);
        btnregister=findViewById(R.id.btnregister);


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(i);
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCheck();  
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null && auth.getCurrentUser().getEmail()!=null)
        {

            Intent intent=new Intent(SignInActivity.this,CommunityActivity.class);
            startActivity(intent);
        }
    }

    private void DataCheck()
    {
        String email=etEmail.getText().toString();
        String passw=etPassword.getText().toString();
        boolean isok=true;
        if(email.length()<4)
        {
            etEmail.setError("Email length error");
            isok=false;
        }
        if(email.indexOf("@")<0 || email.indexOf(".")<0)
        {
            etEmail.setError("email wrong format");
            isok=false;
        }
        if(passw.length()<8)
        {
            etPassword.setError("min length 8");
            isok=false;
        }
        if(isok)
        {
            signIn(email,passw);
        }
    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void signIn(String email, String passw)
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignInActivity.this, CommunityActivity.class);
                    startActivity(intent);
                }
                else{
                    etEmail.setError("email or password is wrong");
                }
            }
        });
    }

}