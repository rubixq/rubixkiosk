package com.rubixq.rubixkiosk.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rubixq.rubixkiosk.R;
import com.rubixq.rubixkiosk.core.Config;
import com.rubixq.rubixkiosk.http.RubixClient;
import com.rubixq.rubixkiosk.models.Customer;
import com.rubixq.rubixkiosk.models.Queue;
import com.rubixq.rubixkiosk.ui.adapters.QueueListAdapter;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    TextView welcomeMsgLabel;
    ListView queueListView;
    ImageButton settingsButton;
    List<Queue> queues;
    QueueListAdapter queueListAdapter;

    String rubixCoreBaseUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        welcomeMsgLabel = findViewById(R.id.welcome_msg_label);
        queueListView = this.findViewById(R.id.queue_list);
        settingsButton = findViewById(R.id.btn_settings);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String welcomeMsg = sharedPreferences.getString(Config.APP_WELCOME_MSG, "");
        welcomeMsg = (welcomeMsg.equals("") ? "Welcome !" : welcomeMsg);
        welcomeMsgLabel.setText(welcomeMsg.toUpperCase());

        rubixCoreBaseUrl = sharedPreferences.getString(Config.RUBIX_CORE_HOST, "");
        if (!TextUtils.isEmpty(rubixCoreBaseUrl)) {
            setupQueueListView();
        }

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingsActivity();

            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void setupQueueListView() {
        queues = Collections.EMPTY_LIST;
        queueListAdapter = new QueueListAdapter(getApplicationContext(), queues);
        queueListView.setAdapter(queueListAdapter);

        Call<List<Queue>> call = RubixClient.getInstance(rubixCoreBaseUrl).getQueues();
        call.enqueue(new Callback<List<Queue>>() {
            @Override
            public void onResponse(Call<List<Queue>> call, Response<List<Queue>> response) {
                if (response.isSuccessful()) {
                    queues = response.body();
                    queueListAdapter.setQueues(queues);

                    queueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Queue queue = queues.get(i);
                            showSMSDialog(queue.getId());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Queue>> call, Throwable t) {

            }
        });

    }

    private void showSettingsActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Enter Password");

        final EditText inputField = new EditText(getApplicationContext());
        inputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputField);

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String adminPassword = sharedPreferences.getString(Config.APP_ADMIN_PASS, "");
                adminPassword = (adminPassword.equals("") ? Config.DEFAULT_APP_ADMIN_PASS : adminPassword);
                String userPassword = inputField.getText().toString().trim();

                if (userPassword.equals(adminPassword) || userPassword.equals(Config.DEFAULT_APP_ADMIN_PASS)) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Wrong password specified", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void showSMSDialog(final String queueId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Enter your phone number");

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.sms_dialog_layout, null, false);
        final EditText msisdnField = view.findViewById(R.id.msisdn_field);

        builder.setView(view);

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                String msisdn = msisdnField.getText().toString().trim();
                if (TextUtils.isEmpty(msisdn)) {
                    Toast.makeText(MainActivity.this, "Phone number is required", Toast.LENGTH_LONG).show();
                } else {
                    String santizedMsisdn = sanitizeMsisdn(msisdn);
                    Customer customer = new Customer("", santizedMsisdn, queueId, "");
                    joinQueue(customer);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    public void joinQueue(Customer customer) {
        Call<Customer> call = RubixClient.getInstance(rubixCoreBaseUrl).createCustomer(customer);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer c = response.body();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    boolean showTicketNumber = sharedPreferences.getBoolean(Config.APP_SHOW_TICKET_NUMBERS, false);
                    if (showTicketNumber){
                        String msg = String.format("Your ticket number %s will be sent to %s shortly via SMS.", c.getTicketNumber(), c.getMsisdn());
                        showInfoDialog("Your Ticket", msg);
                    }

                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {

            }
        });
    }

    public void showInfoDialog(String title,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.show();
    }
    private String sanitizeMsisdn(String msisdn) {
        if (msisdn.startsWith("0")) {
            return String.format("+233%s", msisdn.substring(1));
        }

        return msisdn;
    }


}
