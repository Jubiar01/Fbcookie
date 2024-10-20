package com.example.fbcookie.bottomsheet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fbcookie.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ResultBottomSheetDialog extends BottomSheetDialog {

    private TextView titleTextView;
    private TextView contentTextView;

    public ResultBottomSheetDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_result);

        titleTextView = findViewById(R.id.tv_result_title);
        contentTextView = findViewById(R.id.tv_result_content);
        Button copyButton = findViewById(R.id.btn_copy);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentTextView.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("cookie_data", content);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setContent(String content) {
        contentTextView.setText(content);
    }
}