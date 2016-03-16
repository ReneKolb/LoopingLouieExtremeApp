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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.R;
import de.renekolb.loopinglouieextreme.WheelOfFortuneHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WheelOfFortuneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WheelOfFortuneFragment extends Fragment {

    private ImageSwitcher mISWheel;
    private TextView mTVResult;
    private Button mBTNnext;
    private TextView mTVplayerName;

    private String displayName;
    private int nameBackgroundColor;
    private int wheelResourceID;

    private int currentSpinnerPlayerIndex;

    //private boolean isSpinning;

    private Handler animationWaitHandler;

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private WheelOfFortuneHandler handler;

    private double dragStartPhi;
    private double dragLastPhi;
    //private double dragDPhi;

     /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WheelOfFortuneFragment.
     */

    public static WheelOfFortuneFragment newInstance(WheelOfFortuneHandler handler) {
        WheelOfFortuneFragment result = new WheelOfFortuneFragment();
        result.handler = handler;
        result.animationWaitHandler = new Handler();
        return result;
    }

    public WheelOfFortuneFragment() {
        // Required empty public constructor
    }

    public void setCurrentSpinner(GamePlayer player) {
        if (player == null) {
            this.displayName = "DummyPlayer";
            this.nameBackgroundColor = Color.TRANSPARENT;
        } else {
            this.displayName = player.getDisplayName();
            this.nameBackgroundColor = player.getPlayerColor().getColor();
        }

        this.wheelResourceID = handler.getCurrentSettings().getResourceID();
        if (mTVplayerName != null) {
            mTVplayerName.setText(this.displayName);
            mTVplayerName.setBackgroundColor(this.nameBackgroundColor);
        }
        if (mISWheel != null) {
            this.mISWheel.setImageResource(handler.getCurrentSettings().getResourceID());
        }

    }

    public void setResultText(int textID){
        this.mTVResult.setText(textID);
    }

    public void setResultText(String text){
        this.mTVResult.setText(text);
    }

    public void setEnableNextButton(boolean enabled){
        if(enabled) {
            mBTNnext.setVisibility(View.VISIBLE);
        }else{
            mBTNnext.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wheel_of_fortune, container, false);

        //mIVWheel = (ImageView) view.findViewById(R.id.iv_wheel_of_fortune);
        mISWheel = (ImageSwitcher) view.findViewById(R.id.is_wheel_of_fortune);
        mTVResult = (TextView) view.findViewById(R.id.tv_wheel_of_fortune_result);
        mBTNnext = (Button) view.findViewById(R.id.btn_wheel_of_fortune_next_round);
        mTVplayerName = (TextView) view.findViewById(R.id.tv_wheel_of_fortune_player_name);

        mBTNnext.setVisibility(View.INVISIBLE);

        this.dragStartPhi = -1;
        this.dragLastPhi = -1;

        mISWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!handler.isSpinning() && handler.canSpin()) {
                            dragLastPhi = dragStartPhi = Math.toDegrees(Math.atan2(event.getY() - (mISWheel.getHeight() / 2), event.getX() - (mISWheel.getWidth() / 2))) + 180;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!handler.isSpinning() && handler.canSpin() && dragStartPhi != -1) {
                            double newPhi = Math.toDegrees(Math.atan2(event.getY() - (mISWheel.getHeight() / 2), event.getX() - (mISWheel.getWidth() / 2))) + 180;
                            dragLastPhi = mISWheel.getCurrentView().getRotation() - newPhi + dragStartPhi;
                            mISWheel.getCurrentView().setRotation((float) (newPhi - dragStartPhi));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        dragStartPhi = -1;
                        if (!handler.isSpinning() && handler.canSpin()) {
                            Log.i("WheelOfFortune","LastPhi: "+Math.abs(dragLastPhi));
                            if (Math.abs(dragLastPhi) > 5) {
                                handler.startSpinning(mISWheel.getCurrentView().getRotation(),(int) -Math.signum(dragLastPhi));
                            }
                        }
                        dragLastPhi = -1;
                        break;
                }
                return true;
            }
        });

        mISWheel.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(fa);
                return myView;
            }
        });

        mISWheel.setImageResource(handler.getCurrentSettings().getResourceID());
        mTVplayerName.setText(displayName);
        mTVplayerName.setBackgroundColor(nameBackgroundColor);

        //Animation in = AnimationUtils.loadAnimation(fa, android.R.anim.slide_in_left);
        mISWheel.setInAnimation(fa, R.anim.slide_in_right);
        mISWheel.setOutAnimation(fa, R.anim.slide_out_left);

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


    public void startSpin(float startViewRotation, float rotateAnimation){
        mISWheel.getCurrentView().setRotation(startViewRotation);
        mTVResult.setText("dum die dum ...");
        RotateAnimation anim = new RotateAnimation(0,rotateAnimation, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float x) {
                return (3 * x) - (3 * x * x) + (x * x * x);
            }
        });
        anim.setDuration((int) (1.5f*Math.abs(rotateAnimation)+2000));
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        animationWaitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.onSpinFinish();
            }
        }, anim.getDuration());
        mISWheel.getCurrentView().startAnimation(anim);
    }

    /*private void spinWheel(int direction) {
        //direction 1 -> clockwise
        //         -1 -> counter clockwise
        mTVResult.setText("dum di dum ...");

        float dRot = 1080 + random.nextInt(1080); //3-6 Umdrehungen
        //TODO: sync with clients/server

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

        mISWheel.getCurrentView().startAnimation(anim);

    }*/


  /*  private final Runnable onAnimationFinish = new Runnable() {
        @Override
        public void run() {
            isSpinning = false;

            float rot = (mISWheel.getCurrentView().getRotation() + dRotAnim) % 360;
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

            fa.onWheelSpinFinish(index);

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
                    //TODO: slide to new wheel, sync with host
                    mISWheel.setImageResource(wofSettings.getResourceID());
                }
            }
        }
    };*/

}
