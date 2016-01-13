package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    //private float currentRotation;

    private boolean debug;

    private boolean isSpinning;

    private Handler animationWaitHandler;

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private boolean winnerWheel = true;
    private WheelOfFortuneSettings wofSettings = WheelOfFortuneSettings.WINNER_WHEEL;

    private double startPhi;
    private double dPhi;

    private float dRotAnim;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WheelOfFortuneFragment.
     */

    public static WheelOfFortuneFragment newInstance() {

        return new WheelOfFortuneFragment();
    }

    public WheelOfFortuneFragment() {
        // Required empty public constructor
    }

    public void setPlayerSpin(int firstPlayerIndex, int secondPlayerIndex, int thirdPlayerIndex, int fourthPlayerIndex) {
        //first means playerIndex of first winner, second means second winner, ...
        this.firstPlayer = firstPlayerIndex;
        this.secondPlayer = secondPlayerIndex;
        this.thirdPlayer = thirdPlayerIndex;
        this.fourthPlayer = fourthPlayerIndex;

        debug = firstPlayerIndex == -1;

        this.playerAmount = 2;
        if (this.thirdPlayer != -1)
            this.playerAmount++;
        if (this.fourthPlayer != -1)
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
        if (debug) {
            mTVplayerName.setText("DEBUG PLAYER");
            mTVplayerName.setBackgroundColor(Color.TRANSPARENT);
        } else {
            mTVplayerName.setText(fa.getGame().getGamePlayer(firstPlayer).getDisplayName());
            mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(firstPlayer).getPlayerColor().getColor());
        }

        isSpinning = false;

        wofSettings = WheelOfFortuneSettings.WINNER_WHEEL;


        mIVWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isSpinning && canSpin) {
                            startPhi = Math.toDegrees(Math.atan2(event.getY() - (mIVWheel.getHeight() / 2), event.getX() - (mIVWheel.getWidth() / 2))) + 180;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isSpinning && canSpin && startPhi != -1) {
                            double newPhi = Math.toDegrees(Math.atan2(event.getY() - (mIVWheel.getHeight() / 2), event.getX() - (mIVWheel.getWidth() / 2))) + 180;
                            dPhi = startPhi - newPhi;

                            mIVWheel.setRotation((float) (mIVWheel.getRotation() - dPhi));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        startPhi = -1;
                        if (!isSpinning && canSpin) {
                            if (Math.abs(dPhi) > 2) {
                                spinWheel((int) -Math.signum(dPhi));
                            }
                        }
                        break;
                }
                return true;
            }
        });

        mIVWheel.setImageResource(wofSettings.getResourceID());

        mBTNnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.buttons.WHEEL_OF_FORTUNE_NEXT_ROUND);
            }
        });


        return view;
    }

    private void onButtonPressed(int button) {
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


    private void spinWheel(int direction) {
        //direction 1 -> clockwise
        //         -1 -> counter clockwise
        mTVResult.setText("dum di dum ...");

        float dRot = 1080 + random.nextInt(1080); //3-6 Umdrehungen


        //Animation does not actually turns the entire view. rather the image(drawable)!
        RotateAnimation anim = new RotateAnimation(0, direction * dRot, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dRotAnim = direction * dRot;
        isSpinning = true;


        anim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float x) {
                //return (float)Math.pow(input,0.5d);
                return (3 * x) - (3 * x * x) + (x * x * x);
            }
        });
        anim.setDuration((int) (2.0 * dRot)); // (früher 3.7) keep rotating time constant
        anim.setFillEnabled(true);
        anim.setFillAfter(true);

        animationWaitHandler.postDelayed(onAnimationFinish, anim.getDuration());

        mIVWheel.startAnimation(anim);

    }


    private final Runnable onAnimationFinish = new Runnable() {
        @Override
        public void run() {
            isSpinning = false;

            float rot = (mIVWheel.getRotation() + dRotAnim) % 360;
            if (rot < 0) rot += 360;

            int index = (int) ((360 - rot) / (360 / wofSettings.getFieldAmount()));

            int textRid = wofSettings.getDisplayTextResourceID(index);
            if (textRid == -1) {
                Log.e("Wheel of Fortune", "textRid = -1");
                mTVResult.setText("Error...");
                canSpin = false;
                mBTNnext.setVisibility(View.VISIBLE);
                return;
            }
            mTVResult.setText(textRid);

            canSpin = wofSettings.getFieldType(index).equals(WheelFieldType.AGAIN);

            if (!canSpin) {
                if (debug) {
                    canSpin = true;
                    return;
                }
                currentPosition++;
                if (currentPosition >= playerAmount) {
                    mBTNnext.setVisibility(View.VISIBLE);
                } else {
                    canSpin = true;
                    if (currentPosition == 1) {
                        wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                        mTVplayerName.setText(fa.getGame().getGamePlayer(secondPlayer).getDisplayName());
                        mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(secondPlayer).getPlayerColor().getColor());
                    } else if (currentPosition == 2) {
                        wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                        mTVplayerName.setText(fa.getGame().getGamePlayer(thirdPlayer).getDisplayName());
                        mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(thirdPlayer).getPlayerColor().getColor());
                    } else {
                        wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                        mTVplayerName.setText(fa.getGame().getGamePlayer(fourthPlayer).getDisplayName());
                        mTVplayerName.setBackgroundColor(fa.getGame().getGamePlayer(fourthPlayer).getPlayerColor().getColor());
                    }
                    mIVWheel.setImageResource(wofSettings.getResourceID());
                }
            }
        }
    };

}
