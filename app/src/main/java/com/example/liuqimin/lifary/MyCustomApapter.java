package com.example.liuqimin.lifary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015/8/4.
 */
public class MyCustomApapter extends ArrayAdapter<Diary> implements ListAdapter, Communication{

    private ArrayList<Diary> list = new ArrayList<Diary>();
    private Context context;
    int layoutResouceId;

    ImageView diaryImageView;
    TextView dateText, diaryText;
    ImageButton audioButton;
    boolean isPlaying = false;

    MediaPlayer mediaPlayer;

    Diary diary;

    String userid;



    public MyCustomApapter(Context context, int resource, ArrayList<Diary> objects) {
        super(context, resource, objects);
        list = new ArrayList<Diary>();
        this.context = context;
        this.layoutResouceId = resource;
        this.list = objects;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Diary getItem(int position) {
        return list.get(position);
    }

    public void setUserid(String s){
        userid = s;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.diary_fragment_layout, null);
        }

        isPlaying = false;
        diaryImageView = (ImageView)view.findViewById(R.id.diaryImageView);
        dateText = (TextView) view.findViewById(R.id.dateTextView);
        diaryText = (TextView) view.findViewById(R.id.diaryTextView);
        audioButton = (ImageButton) view.findViewById(R.id.audioImageButton);
        diary = new Diary(0);
        diary = list.get(position);
        readDiary(diary);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Lifary", "CustomAdapter: click title" );

                Diary d = list.get(position);
                int d_id = d.getId();
                Log.d("Lifary", "DiaryList: d_id = " + d_id);
                Intent i = new Intent(v.getContext(), DiaryView.class);
                Log.d("Lifary", "DiaryList: id = " + d_id);
                i.putExtra("DIARY_ID", d_id);
                i.putExtra("USER_ID", userid);
                v.getContext().startActivity(i);
            }
        });

        return view;
    }


    // Play Media File
    private void playMp3(byte[] mp3SoundByteArray)
    {
        try
        {

            File path=new File(context.getCacheDir()+"/musicfile.3gp");

            FileOutputStream fos = new FileOutputStream(path);
            fos.write(mp3SoundByteArray);
            fos.close();

            mediaPlayer = new MediaPlayer();

            FileInputStream fis = new FileInputStream(path);
            mediaPlayer.setDataSource(context.getCacheDir()+"/musicfile.3gp");

            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            audioButton.setImageResource(R.drawable.player_stop);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
        }
        catch (IOException ex)
        {
            String s = ex.toString();
            ex.printStackTrace();
            Log.d("Lifary", "DiaryView: play Mp3 ERROR: " + ex.getLocalizedMessage());
        }
    }

    // -------------------------- Stop Playing Media File --------------------------------
    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
        audioButton.setImageResource(R.drawable.play_sound);

    }

    @Override
    public void com(String contents) {

    }

    @Override
    public void readDiary(Diary d) {
        diary = d;
        Log.d("Lifary", "CustomAdapter: readDiary is called");

        if(diary != null) {
            Log.d("Lifary", "CustomAdapter: diary exists");
            dateText.setText(diary.getDate());
            if (!diary.getImage().equals("")) {
                try {

                    int width = 512;
                    Bitmap bitmap = diary.getImgBitmap();
                    Log.d("Lifary", "DiaryView: bitmap.getHeight = " + bitmap.getHeight() +
                            "\tbitmap.getWidth = " + bitmap.getWidth() +
                            "\t imageView width = " + width);
                    int nh = (int) ( bitmap.getHeight() * (width / bitmap.getWidth()) );
                    Log.d("Lifary", "DiaryView: nh = " + nh +
                            " result = " + (int)( bitmap.getHeight() * (512 / bitmap.getWidth()))  +
                            " result 2 = " +( bitmap.getHeight()*(512/bitmap.getWidth())));
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, nh, true);
                    diaryImageView.setImageBitmap(bitmap);
                    diaryImageView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.d("Lifary", "CustomAdapter: set diaryImageView ERROR: " + e.getLocalizedMessage());
                    diaryImageView.setVisibility(View.GONE);
                }
            }else{
                Log.d("Lifary", "CustomApater: no diary image");
                diaryImageView.setVisibility(View.GONE);
            }
            diaryText.setText(diary.getText());
            // set audioButton onClickListener

            if(!diary.getSound().equals("")) {
                audioButton.setVisibility(View.VISIBLE);
                audioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Lifary", "CustomApater: click button");
                        try {
                            if (v == audioButton && !isPlaying && diary.getAudio() != null) {
                                playMp3(diary.getAudio());
                            } else if (v == audioButton && isPlaying) {
                                stopPlaying();
                            } else if (v == audioButton && !isPlaying && diary.getAudio() == null) {
                                Toast.makeText(context, "Sorry, No Audio can be played", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.d("Lifary", "DiaryView: play audio ERROR: " + e.getLocalizedMessage());
                        }
                    }
                });
            }
            else{
                Log.d("Lifary", "CustomApater: audio == none");
                audioButton.setVisibility(View.GONE);
            }


        }
    }
}
