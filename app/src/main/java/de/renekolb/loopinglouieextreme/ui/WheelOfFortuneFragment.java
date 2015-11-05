package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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
import android.widget.TextView;

import java.security.Guard;
import java.util.Random;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;
import de.renekolb.loopinglouieextreme.WheelFieldType;
import de.renekolb.loopinglouieextreme.WheelOfFortuneSettings;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WheelOfFortuneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WheelOfFortuneFragment extends Fragment {

    private ImageView mIVWheel;
    private TextView mTVResult;
    private Button mBTNnext;
    private TextView mTVplayerName;

    private int firstPlayer;
    private int secondPlayer;
    private int thirdPlayer;
    private int fourthPlayer;

    private int currentPosition;
    private int playerAmount;


    private boolean canSpin;

    private Random random;
    private float currentRotation;

    private boolean isSpinning;

    private Handler animationWaitHandler;

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private boolean winnerWheel = true;
    private WheelOfFortuneSettings wofSettings = WheelOfFortuneSettings.WINNER_WHEEL;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WheelOfFortuneFragment.
     */

    public static WheelOfFortuneFragment newInstance() {
        WheelOfFortuneFragment fragment = new WheelOfFortuneFragment();

        return fragment;
    }

    public WheelOfFortuneFragment() {
        // Required empty public constructor
    }

    public void setPlayerSpin(int firstPlayerIndex, int secondPlayerIndex, int thirdPlayerIndex, int fourthPlayerIndex){
        //first means playerIndex of first winner, second means second winner, ...
        this.firstPlayer = firstPlayerIndex;
        this.secondPlayer = secondPlayerIndex;
        this.thirdPlayer = thirdPlayerIndex;
        this.fourthPlayer = fourthPlayerIndex;

        this.playerAmount = 2;
        if(this.thirdPlayer!=-1)
            this.playerAmount++;
        if(this.fourthPlayer!=-1)
            this.playerAmount++;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
        animationWaitHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wheel_of_fortune, container, false);

        canSpin = true;
        currentPosition = 0;

        mIVWheel = (ImageView) view.findViewById(R.id.iv_wheel_of_fortune);
        mTVResult = (TextView) view.findViewById(R.id.tv_wheel_of_fortune_result);
        mBTNnext = (Button) view.findViewById(R.id.btn_wheel_of_fortune_next_round);
        mTVplayerName = (TextView) view.findViewById(R.id.tv_wheel_of_fortune_player_name);

        mBTNnext.setVisibility(View.INVISIBLE);

        //currentPosition = 0 -> firstPlayer
        mTVplayerName.setText(fa.getGame().getGamePlayer(firstPlayer).getDisplayName());
        mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(firstPlayer).getPlayerColor().getColor());

        currentRotation = 0;
        isSpinning = false;

        wofSettings = WheelOfFortuneSettings.WINNER_WHEEL;

        final GestureDetector gdt = new GestureDetector(fa,new GestureListener(),new Handler());

        mIVWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });

    /*    if(winnerWheel) {
            mBTNMode.setText("set Loser");
        }else{
            mBTNMode.setText("set Winner");
        }*/
        mIVWheel.setImageResource(wofSettings.getResourceID());

        /*mBTNMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSpinning){
                    winnerWheel = !winnerWheel;
                    if(winnerWheel) {
                        wofSettings = WheelOfFortuneSettings.WINNER_WHEEL;
                        mBTNMode.setText("set Loser");
                    }else{
                        wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                         mBTNMode.setText("set Winner");
                    }
                    mIVWheel.setImageResource(wofSettings.getResourceID());
                }
            }
        });*/

        mBTNnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.buttons.WHEEL_OF_FORTUNE_NEXT_ROUND);
            }
        });


        return view;
    }

    public void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fa = (FullscreenActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fa = null;
    }


    private void spinWheel(int direction){
        //direction 1 -> clockwise
        //         -1 -> counter clockwise
        mTVResult.setText("dum di dum ...");

        float dRot = 1080 + random.nextInt(1080); //3-6 Umdrehungen

        RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation+direction*dRot, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        currentRotation = (currentRotation+direction*dRot)%360;
        isSpinning= true;


        anim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float x) {
                //return (float)Math.pow(input,0.5d);
                return (3 * x) - (3 * x * x) + (x * x * x);
            }
        });
        anim.setDuration((int) (3.7 * dRot)); // keep rotating time constant
        anim.setFillEnabled(true);
        anim.setFillAfter(true);

        animationWaitHandler.postDelayed(onAnimationFinish, anim.getDuration());

        mIVWheel.startAnimation(anim);

    }


    private Runnable onAnimationFinish = new Runnable() {
        @Override
        public void run() {
            isSpinning = false;
            int index = (int)((360-currentRotation) / (360/wofSettings.getFieldAmount()));

            mTVResult.setText(wofSettings.getDisplayTextResourceID(index));
            if(wofSettings.getFieldType(index).equals(WheelFieldType.AGAIN)){
                canSpin = true;
            }else {
                canSpin = false;
            }

            if(!canSpin){
                currentPosition++;
                if(currentPosition>=playerAmount){
                    mBTNnext.setVisibility(View.VISIBLE);
                }else{
                    canSpin = true;
                    if(currentPosition==1) {
                        wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                        mTVplayerName.setText(fa.getGame().getGamePlayer(secondPlayer).getDisplayName());
                        mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(secondPlayer).getPlayerColor().getColor());
                    }else if(currentPosition == 2){
                        wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                        mTVplayerName.setText(fa.getGame().getGamePlayer(thirdPlayer).getDisplayName());
                        mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(thirdPlayer).getPlayerColor().getColor());
                    }else{
                        wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                        mTVplayerName.setText(fa.getGame().getGamePlayer(fourthPlayer).getDisplayName());
                        mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(fourthPlayer).getPlayerColor().getColor());
                    }
                    mIVWheel.setImageResource(wofSettings.getResourceID());
                }
            }
        }
    } ;


    //private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(!canSpin){
                return false;
            }

            if(isSpinning){
                return false;
            }

            if(velocityX*velocityX + velocityY*velocityY<SWIPE_THRESHOLD_VELOCITY*SWIPE_THRESHOLD_VELOCITY){
                return false; //swipe speed is too slow
            }

            int direction = 0; //-1 gegen UZ, +1 im UZ, 0 Fehler
            //long duration; //4000 normal

            //Log.i("TAG","x: "+e1.getX()+" y: "+e1.getY()+" w: "+mIVWheel.getWidth()+" h: "+mIVWheel.getHeight());
            if(e1.getX()>=mIVWheel.getWidth()/2 &&e1.getY()<=mIVWheel.getHeight()/2){
                //oben rechst
//                Log.i("TAG", "oben rechts");
                if(velocityX>=0 && velocityY>=0){
                    direction = 1;
                }else if(velocityX<=0&&velocityY<=0){
                    direction = -1;
                }

            }else if(e1.getX()>=mIVWheel.getWidth()/2 &&e1.getY()>mIVWheel.getHeight()/2){
                //unten rechts
  //              Log.i("TAG", "unten rechts");
                if(velocityX<=0 && velocityY>=0){
                    direction = 1;
                }else if(velocityX>=0&&velocityY<=0){
                    direction = -1;
                }
            }else if(e1.getX()<mIVWheel.getWidth()/2&&e1.getY()<=mIVWheel.getHeight()/2){
                //oben links
    //            Log.i("TAG", "oben links");
                if(velocityX>=0 && velocityY<=0){
                    direction = 1;
                }else if(velocityX<=0&&velocityY>=0){
                    direction = -1;
                }
            }else{
                //unten rechts
      //          Log.i("TAG", "unten links");
                if(velocityX<=0 && velocityY<=0){
                    direction = 1;
                }else if(velocityX>=0&&velocityY>=0){
                    direction = -1;
                }
            }

            if(direction != 0 ){
                spinWheel(direction);
            }
            return true;
        }
    }


}
