package com.example.projectattempt1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private EditText eventDate;
    Spinner nationalitySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        //initialize teh spinner
        nationalitySpinner = findViewById(R.id.nationalitySpinner);
        eventDate = findViewById(R.id.eventDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nationalities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalitySpinner.setAdapter(adapter);
    }

    // For date field click
    public void onPickDate(View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            String date = day + "/" + (month + 1) + "/" + year;
            eventDate.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    // For "Confirm" button
    public void onSubmitEvent(View view) {
        EditText eventName = findViewById(R.id.eventName);
        EditText volunteerRole = findViewById(R.id.volunteerRole);
        EditText eventLocation = findViewById(R.id.eventLocation);
        EditText ageInput = findViewById(R.id.ageInput);
        EditText volunteerHours = findViewById(R.id.volunteerHours);

        // Get string values
        String name = eventName.getText().toString().trim();
        String role = volunteerRole.getText().toString().trim();
        String location = eventLocation.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        String date = eventDate.getText().toString().trim();
        String hoursStr = volunteerHours.getText().toString().trim();
        String nationality = nationalitySpinner.getSelectedItem() != null ?
                nationalitySpinner.getSelectedItem().toString() : "";

        // Gender
        RadioGroup genderGroup = findViewById(R.id.genderGroup);
        int genderId = genderGroup.getCheckedRadioButtonId();
        String gender = (genderId != -1) ? ((RadioButton) findViewById(genderId)).getText().toString() : "";

        // Achievement eligibility
        RadioGroup achGroup = findViewById(R.id.achGroup);
        int achId = achGroup.getCheckedRadioButtonId();
        String achElig = (achId != -1) ? ((RadioButton) findViewById(achId)).getText().toString() : "";


        // Validate empty fields
        if (name.isEmpty() || role.isEmpty() || location.isEmpty() || ageStr.isEmpty()
                || date.isEmpty() || hoursStr.isEmpty() || gender.isEmpty()
                || nationality.equals("Select Nationality") || achElig.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields and make valid selections.", Toast.LENGTH_SHORT).show();
            return;
        }


        // Parse numeric fields
        int hr;
        int requiredAge;
        try {
            hr = Integer.parseInt(hoursStr);
            requiredAge = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for age and hours.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean achievementRequired = achElig.equalsIgnoreCase("Required");

        // Save event to database
        DatabaseManager dbManager = new DatabaseManager(this);
        dbManager.insertEvent(
                name,
                location,
                date,
                nationality,
                requiredAge,
                achievementRequired
        );

        // Return to HomePage and refresh
        Intent intent = new Intent(AddEventActivity.this, HomePage.class);
        intent.putExtra("username", getIntent().getStringExtra("username")); // Assuming username is passed to this activity
        startActivity(intent);
        finish();
    }

}