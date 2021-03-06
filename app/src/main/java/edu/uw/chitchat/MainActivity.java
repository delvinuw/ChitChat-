package edu.uw.chitchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

import edu.uw.chitchat.Credentials.Credentials;
import edu.uw.chitchat.chat.Chat;
import edu.uw.chitchat.contactlist.ContactList;
import edu.uw.chitchat.utils.PrefHelper;
import me.pushy.sdk.Pushy;


/**
 * The main activity for Chit Chat.
 *
 * @author Logan Jenny
 * @author Delvin mackenzie
 * @author Joe Lu
 * @author Yohei Sato
 * @3/15/2019
 */
public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        VerifyFragment.OnVerifyFragmentInteractionListener,
        ContactListFragment.OnListFragmentInteractionListener{

    private static final int SPLASH_TIME_OUT = 1500;
    private Credentials mCredentials;

    private boolean mLoadFromChatNotification = false;

    private String mChatId;//for chatid
    private String mMessage;
    private String mSender;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);

        //check if user entered from notification
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("type")) {
                mLoadFromChatNotification = getIntent().getExtras().getString("type").equals("msg");
                mChatId = getIntent().getStringExtra("chatId");
                mSender = getIntent().getStringExtra("sender");
                mMessage = getIntent().getStringExtra("message");
            }
        }

        Credentials credentials = getAllCredentialsPref();

        String email = credentials.getEmail();
        String password = credentials.getPassword();
        String jwt = PrefHelper.getStringPreference(getString(R.string.keys_intent_jwt), this);
        String persistentLogin = PrefHelper.getStringPreference(getString(R.string.keys_persistent_login), this);

        //persistant login. If username and password are not empty
        if (email != null && password != null && jwt != null &&
                persistentLogin != null && persistentLogin.contentEquals("true")) {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra(getString(R.string.keys_intent_credentials), (Serializable) credentials);
            i.putExtra(getString(R.string.keys_intent_jwt), jwt);
            startActivity(i);
            finish();

        } else {
            setUpLoginScreen();
        }
    }

    private void setUpLoginScreen() {
        this.setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_main_container, new SplashFragment())
                .commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_container, new LoginFragment())
                        .setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out)
                        .commit();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRegisterClicked() {
        RegisterFragment registerfragment;
        registerfragment = new RegisterFragment();
        Bundle args = new Bundle();
        //args.putSerializable("key", null);
        // args.putSerializable(getString(R.string.all_Phish_key));
        registerfragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, registerfragment)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();

    }


    @Override
    public void onContactListFragmentInteraction(ContactList mItem) {

    }

    @Override
    public void onMemberAdded(String email, Chat item) {
    }

    @Override
    public String getEmail() {
        return mCredentials.getEmail();
    }

    @Override
    public void onRegisterSuccess(Credentials a) {

        //add all credentials to shared preferences
        putAllCredentialsToPref(a);

        mCredentials = a;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, new VerifyFragment())
                .commit();
    }


    /**
     * put all values from credentials to shared preferences
     * @param credentials user credentials
     * @author Delvin Mackenzie
     */
    private void putAllCredentialsToPref (Credentials credentials) {
        PrefHelper.putStringPreference(getString(R.string.keys_email_stored_onRegister), credentials.getEmail(), this);
        PrefHelper.putStringPreference(getString(R.string.keys_password_stored_onRegister), credentials.getPassword(), this);
        PrefHelper.putStringPreference(getString(R.string.keys_username_stored_onRegister), credentials.getUsername(), this);
        PrefHelper.putStringPreference(getString(R.string.keys_firstname_stored_onRegister), credentials.getFirstName(), this);
        PrefHelper.putStringPreference(getString(R.string.keys_lastname_stored_onRegister), credentials.getLastName(), this);
        PrefHelper.putStringPreference(getString(R.string.keys_repassword_stored_onRegister), credentials.getRePassword(), this);
    }

    /**
     * gets all values pertaining to credentials from shared preferences
     * @author Delvin Mackenzie
     */
    private Credentials getAllCredentialsPref() {
        String email = PrefHelper.getStringPreference (getString(R.string.keys_email_stored_onRegister), this);
        String password = PrefHelper.getStringPreference (getString(R.string.keys_password_stored_onRegister), this);
        String username = PrefHelper.getStringPreference (getString(R.string.keys_username_stored_onRegister), this);
        String first = PrefHelper.getStringPreference (getString(R.string.keys_firstname_stored_onRegister), this);
        String last = PrefHelper.getStringPreference (getString(R.string.keys_lastname_stored_onRegister), this);
        String repassword = PrefHelper.getStringPreference (getString(R.string.keys_repassword_stored_onRegister), this);

        //build credentials
        Credentials credentials =
                new Credentials.Builder(email, password, repassword, username, first, last)
                .build();

        return credentials;
    }

    @Override
    public void onLoginSuccess(Credentials credentials, String jwt) {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra(getString(R.string.keys_intent_credentials), (Serializable) credentials);
        i.putExtra(getString(R.string.keys_intent_jwt), jwt);
        i.putExtra(getString(R.string.keys_intent_notification_msg), mLoadFromChatNotification);
        i.putExtra(getString(R.string.keys_intent_current_chat_id), mChatId);
        i.putExtra(getString(R.string.keys_intent_current_sender), mSender);
        i.putExtra(getString(R.string.keys_intent_current_message), mMessage);
        startActivity(i);
        finish();
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    @Override
    public void onNextClicked() {
        Bundle args = new Bundle();
        LoginFragment loginfragment;
        loginfragment = new LoginFragment();
        args.putSerializable(getString(R.string.keys_email), mCredentials.getEmail());
        args.putSerializable(getString(R.string.keys_passowrd), mCredentials.getPassword());
        args.putSerializable(getString(R.string.keys_repassowrd), mCredentials.getRePassword());
        args.putSerializable(getString(R.string.keys_username_stored_onRegister), mCredentials.getUsername());
        args.putSerializable(getString(R.string.keys_firstname_stored_onRegister),mCredentials.getFirstName());
        args.putSerializable(getString(R.string.keys_lastname_stored_onRegister),mCredentials.getLastName());
        loginfragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, loginfragment);
        transaction.commit();
    }


}
