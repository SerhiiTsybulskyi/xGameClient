package c4r0n0s.xgameclient.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import c4r0n0s.xgameclient.MainActivity;
import c4r0n0s.xgameclient.R;
import c4r0n0s.xgameclient.entities.AccountEntity;
import c4r0n0s.xgameclient.services.AccountManagerService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private static String TAG = AccountFragment.class.getName();
    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabase;

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
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
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

        this.fillFields(inflate, AccountManagerService.getAccountSettings());

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
        CheckBox autoLogin = view.findViewById(R.id.checkBoxAutoLogin);
        EditText refreshTime = view.findViewById(R.id.refreshTimeText);
        EditText uniNumber = view.findViewById(R.id.uniNumber);

        String androidId = MainActivity.getAndroidId();
        AccountEntity accountEntity = new AccountEntity(
                androidId,
                loginText.getText().toString(),
                passwordText.getText().toString(),
                Integer.parseInt(refreshTime.getText().toString()) * 60000, // Convert min to ms
                Integer.parseInt(uniNumber.getText().toString()),
                autoLogin.isChecked()
        );
        mDatabase.child(androidId).setValue(accountEntity);

        Toast.makeText(this.getContext(), "Data was saved", Toast.LENGTH_SHORT).show();
    }

    private void displayCurrentAccountInfo(final View view) {
        mDatabase.child(MainActivity.getAndroidId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AccountEntity account = dataSnapshot.getValue(AccountEntity.class);
                if (account != null) {
                    fillFields(view, account);
                    TextView accountTextInfoUI = view.findViewById(R.id.accountInfoText);
                    accountTextInfoUI.setText(account.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void fillFields(View view, AccountEntity accountSettings) {
        if (Objects.nonNull(accountSettings)) {
            EditText loginText = view.findViewById(R.id.loginText);
            EditText passwordText = view.findViewById(R.id.passwordText);
            CheckBox autoLogin = view.findViewById(R.id.checkBoxAutoLogin);
            EditText refreshTime = view.findViewById(R.id.refreshTimeText);
            EditText uniNumber = view.findViewById(R.id.uniNumber);

            loginText.setText(accountSettings.login);
            passwordText.setText(accountSettings.password);
            autoLogin.setChecked(accountSettings.autoLogin);
            refreshTime.setText(Integer.toString(accountSettings.refreshTime / 60000));
            uniNumber.setText(Integer.toString(accountSettings.uniNumber));
        }
    }
}
