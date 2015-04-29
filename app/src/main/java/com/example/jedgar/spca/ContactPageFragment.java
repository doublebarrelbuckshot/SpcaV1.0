package com.example.jedgar.spca;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by JEdgar on 4/19/2015.
 */
public class ContactPageFragment extends Fragment implements View.OnClickListener, GetActionBarTitle {

    ImageButton buttonSend;
    TextView ident;
    ImageButton appel_b;
    TextView send;
    TextView fone;
    ImageView animalImage;
    String animalID = "";
    String animalImageUrl = "";
    public static ContactPageFragment newInstance(){
        ContactPageFragment fragment = new ContactPageFragment();
        return fragment;
    }

    public static ContactPageFragment newInstance(String animalIDT, String animalImageUrl){

        ContactPageFragment fragment = new ContactPageFragment();
        fragment.animalID = animalIDT;
        if(animalImageUrl != null)
            fragment.animalImageUrl = animalImageUrl;
        return fragment;
    }

    public ContactPageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contact_page_fragment, container, false);
        // View rootView = inflater.inflate(R.layout.search_page_fragment, container, false);
        send =(TextView)rootView.findViewById(R.id.send);
        fone =(TextView)rootView.findViewById(R.id.fone);
        ident =(TextView)rootView.findViewById(R.id.ident);
        appel_b = (ImageButton)rootView.findViewById(R.id.appel_b);
        buttonSend = (ImageButton)rootView.findViewById(R.id.buttonSend);
        animalImage = (ImageView)rootView.findViewById(R.id.animalImage);
        appel_b.setOnClickListener(ContactPageFragment.this);
        buttonSend.setOnClickListener(ContactPageFragment.this);



        if(animalID.equals("")){
            ident.setVisibility(View.GONE);
        }
        else{
            ident.setText("ID: " + animalID);
        }

        if(animalImageUrl.equals("")){
            animalImage.setVisibility(View.GONE);
        }
        else{
            Picasso.with(getActivity().getApplicationContext()).load(animalImageUrl).into(animalImage);


        }


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(6);
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.buttonSend){
            //Toast.makeText(ContactPageFragment.this.getActivity(), "envoyer un courriel ", Toast.LENGTH_SHORT).show();

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

    @Override
    public int getActionBarTitleId() {
        return R.string.contactTitle;
    }



}
