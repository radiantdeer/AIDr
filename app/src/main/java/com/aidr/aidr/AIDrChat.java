package com.aidr.aidr;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AIDrChat extends FragmentActivity {

    private static final int NUM_PAGES = 3; // Number of tabs

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    // Tab-texts
    private TextView reminder;
    private TextView chat;
    private TextView tips;

    public final static String EXTRA_MSG_CHAT = "buttonPressed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidr_chat);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        reminder = (TextView) findViewById(R.id.ReminderButton);
        chat = (TextView) findViewById(R.id.AIDrButton);
        tips = (TextView) findViewById(R.id.TipsButton);

        mPager = (ViewPager) findViewById(R.id.mainPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1);
        changeActiveTab(1);

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                changeActiveTab(position);
            }
        });

        DiseaseDB.initialize(this);
        ReminderDB.initialize(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Launch addReminder activity
    public void launchAddReminder(View view) {
        Intent intent = new Intent(this, AddReminder.class);
        startActivity(intent);
    }

    // Launch chat activity
    public void launchChat(View view) {
        int buttonType;
        if (view.getId() == R.id.speechButton) {
            buttonType = 0;
        } else {
            buttonType = 1;
        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(EXTRA_MSG_CHAT,buttonType);
        startActivity(intent);
    }

    /* switchTo* -> switch current tab to *
       What these functions do are :
       - Switch the ViewPager (mPager) to the desired tab
       - Highlight the appropriate tab-text
     */
    public void switchToReminder(View view) {
        mPager.setCurrentItem(0);
        changeActiveTab(0);
    }

    public void switchToChat(View view) {
        mPager.setCurrentItem(1);
        changeActiveTab(1);
    }

    public void switchToTips(View view) {
        mPager.setCurrentItem(2);
        changeActiveTab(2);
    }

    /* ViewPager tab controller */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private ReminderFragment reminderTab = new ReminderFragment();
        private ChatFragment chatTab = new ChatFragment();
        private TipsFragment tipsTab = new TipsFragment();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return reminderTab;
            } else if (position == 1) {
                return chatTab;
            } else { //position == 2
                return tipsTab;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    /* Highlights the desired tab-text
    *  0 -> reminder tab
    *  1 -> chat tab
    *  2 -> tips tab */
    public void changeActiveTab(int position) {
        reminder.setTextColor(Color.rgb(0, 0, 0));
        chat.setTextColor(Color.rgb(0, 0, 0));
        tips.setTextColor(Color.rgb(0, 0, 0));
        if (position == 0) {
            reminder.setTextColor(Color.rgb(0, 166, 255));
        } else if (position == 1) {
            chat.setTextColor(Color.rgb(0, 166, 255));
        } else {
            tips.setTextColor(Color.rgb(0, 166, 255));
        }
    }
}
