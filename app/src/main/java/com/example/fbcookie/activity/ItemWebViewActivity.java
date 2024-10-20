package com.example.fbcookie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fbcookie.R;
import com.example.fbcookie.SettingsActivity;
import com.example.fbcookie.SettingsAdapter;
import com.example.fbcookie.adapter.Item;
import com.example.fbcookie.bottomsheet.CookieDialogHelper;
import com.example.fbcookie.bottomsheet.ResultBottomSheetDialog;

public class ItemWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private Button extractCookiesButton;
    private CookieDialogHelper cookieDialogHelper;
    private String defaultUserAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_web_view);

        webView = findViewById(R.id.webview);
        extractCookiesButton = findViewById(R.id.btn_extract_cookies);
        Button settingsButton = findViewById(R.id.btn_settings);
        extractCookiesButton.setVisibility(Button.GONE);
        cookieDialogHelper = new CookieDialogHelper(this, true);

        defaultUserAgent = webView.getSettings().getUserAgentString();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                applyUserAgent(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookies = CookieManager.getInstance().getCookie(url);
                if (cookies != null && cookies.contains("c_user")) {
                    if (!url.contains("/profile.php")) {
                        webView.loadUrl("https://m.facebook.com/profile.php");
                    } else {
                        extractCookiesButton.setVisibility(Button.VISIBLE);
                    }
                }
            }
        });

        // Get the title from the intent
        Intent intent = getIntent();
    Item item = (Item) intent.getSerializableExtra("item"); // Make sure the key is "item"

    if (item != null) {
        webView.loadUrl(item.getUrl());
        webView.getSettings().setUserAgentString(item.getUserAgent());
    } else {
        // Handle the case where the item is not found
        finish(); // Or show an error message
    }

        // Use the title to load the appropriate URL
   /*     if (title != null) {
            webView.loadUrl("https://m.facebook.com"); // Load the URL based on the title
        } else {
            // Handle the case where the title is null
            finish(); // Or show an error message
        }*/

        extractCookiesButton.setOnClickListener(v ->
                cookieDialogHelper.showSelectionCookieDialog(webView, cookies -> showCookieDialog(cookies)));

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(ItemWebViewActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        });

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void applyUserAgent(WebView webView) {
        SharedPreferences prefs = getSharedPreferences(SettingsAdapter.PREFS_NAME, MODE_PRIVATE);
        String userAgent = prefs.getString(SettingsAdapter.KEY_USER_AGENT, "Default");
        WebSettings settings = webView.getSettings();
        if (userAgent.equals("Default")) {
            settings.setUserAgentString(defaultUserAgent);
        } else {
            settings.setUserAgentString(userAgent);
        }
    }

    private void showCookieDialog(String message) {
        ResultBottomSheetDialog dialog = new ResultBottomSheetDialog(this);
        dialog.setContent(message);
        dialog.show();
    }
}