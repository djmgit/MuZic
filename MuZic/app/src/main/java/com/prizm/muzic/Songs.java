package com.prizm.muzic;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Songs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Songs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Songs extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView song_list;
    Button ref;
    SongAdapter songAdapter;
    ArrayList<File> songlist = new ArrayList<File>();
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Songs.
     */
    // TODO: Rename and change types and number of parameters
    public static Songs newInstance(String param1, String param2) {
        Songs fragment = new Songs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Songs() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_songs, container, false);

        song_list=(ListView)view.findViewById(R.id.song_list);
        ref=(Button)view.findViewById(R.id.ref);


        songAdapter=new SongAdapter(getActivity(),R.layout.song_item,songlist);
        song_list.setAdapter(songAdapter);
        new Songloader().execute();

        return view;

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
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public ArrayList<File> findSongs(File root)
    {
        File [] files = root.listFiles();
        ArrayList<File> al = new ArrayList<File>();
        for(File singleFile : files)
        {
            if(singleFile.isDirectory()&& !singleFile.isHidden())
            {
                al.addAll(findSongs(singleFile));
            }
            else
            {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav"))
                {
                    al.add(singleFile);
                }
            }
        }
        return  al;
    }



    // backend thread
    private class Songloader extends AsyncTask<String, Integer, ArrayList<File>> {
        private int currentTag;
        protected ArrayList<File> doInBackground(String... params) {
            // TODO Auto-generated method stub
            ArrayList<File> songs;
            songs=findSongs(Environment.getExternalStorageDirectory());
            return songs;

        }

        @Override
        protected void onPostExecute(ArrayList<File> result) {
            // TODO Auto-generated method stub
            for (int i = 0; i < result.size(); i++) {//ignore this comment >
                songlist.add(result.get(i));

            }
            songAdapter.notifyDataSetChanged();
            //rss_list.setVisibility(View.VISIBLE);
            //prog.setVisibility(View.INVISIBLE);
            //postAdapter = new PostAdapter(getActivity(),R.layout.postitem,postList);
            //rss_list.setAdapter(postAdapter);
        }
    }

    // TODO: R

}
