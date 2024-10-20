package com.example.fbcookie.bottomsheet;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.CookieManager;
import com.example.fbcookie.cookie.CookieJson;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.example.fbcookie.R;

public class CookieDialogHelper {

  private Context context;
  private Chip stringChip;
  private Chip jsonChip;
  private boolean extractAsString;

  public CookieDialogHelper(Context context, boolean extractAsString) {
    this.context = context;
    this.extractAsString = extractAsString;
  }

  public void showSelectionCookieDialog(WebView webView, CookieDialogListener listener) {
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    dialog.setContentView(R.layout.dialog_cookie_options);

    stringChip = dialog.findViewById(R.id.chip_string);
    jsonChip = dialog.findViewById(R.id.chip_json);
    Button extractButton = dialog.findViewById(R.id.btn_extract);

    stringChip.setChecked(extractAsString);
    jsonChip.setChecked(!extractAsString);

    dialog.getBehavior().setPeekHeight(1200);

    stringChip.setOnClickListener(
        v -> {
          extractAsString = true;
          stringChip.setChecked(true);
          jsonChip.setChecked(false);
        });

    jsonChip.setOnClickListener(
        v -> {
          extractAsString = false;
          stringChip.setChecked(false);
          jsonChip.setChecked(true);
        });

    extractButton.setOnClickListener(
        v -> {
          String cookies = CookieManager.getInstance().getCookie(webView.getUrl());
          if (cookies != null && cookies.contains("c_user")) {
            if (extractAsString) {
              listener.onCookieExtracted(cookies);
            } else {
              listener.onCookieExtracted(CookieJson.convertCookiesToJson(cookies));
            }
          } else {
            Toast.makeText(context, "No cookies found or user not logged in", Toast.LENGTH_SHORT)
                .show();
          }
          dialog.dismiss();
        });

    dialog.show();
  }

  public interface CookieDialogListener {
    void onCookieExtracted(String cookies);
  }
}
