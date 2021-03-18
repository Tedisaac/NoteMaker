package com.example.notemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
EditText edname,edemail,edpass,edpass2;
Button reg;
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth =  FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        edname = findViewById(R.id.rgname);
        edemail = findViewById(R.id.rgemail);
        edpass = findViewById(R.id.rgpass);
        edpass2 = findViewById(R.id.rgpass2);
        reg = findViewById(R.id.reg);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edname.getText().toString().trim();
                String email = edemail.getText().toString().trim();
                String password = edpass.getText().toString().trim();
                String confirmpassword = edpass2.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(RegisterActivity.this,"PLease input name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this,"PLease input email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this,"PLease input password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmpassword)){
                    Toast.makeText(RegisterActivity.this,"PLease input confirm password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password length should be greater than 6 characters",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals(confirmpassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        Toast.makeText(RegisterActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                //startActivity(new Intent(RegActivity.this,MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}