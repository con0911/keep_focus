package com.accessibility.keepfocus.settings;

import com.accessibility.keepfocus.R;
import com.accessibility.keepfocus.activity.BlockNotificationActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LockConfirmActivity extends Activity implements OnClickListener {

    private static final String TAG = "ChooseColorLock";
    private String mLockPinCode = "";
    private int mPinCodeNumber = 0;
    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8,
            btn_9, btn_0;
    private ImageView pass1, pass2, pass3, pass4;
    private TextView text_Guide;
    private SharedPreferences myPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_color);
        Log.i(TAG, "onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initializeIdentifiers();
        changeDisplayPassword(mPinCodeNumber);
        if (myPass.getString("PinCode", "") != "") {
            //
        } else {
            Intent rightPass = new Intent(LockConfirmActivity.this, BlockNotificationActivity.class);
            startActivity(rightPass);
            finish();
        }
    }

    void initializeIdentifiers() {

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        text_Guide = (TextView) findViewById(R.id.help_choose_Color_Text);
        pass1 = (ImageView) findViewById(R.id.pass_1);
        pass2 = (ImageView) findViewById(R.id.pass_2);
        pass3 = (ImageView) findViewById(R.id.pass_3);
        pass4 = (ImageView) findViewById(R.id.pass_4);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);

        text_Guide.setText(getString(R.string.access_pin_code));
        myPass = getSharedPreferences("myPass", MODE_PRIVATE);
    }

	private void resetColorLock() {
        mPinCodeNumber = 0;
        mLockPinCode = "";
        changeDisplayPassword(mPinCodeNumber);
    }

    private void delayResetPass() { // delay to show full pin code
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
        @Override
            public void run() {
                resetColorLock();
            }
        }, 300);
    }

    public void changeDisplayPassword(int number) {
        if (number == 0) {
            pass1.setImageResource(R.drawable.circle_button_white);
            pass2.setImageResource(R.drawable.circle_button_white);
            pass3.setImageResource(R.drawable.circle_button_white);
            pass4.setImageResource(R.drawable.circle_button_white);
        }
        if (number == 1) {
            pass1.setImageResource(R.drawable.circle_blue);
            pass2.setImageResource(R.drawable.circle_button_white);
            pass3.setImageResource(R.drawable.circle_button_white);
            pass4.setImageResource(R.drawable.circle_button_white);
        }
        if (number == 2) {
            pass1.setImageResource(R.drawable.circle_blue);
            pass2.setImageResource(R.drawable.circle_blue);
            pass3.setImageResource(R.drawable.circle_button_white);
            pass4.setImageResource(R.drawable.circle_button_white);
        }
        if (number == 3) {
            pass1.setImageResource(R.drawable.circle_blue);
            pass2.setImageResource(R.drawable.circle_blue);
            pass3.setImageResource(R.drawable.circle_blue);
            pass4.setImageResource(R.drawable.circle_button_white);
        }
        if (number == 4) {
            pass1.setImageResource(R.drawable.circle_blue);
            pass2.setImageResource(R.drawable.circle_blue);
            pass3.setImageResource(R.drawable.circle_blue);
            pass4.setImageResource(R.drawable.circle_blue);
            if(mLockPinCode.equals(myPass.getString("PinCode", ""))) {
                Intent rightPass = new Intent(LockConfirmActivity.this, BlockNotificationActivity.class);
                startActivity(rightPass);
                finish();
            } else {
                Toast.makeText(this, "Wrong Password! Try again",
                        Toast.LENGTH_LONG).show();
                wrongPassEffect();
                delayResetPass();
            }
        }
    }

    public void wrongPassEffect() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(300);
        findViewById(R.id.layoutColorChoose1_ColorLock).startAnimation(shake);
        findViewById(R.id.layoutColorChoose2_ColorLock).startAnimation(shake);
        findViewById(R.id.layoutColorChoose3_ColorLock).startAnimation(shake);
        findViewById(R.id.layoutColorChoose4_ColorLock).startAnimation(shake);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click));
        switch (v.getId()) {
        case R.id.btn_1:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "1";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_2:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "2";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_3:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "3";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_4:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "4";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_5:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "5";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                       Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_6:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "6";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_7:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "7";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_8:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "8";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_9:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "9";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        case R.id.btn_0:
            if (mPinCodeNumber < 4) {
                mPinCodeNumber++;
                mLockPinCode += "0";
                changeDisplayPassword(mPinCodeNumber);
            } else {
                Toast.makeText(this, "Full 4 colors of password",
                        Toast.LENGTH_LONG).show();
            }
            break;
        }
    }
}
