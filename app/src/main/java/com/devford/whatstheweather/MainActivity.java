package com.devford.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    EditText editText;
    TextView textView2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView =findViewById(R.id.imageView);
        textView =findViewById(R.id.textView);
        editText =findViewById(R.id.editText);
        textView2 =findViewById(R.id.textView2);
        button =findViewById(R.id.button);

    }
    public  void getWeather(View view){
//        editText.getText().toString();
        DownloasTask task = new DownloasTask();
//        task.execute("http://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=b1b15e88fa797225412429c1c50c122a1");
        task.execute("https://openweathermap.org/data/2.5/forecast?q=" + editText.getText().toString() + "&appid=b1b15e88fa797225412429c1c50c122a1");
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);

    }
    public  class DownloasTask extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... urls) {
            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data !=1){
                    char current = (char) data;
                    result = result +current;
                    data =reader.read();

                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception"," url failed");
                Toast.makeText(getApplicationContext(),"Could not find the weather",Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                String message ="";
                for(int i = 0;i<arr.length();i++){
                    JSONObject jsonPart =arr.getJSONObject(i);
                    String main =jsonPart.getString("main");
                    String description =jsonPart.getString("description");
                    if(!(main.equals("")) && !description.equals("")){
                        message +=main +": "+description+"\r\n";
                    }

                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }
                if(!message.equals("")){
                    textView2.setText(message);
                }else {
                    Toast.makeText(getApplicationContext(),"Could not find the weather",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.i("jsonobject", "onPostExecute: ");
                Toast.makeText(getApplicationContext(),"Could not find the weather",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
