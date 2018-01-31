package com.example.android.hedgehogorpenguin;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private ImageButton penguinImageButton;
    private ImageButton hedgehogImageButton;
    private boolean again = false;
    private int numOfPenguins = 0;
    private int numOfHedgehogs = 0;
    private TextView questionText;
    private TextView contentText;
    private Boolean[] answers = {true, true, false, true, true, false, true, false, false, false, true, false, false, true};
    private int questionNumber = 1;
    private TextView nextButton;
    private MovementView movementViewHedgehogs;
    private MovementViewPenguins movementViewPenguins;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        penguinImageButton = (ImageButton) findViewById(R.id.answer_button_penguin);
        hedgehogImageButton = (ImageButton) findViewById(R.id.answer_button_hedgehog);


        movementViewPenguins = findViewById(R.id.view2);
        movementViewHedgehogs = findViewById(R.id.view);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.text_content);

        questionText = findViewById(R.id.header_text);
        contentText = findViewById(R.id.text_content);
        nextButton = findViewById(R.id.next_answer);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle();
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /*This method to restore fullscreen after pausing*/
    @Override
    public void onResume() {
        super.onResume();
        if (!mVisible) {
            hide();
        }
    }

    /*This method to change fullsceen/UI mode*/
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    public void checkAnswerHedgehog(View view) {
        checkAnswer(false);
    }

    public void checkAnswerPenguin(View view) {
        checkAnswer(true);
    }


    public void checkAnswer(boolean isPenguin) {
        penguinImageButton.setClickable(false);
        hedgehogImageButton.setClickable(false);

        if (isPenguin) {
            if (answers[questionNumber - 1]) {
                numOfPenguins++;
                penguinImageButton.setImageResource(R.drawable.penguin_touched_nice);

                movementViewPenguins.addPenguin();

            } else {
                penguinImageButton.setImageResource(R.drawable.penguin_touched_bad);
            }
        } else {
            if (answers[questionNumber - 1]) {
                hedgehogImageButton.setImageResource(R.drawable.hedgehog_touched_bad);

            } else {
                numOfHedgehogs++;
                hedgehogImageButton.setImageResource(R.drawable.hedgehog_touched_nice);

                movementViewHedgehogs.addHedgehog();
            }
        }

        nextButton.setVisibility(View.VISIBLE);

    }

    public void whoWin() {
        if (numOfPenguins > numOfHedgehogs) {
            contentText.setText(getResources().getText(R.string.pfinally));
            penguinImageButton.setImageResource(R.drawable.penguin_king);
        } else if (numOfHedgehogs > numOfPenguins) {
            contentText.setText(getResources().getText(R.string.hfinally));
            hedgehogImageButton.setImageResource(R.drawable.hedgehog_king);
        } else {
            contentText.setText(getResources().getText(R.string.dfinally));
            penguinImageButton.setImageResource(R.drawable.penguin_king);
            hedgehogImageButton.setImageResource(R.drawable.hedgehog_king);
        }
    }

    public void startAgain() {
        nextButton.setText(getResources().getText(R.string.again));
        again = true;
        nextButton.setVisibility(View.VISIBLE);

    }

    public void restart() {
        numOfPenguins = 0;
        numOfHedgehogs = 0;
        questionNumber = 1;
        penguinImageButton.setClickable(true);
        hedgehogImageButton.setClickable(true);
        penguinImageButton.setImageResource(R.drawable.penguin_medium);
        hedgehogImageButton.setImageResource(R.drawable.hedgehog_middle);
        nextButton.setText(getResources().getText(R.string.next));
        movementViewHedgehogs.restartAll();
        movementViewPenguins.restartAll();
        questionText.setText(getResources().getText(R.string.first_question));
        contentText.setText(getResources().getText(R.string.first_content));

    }

    public void nextQuestion(View view) {
        if (again) {
            nextButton.setVisibility(View.INVISIBLE);
            again = false;
            restart();
            return;
        }
        nextButton.setVisibility(View.INVISIBLE);
        penguinImageButton.setClickable(true);
        hedgehogImageButton.setClickable(true);
        penguinImageButton.setImageResource(R.drawable.penguin_medium);
        hedgehogImageButton.setImageResource(R.drawable.hedgehog_middle);

        switch (questionNumber) {
            case 1:
                questionText.setText(getResources().getText(R.string.question1));
                contentText.setText(getResources().getText(R.string.content1));
                break;
            case 2:
                questionText.setText(getResources().getText(R.string.question2));
                contentText.setText(getResources().getText(R.string.content2));
                break;
            case 3:
                questionText.setText(getResources().getText(R.string.question3));
                contentText.setText(getResources().getText(R.string.content3));
                break;
            case 4:
                questionText.setText(getResources().getText(R.string.question4));
                contentText.setText(getResources().getText(R.string.content4));
                break;
            case 5:
                questionText.setText(getResources().getText(R.string.question5));
                contentText.setText(getResources().getText(R.string.content5));
                break;
            case 6:
                questionText.setText(getResources().getText(R.string.question6));
                contentText.setText(getResources().getText(R.string.content6));
                break;
            case 7:
                questionText.setText(getResources().getText(R.string.question7));
                contentText.setText(getResources().getText(R.string.content7));
                break;
            case 8:
                questionText.setText(getResources().getText(R.string.question8));
                contentText.setText(getResources().getText(R.string.content8));
                break;
            case 9:
                questionText.setText(getResources().getText(R.string.question9));
                contentText.setText(getResources().getText(R.string.content9));
                break;
            case 10:
                questionText.setText(getResources().getText(R.string.question10));
                contentText.setText(getResources().getText(R.string.content10));
                break;
            case 11:
                questionText.setText(getResources().getText(R.string.question11));
                contentText.setText(getResources().getText(R.string.content11));
                break;
            case 12:
                questionText.setText(getResources().getText(R.string.question12));
                contentText.setText(getResources().getText(R.string.content12));
                break;
            case 13:
                questionText.setText(getResources().getText(R.string.question13));
                contentText.setText(getResources().getText(R.string.content13));
                break;
            case 14:
                penguinImageButton.setClickable(false);
                hedgehogImageButton.setClickable(false);
                questionText.setText(getResources().getText(R.string.qfinally));
                whoWin();
                startAgain();
                break;
            default:
        }

        questionNumber++;
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
