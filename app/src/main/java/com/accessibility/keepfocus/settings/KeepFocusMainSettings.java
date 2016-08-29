package com.accessibility.keepfocus.settings;

import com.accessibility.keepfocus.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class KeepFocusMainSettings extends Activity {

    private SharedPreferences myPass;
    private CheckBox checkBox;
    private Button set_PIN_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        set_PIN_code = (Button) findViewById(R.id.set_PIN_code);
        set_PIN_code.setEnabled(false);
        myPass = getSharedPreferences("myPass", MODE_PRIVATE);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        LinearLayout layout_access_notifa = (LinearLayout) findViewById(R.id.layout_access_notifi);
        LinearLayout layout_usage_access = (LinearLayout) findViewById(R.id.layout_access_usage);
        // Check version for hide permisson access
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            layout_usage_access.setVisibility(View.GONE);
        }
        layout_access_notifa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });

        layout_usage_access.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent usageAccessIntent = new Intent(
                        Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(usageAccessIntent);
            }
        });

        RelativeLayout title_enable_PIN_code = (RelativeLayout) findViewById(R.id.title_enable_PIN_code);
        title_enable_PIN_code.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
                set_PIN_code.setEnabled(checkBox.isChecked());
                // set_PIN_code.getBackground().setColorFilter(Color.parseColor("#C0C0C0"),
                // Mode.MULTIPLY);
            }
        });

        set_PIN_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KeepFocusMainSettings.this,
                        LockColorActivity.class);
                startActivity(intent);
            }
        });

        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                set_PIN_code.setEnabled(checkBox.isChecked());
                set_PIN_code.setTextColor((getResources().getColor(R.color.color_set_pin_code_button)));
                set_PIN_code.getBackground().setColorFilter(
                        Color.parseColor("#3B5998"), Mode.MULTIPLY);
                set_PIN_code.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click));
                if (!checkBox.isChecked()) {
                    set_PIN_code.setTextColor((getResources().getColor(R.color.color_text_title_setting)));
                    set_PIN_code.getBackground().setColorFilter(
                    Color.parseColor("#FFFFFF"), Mode.MULTIPLY);
                    if(myPass.getString("PinCode", "") != "") {
                    deletePinCode();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        if(myPass.getString("PinCode", "") != "") {
            checkBox.setChecked(true);
            set_PIN_code.setEnabled(true);
            set_PIN_code.getBackground().setColorFilter(
                    Color.parseColor("#3B5998"), Mode.MULTIPLY);
            set_PIN_code.setTextColor((getResources().getColor(R.color.color_set_pin_code_button)));
        }
        super.onResume();
    }

    private void toastWrongPinCode() {
        Toast.makeText(this, "Wrong Password! Try again",Toast.LENGTH_LONG).show();
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(300);
    }

    public void deletePinCode() {
        View view = getLayoutInflater().inflate(R.layout.delete_pin_code_popup, null);
        TextView mTextMsg = (TextView) view.findViewById(R.id.delete_text);
        final EditText oldPinCode = (EditText) view.findViewById(R.id.input_pin_code);
        mTextMsg.setText(getString(R.string.delete_pincode_msg));
        AlertDialog mDeleteDialog = new AlertDialog.Builder(this).setView(view).setTitle(getString(R.string.delete_pincode))
                .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) { // ok to remove pincode
                        if(oldPinCode.getText().toString().equals(myPass.getString("PinCode", ""))) {
                        SharedPreferences.Editor e = myPass.edit();
                        e.putString("PinCode", "");
                        e.commit();
                        checkBox.setChecked(false);
                        } else {
                            toastWrongPinCode();
                            checkBox.setChecked(true);
                        }
                    }
                }).setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) { // cancel make checkbox check again
                        dialog.cancel();
                        checkBox.setChecked(true);
                    }
                }).create();
        mDeleteDialog.show();
        mDeleteDialog.setCancelable(false); // can't cancel dialog by touch out side
    }

}
