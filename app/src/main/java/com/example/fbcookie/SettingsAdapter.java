package com.example.fbcookie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingViewHolder> {

    // In SettingsAdapter.java
    public static final String PREFS_NAME = "settings";
    public static final String KEY_USER_AGENT = "user_agent";

    private List<String> settingsList;
    private Context context;

    public SettingsAdapter(List<String> settingsList, Context context) {
        this.settingsList = settingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        return new SettingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        String settingName = settingsList.get(position);
        holder.settingNameTextView.setText(settingName);

        holder.itemView.setOnClickListener(
                v -> {
                    if (settingName.equals("User-Agent")) {
                        showUserAgentDialog();
                    }
                    // Handle other settings clicks
                });
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    private void showUserAgentDialog() {
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_user_agent, null);
    dialog.setContentView(view);

    ChipGroup userAgentChipGroup = view.findViewById(R.id.user_agent_chip_group);
    Chip defaultChip = view.findViewById(R.id.default_chip);
    Chip customChip = view.findViewById(R.id.custom_chip);
    EditText userAgentInput = view.findViewById(R.id.et_user_agent);
    MaterialButton okButton = view.findViewById(R.id.ok_button);

    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    // Set the initial state based on the stored preference
    String currentUserAgent = prefs.getString(KEY_USER_AGENT, "Default");
    if (currentUserAgent.equals("Default")) {
        defaultChip.setChecked(true);
        userAgentInput.setVisibility(View.GONE);
    } else {
        customChip.setChecked(true);
        userAgentInput.setVisibility(View.VISIBLE);
        userAgentInput.setText(currentUserAgent);
    }

    userAgentChipGroup.setOnCheckedChangeListener(
            (group, checkedId) -> {
                if (checkedId == R.id.default_chip) {
                    userAgentInput.setVisibility(View.GONE); // Hide for default
                } else if (checkedId == R.id.custom_chip) {
                    userAgentInput.setVisibility(View.VISIBLE); // Show for custom
                }
            });
        
    okButton.setOnClickListener(
            v -> {
                SharedPreferences.Editor editor = prefs.edit();
                if (customChip.isChecked()) {
                    String newUserAgent = userAgentInput.getText().toString();
                    editor.putString(KEY_USER_AGENT, newUserAgent);
                } else {
                    editor.putString(KEY_USER_AGENT, "Default");
                }
                editor.apply();
                dialog.dismiss();

                // Show AlertDialog to restart the app
                showRestartAppDialog();
            });

    dialog.show();
}
    private void showRestartAppDialog() {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Restart App")
                .setMessage("To apply the User-Agent changes, the app needs to be restarted. Restart now?")
                .setPositiveButton("Restart", (dialog, which) -> restartApp())
                .setNegativeButton("Later", null)
                .show();
    }

    private void restartApp() {
        // Get the current package name
        String packageName = context.getPackageName();

        // Create an intent to launch the main activity of the package
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // Add the FLAG_ACTIVITY_CLEAR_TASK flag to clear the existing task
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Add the FLAG_ACTIVITY_NEW_TASK flag to start a new task
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Start the activity
            context.startActivity(intent);
        }
    }

    static class SettingViewHolder extends RecyclerView.ViewHolder {
        TextView settingNameTextView;

        SettingViewHolder(View itemView) {
            super(itemView);
            settingNameTextView = itemView.findViewById(R.id.setting_name);
        }
    }
}