package com.github.wahyuadepratama.laporinaja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.wahyuadepratama.laporinaja.ApiHelper.BaseApiService;
import com.github.wahyuadepratama.laporinaja.ApiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_SHARED_PREFERENCES = "my_shared_preferences";
    public static final String SESSION_STATUS = "session_status";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String STATUS = "status";
    public static final String UPDATED_AT = "updated_at";

    EditText etIdentity;
    EditText etPassword;
    Button btnLogin;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;
    SharedPreferences sharedpreferences;

    Boolean session = false;
    String id, name, email, phone, status, updated_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        checkSession();

        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        initComponent();
    }

    public void redirectToRegister(View v){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void checkSession(){
        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        session     = sharedpreferences.getBoolean(SESSION_STATUS, false);
        id          = sharedpreferences.getString(ID, null);
        name        = sharedpreferences.getString(NAME, null);
        email       = sharedpreferences.getString(EMAIL, null);
        phone       = sharedpreferences.getString(PHONE, null);
        status      = sharedpreferences.getString(STATUS, null);
        updated_at  = sharedpreferences.getString(UPDATED_AT, null);

        if (session) {
            Intent redirect = new Intent(this, MainActivity.class);
            redirect.putExtra(ID, id);
            redirect.putExtra(NAME, name);
            redirect.putExtra(EMAIL, email);
            redirect.putExtra(PHONE, phone);
            redirect.putExtra(STATUS, status);
            redirect.putExtra(UPDATED_AT, updated_at);
            finish();
            startActivity(redirect);
        }
    }

    private void initComponent() {
        etIdentity = (EditText) findViewById(R.id.etIdentity);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Please Waiting...", true, false);
                login();
            }
        });
    }

    public void login(){
        mApiService.login(etIdentity.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("status").equals("success")){

                            String id = jsonRESULTS.getJSONObject("user").getString("id");
                            String name = jsonRESULTS.getJSONObject("user").getString("name");
                            String email = jsonRESULTS.getJSONObject("user").getString("email");
                            String phone = jsonRESULTS.getJSONObject("user").getString("phone");
                            String status = jsonRESULTS.getJSONObject("user").getString("status");
                            String updated_at = jsonRESULTS.getJSONObject("user").getString("updated_at");

                            // Menyimpan login ke session
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(SESSION_STATUS, true);
                            editor.putString(ID, id);
                            editor.putString(NAME, name);
                            editor.putString(EMAIL,email);
                            editor.putString(PHONE,phone);
                            editor.putString(STATUS,status);
                            editor.putString(UPDATED_AT,updated_at);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(ID, id);
                            intent.putExtra(NAME, name);
                            intent.putExtra(EMAIL,email);
                            intent.putExtra(PHONE,phone);
                            intent.putExtra(STATUS,status);
                            intent.putExtra(UPDATED_AT,updated_at);
                            finish();

                            Toast.makeText(mContext, "Berhasil Login!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else {
                            // Jika login gagal
                            String error_message = jsonRESULTS.getString("error_msg");
                            Toast.makeText(mContext, error_message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(mContext, "Gagal Login!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(mContext, "Masalah Koneksi Jaringan!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Server Sedang Maintenance!", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Masalah Koneksi Jaringan!", Toast.LENGTH_SHORT).show();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                loading.dismiss();
            }
        });
    }
}
