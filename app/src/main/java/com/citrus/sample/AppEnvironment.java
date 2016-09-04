package com.citrus.sample;

import com.citrus.sdk.Environment;

/**
 * Created by vijay on 14/7/16.
 */
public enum AppEnvironment {
    SANDBOX {
        @Override
        public String getBillUrl() {
            return "https://salty-plateau-1529.herokuapp.com/billGenerator.sandbox.php";
        }

        @Override
        public String getVanity() {
            return "nativeSDK";
        }

        @Override
        public String getSignUpId() {
            return "9hh5re3r5q-signup";
        }

        @Override
        public String getSignUpSecret() {
            return "3be4d7bf59c109e76a3619a33c1da9a8";
        }

        @Override
        public String getSignInId() {
            return "9hh5re3r5q-signin";
        }

        @Override
        public String getSignInSecret() {
            return "ffcfaaf6e6e78c2f654791d9d6cb7f09";
        }

        @Override
        public Environment getEnvironment() {
            return Environment.SANDBOX;
        }
    },
    PRODUCTION {
        @Override
        public String getBillUrl() {
            return "https://salty-plateau-1529.herokuapp.com/billGenerator.production.php";
        }

        @Override
        public String getVanity() {
            return "testing";
        }

        @Override
        public String getSignUpId() {
            return "kkizp9tsqg-signup";
        }

        @Override
        public String getSignUpSecret() {
            return "39c50a32eaabaf382223fdd05f331e1c";
        }

        @Override
        public String getSignInId() {
            return "kkizp9tsqg-signin";
        }

        @Override
        public String getSignInSecret() {
            return "1fc1f57639ec87cf4d49920f6b3a2c9d";
        }

        @Override
        public Environment getEnvironment() {
            return Environment.PRODUCTION;
        }
    };

    public abstract String getBillUrl();

    public abstract String getVanity();

    public abstract String getSignUpId();

    public abstract String getSignUpSecret();

    public abstract String getSignInId();

    public abstract String getSignInSecret();

    public abstract Environment getEnvironment();
}
