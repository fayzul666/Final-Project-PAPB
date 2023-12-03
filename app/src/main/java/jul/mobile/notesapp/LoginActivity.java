package jul.mobile.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    EditText etEmailLogin, etPassLogin;
    ProgressBar pbLogin;
    TextView tvRegist;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPassLogin = findViewById(R.id.etPassLogin);
        pbLogin = findViewById(R.id.pbLogin);
        tvRegist = findViewById(R.id.tvRegistBtn);
        btLogin = findViewById(R.id.btLogin);
        
        btLogin.setOnClickListener((v) -> loginUser());
        tvRegist.setOnClickListener((v) ->
                startActivity(new Intent
                        (LoginActivity.this, CreateAccountActivity.class)));

    }

    private void loginUser() {
        String email = etEmailLogin.getText().toString();
        String password = etPassLogin.getText().toString();

        boolean isValidate = validateData(email, password);
        if (!isValidate) {
            return;
        }

        loginAccountInFirebase(email, password);
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //login success
                            Toast.makeText(LoginActivity.this,
                                    "Login successful",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            //login failed
                            Toast.makeText(LoginActivity.this,
                                    task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            pbLogin.setVisibility(View.VISIBLE);
            btLogin.setVisibility(View.GONE);
        } else {
            pbLogin.setVisibility(View.GONE);
            btLogin.setVisibility(View.VISIBLE);
        }

    }

    private boolean validateData(String email, String pass) {

        // validasi format data yang diinput user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailLogin.setError("Email is invalid");
            return false;
        }
        if (pass.length() < 6) {
            etPassLogin.setError("Password length is invalid");
            return false;
        }

        return true;
    }
}