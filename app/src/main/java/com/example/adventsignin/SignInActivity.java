package com.example.adventsignin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

import java.util.Calendar;
import java.util.Locale;

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

            int year = c.get(Calendar.YEAR);
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

        //CHECKING FIELDS
        EditText[] inputFields = {findViewById(R.id.editFirstName),findViewById(R.id.editLastName),findViewById(R.id.editBirthday)};
        for(EditText ref : inputFields){ref.setHintTextColor(Color.parseColor("#6D66BC"));}
        Boolean emptyF = false;
        for(EditText ref : inputFields) {
            if (ref.getText().toString().isEmpty()) {
                ref.setHintTextColor(Color.parseColor("#FF0000"));
                emptyF = true;
            }
        }
        if(emptyF) return;


    }
}
