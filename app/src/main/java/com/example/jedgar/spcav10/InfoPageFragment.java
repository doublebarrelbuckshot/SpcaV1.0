package com.example.jedgar.spcav10;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InfoPageFragment extends Fragment implements /*View.OnClickListener*/ GetActionBarTitle {
/*
    ImageButton buttonSend;
    TextView ident;
    ImageButton appel_b;
    TextView send;
    TextView fone;
*/
    String animalID = "";
    public static InfoPageFragment newInstance(){

        InfoPageFragment fragment = new InfoPageFragment();
        return fragment;
    }

    public static InfoPageFragment newInstance(String animalIDT){

        InfoPageFragment fragment = new InfoPageFragment();
        fragment.animalID = animalIDT;
        return fragment;
    }

    public InfoPageFragment(){}



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.info_page_fragment, container, false);
        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(2);
    }

 /*

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.buttonSend){
            Toast.makeText(InfoPageFragment.this.getActivity(), "envoyer un courriel ", Toast.LENGTH_SHORT).show();

            String to=ident.getText().toString();
            String message = send.getText().toString();
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ message});
            email.putExtra(Intent.EXTRA_TEXT, to);
            email.setType("message/rfc822");
            // startActivity(Intent.createChooser(email, "Choisissez un client de messagerie:"));
            startActivity(Intent.createChooser(email, message));
        }

        if(v.getId()==R.id.appel_b){

            String tel = fone.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + tel));
            startActivity(callIntent);
        }

    }
*/
    @Override
    public int getActionBarTitleId() {
        return R.string.infoTitle;
    }
}
