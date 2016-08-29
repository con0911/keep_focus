package com.accessibility.keepfocus.activity;

import java.util.ArrayList;

import com.accessibility.keepfocus.R;
import com.accessibility.keepfocus.database.KeepFocusDatabaseHelper;
import com.accessibility.keepfocus.database.KeepFocusItem;
import com.accessibility.keepfocus.database.NotificationItemMissHistory;
import com.accessibility.keepfocus.services.BlockLaunchService;
import com.accessibility.keepfocus.services.KeepFocusMainService;
import com.accessibility.keepfocus.settings.KeepFocusMainSettings;
import com.accessibility.keepfocus.utils.Utils;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class BlockNotificationActivity extends Activity implements OnClickListener {

    private ListView listProperties;
    private LinearLayout headerView;
    private ImageView mFABBtnCreate;
    public BlockNotificationActivity CustomListView = null;
    public ArrayList<KeepFocusItem> listBlockPropertiesArr;
    private KeepFocusDatabaseHelper mDataHelper;
    private Context mContext;
    private View mView;
    private EditText mEditText;
    private AlertDialog mAlertDialog;
    private TextView mTextMsg;
    static Button notifCount;
    static int mNotifCount = 0;
    private ProfileAdapter mProfileAdapter;
    private DialogNotiHistory mDialogNotiHistory;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_activity_main);
        mContext = this;

        listProperties = (ListView) findViewById(R.id.listP);
        headerView = (LinearLayout) getLayoutInflater().inflate(R.layout.header_view_profile, null);
        mFABBtnCreate = (ImageView) findViewById(R.id.createButton);
        listProperties.addHeaderView(headerView);
        listProperties.addFooterView(headerView);
        mFABBtnCreate.setOnClickListener(this);
        listProperties.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent = new Intent(BlockNotificationActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });
        mDataHelper = new KeepFocusDatabaseHelper(mContext);
        displayProfile();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
        	startService(new Intent(this, KeepFocusMainService.class));
        } 
        startService(new Intent(this, BlockLaunchService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.createButton:
            createNewProfile();
            break;
        default:
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        
        View count = menu.findItem(R.id.badge).getActionView();
        notifCount = (Button) count.findViewById(R.id.notif_count);
        notifCount.setText(mNotifCount+"");
        MenuItem item = menu.findItem(R.id.badge);
        if (mNotifCount == 0) {
            item.setVisible(false);
        }
        count.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mDialogNotiHistory = new DialogNotiHistory(mContext,BlockNotificationActivity.this);
                mDialogNotiHistory.show();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.setting:
            Intent mShowInstalledAppIntent = new Intent(this, KeepFocusMainSettings.class);
            startActivity(mShowInstalledAppIntent);
            break;

        case R.id.about:
            Intent mShowAbout = new Intent(this, AboutActivity.class);
            startActivity(mShowAbout);
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayProfile();
        updateMissingNotifications();
    }

    public void createNewProfile() {
        mView = getLayoutInflater().inflate(R.layout.edit_name_popup_layout, null);
        mEditText = (EditText) mView.findViewById(R.id.edit_name_edittext_popup);
        mTextMsg = (TextView) mView.findViewById(R.id.edit_name_text);
        mTextMsg.setText(R.string.create_profile_popup_msg);

        mAlertDialog = new AlertDialog.Builder(this).setView(mView).setTitle(getString(R.string.create_new_profile))
                .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!mEditText.getText().toString().equals("")) {
                            KeepFocusItem keepFocus = new KeepFocusItem();
                            keepFocus.setNameFocus(mEditText.getText().toString());
                            keepFocus.setDayFocus("");
                            mDataHelper.addNewFocusItem(keepFocus);
                            Utils.keepTempFocus = keepFocus;
                            Intent intent = new Intent(BlockNotificationActivity.this, ProfileEditActivity.class);
                            startActivity(intent);
                        } else {
                            dialog.cancel();
                        }
                    }
                }).setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }).create();

        mAlertDialog.show();
    }


    private ArrayList<NotificationItemMissHistory> getListMissingNotification(){
    	return mDataHelper.getListNotificaionHistoryItem();
    }
    
    private void updateMissingNotifications() {
        int size = getListMissingNotification().size();
        setNotifCount(size);
    }

    public void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    public void displayProfile() {
        listBlockPropertiesArr = mDataHelper.getAllKeepFocusFromDb();
        mProfileAdapter = new ProfileAdapter(this, R.layout.tab_profile,
               0, listBlockPropertiesArr);
        listProperties.setAdapter(mProfileAdapter);
    }

    public void onItemClick(int position) {
        Utils.keepTempFocus = mProfileAdapter.getItem(position);
        Intent intent = new Intent(this, ProfileEditActivity.class);
        startActivity(intent);
    }

    public void onItemLongClick(int position) {
        deleteProfile(position);
    }

/*    public void longClickProfile(int position) {
        final int mPosition = position;
        mView = getLayoutInflater().inflate(R.layout.setup_profile_popup, null);
        Button btn_Delete = (Button) mView.findViewById(R.id.btnDelete);
        mAlertDialog = new AlertDialog.Builder(this).setView(mView).setTitle(mProfileAdapter.getItem(mPosition).getNameFocus()).create();
        btn_Delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfile(mPosition);
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.show();
    }*/

    public void deleteProfile(int position) {
        final int mPosition = position;
        View view = getLayoutInflater().inflate(R.layout.delete_profile_popup, null);
        TextView mTextMsg = (TextView) view.findViewById(R.id.delete_text);
        mTextMsg.setText(R.string.delete_profile_popup_msg);
        AlertDialog mDeleteDialog = new AlertDialog.Builder(this).setView(view).setTitle(getString(R.string.popup_setting_delete))
                .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDataHelper.deleteFocusItemById(mProfileAdapter.getItem(mPosition).getKeepFocusId());
                        displayProfile();
                    }
                }).setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }).create();
        mDeleteDialog.show();
    }
}
