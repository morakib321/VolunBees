package com.example.projectattempt1;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.HashSet;

public class EditProfileFragment extends DialogFragment {

    private EditText usernameInput, ageInput, nationalityInput;
    private Button saveButton;
    private HashSet<String> existingUsernames;

    public static EditProfileFragment newInstance(String currentUsername, HashSet<String> usernames) {
        EditProfileFragment frag = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("username", currentUsername);
        args.putSerializable("usernames", usernames);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        usernameInput = view.findViewById(R.id.usernameInput);
        ageInput = view.findViewById(R.id.ageInput);
        nationalityInput = view.findViewById(R.id.nationalityInput);
        saveButton = view.findViewById(R.id.saveButton);

        String currentUsername = getArguments().getString("username");
        usernameInput.setText(currentUsername);
        existingUsernames = (HashSet<String>) getArguments().getSerializable("usernames");

        saveButton.setOnClickListener(v -> validateAndSave());

        return view;
    }

    private void validateAndSave() {
        String username = usernameInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        String nationality = nationalityInput.getText().toString().trim();

        if (username.isEmpty() || ageStr.isEmpty() || nationality.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (existingUsernames.contains(username)) {
            Toast.makeText(getContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        if (age < 18) {
            Toast.makeText(getContext(), "You must be at least 18 years old", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    public interface OnProfileUpdatedListener {
    }
}
