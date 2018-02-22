package ca.uwo.eng.se3313.lab2;

import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author      Shima Kananitodashki <skananit@uwo.ca>
 * @version     1.0
 * @since       2017-11-20
 */


public class MainActivity extends AppCompatActivity {

    /**
     * View that showcases the image
     */
    private ImageView ivDisplay;

    /**
     * Skip button: causes next image to be shown
     * skips rest of interval (timer resets when new image is shown)
     */
    private ImageButton skipBtn;

    /**
     * Progress bar showing how many seconds left (percentage).
     */
    private ProgressBar pbTimeLeft;

    /**
     * Label showing the seconds left.
     */
    private TextView tvTimeLeft;

    /**
     * Control to change the interval between switching images.
     *must be between 5-60seconds (change min/max values)
     */
    private SeekBar sbWaitTime;

    /**
     * Editable text to change the interval with {@link #sbWaitTime}.
     *Must be bound between 5-60seconds (verify input- show error if outside range)
     */
    private EditText etWaitTime;


    /**
     * Used to download images from the {@link #urlList}.
     */
    private IImageDownloader imgDownloader;

    /**
     * List of image URLs of cute animals that will be displayed.
     */
    private static final List<String> urlList = new ArrayList<String>() {{
        add("http://i.imgur.com/CPqbVW8.jpg");
        add("http://i.imgur.com/Ckf5OeO.jpg");
        add("http://i.imgur.com/3jq1bv7.jpg");
        add("http://i.imgur.com/8bSITuc.jpg");
        add("http://i.imgur.com/JfKH8wd.jpg");
        add("http://i.imgur.com/KDfJruL.jpg");
        add("http://i.imgur.com/o6c6dVb.jpg");
        add("http://i.imgur.com/B1bUG2K.jpg");
        add("http://i.imgur.com/AfxvVuq.jpg");
        add("http://i.imgur.com/DSDtm.jpg");
        add("http://i.imgur.com/SAVYw7S.jpg");
        add("http://i.imgur.com/4HznKil.jpg");
        add("http://i.imgur.com/meeB00V.jpg");
        add("http://i.imgur.com/CPh0SRT.jpg");
        add("http://i.imgur.com/8niPBvE.jpg");
        add("http://i.imgur.com/dci41f3.jpg");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Insert your code here (and within the class!)


        /**
         * Initialization of variables.
         */

        skipBtn = (ImageButton) findViewById(R.id.btnSkip);
        ivDisplay = (ImageView) findViewById(R.id.ivDisplay);
        pbTimeLeft = (ProgressBar) findViewById(R.id.pbTimeLeft);
        tvTimeLeft = (TextView) findViewById(R.id.tvTimeLeft);

        //sbWaitTime will be used to change the interval time between changing images
        //The time must be bound between 5 and 60 seconds
        //The max value of the seekbar is set to 55 to adjust it to keep this range
        sbWaitTime = (SeekBar) findViewById(R.id.sbWaitTime);
        etWaitTime = (EditText) findViewById(R.id.etWaitTime);
        sbWaitTime.setMax(55);

        //Generate a random number for every element on the list and store it in an int array
        Random rand = new Random();
        int[] randomPicture = {rand.nextInt(16)};

        //Store the remaining time in the interval - default is 50 seconds
        final int[] timeRemaining = {51};
        pbTimeLeft.setMax(timeRemaining[0]);

        //Display the random image by creating an instance of the imageDownloader class and passing the list in the argument
        imgDownloader = new imageDownloader();
        imgDownloader.download(urlList.get(randomPicture[0]), (@NonNull final Bitmap image) -> {ivDisplay.setImageBitmap(image);});


        //Create a timer and start it to keep track
        final Clock[] clock = {new Clock((timeRemaining[0] * 1000), 1000)};
        clock[0].start();

        //Whenever user modifies the text = user wants to change the time
        etWaitTime.addTextChangedListener(new TextWatcher() {

            //Do nothing before the text has been changed
            @Override
            /**
             *
             */
            public void beforeTextChanged(CharSequence charSequence, int index_1, int index_2, int index_3) {}

            //Do nothing while the text is changing
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //After the text has been changed = user has inputted a new value
            @Override
            public void afterTextChanged(Editable editable) {

                //Get the user input
                String userInput = etWaitTime.getText().toString();

                //Check if user input is out of range
                if(Integer.parseInt(userInput) < 5 || Integer.parseInt(userInput) > 60) {
                    //Display error
                    etWaitTime.setError("Must be between 5-60 seconds");
                }

                else { //Otherwise, change the time remaining
                    timeRemaining[0] = Integer.parseInt(userInput) ;
                    pbTimeLeft.setMax(timeRemaining[0]);
                    clock[0].cancel();

                }
            }
        });



        /**
         * Classifying what will happen when the next button is clicked
         * <p>
         *     It will set a new random image using a randomly generated number, and the URL list
         *     It then cancels whatever instance of the timer is already running
         *     Then it starts the timer for its time to countdown
         * </p>
         * @param  millisUntilFinished specifies how many milliseconds until the timer is done counting down
         */
        skipBtn.setOnClickListener(v -> {

            //Generate a new random number between 0-15 and assign it to random picture to change the image
            randomPicture[0] = rand.nextInt(16);
            imgDownloader.download(urlList.get(randomPicture[0]), (@NonNull final Bitmap image) -> {ivDisplay.setImageBitmap(image);});

            //Reset the clock
            clock[0].cancel();
            clock[0] = new Clock((timeRemaining[0] * 1000), 1000);
            clock[0].start();

        });

        /**
         * Classifying what will happen when the seekBar is changed
         * Whenever user modifies the seekbar = user wants to change the time
         */
        sbWaitTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            /**
             * Classifying what will when the value of the bar changes
             * <p>
             *     Gets the user value
             *     Calculates the time remaining
             *     Cancels old instances of the timer
             *     Instantiates a new timer to run for the amount of time specified by the seekBar
             * </p>
             */
            public void onProgressChanged(SeekBar seekBar, int seconds, boolean nothing) {

                //Get the new user value and update the time remaining 
                etWaitTime.setText(String.valueOf(seconds+5));
                timeRemaining[0] = seconds + 7;
                pbTimeLeft.setMax(timeRemaining[0] -1);

                //Create a new timer to start at the new time
                clock[0].cancel();
                clock[0] = new Clock((timeRemaining[0] * 1000), 1000);
                clock[0].start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    private class Clock extends CountDownTimer {
       
        int barIndex = 0; 

        public Clock(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            tvTimeLeft.setText(String.valueOf(millisInFuture / 1000));
        }

        @Override
        /**
         * Classifying what will happen after every tick of the timer, in this case every second
         * <p>
         *     Every second  text field is updated to show how much time (in seconds) is left before the countdown is done
         * </p>
         * @param  millisUntilFinished specifies how many milliseconds until the timer is done counting down
         */
        public void onTick(long millisUntilFinished) {
            performTick(millisUntilFinished);
        }
        void performTick(long mil) {
            tvTimeLeft.setText("" + ((mil / 1000) - 1));
            barIndex++;
            pbTimeLeft.setProgress(barIndex);
        }


        @Override
        /**
         * Classifying what will happen after the timer has finished its countdown
         * <p>
         *     At the end of the timer countdown a random number is chosen.
         *     This random number is used to select a URL from the list
         *     This URL is then used to call the function that sets the random image and
         *     It then restarts the timer
         * </p>
         */
        public void onFinish() {
            Random rand = new Random();
            int[] random = {rand.nextInt(16)};
            imgDownloader.download(urlList.get(random[0]), (@NonNull final Bitmap image) -> {ivDisplay.setImageBitmap(image);});
            barIndex = 0;
            start();
        }


    }


}

