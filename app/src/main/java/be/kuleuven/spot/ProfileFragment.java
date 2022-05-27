package be.kuleuven.spot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView pf_username_top;
    private TextView pf_username;
    private TextView pf_email;
    private Button btn_changePassword;

    private String email;
    private String username;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pf_username_top = (TextView) getView().findViewById(R.id.pf_username_top);
        pf_username = (TextView) getView().findViewById(R.id.pf_username);
        pf_email = (TextView)getView().findViewById(R.id.pf_email);
        btn_changePassword = (Button) getView().findViewById(R.id.btn_changePassword);
        btn_changePassword.setOnClickListener(this);

        email = getActivity().getIntent().getExtras().getString("email");
        username = getActivity().getIntent().getExtras().getString("username");
        //Toast.makeText(getActivity(), email , Toast.LENGTH_LONG).show();

        pf_email.setText(email);
        pf_username.setText(username);
        pf_username_top.setText(username);
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), activity_changePassword.class);
        intent.putExtras(getActivity().getIntent().getExtras());
        startActivity(intent);
    }
}