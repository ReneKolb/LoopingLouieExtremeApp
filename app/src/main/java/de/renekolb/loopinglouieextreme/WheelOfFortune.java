package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WheelOfFortune#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WheelOfFortune extends Fragment {

    private ImageView mWheel;
    private Random random;
    private float currentRotation;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WheelOfFortune.
     */

    public static WheelOfFortune newInstance() {
        WheelOfFortune fragment = new WheelOfFortune();

        return fragment;
    }

    public WheelOfFortune() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wheel_of_fortune, container, false);

        mWheel = (ImageView) view.findViewById(R.id.iv_wheel_of_fortune);

        currentRotation = 0;

        final GestureDetector gdt = new GestureDetector(FullscreenActivity.reference,new GestureListener(),new Handler());

        mWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });


        return view;
    }

    public void onButtonPressed(int button) {
        if (mListener != null) {
            mListener.onFragmentInteraction(button);
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


    private void TEST_ROTATE(int direction){
        //direction 1 -> clockwise
        //         -1 -> counter clockwise

        float dRot = 720 + random.nextInt(720); //2-4 Umdrehungen

        RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation+direction*dRot, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        currentRotation = (currentRotation+direction*dRot)%360;


        anim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float x) {
                //return (float)Math.pow(input,0.5d);
                return (3 * x) - (3 * x * x) + (x * x * x);
            }
        });
        anim.setDuration(4000);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        mWheel.startAnimation(anim);

    }




    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(velocityX*velocityX + velocityY*velocityY<SWIPE_THRESHOLD_VELOCITY*SWIPE_THRESHOLD_VELOCITY){
                return false; //swipe speed is too slow
            }

            int direction = 0; //-1 gegen UZ, +1 im UZ, 0 Fehler
            //long duration; //4000 normal

            Log.i("TAG","x: "+e1.getX()+" y: "+e1.getY()+" w: "+mWheel.getWidth()+" h: "+mWheel.getHeight());
            if(e1.getX()>=mWheel.getWidth()/2 &&e1.getY()<=mWheel.getHeight()/2){
                //oben rechst
                Log.i("TAG", "oben rechts");
                if(velocityX>=0 && velocityY>=0){
                    direction = 1;
                }else if(velocityX<=0&&velocityY<=0){
                    direction = -1;
                }

            }else if(e1.getX()>=mWheel.getWidth()/2 &&e1.getY()>mWheel.getHeight()/2){
                //unten rechts
                Log.i("TAG", "unten rechts");
                if(velocityX<=0 && velocityY>=0){
                    direction = 1;
                }else if(velocityX>=0&&velocityY<=0){
                    direction = -1;
                }
            }else if(e1.getX()<mWheel.getWidth()/2&&e1.getY()<=mWheel.getHeight()/2){
                //oben links
                Log.i("TAG", "oben links");
                if(velocityX>=0 && velocityY<=0){
                    direction = 1;
                }else if(velocityX<=0&&velocityY>=0){
                    direction = -1;
                }
            }else{
                //unten rechts
                Log.i("TAG", "unten links");
                if(velocityX<=0 && velocityY<=0){
                    direction = 1;
                }else if(velocityX>=0&&velocityY>=0){
                    direction = -1;
                }
            }

            if(direction != 0){
                TEST_ROTATE(direction);
            }
            return true;
        }
    }


}
