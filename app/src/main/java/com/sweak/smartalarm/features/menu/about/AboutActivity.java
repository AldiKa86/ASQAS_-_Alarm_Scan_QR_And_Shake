package com.sweak.smartalarm.features.menu.about;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.sweak.smartalarm.R;
import com.sweak.smartalarm.features.main.MainActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        SectionPagerAdapter sectionsPagerAdapter = new SectionPagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        Button nextPageButton = findViewById(R.id.nextPageButton); // Replace with your button ID
        nextPageButton.setOnClickListener(v -> {
            // Move to the next page (if available)
            int nextPage = viewPager.getCurrentItem() + 1;
            if (nextPage < sectionsPagerAdapter.getItemCount()) {
                viewPager.setCurrentItem(nextPage);
            }else{
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
    }
}