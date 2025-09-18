package com.example.projectattempt1;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

public class RegisterActivity extends Activity {

    EditText nameInput, ageInput, phoneInput, usernameInput, passwordInput, confirmPasswordInput;
    RadioGroup genderGroup, roleGroup;
    Spinner nationalitySpinner;
    Button registerButton;
    TextView passwordNotification;

    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseManager(this);

        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        phoneInput = findViewById(R.id.phoneInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        passwordNotification = findViewById(R.id.passwordNotification);
        registerButton = findViewById(R.id.registerButton);

        genderGroup = findViewById(R.id.genderGroup);
        roleGroup = findViewById(R.id.roleGroup);
        nationalitySpinner = findViewById(R.id.nationalitySpinner);

        //Setup nationality spinner adapter
        //this is like a droplist
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nationalities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalitySpinner.setAdapter(adapter);

        //disable register button until passwords match and are strong
        registerButton.setEnabled(false);

        //add password validation
        passwordInput.addTextChangedListener(passwordWatcher);
        confirmPasswordInput.addTextChangedListener(passwordWatcher);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    //this is used for validating a password
    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            validatePasswordInput();
        }
        @Override public void afterTextChanged(Editable s) {}
    };

    private void validatePasswordInput() {
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (!password.equals(confirmPassword)) {
            passwordNotification.setText("Passwords do not match!");
            passwordNotification.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            registerButton.setEnabled(false);
        } else if (!isPasswordStrong(password)) {
            passwordNotification.setText("Password must be 8+ chars with uppercase, lowercase, number, and symbol.");
            passwordNotification.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            registerButton.setEnabled(false);
        } else {
            passwordNotification.setText("Password is strong and matches!");
            passwordNotification.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            registerButton.setEnabled(true);
        }
    }

    private boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        String nationality = nationalitySpinner.getSelectedItem().toString();

        //Basic empty-field check
        if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()
                || genderGroup.getCheckedRadioButtonId() == -1 || roleGroup.getCheckedRadioButtonId() == -1
                || nationality.equals("Select Nationality")) {
            Toast.makeText(this, "Please fill all fields and select your nationality.", Toast.LENGTH_SHORT).show();
            return;
        }

        //age restriction, must be numeric and at least 18
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid numeric age.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (age < 18) {
            Toast.makeText(this, "You must be at least 18 years old to register.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Username uniqueness
        if (db.checkUsernameExists(username)) {
            Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gender and role values
        String gender = ((RadioButton) findViewById(genderGroup.getCheckedRadioButtonId())).getText().toString();
        String role = ((RadioButton) findViewById(roleGroup.getCheckedRadioButtonId())).getText().toString();

        db.insertUser(name, age, gender, phone, username, password, 0, nationality, role, 0);

        Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
        clearFields();
        finish();
    }

    private void clearFields() {
        nameInput.setText("");
        ageInput.setText("");
        phoneInput.setText("");
        usernameInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
        passwordNotification.setText("");
        genderGroup.clearCheck();
        roleGroup.clearCheck();
        nationalitySpinner.setSelection(0);
        registerButton.setEnabled(false);
    }
}
