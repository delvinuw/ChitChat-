package edu.uw.chitchat;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.uw.chitchat.ConnectionRequestList.ConnectionRequestList;
import edu.uw.chitchat.ConnectionSendRequestListFragment.OnListFragmentInteractionListener;
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
public class MyConnectionSendRequestsRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionSendRequestsRecyclerViewAdapter.ViewHolder> {

    private final List<ConnectionRequestList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyConnectionSendRequestsRecyclerViewAdapter(List<ConnectionRequestList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connection_send_request_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUserNameView.setText(mValues.get(position).getUsername());

        holder.cancelButton.setOnClickListener(new View.OnClickListener()

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
                    test.put("email_A", myEmail);
                    test.put("email_B", friendEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new SendPostAsyncTask.Builder(uri.toString(), test)
                        .build().execute();
                mValues.remove(position);
                notifyDataSetChanged();
                Log.i("Delete Button Clicked","Delete!");
                //Toast.makeText(context, "Delete button Clicked", Toast.LENGTH_LONG).show();
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onConnectionSendRequestListFragmentInteraction(holder.mItem);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUserNameView;
        public ConnectionRequestList mItem;
        public Button cancelButton = null;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserNameView = (TextView) view.findViewById(R.id.textView_ConnectionSendRequestname);
            cancelButton = (Button) view.findViewById(R.id.request_cancel_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUserNameView.getText() + "'";
        }
    }
}
