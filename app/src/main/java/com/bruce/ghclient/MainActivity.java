package com.bruce.ghclient;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.ghclient.models.realm.UserRealm;
import com.bruce.ghclient.network.GithubService;
import com.bruce.ghclient.network.GithubServiceManager;
import com.bruce.ghclient.network.github.GithubPreManager;
import com.bruce.ghclient.utils.ImageUtils;
import com.bruce.ghclient.views.fragment.TestFragment;
import com.bruce.ghclient.widget.CircleImageView;
import com.bruce.ghclient.widget.ProgressWheel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.support.design.widget.RxNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.view_pager)
    ViewPager mPager;
    @Bind(R.id.tool_bar)
    Toolbar mToolBar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    private long preClickTime;
    private GithubService mGithubService;
    private View mHeaderView;
    private CircleImageView mAvatar;
    private TextView mName;
    private TextView mLocation;
    private ProgressWheel mProgress;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGithubService = GithubServiceManager.createGithubService();
        mRealm = Realm.getDefaultInstance();

        setUpToolBar();

        setUpHeaderView();

        setUpViewPager();
    }

    private void setUpHeaderView() {
        mHeaderView = mNavView.getHeaderView(0);
        mAvatar = (CircleImageView) mHeaderView.findViewById(R.id.img_avatar);
        mName = (TextView) mHeaderView.findViewById(R.id.text_name);
        mLocation = (TextView) mHeaderView.findViewById(R.id.text_location);
        mProgress = (ProgressWheel) mHeaderView.findViewById(R.id.progress);
        mProgress.setBarColor(R.color.colorPrimary);
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolBar);

        //Set up home button animation
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        RxNavigationView.itemSelections(mNavView).subscribe(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            switch (menuItem.getItemId()){
                case R.id.nav_gist:

                    break;
                case R.id.nav_issue:

                    break;
                case R.id.nav_bookmark:

                    break;
                case R.id.nav_feedback:

                    break;
            }
        });
    }

    private void setUpViewPager() {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new TestFragment(), "Category 1");
        adapter.addFragment(new TestFragment(), "Category 2");
        adapter.addFragment(new TestFragment(), "Category 3");
        mPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mRealm.where(UserRealm.class).findAll().size() != 0){
            Timber.e("There are data persis in Realm");
             mRealm.where(UserRealm.class).findAllSortedAsync("name").asObservable()
                     .subscribe(userRealms -> refreshHeaderView(userRealms.first()));
        }else {
            Timber.e("Fetch data from Remote...");
            mGithubService.getUserProfile(GithubPreManager.getUserName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserRealm>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(UserRealm user) {

                            //persis data
                            mRealm.beginTransaction();
                            mRealm.copyToRealmOrUpdate(user);
                            mRealm.commitTransaction();

                            refreshHeaderView(user);
                        }
                    });
        }
    }

    private void refreshHeaderView(UserRealm user) {
        mProgress.setVisibility(View.GONE);
        mName.setText(user.getName());
        mLocation.setText(user.getLocation());
        Glide.with(MainActivity.this)
                .load(user.getAvatar_url())
                .asBitmap()
                .placeholder(R.drawable.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mAvatar.setImageBitmap(resource);
                        mHeaderView.setBackground(new BitmapDrawable(ImageUtils.blurBitmap(resource, 20)));
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:

                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - preClickTime >= 2000) {
            preClickTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "Press again to Exit!",
                    Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
