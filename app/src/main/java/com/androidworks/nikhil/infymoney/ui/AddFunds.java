package com.androidworks.nikhil.infymoney.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidworks.nikhil.infymoney.R;
import com.androidworks.nikhil.infymoney.utils.Constants;
import com.androidworks.nikhil.infymoney.utils.DataStore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFunds extends AppCompatActivity {

    @BindView(R.id.add_fund_button)
    Button addFunds;
    @BindView(R.id.add_funds_balance)
    TextView balance;
    @BindView(R.id.add_funds_amount)
    EditText amount;
    @BindView(R.id.add_account)
    TextView account;
    String smsBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_funds);
        ButterKnife.bind(this);
        balance.setText(DataStore.getInstance(this).getBalance());
        account.setText(DataStore.getInstance(this).getAccount());
    }

    @OnClick(R.id.add_fund_button)
    public void addFunds() {
        smsBody = "Mycard " + amount.getText().toString() + " " + DataStore.getInstance(this).getAccount();
        // Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
        // Send a text based SMS
        smsManager.sendTextMessage(Constants.PHONE_NUMBER, null, smsBody, null, null);
        startActivity(new Intent(AddFunds.this, SMSActivity.class));
    }
}
