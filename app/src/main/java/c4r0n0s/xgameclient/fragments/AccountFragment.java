package c4r0n0s.xgameclient.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import c4r0n0s.xgameclient.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    SharedPreferences sPref;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View inflate = inflater.inflate(R.layout.fragment_account, container, false);
        this.displayCurrentAccountInfo(inflate);
        inflate.findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountInfo(inflate);
            }
        });

        return inflate;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void saveAccountInfo(View view) {
        EditText loginText = view.findViewById(R.id.loginText);
        EditText passwordText = view.findViewById(R.id.passwordText);

        sPref = Objects.requireNonNull(this.getActivity()).getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor sEditor = sPref.edit();
        sEditor.putString("login", loginText.getText().toString());
        sEditor.putString("password", passwordText.getText().toString());

        sEditor.commit();
        Toast.makeText(this.getContext(), "Data saved", Toast.LENGTH_SHORT).show();
        this.displayCurrentAccountInfo(view);
    }

    private Map<String, String> loadCurrentAccountData() {
        sPref = Objects.requireNonNull(this.getActivity()).getPreferences(MODE_PRIVATE);
        Map<String, String> accountInfo = new HashMap<>();
        accountInfo.put("login", sPref.getString("login", ""));
        accountInfo.put("password", sPref.getString("password", ""));

        return accountInfo;
    }

    private void displayCurrentAccountInfo(View view) {
        Map<String, String> accountInfo = this.loadCurrentAccountData();
        TextView accountTextInfoUI = view.findViewById(R.id.accountInfoText);
        accountTextInfoUI.setText(accountInfo.toString());
    }
}
