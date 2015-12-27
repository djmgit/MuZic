package com.prizm.muzic;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;


public class Player extends Activity implements View.OnClickListener {
    static MediaPlayer mp;
    int position; // position of the song in the list.
    ArrayList<File> mySongs;  // the array list which will hold the list of all songs in the device
    SeekBar sb;
    public Uri u;
    Thread updateSeekBar;
    Button btPlay, btFF, btPv, btNxt,btFB;
    TextView songName, artist, album;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // setting up ids for each button
        btPlay = (Button)findViewById(R.id.btPlay);
        btFF = (Button)findViewById(R.id.btFF);
        btPv = (Button)findViewById(R.id.btPv);
        btNxt = (Button)findViewById(R.id.btNxt);
        btFB = (Button)findViewById(R.id.btFB);

        // Setting up ids for each Text View
        songName = (TextView)findViewById(R.id.songName);
        artist = (TextView)findViewById(R.id.artist);
        album = (TextView)findViewById(R.id.album);

        // setting up id of the seekbar
        sb = (SeekBar)findViewById(R.id.seekBar);
        updateSeekBar = new Thread()
        {
            public void run()
            {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                //sb.setMax(totalDuration); // set the maximum length of the Seek Bar to be that of the Song size
                while (currentPosition<totalDuration)
                {
                    try
                    {
                        sleep(500);
                        currentPosition=mp.getCurrentPosition(); // updating the progress of the music
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };

        if(mp!=null) // checking if the list is called when another song is already running
        {
            mp.stop(); // stop the currently playing song
            mp.release();
        }
        // setting onClickListener to each Button
        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btPv.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btFB.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        // getting the songs list from the MainActivity.java class
        mySongs = (ArrayList)b.getParcelableArrayList("songslist");
        // getting the position of the song
        position = b.getInt("pos",0);

        // Testing to get the name
        songName.setText(mySongs.get(position).getName());
        // end of test

        u = Uri.parse(mySongs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(), u);
        mp.start(); // start playing the song

        sb.setMax(mp.getDuration()); // set the maximum length of the Seek Bar to be that of the Song size

        updateSeekBar.start(); // starting the seekBar

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.btPlay :
                if(mp.isPlaying())
                {
                    btPlay.setText(">");
                    mp.pause();
                }
                else
                {
                    btPlay.setText("||");
                    mp.start();
                }
                break;
            case R.id.btFF :
                mp.seekTo(mp.getCurrentPosition()+5000); // Fast Forward to 5 seconds
                break;
            case R.id.btFB :
                mp.seekTo(mp.getCurrentPosition()-5000); // Fast Forward to 5 seconds
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                btPlay.setText("||");
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start(); // start playing the song
                getSongName(); // changing the song name
                sb.setMax(mp.getDuration()); // set the maximum length of the Seek Bar to be that of the Song size
                //updateSeekBar.start(); // starting the seekBar
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position-1<0) ? mySongs.size()-1 : position-1;
                u = Uri.parse(mySongs.get(position).toString());
                btPlay.setText("||");
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start(); // start playing the song
                sb.setMax(mp.getDuration()); // set the maximum length of the Seek Bar to be that of the Song size
                getSongName(); // changing the song name
                //updateSeekBar.start(); // starting the seekBar
                break;
        }
    }

    public void getSongName()
    {
        songName.setText(mySongs.get(position).getName());
    }
}
