package com.mbds.vamp.dashboardapp.controllers;


        import android.os.Bundle;

        import com.mbds.vamp.dashboardapp.R;
        import com.mbds.vamp.dashboardapp.controllers.fragments.CarFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.ControlsFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.HomeFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.LocationFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.MusicFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.NavFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.ProfileFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.SearchFragment;
        import com.mbds.vamp.dashboardapp.controllers.fragments.TemperatureFragment;

        import android.support.v7.app.AppCompatActivity;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.View;


public class AppActivity extends AppCompatActivity implements
        CarFragment.OnItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
       // mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.vocal_assistant);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }
    @Override
    public void onRssItemSelected(String link) {
       /* DetailFragment fragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailFragment);

        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(link);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_URL, link);
            startActivity(intent);

        }*/
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {

                case 0 : return new HomeFragment();
                case 1 : return new SearchFragment();
                case 2 : return new NavFragment();
                case 3 : return new ControlsFragment();
                case 4 : return new LocationFragment();
                case 5 : return new TemperatureFragment();
                case 6 : return new MusicFragment();
                case 7 : return new ProfileFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 8 total pages.
            return 8;
        }
    }
}
