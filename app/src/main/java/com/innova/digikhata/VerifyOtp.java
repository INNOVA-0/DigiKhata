package com.innova.digikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class VerifyOtp extends AppCompatActivity {

    private String verificationId;
    public ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView number;
    private EditText editText;   // Never use material component on backend for variable binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

            mAuth = FirebaseAuth.getInstance();

            progressBar = findViewById(R.id.progressbar);
            editText = findViewById(R.id.editTextCode);
            number = findViewById(R.id.numberVerify);

            String phonenumber = getIntent().getStringExtra("mobile");

            String displaynumber = phonenumber.substring(3);
            number.setText( displaynumber + "            پر کوڈ بھیجا گیا ہے");

            sendVerificationCode(phonenumber);

            findViewById(R.id.SentButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String code = editText.getText().toString().trim();

                    if (code.isEmpty() || code.length() < 6) {

                        editText.setError("Enter code...");
                        editText.requestFocus();
                        return;
                    }
                    verifyCode(code);
                }
            });

        }

        private void verifyCode(String code) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }

        private void signInWithCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

//                                Intent intent = new Intent(VerifyOtp.this, SplashScreen.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                                startActivity(intent);
                                Toast.makeText(VerifyOtp.this, "OTP Sent", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(VerifyOtp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        private void sendVerificationCode(String number) {

            progressBar.setVisibility(View.VISIBLE);
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(number)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        }

        private PhoneAuthProvider.OnVerificationStateChangedCallbacks
                mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    editText.setText(code);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(VerifyOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }
