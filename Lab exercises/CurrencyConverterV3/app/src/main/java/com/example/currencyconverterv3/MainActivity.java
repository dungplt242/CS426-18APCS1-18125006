package com.example.currencyconverterv3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private String expression="";
    private double currentRes = 0;
    private CurrentList currentList;
    private ToChooseList toChooseList;
    private RecordList recordList;
    Intent intent;
    RequestQueue mQueue;
    private static final int REQUEST_CODE_SELECTION = 1;
    private static final int REQUEST_CODE_HISTORY = 2;

    private View.OnClickListener helper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String input = ((Button) view).getText().toString();
            processInput(input);
        }

        private void processInput(String input) {
            if (input == "⌫") {
                if (expression.length()<=1) expression="";
                else expression = expression.substring(0, expression.length()-1);
                updateExpressionTextView(expression, false);
            }
            else if (input == "=") {
                //calculate
                double temp = EvaluateString.calculate(expression);
                //BAD RESULT
                if (temp < 0) {
                    updateExpressionTextView("BAD EXPRESSION!",true);
                    expression="";
                }
                else {
                    //update baseCurrency
                    currentRes = temp;
                    updateBaseCurrency();
                    //convert
                    checkNetworkAndConvert();
                }
            }
            else
                updateExpressionTextView(expressionUpdate(input),false);
        }


        private String expressionUpdate(String input) {
            expression += input;
            return expression;
        }

    };

    private void checkNetworkAndConvert() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            realtimeConvert(currentRes, MainActivity.this);

        } else {
            currentList.updateCurrency(currentRes, MainActivity.this);
        }

    }

    public void storeANewRecord() {
        MyRecord newRecord = new MyRecord(currentRes, currentList, LocalDateTime.now());
        recordList.addNewRecord(newRecord, this);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int unmaskedRequestCode = requestCode & 0x0000ffff;
        if (unmaskedRequestCode == REQUEST_CODE_SELECTION)
            checkNetworkAndConvert();
    }

    public void realtimeConvert(final double currentRes, final MainActivity mainActivity) {
        String url = "https://api.exchangeratesapi.io/latest";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rates = response.getJSONObject("rates");
                            currentList.updateRealTimeRate(rates);
                            toChooseList.updateRealTimeRate(rates);
                            currentList.updateCurrency(currentRes, mainActivity);
                            saveBackupRate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void saveBackupRate() {
        try {
            PrintStream output = new PrintStream(openFileOutput("backup.txt", MODE_PRIVATE));
            currentList.exportRate(output);
            toChooseList.exportRate(output);
            output.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscape_layout);
        setDefault();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                intent = new Intent(this, UserHistory.class);
                startActivityForResult(intent, REQUEST_CODE_HISTORY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDefault() {
        currentList = new CurrentList(this);
        toChooseList = new ToChooseList();
        recordList = new RecordList();
        mQueue = Volley.newRequestQueue(this);
        currentList.loadList();
        toChooseList.loadList();
        loadBackUpRates();
        recordList.loadList(this);
        createButtons();
        currentList.setView();
        setAddButton();
    }

    private void loadBackUpRates() {
        try {
            Map<String, Double> rates = new HashMap<String, Double>();
            Scanner scan = new Scanner(openFileInput("backup.txt"));
            while (scan.hasNextLine()) {
                String country = scan.nextLine();
                Double rate = Double.parseDouble(scan.nextLine());
                rates.put(country, rate);
            }
            scan.close();
            currentList.getBackupRate(rates);
            toChooseList.getBackupRate(rates);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main);

        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscape_layout);
        setLayout();
    }

    private void setLayout() {
        updateExpressionTextView(expression, false);
        updateBaseCurrency();
        createButtons();
        currentList.setListView(this);
        currentList.setView();
        setAddButton();
    }

    private void setAddButton() {
        Button add = (Button)findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), Selection.class);
                startActivityForResult(intent, REQUEST_CODE_SELECTION);
            }
        });
    }

    private void createButtons(){
        GridLayout gridLayout = getGridLayout();
        String[] labels = getStrings();
        for (int i=0; i<labels.length; ++i) {
            Button button = createButton(labels[i], i);
            addButtonToGridView(button, gridLayout);
        }
    }

    private GridLayout getGridLayout() {
        return (GridLayout)findViewById(R.id.numberPad);
    }

    private String[] getStrings() {
        return new String[]{"7","8","9","x",
                "4","5","6","÷",
                "1","2","3","-",
                ".","0","⌫","+","="};
    }

    private Button createButton(String label, int i) {
        Button button=new Button( this);
        button.setText(label);
        button.setId(i);
        button.setOnClickListener(helper);

        return button;
    }


    private void addButtonToGridView(Button button, GridLayout gridLayout) {
        gridLayout.addView(button);
    }

    private void updateExpressionTextView(String string, boolean b) {
        TextView textView=(TextView) findViewById(R.id.expressionTextView);
        if (b) textView.setTextColor(Color.parseColor("#FFFF0000"));
        else textView.setTextColor(Color.parseColor("#000000"));
        textView.setText(string);
    }

    private void updateBaseCurrency() {
        TextView textView1 = (TextView) findViewById(R.id.baseCurrency);
        DecimalFormat df = new DecimalFormat("#.##");
        textView1.setText(String.valueOf(Double.valueOf(df.format(currentRes)))); //calculate result
    }
}