package com.aidr.aidr;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AIDrChat extends FragmentActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private TextView reminder;
    private TextView chat;
    private TextView tips;

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void launchAddReminder(View view) {
        Intent intent = new Intent(this, AddReminder.class);
        startActivity(intent);
    }

    public void launchChat(View view) {
        int buttonType;
        if (view.getId() == R.id.speechButton) {
            buttonType = 0;
        } else {
            buttonType = 1;
        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("buttonPressed",buttonType);
        startActivity(intent);
    }

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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ReminderFragment();
            } else if (position == 1) {
                return new ChatFragment();
            } else { //position == 2
                return new TipsFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

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
