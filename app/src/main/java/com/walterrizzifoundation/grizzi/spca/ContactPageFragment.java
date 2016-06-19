package com.walterrizzifoundation.grizzi.spca;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.walterrizzifoundation.grizzi.spca.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by JEdgar on 4/19/2015.
 */
public class ContactPageFragment extends Fragment implements View.OnClickListener, GetActionBarTitle {

    /*
     *
     * ONLY FOR CALL AND EMAIL BUTTONS
     *
     */
    ImageButton buttonSend;
    ImageButton appel_b;
    String fone = "5147352711";
    String emailAddress = "adoption@spca.com";
    //TextView send;
    //TextView fone;

    TextView ident;
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
         /*
         *
         * ONLY FOR CALL AND EMAIL BUTTONS
         *
         */
        //send =(TextView)rootView.findViewById(R.id.send);
        //fone =(TextView)rootView.findViewById(R.id.fone);
        appel_b = (ImageButton)rootView.findViewById(R.id.appel_b);
        buttonSend = (ImageButton)rootView.findViewById(R.id.buttonSend);
        appel_b.setOnClickListener(ContactPageFragment.this);
        buttonSend.setOnClickListener(ContactPageFragment.this);

        ident =(TextView)rootView.findViewById(R.id.ident);
        LinearLayout idLayout = (LinearLayout)rootView.findViewById(R.id.idLayout);
        animalImage = (ImageView)rootView.findViewById(R.id.animalImage);


        if(animalID.equals("")){
            idLayout.setVisibility(View.GONE);
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

        /*
         *
         * ONLY FOR CALL AND EMAIL BUTTONS
         *
         */

        if(v.getId()==R.id.buttonSend){

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"adoption@spca.com"});

            PackageManager pm = getActivity().getPackageManager();
            Intent openInChooser = Intent.createChooser(emailIntent,getResources().getText(R.string.emailSPCAtitle));

            List<ResolveInfo> resInfo = pm.queryIntentActivities(emailIntent, 0);
            List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
            for (int i = 0; i < resInfo.size(); i++) {
                ResolveInfo ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                if (packageName.contains("android.email")) {
                    emailIntent.setPackage(packageName);
                }
                else if (packageName.contains("android.gm")) { // If Gmail shows up twice, try removing this else-if clause and the reference to "android.gm" above
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"adoption@spca.com"});
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("message/rfc822");

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                }
            }
            LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            startActivity(openInChooser);
        }

        if(v.getId()==R.id.appel_b){

            String tel = fone;
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
