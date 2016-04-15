package br.com.pi.pi4.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.pi.pi4.R;
import br.com.pi.pi4.SntpClient;
import br.com.pi.pi4.entity.ParticipanteGrupo;
import br.com.pi.pi4.rest.PIService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GroupListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView = null;
    private ArrayList<ParticipanteGrupo> participanteGrupo = new ArrayList<ParticipanteGrupo>();;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupListFragment newInstance(int columnCount) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private void handleInjectNtpTime() {

        SntpClient client = new SntpClient();
        long delay;

        if (client.requestTime("a.st1.ntp.br", 20000)) {
            long time = client.getNtpTime();
            long timeReference = client.getNtpTimeReference();
            int certainty = (int)(client.getRoundTripTime()/2);
            long now = System.currentTimeMillis();
            long systemTimeOffset = time - now;

            Log.d("time", "NTP server returned: "
                    + time + " (" + new Date(time)
                    + ") reference: " + timeReference
                    + " certainty: " + certainty
                    + " system time offset: " + systemTimeOffset);

            // sanity check NTP time and do not use if it is too far from system time
            if (systemTimeOffset < 0) {
                systemTimeOffset = -systemTimeOffset;
            }


        } else {
             Log.d("time", "requestTime failed");

        }


    }

    public void refreshListaParticipantes (ArrayList<ParticipanteGrupo> participantes) {
        this.participanteGrupo = participantes;
        if (recyclerView != null) {
            MyItemRecyclerViewAdapter irva = (MyItemRecyclerViewAdapter) recyclerView.getAdapter();
            irva.setmValues(participantes);
            irva.notifyDataSetChanged();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


        //List<ParticipanteGrupo> lpg = new ArrayList<ParticipanteGrupo>();



        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(participanteGrupo, mListener));

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ParticipanteGrupo item);
    }
}
