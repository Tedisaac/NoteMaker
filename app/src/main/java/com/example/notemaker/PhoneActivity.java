package com.example.notemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {
     Button mSendOTPBtn;
    // TextView processText;
     EditText countryCodeEdit , phoneNumberEdit;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);


        mSendOTPBtn = findViewById(R.id.edverificationcode);
       // processText = findViewById(R.id.text_process);
        countryCodeEdit = findViewById(R.id.edcountry);
        phoneNumberEdit = findViewById(R.id.edphone);

        auth = FirebaseAuth.getInstance();

        mSendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = countryCodeEdit.getText().toString();
                String phone = phoneNumberEdit.getText().toString();
                String phoneNumber = "+" + country_code + "" + phone;
                if (!country_code.isEmpty() || !phone.isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L , TimeUnit.SECONDS)
                            .setActivity(PhoneActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else{
                    Toast.makeText(getApplicationContext(),"Please Enter Country Code and Phone Number ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //sometime the code is not detected automatically
                //so user has to manually enter the code
                Toast.makeText(getApplicationContext(),"OTP has been Sent ",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent otpIntent = new Intent(PhoneActivity.this , OtpActivity.class);
                        otpIntent.putExtra("auth" , s);
                        startActivity(otpIntent);
                    }
                }, 10000);

            }
        };
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user !=null){
            sendToMain();
        }
    }*/
    private void sendToMain(){

       //reference.setValue("First Record");
        Intent mainIntent = new Intent(PhoneActivity.this , NoteActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendToMain();
                }else{
                    Toast.makeText(getApplicationContext(),"Failed ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}