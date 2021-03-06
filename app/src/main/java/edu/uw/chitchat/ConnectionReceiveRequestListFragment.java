package edu.uw.chitchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.chitchat.ConnectionRequestList.ConnectionRequestList;


/**
 * @author Yohei Sato
 *
 */

public class ConnectionReceiveRequestListFragment extends Fragment implements View.OnClickListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    /**
     * inerface variavle for using interface
     */
    private OnListFragmentInteractionListener mListener;
    /**
     * The list is saving connection information for showing recevi
     */
    private List<ConnectionRequestList> mConnectionRequestList;
    private static final String ARG_RECEIVE_LIST = "receiving requests lists";
    //private static final String ARG_RECIEVE_LIST = "receiving requests lists";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionReceiveRequestListFragment() {
    }

    // TODO: Customize parameter initialization

    /**
     * This method is for going to connection sending request list page
     *
     * @param columnCount that's the number of column
     * @return fragment that's fragment information
     */
    public static ConnectionSendRequestListFragment newInstance(int columnCount) {
        ConnectionSendRequestListFragment fragment = new ConnectionSendRequestListFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_COLUMN_COUNT, columnCount );
        fragment.setArguments( args );
        return fragment;
    }

    /**
     * The method is necessary for creating the fragment class
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (getArguments() != null) {
            mConnectionRequestList = new ArrayList<ConnectionRequestList>( Arrays.asList((ConnectionRequestList[]) getArguments().getSerializable(ARG_RECEIVE_LIST)));
            //mConnectionRequestList2 = new ArrayList<ConnectionRequestList>( Arrays.asList((ConnectionRequestList[]) getArguments().getSerializable(ARG_RECIEVE_LIST)));
            for (ConnectionRequestList temp : mConnectionRequestList) {
                Log.d("joe", temp.getEmailAddress());
            }
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
//        if (getArguments() != null) {
//            mConnectionRequestList2 = new ArrayList<ConnectionRequestList>( Arrays.asList((ConnectionRequestList[]) getArguments().getSerializable(ARG_RECIEVE_LIST)));
//            for (ConnectionRequestList temp : mConnectionRequestList2) {
//                Log.d("yohei", temp.getEmailAddress());
//            }
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//        else {
//            mConnectionRequestList = Arrays.asList( ContactListGenerator.mContacts);
//        }
    }

    /**
     * This method is necessary for making visible of fragment
     *
     * @param inflater   is used in the java class of the corresponding layout(xml) file.
     * @param container  this is the container for the view function
     * @param savedInstanceState that's a bundle information
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_connection_receive_requestlist, container, false );
//        Button b1 = (Button) view.findViewById(R.id.accept_button);
//        b1.setOnClickListener(this);
//        Button b2 = (Button) view.findViewById(R.id.delete_button);
//        b2.setOnClickListener(this);

//        if (mListener != null) {
//            //mListener.onContactListClicked();
//            switch (view.getId()) {
//                case R.id.accept_button:
//                    //Listener.onAddContactClicked();
//                    break;
//                case R.id.delete_button:
//                    //mListener.
//            }
//        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager( new LinearLayoutManager( context ) );
            } else {
                recyclerView.setLayoutManager( new GridLayoutManager( context, mColumnCount ) );
            }
            recyclerView.setAdapter( new MyConnectionReceiveRequestRecyclerViewAdapter(mConnectionRequestList, mListener) );
        }
        return view;
    }


    /**
     *
     * This method is for when need to get the current users E-mail address
     *
     * @return current users emaild address
     */
    public String getEmail(){
        return getArguments().getString("email");
    }


    /**
     * This method is the method connecting between fragment and activity.
     *
     * @param context that can be connected to activity.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnListFragmentInteractionListener" );
        }
    }

    /**
     * The method must be called immediately prior to
     * the fragment no longer being associated with its activity.
     *
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This method is click action method.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onConnectionReceiveRequestListFragmentInteraction(ConnectionRequestList item);
        String getEmail();
    }
}
