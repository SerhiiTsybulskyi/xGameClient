package c4r0n0s.xgameclient.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Objects;
import c4r0n0s.xgameclient.Constants;
import c4r0n0s.xgameclient.R;
import c4r0n0s.xgameclient.services.TaskManagerService;
import c4r0n0s.xgameclient.utils.ServiceTools;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public StatusFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_status, container, false);
        final TextView statusTextView = view.findViewById(R.id.statusTextView);
        boolean isServiceRunning = ServiceTools.isServiceRunning(Objects.requireNonNull(getContext()), TaskManagerService.class);
        statusTextView.setText("Service is running: " + isServiceRunning);

        Button startButton = view.findViewById(R.id.startServiceButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                startForegroundService();
                boolean isServiceRunning = ServiceTools.isServiceRunning(Objects.requireNonNull(getContext()), TaskManagerService.class);
                statusTextView.setText("Service is running: " + isServiceRunning);
            }
        });

        Button stopButton = view.findViewById(R.id.stopServiceButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                stopForegroundService();
                boolean isServiceRunning = ServiceTools.isServiceRunning(Objects.requireNonNull(getContext()), TaskManagerService.class);
                statusTextView.setText("Service is running: " + isServiceRunning);
            }
        });

        return view;
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
        void onFragmentInteraction(Uri uri);
    }


    private void startForegroundService() {
        Intent taskManagerServiceIntent = new Intent(getActivity(), TaskManagerService.class);
        taskManagerServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        Objects.requireNonNull(getActivity()).startService(taskManagerServiceIntent);
    }

    private void stopForegroundService() {
        Intent taskManagerServiceIntent = new Intent(getActivity(), TaskManagerService.class);
        taskManagerServiceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        Objects.requireNonNull(getActivity()).startService(taskManagerServiceIntent);
        getActivity().stopService(taskManagerServiceIntent);
    }
}
