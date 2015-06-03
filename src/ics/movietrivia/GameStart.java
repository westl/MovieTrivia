package ics.movietrivia;

import java.util.ArrayList;

import ics.movietrivia.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
//import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */

public class GameStart extends Activity {

	/*global variables */
	private final static String TAG = "TestActivity";
	private TextView mainText ; 
	private boolean readyForNext =false;

	/*pause button*/
	private TextView pauser; //set to Pause or Resume

	/*timer variables */
	private boolean finished = false;
	CountDownTimer clock;
	private TextView timer;  //the text for the timer
	boolean timerStarted = false;
	long secondsRemaining = 60000;

	/*variables for answers and the answerPane  */
	private TextView answer1;
	private TextView answer2;
	private TextView answer3;
	private TextView answer4;
	private View answerPane; 

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_start);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);


		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
		.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(
								android.R.integer.config_shortAnimTime);
					}
					controlsView.animate()
					.translationY(visible ? 0 : mControlsHeight)
					.setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}


			}
		});


		mainText = (TextView) findViewById(R.id.textView1);
		pauser = (TextView) findViewById(R.id.pausebutton);
		answerPane = findViewById(R.id.answerPane);
		answer1 = (TextView) findViewById(R.id.option1);
		answer2 = (TextView) findViewById(R.id.option2);
		answer3 = (TextView) findViewById(R.id.option3);
		answer4 = (TextView) findViewById(R.id.option4);
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "On Resume .....");

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "On Start .....");
		startQuestions();



	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "On Stop .....");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);

	}


	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	/*  View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    }; */



	//handlers and runnable 
	Handler  questionHandler =  new Handler();
	Runnable beginQuestions = new Runnable(){

		@Override
		public void run() {
			/*Change the text of the main text box */
			mainText.setText("Ready...Set...Go!");


			while(!finished && readyForNext ){
				//generate questions and answers
				generateQuestions();
			}

		}

		private void generateQuestions() {

			//get the result set from an SQLiteCommand here

			//set the question 

			//set answers

			answer1.setText("test1");
			answer2.setText("test2");
			answer3.setText("test3");
			answer4.setText("test4");

		}

	};



	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	public void pauseTimer(View v){

		if(pauser.getText().equals("Pause"))
		{
			pauser.setText("Resume");
			clock.cancel();
		}else if(pauser.getText().equals("Resume")){
			pauser.setText("Pause");
			createCountDownTimer();
		}

	}
	public void countdown(View v)
	{
		if(!timerStarted){
			timerStarted =  true;
			timer = (TextView) findViewById(R.id.clocktimer);
			createCountDownTimer(); //first time the clock is ever started
		}
	}

	public void createCountDownTimer(){

		clock =  new CountDownTimer(secondsRemaining, 1000) {

			public void onTick(long millisUntilFinished) {
				timer.setText("Time:\n"+ millisUntilFinished / 1000);
				secondsRemaining = millisUntilFinished;
			}
			public void onFinish() {
				finished  = !finished;
				timer.setText("Gathering Results...");
				pauser.setVisibility(View.INVISIBLE);
				mainText.setText("Congratulations, play again!");
				findViewById(R.id.submitbutton).setVisibility(View.INVISIBLE); //hides the submit button
				//hides answers
				findViewById(R.id.option1).setVisibility(View.INVISIBLE); 
				findViewById(R.id.option2).setVisibility(View.INVISIBLE);
				findViewById(R.id.option3).setVisibility(View.INVISIBLE);
				findViewById(R.id.option4).setVisibility(View.INVISIBLE);

				//Create a new activity for statistics here, in that activity create
				//this activity again with a Play Again button. 
			}
		}.start();
	}
	
	public void submit(View v){
		
		//Get list of radioButtons, we currently have 4, remove those that are
		//not selected since we don't care about those. 
		System.out.println("Submitting answers");
	
		for(View radio: answerPane.getTouchables()){
			if( ((RadioButton)radio).isChecked()){
				System.out.println(((RadioButton)radio).getText());
			}
		
		}
		
		/* possible stats 
		 *  - questions / questions answer correctly
		 *  etc 
		 */
		

		//logic to see if the answer is correct, do something to store stats 
		readyForNext = true;
		
	}
	
	private void startQuestions(){
		questionHandler.post(beginQuestions);
	}




}
