package com.androidworks.nikhil.infymoney.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidworks.nikhil.infymoney.R;
import com.androidworks.nikhil.infymoney.utils.Constants;
import com.androidworks.nikhil.infymoney.utils.DataStore;
import com.androidworks.nikhil.infymoney.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFundsActivity extends AppCompatActivity {

    @BindView(R.id.add_fund_button)
    Button addFunds;
    @BindView(R.id.add_funds_balance)
    TextView balance;
    @BindView(R.id.add_funds_amount)
    EditText amount;
    @BindView(R.id.add_funds_account)
    TextView account;
    String smsBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_funds);
        ButterKnife.bind(this);
        balance.setText("Balance : " + DataStore.getInstance(this).getBalance());
        account.setText(DataStore.getInstance(this).getAccount());
    }

    @OnClick(R.id.add_fund_button)
    public void addFunds() {
        smsBody = "Mycard " + amount.getText().toString() + " " + DataStore.getInstance(this).getAccount();
        // Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
        // Send a text based SMS
        smsManager.sendTextMessage(Constants.PHONE_NUMBER, null, smsBody, null, null);
        startActivity(new Intent(AddFundsActivity.this, SMSActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_funds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_account:
                askForAccount();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void askForAccount() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = View.inflate(this, R.layout.get_account_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText accountET = (EditText) dialogView.findViewById(R.id.account);

        dialogBuilder.setTitle("Account Number");
        dialogBuilder.setMessage("Please enter the last 6 digits of your associated account number");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (accountET.getText() != null)
                    DataStore.getInstance(AddFundsActivity.this).storeAccount(accountET.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Utils.showToast(AddFundsActivity.this, "We will need your account number to add funds, please go to settings and add an account number");
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
