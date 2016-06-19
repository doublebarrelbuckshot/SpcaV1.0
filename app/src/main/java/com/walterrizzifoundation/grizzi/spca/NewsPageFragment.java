package com.walterrizzifoundation.grizzi.spca;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.walterrizzifoundation.grizzi.spca.R;


public class NewsPageFragment extends Fragment implements GetActionBarTitle{

    String newsHeadline;
    String news;
    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static NewsPageFragment newInstance() {
        NewsPageFragment fragment = new NewsPageFragment();
        return fragment;
    }

    public static NewsPageFragment newInstance(String newsHeadline, String news){

        NewsPageFragment fragment = new NewsPageFragment();
        fragment.newsHeadline = newsHeadline;
        fragment.news = news;
        return fragment;
    }

    public NewsPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_news_page, container, false);
        WebView newsHeadlineTV = (WebView)rootView.findViewById(R.id.newsHeadLineTV);
        WebView newsTextTV = (WebView)rootView.findViewById(R.id.newsTextTV);

//        WebSettings settings = newsHeadlineTV.getSettings();
//        settings.setDefaultTextEncodingName("utf-8");
//
//        settings = newsTextTV.getSettings();
//        settings.setDefaultTextEncodingName("utf-8");

        newsHeadlineTV.loadData(newsHeadline, "text/html; charset=utf-8", null);//, null, null);
        newsTextTV.loadData(news, "text/html; charset=utf-8", null);//, null, null);
        //newsHeadlineTV.setText(trimTrailingWhitespace(Html.fromHtml(newsHeadline)));
        //newsTextTV.setText(Html.fromHtml(news));

        return rootView;
    }

    public static CharSequence trimTrailingWhitespace(CharSequence source) {
        if(source == null)
            return "";
        int i = source.length();

        // loop back to the first non-whitespace character
        while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }
        return source.subSequence(0, i+1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).onSectionAttached(7);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public int getActionBarTitleId() {
        return R.string.newsTitle;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}


