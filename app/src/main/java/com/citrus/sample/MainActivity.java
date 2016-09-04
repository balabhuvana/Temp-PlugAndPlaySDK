package com.citrus.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citrus.sdk.ui.activities.CitrusUIActivity;
import com.citrus.sdk.ui.fragments.ResultFragment;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.ResultModel;
import com.orhanobut.logger.Logger;



public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String returnUrlLoadMoney = "https://salty-plateau-1529.herokuapp" +
            ".com/redirectUrlLoadCash.php";
    public static final String TAG = "MainActivity";
    private static final long MENU_DELAY = 300;
    public static String dummyMobile = "9769507476";
    public static String dummyEmail = "developercitrus@mailinator.com";
    public static String dummyAmount = "5";
    private Button logoutBtn;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("settings", MODE_PRIVATE);
        logoutBtn = (Button) findViewById(R.id.logout_button);
        if (settings.getBoolean("is_prod_env", false)) {
            ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
        } else {
            ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
        }
        setupCitrusConfigs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        try {
            if (CitrusClient.getInstance(this) != null) {
                CitrusClient.getInstance(this).isUserSignedIn(new Callback<Boolean>() {
                    @Override
                    public void success(Boolean aBoolean) {
                        if (aBoolean) {
                            logoutBtn.setVisibility(View.VISIBLE);
                        } else {
                            logoutBtn.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void error(CitrusError citrusError) {

                    }
                });
            }
        } catch (Exception e) {
            Logger.e(e, "");
            logoutBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.env_menu, menu);
        boolean isProdEnv = settings.getBoolean("is_prod_env", false);
        MenuItem sandbox_item = menu.findItem(R.id.env_sandbox);
        MenuItem prod_item = menu.findItem(R.id.env_prod);
        if (isProdEnv) {
            prod_item.setChecked(true);
        } else {
            sandbox_item.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.env_prod:
                setItem(item);
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
                        editor = settings.edit();
                        editor.putBoolean("is_prod_env", true);
                        editor.apply();
                        logoutBtn.setVisibility(View.GONE);
                        CitrusClient.getInstance(MainActivity.this).signOut(new Callback<CitrusResponse>() {
                            @Override
                            public void success(CitrusResponse citrusResponse) {

                            }

                            @Override
                            public void error(CitrusError error) {

                            }
                        });
                        CitrusClient.getInstance(MainActivity.this).destroy();
                        setupCitrusConfigs();
                    }
                }, MENU_DELAY);
                return true;
            case R.id.env_sandbox:
                setItem(item);
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
                        editor = settings.edit();
                        editor.putBoolean("is_prod_env", false);
                        editor.apply();
                        logoutBtn.setVisibility(View.GONE);
                        CitrusClient.getInstance(MainActivity.this).signOut(new Callback<CitrusResponse>() {
                            @Override
                            public void success(CitrusResponse citrusResponse) {

                            }

                            @Override
                            public void error(CitrusError error) {

                            }
                        });
                        CitrusClient.getInstance(MainActivity.this).destroy();
                        setupCitrusConfigs();
                    }
                }, MENU_DELAY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setItem(MenuItem item) {
        if (item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);
    }


    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        if (appEnvironment == AppEnvironment.PRODUCTION) {
            Toast.makeText(MainActivity.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }
        CitrusFlowManager.initCitrusConfig(appEnvironment.getSignUpId(),
                appEnvironment.getSignUpSecret(), appEnvironment.getSignInId(),
                appEnvironment.getSignInSecret(), ContextCompat.getColor(this, R.color.white),
                MainActivity.this, appEnvironment.getEnvironment(), appEnvironment.getVanity(), appEnvironment.getBillUrl(),
                returnUrlLoadMoney, true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == CitrusFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            // You will get data here if transaction flow is started through pay options other than wallet
            TransactionResponse transactionResponse = data.getParcelableExtra(CitrusUIActivity
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
            // You will get data here if transaction flow is started through wallet
            ResultModel resultModel = data.getParcelableExtra(ResultFragment.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getJsonResponse() != null) {
                // Decide what to do with this data
                Log.d(TAG, "transaction response" + transactionResponse.getJsonResponse());
            } else if (resultModel != null && resultModel.getTransactionResponse() != null) {
                // Decide what to do with this data
                Log.d(TAG, "result response" + resultModel.getTransactionResponse().getTransactionId());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick_pay:
                /*CitrusFlowManager.startShoppingFlow(MainActivity.this,
                        dummyEmail, dummyMobile, dummyAmount, false);*/
                CitrusFlowManager.startShoppingFlowStyle(MainActivity.this,
                        dummyEmail, dummyMobile, dummyAmount, R.style.AppTheme_blue, false);
                break;
            case R.id.custom_button:
                startActivity(new Intent(MainActivity.this, CustomDetailsActivity.class));
                break;
            case R.id.wallet_button:
                CitrusFlowManager.startWalletFlow(MainActivity.this, dummyEmail, dummyMobile);
                break;
            case R.id.pink:
                CitrusFlowManager.startShoppingFlowStyle(MainActivity.this,
                        dummyEmail, dummyMobile, dummyAmount, R.style.AppTheme_pink, true);
                Toast.makeText(MainActivity.this, "Result Screen will Override", Toast.LENGTH_SHORT).show();
                break;
            case R.id.blue:
                CitrusFlowManager.startShoppingFlowStyle(MainActivity.this,
                        dummyEmail, dummyMobile, dummyAmount, R.style.AppTheme_blue, false);
                break;
            case R.id.green:
                CitrusFlowManager.startShoppingFlowStyle(MainActivity.this,
                        dummyEmail, dummyMobile, dummyAmount, R.style
                                .AppTheme_Green, false);
                break;
            case R.id.purple:
                CitrusFlowManager.startShoppingFlowStyle(MainActivity.this,
                        dummyEmail, dummyMobile, dummyAmount, R.style
                                .AppTheme_purple, false);
                break;
            case R.id.logout_button:
                CitrusFlowManager.logoutUser(MainActivity.this);
                logoutBtn.setVisibility(View.GONE);
                break;
        }
    }
}
