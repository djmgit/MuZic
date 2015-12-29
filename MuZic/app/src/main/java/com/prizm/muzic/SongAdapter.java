package com.prizm.muzic;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by deep on 12/29/15..
 */
public class SongAdapter extends ArrayAdapter<File> {
    private ArrayList<File> songlist;
    private LayoutInflater inflater;
    MediaMetadataRetriever metaRetriver;
    public SongAdapter(Context context, int textViewResourceId,
                       ArrayList<File> songlist) {
        super(context, textViewResourceId,songlist);
        //TODO Auto-generated constructor stub
        inflater = ((Activity)context).getLayoutInflater();
        this.songlist=songlist;
    }
    private static class ViewHolder {
        TextView name;
        TextView artist;
        TextView album;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.song_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.song_title);
            viewHolder.album=(TextView)convertView.findViewById(R.id.song_album);
            viewHolder.artist=(TextView)convertView.findViewById(R.id.song_artist);


            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder)convertView.getTag();
        //load image directly
       /* Bitmap imageBitmap = null;
        try {
            URL imageURL = new URL(imageURLArray[position]);
            imageBitmap = BitmapFactory.decodeStream(imageURL.openStream());
            viewHolder.imageView.setImageBitmap(imageBitmap);
        } catch (IOException e) {
            // TODO: handle exception
            Log.e("error", "Downloading Image Failed");
            viewHolder.imageView.setImageResource(R.drawable.postthumb_loading);
        }*/

        viewHolder.name.setText(songlist.get(position).getName());
        metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(songlist.get(position).getAbsolutePath());
        try {
            viewHolder.album.setText(metaRetriver
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));

            viewHolder.artist.setText(metaRetriver
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

        } catch (Exception e) {

            viewHolder.album.setText("Unknown Album");
            viewHolder.artist.setText("Unknown Artist");

        }





        return convertView;
    }
}