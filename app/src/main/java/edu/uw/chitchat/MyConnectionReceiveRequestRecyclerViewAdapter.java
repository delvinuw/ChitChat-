package edu.uw.chitchat;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.uw.chitchat.ConnectionReceiveRequestListFragment.OnListFragmentInteractionListener;
import edu.uw.chitchat.ConnectionRequestList.ConnectionRequestList;
import edu.uw.chitchat.utils.SendPostAsyncTask;

/**
 * @author Yohei Sato
 *
 */

/**
 * {@link RecyclerView.Adapter} that can display a {@link ConnectionRequestList} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyConnectionReceiveRequestRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionReceiveRequestRecyclerViewAdapter.ViewHolder> {
    /**
     * this list is for connection request list
     */
    private final List<ConnectionRequestList> mValues;
    /**
     * This is the interface variable
     */
    private final OnListFragmentInteractionListener mListener;
    /**
     * the context variable for connecting activity
     */
    private Context mContext;

    /**
     * This method is constructor
     *
     * @param items each connection data list value
     * @param listener interface value
     */
    public MyConnectionReceiveRequestRecyclerViewAdapter(List<ConnectionRequestList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

    }

    /**
     * The method is viwer holder one for keeping view holder class work
     *
     * @param parent view group variable
     * @param viewType view type variable
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connection_receive_requestlist_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * The method is bind view holder one that can connect view holder and action listener
     *
     * @param holder view holder variable
     * @param position the index for in the list
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUserNameView.setText(mValues.get(position).getUsername());
        holder.deleteButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                String myEmail = mListener.getEmail();
                String friendEmail = mValues.get(position).getEmailAddress();

                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath("tcss450-app.herokuapp.com")
                        .appendPath("connection")
                        .appendPath("deleteRequest")
                        .build();

                JSONObject test = new JSONObject();
                try {
                    test.put("email_A", friendEmail);
                    test.put("email_B", myEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new SendPostAsyncTask.Builder(uri.toString(), test)
                        .build().execute();
                mValues.remove(position);
                Toast.makeText(v.getContext(), "You declined the request",
                        Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
                Log.i("Delete Button Clicked","Delete!");
                //Toast.makeText(context, "Delete button Clicked", Toast.LENGTH_LONG).show();
            }
        });
        holder.acceptButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String myEmail = mListener.getEmail();
                String friendEmail = mValues.get(position).getEmailAddress();


                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath("tcss450-app.herokuapp.com")
                        .appendPath("connection")
                        .appendPath("confirmRequest")
                        .build();

                //mListener.onRegisterSuccess(credentials);
                //build the web service URL
                //build the JSONObject

                //String emailstored = getSharedPreference (getString(R.string.keys_email_stored_onRegister));

                JSONObject test = new JSONObject();
                try {
                    test.put("email_A", myEmail);
                    test.put("email_B", friendEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject test2 = new JSONObject();
                try {
                    test2.put("email_A", friendEmail);
                    test2.put("email_B", myEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //instantiate and execute the AsyncTask.
                new SendPostAsyncTask.Builder(uri.toString(), test)
                        .build().execute();
                new SendPostAsyncTask.Builder(uri.toString(), test2)
                        .build().execute();
//                new SendPostAsyncTask.Builder(uri.toString(), test2)
//                        .build().execute();

                mValues.remove(position);
                Toast.makeText(v.getContext(), "You accepted the friend request!",
                        Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
                Log.i("accept Button Clicked","Accept!");
                //Toast.makeText(context, "Delete button Clicked", Toast.LENGTH_LONG).show();
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onConnectionReceiveRequestListFragmentInteraction(holder.mItem);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * View holder inner class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUserNameView;
        public Button deleteButton = null;
        public Button acceptButton = null;
        public ConnectionRequestList mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserNameView = (TextView) view.findViewById(R.id.textView_ConnectionReceiveRequestname);
            deleteButton = (Button) view.findViewById(R.id.delete_button);
            acceptButton = (Button) view.findViewById(R.id.accept_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUserNameView.getText() + "'";
        }
    }


}
