package com.citrus.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.Utils;

public class CustomDetailsActivity extends BaseActivity {

    EditText emailEt;
    EditText mobileEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailEt = (EditText) findViewById(R.id.email_et);
        mobileEt = (EditText) findViewById(R.id.mobile_et);
        findViewById(R.id.quick_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDetails()) {
                    String mobile = mobileEt.getText().toString().trim();
                    String email = emailEt.getText().toString().trim();
                    CitrusFlowManager.startShoppingFlow(CustomDetailsActivity.this, email, mobile, "5", false);
                }
            }
        });
        findViewById(R.id.wallet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDetails()) {
                    String mobile = mobileEt.getText().toString().trim();
                    String email = emailEt.getText().toString().trim();
                    CitrusFlowManager.startWalletFlow(CustomDetailsActivity.this, email, mobile);
                }
            }
        });
    }

    private boolean validateDetails() {
        String mobile = mobileEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Mobile Empty", Toast.LENGTH_SHORT).show();
            mobileEt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email Empty", Toast.LENGTH_SHORT).show();
            emailEt.requestFocus();
            return false;
        } else if (!Utils.validate(email)) {
            Toast.makeText(this, "Email address is not valid", Toast.LENGTH_SHORT).show();
            emailEt.requestFocus();
            return false;
        } else {

            return true;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_custom_details;
    }

}
