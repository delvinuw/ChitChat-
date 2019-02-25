package edu.uw.chitchat;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import edu.uw.chitchat.Credentials.Credentials;
import edu.uw.chitchat.contactlist.ContactList;
import edu.uw.chitchat.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends Fragment implements View.OnClickListener{
    private OnAddContactFragmentInteractionListener mListener;
    public Credentials mCredentials;
    public AddContactFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_contact, container, false);
        ((Button) v.findViewById(R.id.button_AddContact)).setOnClickListener(this::addNewContact);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactListFragment.OnListFragmentInteractionListener) {
            mListener = (OnAddContactFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public void addNewContact(View view) {
        EditText firstEmailAddress = getActivity().findViewById(R.id.editText_first_email_address);
        EditText secondEmailAddress = getActivity().findViewById(R.id.editText_second_email_address);
        String email = getSharedPreference(getString(R.string.keys_email_stored_onRegister));
        String password = firstEmailAddress.getText().toString().trim();
        String repassword = secondEmailAddress.getText().toString().trim();

        boolean hasError = false;
        //regex used for names to not allow special characters in text fields
        //allows input anything outside of below characters
        Pattern StringRegex = Pattern.compile("^[^±!@£$%^&*_+§¡~€#¢§¶•ªº«\\[\\]\\/<>?:;|=.,]+$");

        //password regex, cannot start with . or -, has at least 1 cap letter and number
        //allows the listed special characters, with a minimum length of 6
        Pattern passwordRegex = Pattern.compile(
                "^(?=.*[0-9])(?=.*[A-Z])[^.\\-][A-Z0-9a-z!()?_'~;:.\\]\\[\\-!#@$%^&*+=]{6,}$"
        );

        if (!passwordRegex.matcher(password).matches()) {
            hasError = true;
            firstEmailAddress.setError("Please enter a minimum of 6 characters with 1 upper case and 1 digit.");
        }
        if (!password.equals(repassword)) {
            hasError = true;
            secondEmailAddress.setError("Your password and retyped one do not match.");
        }

        if (!hasError) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_contact))
                    .appendPath(getString(R.string.ep_add))
                    .build();
            //mListener.onRegisterSuccess(credentials);
            //build the web service URL
            //build the JSONObject
            Credentials credentials = new Credentials.Builder(email,password).build();
            JSONObject msg = credentials.asJSONObject();

            mCredentials = credentials;
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPostExecute(this::handleUpDdateNewContact)
                    .build().execute();
        } else {
            return;
        }
        //mListener.onPasswordUpdate();
    }

    private void handleUpDdateNewContact(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(getString(R.string.keys_json_login_success));
            if (success) {
                Toast.makeText(getActivity(), "You successfully made the new contact",
                        Toast.LENGTH_LONG).show();
                return;
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            // String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result + System.lineSeparator() + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.editText_first_email_address))
                    .setError("Making New contact Unsuccessful");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }
    private String getSharedPreference (String key) {
        SharedPreferences sharedPref =
                getActivity().getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddContactFragmentInteractionListener {
        // TODO: Update argument type and name

        void onListFragmentInteraction(ContactList mItem);

        void onWaitFragmentInteractionHide();
    }
}
