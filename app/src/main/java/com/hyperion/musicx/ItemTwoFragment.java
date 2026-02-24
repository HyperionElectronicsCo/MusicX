package com.hyperion.musicx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class ItemTwoFragment extends Fragment {
    private WebView mWebView;

    // Using a simplified mobile embed URL to avoid complex API handshakes
    private final String EMBED_URL = "https://everynoise.com/#updates";

    public static ItemTwoFragment newInstance() {
        return new ItemTwoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout root = new LinearLayout(getActivity());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        root.setBackgroundColor(0xFF000000);

        mWebView = new WebView(getActivity());
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));

        setupWebView();

        // Step 1: Clear all previous data to prevent SoundCloud from flagging a "stuck" session
        mWebView.clearCache(false);
        mWebView.clearHistory();
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        // Step 2: Load the standard mobile page which is more forgiving than the widget iframe
        mWebView.loadUrl(EMBED_URL);

        root.addView(mWebView);
        return root;
    }

    private void setupWebView() {
        WebSettings s = mWebView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);

        // Set a modern mobile User Agent that avoids the "Whoa" server-side check
        String mobileUA = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Mobile Safari/537.36";
        s.setUserAgentString(mobileUA);

        // Required for rendering stability on legacy SDK 25
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onDestroyView() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroyView();
    }
}

