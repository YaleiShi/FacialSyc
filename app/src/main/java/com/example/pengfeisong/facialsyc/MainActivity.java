package com.example.pengfeisong.facialsyc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.example.pengfeisong.facialsyc.model.Anger;
import com.example.pengfeisong.facialsyc.model.FacialInfo;
import com.example.pengfeisong.facialsyc.model.Joy;
import com.example.pengfeisong.facialsyc.model.Synchronization;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, Detector.ImageListener {

    private static final String TAG = "Main Activity";

    class JoyDataPoint implements Comparable<JoyDataPoint>{
        float score;
        long time;

        JoyDataPoint(float s, long t) {
            this.score = s;
            this.time = t;
        }

        public int compareTo(@NonNull JoyDataPoint p2) {
            return (int)(this.time - p2.time) / 1000;
        }

    }


    //1.Create instance variables for the Surface View and Camera Detector references
    TextView syncScore;

    SurfaceView cameraDetectorSurfaceView;
    CameraDetector cameraDetector;

    Joy joy;
    Anger anger;

    FacialInfo fcialInfo1;
    FacialInfo fcialInfo2;

    //    LinkedList<Float> window;
    //2.Specify the max processing rate used by the Camera Detector. (This is in FPS, Frames per Second)
    int maxProcessingRate = 10;
    private FirebaseAuth mAuth;
    String userId;
    private DatabaseReference mDatabase;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        joy = new Joy();
        anger = new Anger();
        //3.Grab the reference to the Surface View by calling the Activity’s findViewById() method, which returns the View object
        // from the Activity’s layout based on the given id. The id is retrieved from the Resource class (R). This is where the id
        // we gave the Surface View when we created it comes in handy. Then cast it to a Surface View object since the findViewById()
        // method returns an Object type by default, but it is safe to force cast it since we are sure that the object specified by the
        // id is in fact a Surface View object.
        cameraDetectorSurfaceView = findViewById(R.id.cameraDetectorSurfaceView);
        // grep the textViews
//        textView1  = findViewById(R.id.textView1);
//        textView2  = findViewById(R.id.textView2);
//        textView3  = findViewById(R.id.textView3);
//
//        imageView = findViewById(R.id.imageView);
        //4. Initialize the Camera Detector passing in: “this” which is a reference to the Activity, the phone’s cameras we are using,
        // and the Surface View to embed the detector in

        cameraDetector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceView);
        //5. Set the processing rate
        cameraDetector.setMaxProcessRate(maxProcessingRate);


        //Record last 3 mins facial information
        fcialInfo1 = new FacialInfo(1800);
        fcialInfo2 = new FacialInfo(1800);

//        window = new LinkedList<Float>();
        //6. Set the MainActivity class to be the listener for the
        // Camera Detector. (This will cause an error that we will fix in the
        // next section, “Implementing the Camera Detector”)
        cameraDetector.setImageListener(this);
        cameraDetector.setOnCameraEventListener(this);

        //7. Set the MainActivity class to be the listener for
        // the Camera Detector. (This will cause an error that we will fix in the
        // next section, “Implementing the Camera Detector”)
        cameraDetector.setDetectAllEmotions(true);
        //8. Tell the Camera Detector to start detecting
        cameraDetector.start();
        //Set up database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        syncScore = findViewById(R.id.syncScore);

        //Add joys
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("1oLs06w3AUb9EfFOEUQeovNYtEr2").child("joy").addChildEventListener(new ChildEventListener() {
            //perrysong@gmail.com 's data update
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Float joy = dataSnapshot.getValue(Float.class);
                System.out.println("adding joy1 = " + joy);
                fcialInfo1.addJoy(joy);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("hGTfuATN1SR7qempPKTkNugYwFZ2").child("joy").addChildEventListener(new ChildEventListener() {
            //yaleishi@gmail.com data
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Float joy = dataSnapshot.getValue(Float.class);
                System.out.println("adding joy2 = " + joy);
                fcialInfo2.addJoy(joy);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        //When adding anger
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("1oLs06w3AUb9EfFOEUQeovNYtEr2").child("anger").addChildEventListener(new ChildEventListener() {
            //perrysong@gmail.com 's data update
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Float anger = dataSnapshot.getValue(Float.class);
                System.out.println("adding anger1 = " + anger);
                fcialInfo1.addAnger(anger);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("hGTfuATN1SR7qempPKTkNugYwFZ2").child("anger").addChildEventListener(new ChildEventListener() {
            //yaleishi@gmail.com data
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Float anger = dataSnapshot.getValue(Float.class);
                System.out.println("adding anger2 = " + anger);
                fcialInfo2.addAnger(anger);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        //Add brow raise
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("1oLs06w3AUb9EfFOEUQeovNYtEr2").child("browRaise").addChildEventListener(new ChildEventListener() {
            //perrysong@gmail.com 's data update
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Float browRaise = dataSnapshot.getValue(Float.class);
                System.out.println("adding browRaise1 = " + browRaise);
                fcialInfo1.addBrowRaise(browRaise);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("hGTfuATN1SR7qempPKTkNugYwFZ2").child("browRaise").addChildEventListener(new ChildEventListener() {
            //yaleishi@gmail.com data
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Float browRaise = dataSnapshot.getValue(Float.class);
                System.out.println("adding browRaise = " + browRaise);
                fcialInfo2.addBrowRaise(browRaise);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        //Add attention
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("1oLs06w3AUb9EfFOEUQeovNYtEr2").child("attention").addChildEventListener(new ChildEventListener() {
            //perrysong@gmail.com 's data update
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Float attention = dataSnapshot.getValue(Float.class);
                System.out.println("adding attention1 = " + attention);
                fcialInfo1.addAttention(attention);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("hGTfuATN1SR7qempPKTkNugYwFZ2").child("attention").addChildEventListener(new ChildEventListener() {
            //yaleishi@gmail.com data
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Float attention = dataSnapshot.getValue(Float.class);
                System.out.println("adding attention 2 = " + attention);
                fcialInfo2.addAttention(attention);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        //Add smile
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("1oLs06w3AUb9EfFOEUQeovNYtEr2").child("smile").addChildEventListener(new ChildEventListener() {
            //perrysong@gmail.com 's data update
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Float smile = dataSnapshot.getValue(Float.class);
                System.out.println("adding smile 1 = " + smile);
                fcialInfo1.addSmile(smile);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("hGTfuATN1SR7qempPKTkNugYwFZ2").child("smile").addChildEventListener(new ChildEventListener() {
            //yaleishi@gmail.com data
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Float smile = dataSnapshot.getValue(Float.class);
                System.out.println("adding smile 2 = " + smile);
                fcialInfo2.addSmile(smile);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    @Override
//  This method allows the specification of the camera’s height and width.
//  The SDK will provide the params cameraWidth, cameraHeight, and rotation.
//  These parameters give the recommended sizing for the Camera Detector that will
//  work best with the Affectiva SDK and the current orientation of the phone.
// Therefore the onCameraSizeSelected() method is where the sizing of the camera detector is specified.

    /**
     * Sizing the Camera Detector container for what works best with the Affectiva SDK
     */
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotation) {
        //1.Grab the Layout Parameters from the Surface View. (Every view component in Android has Layout Parameters)

        ViewGroup.LayoutParams params = cameraDetectorSurfaceView.getLayoutParams();
        //2. Change the parameter’s height and width to the recommended sizing given by the Affectiva SDK
        params.height = cameraHeight;
        params.width = cameraWidth;

        //3. Set the Surface View’s Layout Params with the new sizing
        cameraDetectorSurfaceView.setLayoutParams(params);
    }


//  List<Face> faces, Frame frame, float timeStamp
//  This method gives the main information for Affectiva’s
//   emotion detection. It gives a list of face objects which
// wrap all sorts of information about a face (Level of emotions,
// expressions, characteristics, etc.), the frame object which is a
// wrapper for the image captured, and a timestamp of when the frame was captured.

//    Check if the frame was processed
//    Check if there are any faces currently recognized.
//    Grab the first face
//    Grab any emotion data from the processed frame.
//    This is where the actual implementation of the facial expressions will occur.
//    The Face object provides all the different information that can be used.
//    Print the levels of emotion. (The scale goes from 0-100)

    /**
     * Process image results from Affectiva SDK
     * */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {

        //1. Set the Surface View’s Layout Params with the new sizing
        if (faces == null)
            return; //frame was not processed

        //2. Check if there are any faces currently recognized.
        if (faces.size() == 0)

            return; //no face found

        //3. Check if there are any faces currently recognized.
        Face face = faces.get(0);

        //4. Grab any emotion data from the processed frame. This is where the actual implementation
        // of the facial expressions will occur. The Face object provides all the different information that can be used.

        float meanJoy = joy.lastTenSecMeanJoy(face);

        float meanAnger = anger.lastTenSecMeanAnger(face);

        float br = face.expressions.getBrowRaise();
        float at = face.expressions.getAttention();
        float s = face.expressions.getSmile();


        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child(userId).child("joy").push().setValue(meanJoy);
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child(userId).child("anger").push().setValue(meanAnger);
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child(userId).child("browRaise").push().setValue(br);
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child(userId).child("attention").push().setValue(at);
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child(userId).child("smile").push().setValue(s);

    }



    public void sync(View view) {
        ArrayList<Float> joys1 = fcialInfo1.getJoyData();
        ArrayList<Float> joys2 = fcialInfo2.getJoyData();

        ArrayList<Float> angers1 = fcialInfo1.getAngerData();
        ArrayList<Float> angers2 = fcialInfo2.getAngerData();

        ArrayList<Float> browRaises1 = fcialInfo1.getBrowRaiseData();
        ArrayList<Float> browRaises2 = fcialInfo2.getBrowRaiseData();

        ArrayList<Float> attentions1 = fcialInfo1.getAttentionData();
        ArrayList<Float> attentions2 = fcialInfo2.getAttentionData();

        ArrayList<Float> smiles1 = fcialInfo1.getSmilesData();
        ArrayList<Float> smiles2 = fcialInfo2.getSmilesData();



        double joyScore = Synchronization.synScore(joys1, joys2);
        double angerScore = Synchronization.synScore(angers1, angers2);
        double brScore = Synchronization.synScore(browRaises1, browRaises2);
        double attentionScore = Synchronization.synScore(attentions1, attentions2);
        double smilesScore = Synchronization.synScore(smiles1, smiles2);

        double fscore = ((joyScore + angerScore + brScore + attentionScore + smilesScore) / 5) * 50 + 50;

        this.syncScore.setText(String.valueOf(fscore));

        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child(userId).removeValue();
    }


}