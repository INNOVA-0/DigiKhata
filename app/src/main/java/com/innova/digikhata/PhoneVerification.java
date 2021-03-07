package com.innova.digikhata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneVerification extends AppCompatActivity {

    TextView toolbarHeading;
    ImageView logo;
    EditText phoneEditText;
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // making components in toolbar visible
        toolbarHeading = findViewById(R.id.toolbar_heading);
        toolbarHeading.setVisibility(View.VISIBLE);
        toolbarHeading.setText(getText(R.string.app_name));

        logo = findViewById(R.id.toolbar_logo);
        logo.setVisibility(View.VISIBLE);

        phoneEditText = findViewById(R.id.phoneNumber);


        findViewById(R.id.sendOTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = phoneEditText.getText().toString().trim();

                String phoneNumber = "+92" + mobile;

                if(mobile.isEmpty() || mobile.length() < 10){
                    phoneEditText.setError("Enter a valid mobile");
                    phoneEditText.requestFocus();
                    return;
                }

                Intent intent = new Intent(PhoneVerification.this, VerifyOtp.class);
                intent.putExtra("mobile", phoneNumber);
                startActivity(intent);
            }
        });
    }
}