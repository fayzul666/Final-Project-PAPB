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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etEmail, etPass;
    ProgressBar progressBar;
    TextView btnLogin;
    Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        progressBar = findViewById(R.id.pbRegister);
        btnLogin = findViewById(R.id.tvLoginBtn);
        btRegister = findViewById(R.id.btRegister);
        
        btRegister.setOnClickListener(v -> createAccount());
        btnLogin.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));

    }

    private void createAccount() {
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        boolean isValidate = validateData(email, password);
        if (!isValidate) {
            return;
        }
        
        createAccountInFirebase(email, password);
    }

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            //jika account selesai dibuat
                            Toast.makeText(CreateAccountActivity.this, "Succesfully create account", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            //jika account gagal dibuat
                            Toast.makeText(CreateAccountActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btRegister.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btRegister.setVisibility(View.VISIBLE);
        }

    }

    private boolean validateData(String email, String pass) {

        // validasi format data yang diinput user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email is invalid");
            return false;
        }
        if (pass.length() < 6) {
            etPass.setError("Password length is invalid");
            return false;
        }

        return true;
    }


}