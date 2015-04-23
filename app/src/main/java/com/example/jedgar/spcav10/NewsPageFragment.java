package com.example.jedgar.spcav10;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
        TextView newsHeadlineTV = (TextView)rootView.findViewById(R.id.newsHeadLineTV);
        TextView newsTextTV = (TextView)rootView.findViewById(R.id.newsTextTV);

        newsHeadlineTV.setText(Html.fromHtml(newsHeadline));
        newsTextTV.setText(Html.fromHtml(news));

        return rootView;
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


