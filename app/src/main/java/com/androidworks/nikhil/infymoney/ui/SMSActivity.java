package com.androidworks.nikhil.infymoney.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidworks.nikhil.infymoney.BuildConfig;
import com.androidworks.nikhil.infymoney.R;
import com.androidworks.nikhil.infymoney.adapters.MessagesAdapter;
import com.androidworks.nikhil.infymoney.model.SMS;
import com.androidworks.nikhil.infymoney.utils.DataStore;
import com.androidworks.nikhil.infymoney.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SMSActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.add_fund_button)
    Button addFunds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission();
        } else {
            Utils.getMessages(this);
            MessagesAdapter adapter = new MessagesAdapter(this, DataStore.getInstance(this).getMessages());
            listView.setAdapter(adapter);
        }

        addFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SMSActivity.this, AddFunds.class));
            }
        });

        if (!DataStore.getInstance(this).isLoggedIn())
            askForAccount();
        DataStore.getInstance(this).setIsLoggedIn();

    }

    private void requestPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Utils.getMessages(this);
            balance.setText(DataStore.getInstance(this).getBalance());
            MessagesAdapter adapter = new MessagesAdapter(this, DataStore.getInstance(this).getMessages());
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.getMessages(this);
                    balance.setText(DataStore.getInstance(this).getBalance());
                    MessagesAdapter adapter = new MessagesAdapter(this, DataStore.getInstance(this).getMessages());
                    listView.setAdapter(adapter);

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    requestPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
                    DataStore.getInstance(SMSActivity.this).storeAccount(accountET.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Utils.showToast(SMSActivity.this, "We will need your account number to add funds, please go to settings and add an account number");
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_funds:
                startActivity(new Intent(this, AddFunds.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
