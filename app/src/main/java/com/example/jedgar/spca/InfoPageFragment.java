package com.example.jedgar.spca;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import android.widget.TextView;

public class InfoPageFragment extends Fragment implements /*View.OnClickListener*/ GetActionBarTitle {

    private FragmentTabHost tabHost;



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
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.info_page_fragment);

        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator(getString(R.string.general_tab),
                        getResources().getDrawable(R.drawable.tab_indicator_ab_example)),
                GeneralInfoFragment.class, arg1 );

        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator(getString(R.string.cat_tab),
                getResources().getDrawable(R.drawable.tab_indicator_ab_example)),
                CatInfoFragment.class, arg2);

        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag3", 3);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator(getString(R.string.dog_tab),
                getResources().getDrawable(R.drawable.tab_indicator_ab_example)),
                DogInfoFragment.class, arg3);

        Bundle arg4 = new Bundle();
        arg4.putInt("Arg for Frag4", 4);
        tabHost.addTab(tabHost.newTabSpec("Tab4").setIndicator(getString(R.string.others_tab),
                getResources().getDrawable(R.drawable.tab_indicator_ab_example)),
                OthersInfoFragment.class, arg4);

        //Apply color to tabs! (little underlining color)
        TabWidget widget = tabHost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_indicator_ab_example);

        }
        return tabHost;

       // View rootView = inflater.inflate(R.layout.info_page_fragment, container, false);
        //return rootView;
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
