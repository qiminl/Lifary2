package com.example.liuqimin.lifary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;


public class EditDiaryActivity extends Activity implements View.OnClickListener, Communication{

    public final static String DEBUG = "Lifary";
    public final static int RESULT_TAKE_PHOTO = 1;
    public final static int RESULT_LOAD_IMG = 2;
    private SessionManager session;
    private String imagePath;
    private Uri soundUri;
    private Diary d;

    EditText diaryText;
    ImageButton cameraButton;
    ImageButton deleteImageButton;
    ImageButton soundButton;
    ImageButton deleteSoundButton;
    TextView soundTextView;
    ImageButton locationButton;
    TextView addressTextView;
    RelativeLayout shareLayout;
    TextView shareStatusTextView;
    Button submitButton;

    Bitmap bitmap;
    private static String mFileName = null;
    String imgDecodableString;


    MediaRecorder mRecorder = null;
    MediaPlayer mPlayer =null;
    boolean isRecorded = false;
    ImageButton playButton;
    boolean isRecording = false;
    boolean isPlaying = false;

    Location currentLocation;
    float latitude = 0;
    float longtitude = 0;
    int shareSelect = 0;  // 0 stands for privately, 1 stands for publicly

    byte[] audioByte;

    ReadLocation readLocation;

    Firebase rootRef;

    double diaryCounter = 1;
    private boolean flag = false;


    // --------------------- Create Activity -------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG, "EditDiary created is called");

        super.onCreate(savedInstanceState);

        // user control
        session  = new SessionManager(getApplicationContext());
        Log.d(DEBUG, "EditDiary super created is called");

        setContentView(R.layout.activity_edit_diary);

        Log.d(DEBUG, "EditDiary is created");
        diaryText = (EditText) findViewById(R.id.diaryEditText);

        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(this);
        deleteImageButton = (ImageButton) findViewById(R.id.deleteImageButton);
        deleteImageButton.setOnClickListener(this);

        soundButton = (ImageButton) findViewById(R.id.soundButton);
        soundButton.setOnClickListener(this);
        deleteSoundButton = (ImageButton) findViewById(R.id.deleteSoundButton);
        deleteSoundButton.setOnClickListener(this);
        soundTextView = (TextView) findViewById(R.id.soundSecTextView);
        playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        locationButton = (ImageButton) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(this);

        shareLayout = (RelativeLayout) findViewById(R.id.shareLayout);
        shareLayout.setOnClickListener(this);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        addressTextView = (TextView) findViewById(R.id.adressTextView);

        shareStatusTextView = (TextView) findViewById(R.id.shareStatusTextView);

            Log.d(DEBUG, "created done1");
            Communication cc = (Communication) this;
            Log.d(DEBUG, "created done 2");
            readLocation = new ReadLocation(this, cc);
            Log.d(DEBUG, "created done 3");

        Firebase.setAndroidContext(this);
        rootRef = new Firebase("https://kimmyblog.firebaseio.com/");
        Firebase refA = rootRef.child("1").child("diary");
        //Firebase refA = new Firebase("https://kimmyblog.firebaseio.com/1/");
        refA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                diaryCounter = snapshot.getChildrenCount();
                //Log.d("fb", "there are " + diaryCounter + " diaries");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //postSnapshot;
                    //Log.d("fb", "there are " + diaryCounter + " diaries");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("fb","The read failed: " + firebaseError.getMessage());
            }
        });
        //Log.d("fb", "@editDiary there are " + diaryCounter + " diaries");

    }

    // -------------------------- Create menu in Actionbar ----------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_diary, menu);
        return true;
    }

    // --------------------------- When Option in menu is selected ----------------------
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

    // -------------------------- On Click Handler --------------------------------------
    @Override
    public void onClick(View v) {

        // ---------------------- Camera Button ------------------------------------------
        if(v ==  cameraButton){
            Log.d("Lifary", "cameraButton is clicked");

            selectImage();

            // ----------------- Delete Image Button ----------------------------------
        }else if(v == deleteImageButton){
            Log.d("Lifary", "deleteImageButton clicked");
            cameraButton.setImageResource(R.drawable.camera);
            bitmap =null;
            // ------------- Record Sound Button -------------------------------------------
        }else if(v == soundButton){
            Log.d("Lifary", "soundButton clicked");
            // if it is recording, then stop
            if(!isPlaying) {
                if (!isRecording) {
                    startRecording();
                } else {  // else then start
                    stopRecording();
                }
            }

            // -------------------- play Audio Button -----------------------------------------------
        }else if(v == playButton){
            Log.d(DEBUG, "playButton clicked");
            if(isRecorded && !isRecording ) {
                if (!isPlaying) {
                    startPlaying();
                } else {
                    stopPlaying();
                }
            }
            // -------------------- Delete SoundButton -------------------------------------------
        }else if(v == deleteSoundButton){
            Log.d("Lifary", "deleteSoundButton clicked");
            if(isRecorded  && !isRecording ){
                initializeFile();
                isRecorded = false;
            }

            // ----------------------- get Location Button--------------------------------------
        }else if(v == locationButton){
            Log.d(DEBUG, "location button clicked");
            currentLocation = getLastBestLocation();
            if(currentLocation != null){
                latitude = (float)currentLocation.getLatitude();
                longtitude = (float)currentLocation.getLongitude();
                String address = "(" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + ")";
                readLocation.getLocation(""+latitude, ""+longtitude);
                Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Unable to get address", Toast.LENGTH_LONG).show();
            }


            // --------------- Share Button ------------------------------------------
        }else if(v == shareLayout){
            Log.d(DEBUG, "shareLayout clicked");
            shareOption();


            // ---------------- Submit Button ---------------------
        }else if(v == submitButton){
            Log.d(DEBUG, "submitButton clicked");
            //@todo auto increment Diary ID
            Diary diary = new Diary(0);
            // add text to diary
            diary.addContents(diaryText.getText().toString());
            // add location
            diary.addLocation(latitude, longtitude);
            Calendar c= Calendar.getInstance();
            Log.d(DEBUG, "test -3");
            double date =  c.get(Calendar.SECOND)+ c.get(Calendar.MINUTE)*60+c.get(Calendar.HOUR)*3600
                    +c.get(Calendar.DAY_OF_MONTH)*3600*24 +  c.get(Calendar.MONTH)*3600*24*30
                    + c.get(Calendar.YEAR)*3600*24*30*12;
            Log.d(DEBUG, "test -2.5: " + session.get_uniqe_id());
            Log.d(DEBUG, "test -2.5: " + date);
            //todo convert unique id into data
            //double base = Double.parseDouble(session.get_uniqe_id());

            Log.d(DEBUG, "test -2.75");
            diary.setId(date);
            Log.d(DEBUG, "test -2");
            // convert audio file to byte[]
            try{
                FileInputStream is = new FileInputStream(mFileName);
                Log.d("Lifary", "Method 2, fileinputstream");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Log.d("Lifary", "Byte array output stream");
                byte[] b = new byte[1024];
                Log.d("Lifary", "new byte b");
                int bytesRead = is.read(b);
                Log.d("Lifary", "read byte");
                while (bytesRead != -1) {
                    bos.write(b, 0, bytesRead);
                    bytesRead = is.read(b);
                }
                Log.d("Lifary", "Read byte done");
                audioByte = bos.toByteArray();
                Log.d("Lifary", "convert to byte array");
                Log.d("Lifary", "Edit Diary: Audio byte = " + Arrays.toString(audioByte));
                diary.addSound(audioByte);
                Log.d("Lifary", "add Sound");

            }catch (Exception e){
                Log.d("Lifary", "Audio Load ERROR: " + e.getLocalizedMessage());
            }

            // add img
            Log.d(DEBUG, "test -1");
            try{
                if(bitmap == null){

                    Log.d("Lifary", "image bitmap ===== null");
                }
                else {
                    //Log.d("Lifary", "bitmap != null");
                    diary.addImage(bitmap);

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    byte[] imageByte = os.toByteArray();
                    String encoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
                    Log.d("Lifary", "SUCCESS: adding image success");
                }
            }catch (Exception e){
                Log.d("Lifary", "Image load ERROR: " + e.getLocalizedMessage());
            }

            Log.d(DEBUG, "test 0");
            // set share
            diary.setShare(shareSelect);
            Log.d(DEBUG, "test set userid");
            diary.setUserid(session.get_uniqe_id());

            //todo figure out local db settings
            DiaryDBHandler myDiaryDBHandler = new DiaryDBHandler(this, null, null, 1);
            //Log.d(DEBUG, "try add Diary");
            myDiaryDBHandler.addDiary(diary);
            Log.d("http", "Diary db added");

            d = diary;

            Log.d("http", "start connection");
            String response = " ";

            new uploadDiary().execute(response, response, response);

            if (response.equals("success" )|| flag) {
                Log.d("http", "connection fine");

                Toast.makeText(this, "Upload Finished!",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EditDiaryActivity.this,
                        UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
            else if(response.equals("failed") || !flag) {
                Log.d("http", "connection nooooo wut?");
            }

            //diary.print();
            String s = ""+(int) diaryCounter;
            Log.d("fb", "s == " + s);
            Firebase ref_diary = rootRef.child("1").child("diary").child(s);
            try {
                DiaryHelper dh = new DiaryHelper(0);
                dh.convert(diary);
                //dh.print();
                ref_diary.setValue(dh);
            }catch (Exception e){
                Log.d("Lifary", "Online dataase ERROR: " + e.getLocalizedMessage());
            }
        }

    }

    // --------------------- the last know best location ---------------------------/
    private Location getLastBestLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            buildAlertMessageNoGps();
            return null;
        }
        else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            long GPSLocationTime = 0;
            if (locationGPS != null) {
                GPSLocationTime = locationGPS.getTime();
                Log.d(DEBUG, "GPS location != null");
            } else
                Log.d(DEBUG, "GPS location == null");
            long NetLocationTime = 0;
            if (locationNet != null) {
                NetLocationTime = locationNet.getTime();
                Log.d(DEBUG, "Net location != null");

            }else Log.d(DEBUG, "Net location == null");

            if (GPSLocationTime - NetLocationTime > 0) {
                Log.d(DEBUG, "return location GPS");
                return locationGPS;
            } else {
                Log.d(DEBUG, "return location Net");
                return locationNet;
            }
        }
    }


    // ---------------------------- Alert no GPS available ------------------------
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    // ------------------------ Receive Activity Result ------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_LOAD_IMG && data!=null) {
                    // Get the Image from dataå
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row

                    //todo handle null exception
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    Log.d(DEBUG, "imDecodableString = " + imgDecodableString);
                    cursor.close();
                    try {
                        bitmap = BitmapFactory.decodeFile(imgDecodableString);
                        Log.d(DEBUG, "EditDiary: bitmap.getHeight = " + bitmap.getHeight()
                                + "\tbitmap.getWidth = " + bitmap.getWidth() );
                        //@todo 512.0 should not be static.
                        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                        Log.d(DEBUG, "EditDiary: nh = " + nh
                                + " result = " + ( bitmap.getHeight() * (512 / bitmap.getWidth()))
                                + " result 2 = " +( bitmap.getHeight()*(512/bitmap.getWidth())));
                        bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                        Log.d(DEBUG, "EditDiary: new bitmap.getHeight = " + bitmap.getHeight()
                                + "\tbitmap.getWidth = " + bitmap.getWidth() );

                        cameraButton.setImageBitmap(bitmap);

                        imagePath = imgDecodableString;
                        Log.d(DEBUG, "image path:"+imagePath);
                        Log.d(DEBUG, "camera loads pic successfully");
                    } catch (Exception e) {
                        Log.d(DEBUG, "camera loads pic failed ERROR: " + e.getLocalizedMessage());
                    }

                }
                else if(requestCode == RESULT_TAKE_PHOTO){
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                                bitmapOptions);
                        cameraButton.setImageBitmap(bitmap);

                        String path = android.os.Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();//todo handle return
                        OutputStream outFile;
                        File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        try {
                            outFile = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                            imagePath = file.getAbsolutePath();
                            outFile.flush();
                            outFile.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                    Log.d(DEBUG, "resultCode = " + resultCode + "\tresultOK = " + RESULT_OK);
                    if(data == null)    Log.d(DEBUG, "data = null");
                else{
                        Log.d(DEBUG, "data != null");
                    }
                }
            }
            catch(Exception e){
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }


    }

    // --------------------- Share Option Dialog ---------------------------------

    private void shareOption() {

        final CharSequence[] options = { "Privately", "Publicly" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditDiaryActivity.this);
        builder.setTitle("Share");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Privately"))
                {
                    shareStatusTextView.setText(R.string.privately);
                    shareSelect = 0;
                }
                else if (options[item].equals("Publicly"))
                {
                    shareStatusTextView.setText(R.string.publicly);
                    shareSelect = 1;

                }
            }
        });
        builder.show();
    }

    // --------------------- Select Image from Gallery --------------------------------

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditDiaryActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, RESULT_TAKE_PHOTO);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMG);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // --------------------------- Initialize media file output name -------------------
    private void initializeFile(){

        mRecorder = null;

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        soundTextView.setText("0\"");
    }

    // ----------------------- Start Recording -----------------------------------
    private void startRecording() {

        initializeFile();
        Log.d(DEBUG, "mFileName startRecording = " + mFileName);

        Log.d(DEBUG, "mRecorder");
        mRecorder = new MediaRecorder();
        Log.d(DEBUG, "mRecorder initialized");

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        Log.d(DEBUG, "mRecorder set audio source");

        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        Log.d(DEBUG, "mRecorder set output format");

        mRecorder.setOutputFile(mFileName);
        Log.d(DEBUG, "mRecorder set output file");

        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        Log.d(DEBUG, "mRecorder set audio encoder");



        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(DEBUG, "prepare() failed");
        }

        mRecorder.start();
        isRecorded = true;
        isRecording = true;
        soundButton.setImageResource(R.drawable.player_recording);
    }

    // -------------------------- Stop Recording ---------------------------------
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        isRecording = false;
        soundButton.setImageResource(R.drawable.audrio_micro);
        mPlayer = new MediaPlayer();
        int duration = 0;
        try{
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            duration = mPlayer.getDuration() / 1000;
        }catch (IOException e){
            Log.d(DEBUG, "prepare() failed");
        }

        mPlayer.release();
        mPlayer = null;
        soundTextView.setText(duration + "\"");

    }

    // ------------------------------Start Playing Media File--------------------------------
    private void startPlaying() {
        mPlayer = new MediaPlayer();


        try {
            mPlayer.setDataSource(mFileName);
            Log.d(DEBUG, "mFileName = " + mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(DEBUG, "prepare() failed");
        }


        isPlaying = true;
        playButton.setImageResource(R.drawable.player_stop);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

    }

    // -------------------------- Stop Playing Media File --------------------------------
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        isPlaying = false;
        playButton.setImageResource(R.drawable.play_sound);

    }


    @Override
    public void com(String contents) {
        addressTextView.setText(contents);
    }

    /**
     * AsyncTask for upload Diary.
     */
    private class uploadDiary extends AsyncTask<String, Integer, String> {
        HttpURLConnection urlc;
        int result = -1;

        private final ProgressDialog pDialog = new ProgressDialog(EditDiaryActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //@todo make it nice
            pDialog.setMessage("Uploading files. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("http", "test 3");
            try {
                ConnectionWithPost con = new ConnectionWithPost();

                //@todo need to upload image first
                if (imagePath!= null){
                    String temp  = con.uploadFile(imagePath);
                    Log.d("http", "image url: " + temp);
                    d.setImageUrl(temp);
                    Log.d("http", "image path: " + imagePath);
                    d.setImageUri(imagePath);
                    d.setImage("");
                }
                HashMap<String, String> map = d.toHashMap();
                //Log.d("http", map.toString());
                String url = getString(R.string.UPLOAD_DIARY_URL);
                String respond = con.uploadDiary(map,url );

                Log.d("http", "respond:  " + respond);
                flag = true;
                /*need looper prepare
                Context i = getApplicationContext();
                Toast.makeText(i, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
                */
                //@todo try toasting a success message
                Intent intent = new Intent(EditDiaryActivity.this,
                        DiaryListActivity.class);
                startActivity(intent);
                finish();
                return "success";
            }catch (Exception e){
                e.printStackTrace();
                Log.d("http","CONNECTION ERROR: " + "\t" + e.getLocalizedMessage());
            }
            flag = false;
            return "failed";
            //return Main.this.register();
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            if (this.pDialog.isShowing()) {
                this.pDialog.dismiss();
            }
        }
        protected void onPostExecute(Long result) {
        }
    }
}
