package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
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

        Button btnSpin = (Button) view.findViewById(R.id.btn_wheel_of_fortune_spin);
        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEST_ROTATE();
            }
        });
        currentRotation = 0;

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


    private void TEST_ROTATE(){

        float dRot = 720 + random.nextInt(720); //2-4 Umdrehungen

        RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation+dRot, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        currentRotation = (currentRotation+dRot)%360;


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
        /*Matrix matrix = new Matrix();*/
        //mWheel = view.finfViewByID(R.id.);
        //mWheel.setScaleType(ImageView.ScaleType.MATRIX); //required
        //mWheel.setPivotX(mWheel.getDrawable().getBounds().width() / 2);
        //mWheel.setPivotY(mWheel.getDrawable().getBounds().height() / 2);
        //mWheel.setRotation(72);//0..360

        //vllt als RelativeView???
/*
        float pivotX = mWheel.getDrawable().getBounds().width()/2;
        float pivotY = mWheel.getDrawable().getBounds().height()/2;

        float angle = 72;

        matrix.postRotate((float) angle, pivotX, pivotY);
        mWheel.setImageMatrix(matrix);*/
    }

}
