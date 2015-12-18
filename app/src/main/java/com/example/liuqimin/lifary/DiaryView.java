package com.example.liuqimin.lifary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class DiaryView extends Activity implements View.OnClickListener, Communication{

    Diary diary;
    TextView date, share, content, location;
    ImageView img;
    ImageButton audioPlay;
    MediaPlayer mediaPlayer;

    boolean isPlaying = false;

    Firebase rootRef;
    double diaryCounter = 0;
    DiaryHelper targetPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_view);

        Log.d("Lifary", "enter onvertingasdf");
        //-------- db setups
        Firebase.setAndroidContext(this);
        rootRef = new Firebase("https://kimmyblog.firebaseio.com/");
        Firebase refA = rootRef.child("1").child("diary");
        //refA.addValueEventListener(this);
        //Log.d("Lifary", "diasdfasdf");

        refA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                diaryCounter = snapshot.getChildrenCount();
                //Log.d("fb", "there are " + diaryCounter + " diaries");
                int i = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //postSnapshot;
                    DiaryHelper post = postSnapshot.getValue(DiaryHelper.class);
                    Log.d("fb", "post print: " + i);
                    post.print();
                    if (post.getId() == 2) {
                        Log.d("fb", "hell no");
                        post.past(targetPost);
                        targetPost.print();
                        Log.d("fb", "post print: " + i);
                        break;
                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("fb", "The read failed: " + firebaseError.getMessage());
            }
        });

        //Log.d("fb", "there are " + diaryCounter + " diaries");
        targetPost.print();
        //Diary targetDiary = new Diary(0);
        //targetDiary.convert(targetPost);
        //targetDiary.print();

        date = (TextView) findViewById(R.id.timeTextView);
        share = (TextView) findViewById(R.id.shareTextView);
        content = (TextView) findViewById(R.id.diaryTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        img = (ImageView) findViewById(R.id.diaryImageView);
        audioPlay = (ImageButton) findViewById(R.id.playAudioButton);
        audioPlay.setOnClickListener(this);
        diary = null;

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int id = extra.getInt("DIARY_ID");
            String url = extra.getString("ajslajl");
            Log.d("Lifary", "diary get the date: id = " + id);
            DiaryDBHandler myDiaryDBHandler = new DiaryDBHandler(this, null, null, 1);
            try {
                diary = myDiaryDBHandler.findDiaryByID(id);
                Log.d("Lifary", "found diary");
            }catch (Exception e){
                Log.d("Lifary", "diary found ERROR: " + e.getLocalizedMessage());
            }
        }
        if(diary != null){

            date.setText(diary.getDate());      // set date
            content.setText(diary.getContent());    // set diary text

            // set share
            if(diary.getShare() == 0){share.setText("privately");}
            else{share.setText("publicly");}

            // get location
            float longitude = diary.getLongitude();
            float latitude = diary.getLatitude();

            if(longitude == 0 || latitude == 0){
                location.setText("not available");
            }
            else {
                Communication cc = (Communication) this;
                ReadLocation readLocation = new ReadLocation(this, cc);
                readLocation.getLocation("" + latitude, "" + longitude);
            }

            try{
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                Bitmap bitmap = diary.getImgBitmap();
                Log.d("Lifary", "DiaryView: bitmap.getHeight = " + bitmap.getHeight() +
                        "\tbitmap.getWidth = " + bitmap.getWidth() +
                        "\t imageView width = " + width);
                int nh = (int) ( bitmap.getHeight() * (width / bitmap.getWidth()) );
                Log.d("Lifary", "DiaryView: nh = " + nh +
                        " result = " + (int)( bitmap.getHeight() * (512 / bitmap.getWidth()))  +
                        " result 2 = " +( bitmap.getHeight()*(512/bitmap.getWidth())));
                bitmap = Bitmap.createScaledBitmap(bitmap, width, nh, true);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                Log.d("Lifary", "DiaryView: bitmap is loaded ");
            }catch (Exception e){
                Log.d("Lifary", "failed to load image ERROR: " + e.getLocalizedMessage());
                img.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diary_view, menu);
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
    public void onClick(View v) {
        try {
            if (v == audioPlay && !isPlaying && diary.getAudio() != null) {
                playMp3(diary.getAudio());
            } else if (v == audioPlay && isPlaying) {
                stopPlaying();
            }else if (v == audioPlay && !isPlaying && diary.getAudio() == null){
                Toast.makeText(this, "Sorry, No Audio can be played", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.d("Lifary", "DiaryView: play audio ERROR: " + e.getLocalizedMessage());
        }

    }

    @Override
    public void com(String contents) {
        location.setText(contents);
    }

    private void playMp3(byte[] mp3SoundByteArray)
    {
        try
        {

            File path=new File(getCacheDir()+"/musicfile.3gp");

            FileOutputStream fos = new FileOutputStream(path);
            fos.write(mp3SoundByteArray);
            fos.close();

            mediaPlayer = new MediaPlayer();

            FileInputStream fis = new FileInputStream(path);
            mediaPlayer.setDataSource(getCacheDir()+"/musicfile.3gp");

            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            audioPlay.setImageResource(R.drawable.player_stop);
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
            Log.d("Lifary", "DiaryView: play Mp3 ERROR: " + ex.getLocalizedMessage() );
        }
    }

    // -------------------------- Stop Playing Media File --------------------------------
    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
        audioPlay.setImageResource(R.drawable.play_sound);

    }

}
