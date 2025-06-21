package com.example.adventsignin;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    // Android-compatible Google API imports
    import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
    import com.google.api.client.http.HttpTransport;
    import com.google.api.client.http.javanet.NetHttpTransport;
    import com.google.api.client.json.JsonFactory;
    import com.google.api.client.json.gson.GsonFactory;
    import com.google.api.services.sheets.v4.Sheets;
    import com.google.api.services.sheets.v4.SheetsScopes;
    import com.google.api.services.sheets.v4.model.ValueRange;

    // Google Sign-In
    import com.google.android.gms.auth.api.signin.GoogleSignIn;
    import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
    import com.google.android.gms.auth.api.signin.GoogleSignInClient;
    import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
*/

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void setBirthday(View v){
        EditText bday = findViewById(R.id.editBirthday);
        bday.setInputType(InputType.TYPE_NULL);
        bday.setOnClickListener((a) -> {
            Calendar c = Calendar.getInstance();

            int year = 1970;//c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Add leading zero to month and day if needed
                        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                selectedMonth + 1, selectedDay, selectedYear);
                        bday.setText(date);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    /*  SPREADSHEET ID:
            1dydBB7lR9-WGceNFW2PPgMsDEukUlZ7E5qJxzvypoCs
    */
    public void onSubmit(View v){

        //CHECKING TEXT
        TextView warning = findViewById(R.id.warningText);
        warning.setVisibility(INVISIBLE);
        EditText[] inputFields = {findViewById(R.id.editFirstName),findViewById(R.id.editLastName),findViewById(R.id.editBirthday)};
        for(EditText ref : inputFields){ref.setHintTextColor(Color.parseColor("#6D66BC"));}
        ArrayList<String> jPrep = new ArrayList<>();

        Boolean emptyF = false;
        for(EditText ref : inputFields) {
            String r = ref.getText().toString();
            if (r.isEmpty()) {
                ref.setHintTextColor(Color.parseColor("#FE5F5F"));
                emptyF = true;
            }else jPrep.add(r);
        }
        if(emptyF){
            warning.setVisibility(VISIBLE);
            return;
        }

        try {
                JSONObject jject = new JSONObject();
                jject.put("firstName", jPrep.get(0));
                jject.put("lastName", jPrep.get(1));
                jject.put("birthday", jPrep.get(2));
                jject.put("tstamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a")));

            new PostInfo().execute(jject);
            startActivity(new Intent(this,Confirm.class));
        }catch (JSONException jsonE) {
            Log.e("JSON err","Error parsing User JSON.",jsonE);
        }
    }

    private static class PostInfo extends AsyncTask<JSONObject,Void,String> {
        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            JSONObject jject = jsonObjects[0];
            try {
                URL url = new URL("http://10.0.2.2:8080/submit");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-type","application/json; utf-8");
                connection.setDoOutput(true);

                try(OutputStream out= connection.getOutputStream()){
                    byte[] input = jject.toString().getBytes("utf-8");
                    out.write(input,0,input.length);
                }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                reader.close();

                return "Response Code: " + connection.getResponseCode() + ", Response: " + response;

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String result){
            Log.d("POST-RESULT",result);
        }
    }
}
