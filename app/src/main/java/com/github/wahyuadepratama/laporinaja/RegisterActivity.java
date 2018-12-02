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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    public static final String MY_SHARED_PREFERENCES = "my_shared_preferences";
    public static final String SESSION_STATUS = "session_status";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String STATUS = "status";
    public static final String UPDATED_AT = "updated_at";

    EditText etNameRegister;
    EditText etEmailRegister;
    EditText etPhoneRegister;
    EditText etPasswordRegister;
    EditText etEtPasswordRegisterConfirmation;
    Button btnRegisterNow;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;
    SharedPreferences sharedpreferences;

    Boolean session = false;
    String id, name, email, phone, status, updated_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Register New User");
        setContentView(R.layout.activity_register);

        checkSession();

        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        initComponent();
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
        etNameRegister = (EditText) findViewById(R.id.etNameRegister);
        etEmailRegister = (EditText) findViewById(R.id.etEmailRegister);
        etPhoneRegister = (EditText) findViewById(R.id.etPhoneRegister);
        etPasswordRegister = (EditText) findViewById(R.id.etPasswordRegister);
        etEtPasswordRegisterConfirmation = (EditText) findViewById(R.id.etPasswordRegisterConfirmation);
        btnRegisterNow = (Button) findViewById(R.id.btnRegisterNow);

        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Please Waiting...", true, false);
                register();
            }
        });
    }

    public void register(){
        if(etPasswordRegister.getText().toString().equals(etEtPasswordRegisterConfirmation.getText().toString())){
            mApiService.register(
                    etNameRegister.getText().toString(),
                    etEmailRegister.getText().toString(),
                    etPhoneRegister.getText().toString(),
                    etPasswordRegister.getText().toString()
            ).enqueue(new Callback<ResponseBody>() {

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

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.putExtra(ID, id);
                                intent.putExtra(NAME, name);
                                intent.putExtra(EMAIL,email);
                                intent.putExtra(PHONE,phone);
                                intent.putExtra(STATUS,status);
                                intent.putExtra(UPDATED_AT,updated_at);
                                finish();

                                Toast.makeText(mContext, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            } else {
                                // Jika register gagal
                                String error_message = jsonRESULTS.getString("error");
                                Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(mContext, "Register Failed! "+response.message().toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            Toast.makeText(mContext, "Network Connection Problem!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(mContext, "Server Maintenance!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(mContext, "Network Connection Problem!", Toast.LENGTH_SHORT).show();
                    Log.e("debug", "onFailure: ERROR > " + t.toString());
                    loading.dismiss();
                }
            });
        }else{
            Toast.makeText(mContext, "Please check your confirmation password!", Toast.LENGTH_SHORT).show();
            loading.dismiss();
        }
    }
}
