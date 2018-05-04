package c4r0n0s.xgameclient.services;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import c4r0n0s.xgameclient.MainActivity;
import c4r0n0s.xgameclient.entities.AccountEntity;

public class AccountManagerService {
    private static String LOG_TAG = AccountManagerService.class.getName();
    private static final AccountManagerService ourInstance = new AccountManagerService();
    private static AccountEntity accountSettings;

    private AccountManagerService() {}

    public static AccountManagerService getInstance() {
        return ourInstance;
    }

    public static void loadAccount(Context context) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        mDatabase.child(MainActivity.getAndroidId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountSettings = dataSnapshot.getValue(AccountEntity.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public static AccountEntity getAccountSettings() {
        return accountSettings;
    }
}
