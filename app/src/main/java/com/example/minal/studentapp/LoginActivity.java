package com.example.minal.studentapp;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;


public class LoginActivity extends AppCompatActivity {


    public static String username,password;
    private String TAG = "Response Login: ";

    private EditText editText_ID=null,editText_Password=null;
    private CheckBox saveLogin_CheckBox=null;
    public static SharedPreferences loginPreferences=null;
    public static SharedPreferences.Editor loginPrefs_Editor=null;
    private Boolean saveLogin=null;
    public static Boolean StayLogged=null;
    private SoapPrimitive resultString=null;
    private String data =null;
    private String dataParsed =null;
    private String Authenticated = "Authenticated";
    private AutoStarting_BackgroundApplication alarm_GPA;
    private AutoStarting_BackgroundApplication alarm_Deadlines;
    private AutoStarting_BackgroundApplication alarm_Attendence;
    private AutoStarting_BackgroundApplication alarm_Coursework;
    private AutoStarting_BackgroundApplication alarm_Warning;
    private AutoStarting_BackgroundApplication alarm_News;
    private ConnectionDetector cdr;
    Intent mServiceIntent;
    private SensorService mSensorService;

    Context ctx;

    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        cdr=new ConnectionDetector(this);

        CardView Login = findViewById(R.id.Login_Card);
        editText_ID = findViewById(R.id.ID_Text);
        editText_Password = findViewById(R.id.Password_Text);
        saveLogin_CheckBox = findViewById(R.id.RememberMecheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefs_Editor = loginPreferences.edit();

        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(cdr.isConnected()){
                //login();

                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                    // TODO: Implement your own authentication logic here.
                    LoginActivity.AsyncCallWS_LoginAuthentication loginAuthenticator = new LoginActivity.AsyncCallWS_LoginAuthentication();
                    loginAuthenticator.execute();

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    onLoginSuccess();
                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 500);

                }
                else
                {
                    if(StayLogged && saveLogin)
                        onLoginSuccess();
                }
            }
        });

        StayLogged = loginPreferences.getBoolean("StayLogged",false);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            editText_ID.setText(loginPreferences.getString("username", ""));
            editText_Password.setText(loginPreferences.getString("password", ""));
            saveLogin_CheckBox.setChecked(true);
            if(StayLogged)
                Login.performClick();
        }
        ctx = this;
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }


    private class AsyncCallWS_LoginAuthentication extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");

            SharedPreferences sharedpreferences = getSharedPreferences("Deadlines", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Deadlines_Notification_Message", "No Deadline Yet");
            editor.putString("Deadlines_DueDate", "1/1/2000");
            editor.commit();

            sharedpreferences = getSharedPreferences("Alarm Counter", Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            editor.putInt("Counter", 1);
            editor.commit();


            sharedpreferences = getSharedPreferences("Alarm_secondCounter", MODE_PRIVATE);
            editor = sharedpreferences.edit();
            editor.putInt("Counter2", 1);
            editor.commit();


        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            Get_Login();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            if(dataParsed.compareTo(Authenticated) == 0)
            { login();

            //alarm should start only on complete authentication
                ReadDeadlines.LoadAllDeadlines(LoginActivity.this, username);
            alarm_Deadlines =new AutoStarting_BackgroundApplication(0, 1,LoginActivity.this,username);
            alarm_Deadlines.setAlarm(getApplicationContext());
}

            else
                onLoginFailed();
        }

        public void Get_Login() {

            username = editText_ID.getText().toString();
            password = editText_Password.getText().toString();

            SOAP_Access serverAccessClass= SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(username+","+password+",0");

            try {
                data = resultString.toString();
                JSONObject JBO = new JSONObject(data);
                JSONObject JO = (JSONObject)JBO.get("Authentication");
                dataParsed = (String) JO.get("Status");

            } catch (
                    JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    public void login() {

        if (!validateInput()) {
            onLoginFailed();
            return;
        }

        username = editText_ID.getText().toString();
        password = editText_Password.getText().toString();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText_ID.getWindowToken(), 0);

        if (saveLogin_CheckBox.isChecked()) {
            loginPrefs_Editor.putBoolean("StayLogged",true);
            loginPrefs_Editor.putBoolean("saveLogin", true);
            loginPrefs_Editor.putString("username", username);
            loginPrefs_Editor.putString("password", password);
            loginPrefs_Editor.commit();
        } else {
            loginPrefs_Editor.clear();
            loginPrefs_Editor.commit();
        }

        CardView Login = findViewById(R.id.Login_Card);
        EditText ID_text = findViewById(R.id.ID_Text);
        EditText Pass_text = findViewById(R.id.Password_Text);
        Login.setEnabled(false);

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        CardView Login = findViewById(R.id.Login_Card);
        Login.setEnabled(true);
        Intent IntentActivity2 = new Intent(this,NavDrawerActivity.class);
        startActivity(IntentActivity2);
        finish();
    }

    public void onLoginFailed() {
        CardView Login = findViewById(R.id.Login_Card);
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        Login.setEnabled(true);
    }

    public boolean validateInput() {
        boolean isvalid = true;

        EditText ID_text = findViewById(R.id.ID_Text);
        EditText Pass_text = findViewById(R.id.Password_Text);


        String ID = (ID_text.getText().toString());
        String password = Pass_text.getText().toString();

        if (ID.isEmpty()) {
            ID_text.setError("Enter a valid ID");
            isvalid = false;
        } else {
            ID_text.setError(null);
        }

        if (password.isEmpty()) {
            Pass_text.setError("Enter a valid password");
            isvalid = false;
        } else {
            Pass_text.setError(null);
        }

        return isvalid;
    }


}


