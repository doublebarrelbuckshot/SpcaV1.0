package com.example.jedgar.spcav10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowsePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrowsePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */




public class BrowsePageFragment extends Fragment implements GetActionBarTitle {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String cameFrom;
    private int titleID;
    private LayoutInflater inflater;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static SearchCriteria sc;

    private OnFragmentInteractionListener mListener;
    DBHelper dbh;
    SQLiteDatabase db;

    private Cursor c;

    BaseAdapter adapter = null;


    public interface OnDetailsListener {
        public void doDetails(String cursorName, int pos);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment BrowsePageFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static BrowsePageFragment newInstance() {
        BrowsePageFragment fragment = new BrowsePageFragment();
        return fragment;
    }





    public BrowsePageFragment() {
        // Required empty public constructor
    }

//    public BrowsePageFragment(String title){
//        super();
//        if(title.equals("Favorites"))
//            titleID = R.string.favoritesTitle;
//        else titleID = R.string.searchResults;
//    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }*/

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem refreshMI = menu.findItem(R.id.refresh);
        if (refreshMI != null) {
            refreshMI.setVisible(false);
        }

        if (menu.findItem(R.id.removeAllMI) != null) {
            if (cameFrom.equals("Favorites") || cameFrom.equals("New")) {
                if (c.getCount() > 0) {  // show bin only if there is something to clear
                    menu.findItem(R.id.removeAllMI).setVisible(true);
                    menu.findItem(R.id.removeAllMI).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (cameFrom.equals("Favorites")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.clearFavs)
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dbh.removeAllFromFavoriteList(db);
                                                Toast.makeText(getActivity(), "All Favorites Cleared", Toast.LENGTH_SHORT).show();
                                                c = dbh.getFavoriteList(db);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                builder.create();
                                builder.show();
                            } else if (cameFrom.equals("New")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.clearNew)
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dbh.removeAllFromNewList(db);
                                                Toast.makeText(getActivity(), "All New Cleared", Toast.LENGTH_SHORT).show();
                                                c = dbh.getNewAnimalsList(db);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                builder.create();
                                builder.show();
                            }
                            return false;
                        }
                    });
                }
            }
        }

        //((MainActivity)getActivity()).displaySystemStatus(this, menu);

        return;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbh = DBHelper.getInstance(getActivity());
        db = dbh.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse_page, container, false);
        this.inflater = inflater;

        setHasOptionsMenu(true);

        Intent intent = ((MainActivity)BrowsePageFragment.this.getActivity()).getIntent();
        Bundle data = intent.getExtras();
        String sender = data.getString("sender");
        String emptyListMsg;
        if (sender.equals(DBHelper.CURSOR_NAME_FAVORITE_ANIMALS)) {
            Log.d("Browse", "Favorites");
            dbh.setCursorForFavoriteList(db);
            c = dbh.getCursorForFavoriteList();
            cameFrom = "Favorites";
            emptyListMsg = getString(R.string.emptyListFavoritesMSG);
        } else if (sender.equals(DBHelper.CURSOR_NAME_NEW_ANIMALS)) {
            Log.d("Browse","New");
            SearchCriteria sc = new SearchCriteria(db);
            String sql = new String(sc.getCommandForNotifs());
            dbh.setCursorForSelect(db, sql, DBHelper.CURSOR_NAME_NEW_ANIMALS);
            c = dbh.getCursorForSelect(DBHelper.CURSOR_NAME_NEW_ANIMALS);
            /*
            dbh.setCursorForNewAnimalsList(db);
            c = dbh.getCursorForNewAnimalsList();
            */
            cameFrom = "New";
            emptyListMsg = getString(R.string.emptyListNewMSG);
        } else {
            Log.d("Browse","else");
            // do default search
            SearchCriteria sc = (SearchCriteria) data.getParcelable("SearchCriteria");
            String sql = new String(sc.getSelectCommand());
            dbh.setCursorForSelect(db, sql, DBHelper.CURSOR_NAME_SEARCH_ANIMALS);
            c = dbh.getCursorForSelect(DBHelper.CURSOR_NAME_SEARCH_ANIMALS);
            cameFrom = "Search";
            emptyListMsg = getString(R.string.emptyListMSG);
        }

        // Getting adapter by passing xml data ArrayList
        ListView list = (ListView) rootView.findViewById(R.id.browseView);
        TextView tv = (TextView)rootView.findViewById(R.id.emptyList);
        tv.setText(emptyListMsg);
        list.setEmptyView(tv);
        if (adapter == null)
            adapter = new LazyAdapter(cameFrom);
        list.setAdapter(adapter);

        // now that we have a valid cursor...
        if (sender.equals(DBHelper.CURSOR_NAME_FAVORITE_ANIMALS)) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view,
                                        int position, long id) {
                    ((MainActivity) BrowsePageFragment.this.getActivity()).doDetails(DBHelper.CURSOR_NAME_FAVORITE_ANIMALS, position);
                }
            });

        } else if (sender.equals(DBHelper.CURSOR_NAME_NEW_ANIMALS)) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view,
                                        int position, long id) {
                    ((MainActivity) BrowsePageFragment.this.getActivity()).doDetails(DBHelper.CURSOR_NAME_NEW_ANIMALS, position);
                }
            });
        } else {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view,
                                        int position, long id) {
                    ((MainActivity) BrowsePageFragment.this.getActivity()).doDetails(DBHelper.CURSOR_NAME_SEARCH_ANIMALS, position);
                }
            });
        }

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
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public int getActionBarTitleId() {
        if(cameFrom.equals("Favorites")){
            return R.string.favoritesTitle;

        }
        else if(cameFrom.equals("New")){
                   return R.string.newTitle;

        }
        else //cameFrom equals search
                   return R.string.searchResultsTitle;
    }
//
//    @Override
//    public int getActionBarTitleId() {
//        return titleID;
//    }



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




    private class LazyAdapter extends BaseAdapter {

        String typeOfView = "";
        public LazyAdapter(String cameFrom) {
            this.typeOfView = cameFrom;
        }

        public int getCount() {
            return c.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LinearLayout vi = (LinearLayout) inflater.inflate(R.layout.browse_row, null);
            c.moveToPosition(position);
            String animalIDStr = Integer.toString(c.getInt(DBHelper.C_ANIMAL_ID));
            String d = c.getString(DBHelper.C_ANIMAL_NAME);
            String p1 = c.getString(DBHelper.C_ANIMAL_PHOTO1);
            String a = c.getString(DBHelper.C_ANIMAL_AGE);
            if(p1 != null && !p1.equals("")) {
                ImageView animalImage = (ImageView) vi.findViewById(R.id.animalImage);
                Picasso.with(getActivity().getApplicationContext()).load(p1).into(animalImage);
            }


            if(typeOfView.equals("Favorites")) {

                String isFavAvailable = c.getString(DBHelper.C_FAVORITE_ANIMAL_AVAILABLE);
                if(isFavAvailable.equals("N")){
                    TextView adoptedOverlayTV = (TextView)vi.findViewById(R.id.adoptedOverlayTV);
                    adoptedOverlayTV.setVisibility(View.VISIBLE);

                }
            }

            TextView animalID = (TextView) vi.findViewById(R.id.animalID);
            TextView animalName = (TextView) vi.findViewById(R.id.animalName);
            TextView animalAge = (TextView) vi.findViewById(R.id.animalAge);

            animalID.setText("ID: "+animalIDStr);
            animalName.setText("Name: "+d);
            animalAge.setText("Age: :"+a);


            ImageButton btnFav = (ImageButton)vi.findViewById(R.id.btnFav);
            boolean isFav;
            if(dbh.isFavorite(db,animalIDStr )){
                isFav = true;
                btnFav.setImageResource(R.drawable.ic_favorite_black_36dp);
            }
            else{
                btnFav.setImageResource(R.drawable.ic_favorite_outline_black_36dp);
                isFav = false;
            }
            btnFav.setTag(R.id.favTag, Boolean.valueOf(isFav));
            btnFav.setTag(R.id.anIDTag, animalIDStr);
            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton btnFav=(ImageButton)v;
                    boolean isFav=((Boolean)btnFav.getTag(R.id.favTag)).booleanValue();
                    String anID = (String)btnFav.getTag(R.id.anIDTag);

                    if(!isFav) {
                        btnFav.setImageResource(R.drawable.ic_favorite_black_36dp);
                        dbh.addToFavoriteList(db, anID);
                        // Toast.makeText(getActivity(), ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID, Toast.LENGTH_LONG).show();
                        Log.d("ISFAV", ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID);
                        isFav = true;
                    }
                    else{
                        btnFav.setImageResource(R.drawable.ic_favorite_outline_black_36dp);
                        isFav = false;
                        dbh.removeFromFavoriteList(db, anID);
                        //Toast.makeText(getActivity(), ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID, Toast.LENGTH_LONG).show();
                        Log.d("ISFAV", ""+dbh.isFavorite(db, anID) + "....AnimalID: " + anID);
                    }
                    btnFav.setTag(R.id.favTag, Boolean.valueOf(isFav));
                }
            });
            return vi;
        }
    }

}