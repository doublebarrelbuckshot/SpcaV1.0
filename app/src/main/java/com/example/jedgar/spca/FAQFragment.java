package com.example.jedgar.spca;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
public class FAQFragment extends Fragment implements View.OnClickListener, GetActionBarTitle {

    /*
     *
     * ONLY FOR CALL AND EMAIL BUTTONS
     *
     */
    //ImageButton buttonSend;
    //ImageButton appel_b;
    //TextView send;
    //TextView fone;

    //TextView taTitle;
    TextView taText;

    String faqText;
    //String faqTitle;
    public static FAQFragment newInstance(){
        FAQFragment fragment = new FAQFragment();
        return fragment;
    }

    public FAQFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_faq, container, false);
         /*
         *
         * ONLY FOR CALL AND EMAIL BUTTONS
         *
         */
        //send =(TextView)rootView.findViewById(R.id.send);
        //fone =(TextView)rootView.findViewById(R.id.fone);
        //appel_b = (ImageButton)rootView.findViewById(R.id.appel_b);
        //buttonSend = (ImageButton)rootView.findViewById(R.id.buttonSend);
        //appel_b.setOnClickListener(ContactPageFragment.this);
        //buttonSend.setOnClickListener(ContactPageFragment.this);

        //taTitle =(TextView)rootView.findViewById(R.id.title_text);
        taText = (TextView)rootView.findViewById(R.id.html_text);

        faqText = getString(R.string.faqText);
        //faqTitle =  getString(R.string.faqTitle);

        taText.setText(Html.fromHtml(getString(R.string.faqText)));
        //taTitle.setText(Html.fromHtml(faqTitle));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(8);
    }


    @Override
    public void onClick(View v) {

        /*
         *
         * ONLY FOR CALL AND EMAIL BUTTONS
         *
         */

//        if(v.getId()==R.id.buttonSend){
//            //Toast.makeText(ContactPageFragment.this.getActivity(), "envoyer un courriel ", Toast.LENGTH_SHORT).show();
//
//            String to=ident.getText().toString();
//            String message = send.getText().toString();
//            Intent email = new Intent(Intent.ACTION_SEND);
//            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ message});
//            email.putExtra(Intent.EXTRA_TEXT, to);
//            email.setType("message/rfc822");
//            // startActivity(Intent.createChooser(email, "Choisissez un client de messagerie:"));
//            startActivity(Intent.createChooser(email, message));
//        }
//
//        if(v.getId()==R.id.appel_b){
//
//            String tel = fone.getText().toString();
//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            callIntent.setData(Uri.parse("tel:" + tel));
//            startActivity(callIntent);
//        }

    }

    @Override
    public int getActionBarTitleId() {
        return R.string.faqTitle;
    }



}
