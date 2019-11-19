package com.screamxo.Activity.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.MessageFormat;

import static com.screamxo.Activity.instagram.CommonUtils.checkNotNull;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.AUTH_URL;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.CLIENT_ID_DEF;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.INSTA_AUTH_URL;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.INSTA_LOGIN;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.INSTA_REDIRECT_URL;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.REDIRECT_URI_DEF;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.RESPONSE_TYPE_DEF;
import static com.screamxo.Activity.instagram.InstagramHelperConstants.SCOPE_TYPE_DEF;

public class InstagramHelper {

    private String clientId;
    private String redirectUri;
    private String scope;
    private String token;
    private static final String TAG = "InstagramHelper";

    private InstagramHelper(String clientId, String redirectUri, String scope) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    public void loginFromActivity(Activity context) {
        Log.d(TAG, "loginFromActivity: ");
        String authUrl = MessageFormat.format(AUTH_URL + CLIENT_ID_DEF + "{0}" + REDIRECT_URI_DEF + "{1}" + SCOPE_TYPE_DEF + "{2}" + RESPONSE_TYPE_DEF, clientId, redirectUri, scope);
        Intent intent = new Intent(context, InstagramLoginActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(INSTA_AUTH_URL, authUrl);
        bundle.putString(INSTA_REDIRECT_URL, redirectUri);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, INSTA_LOGIN);
    }

    public InstagramUser getInstagramUser(Context context) {
        return SharedPrefUtils.getInstagramUser(context);
    }

    public static final class Builder {
        private String clientId;
        private String redirectUrl;
        private String scope;

        public Builder withClientId(String clientId) {
            this.clientId = checkNotNull(clientId, "clientId == null");
            return this;
        }

        public Builder withRedirectUrl(String redirectUrl) {
            this.redirectUrl = checkNotNull(redirectUrl, "redirectUrl == null");
            return this;
        }

        public Builder withScope(String scope) {
            this.scope = checkNotNull(scope, "scope == null");
            return this;
        }

        public InstagramHelper build() {
            return new InstagramHelper(clientId, redirectUrl, scope);
        }
    }
}
