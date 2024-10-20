package com.example.fbcookie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fbcookie.activity.VirtualActivity;
import com.example.fbcookie.bottomsheet.CookieDialogHelper;
import com.example.fbcookie.bottomsheet.ResultBottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Button extractCookiesButton;
    private CookieDialogHelper cookieDialogHelper;
    private String defaultUserAgent; // Store default User-Agent
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        extractCookiesButton = findViewById(R.id.btn_extract_cookies);
        Button settingsButton = findViewById(R.id.btn_settings);
        Button virtualButton = findViewById(R.id.btn_virtual);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        extractCookiesButton.setVisibility(Button.GONE);
        cookieDialogHelper = new CookieDialogHelper(this, true);

        // Initialize default User-Agent
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

        webView.loadUrl("https://m.facebook.com");

        extractCookiesButton.setOnClickListener(v ->
                cookieDialogHelper.showSelectionCookieDialog(webView, cookies -> showCookieDialog(cookies)));

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
        virtualButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VirtualActivity.class);
            startActivity(intent);
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