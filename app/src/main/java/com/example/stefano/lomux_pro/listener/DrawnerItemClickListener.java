package com.example.stefano.lomux_pro.listener;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.stefano.lomux_pro.LomuxMapActivity;
import com.example.stefano.lomux_pro.R;

import java.security.acl.Group;

/**
 * Created by Stefano on 16/10/2017.
 */

public class DrawnerItemClickListener implements  NavigationView.OnNavigationItemSelectedListener {
    private LomuxMapActivity view;

    private SearchView searchView;

    public DrawnerItemClickListener(LomuxMapActivity view) {
        this.view = view;
    }

    /**
     * Swaps fragments in the main content view
     */
   /* private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            control_check(item);
            press_search();

        } else if (id == R.id.nav_gallery) {
            control_check(item);

        } else if (id == R.id.nav_slideshow) {
            control_check(item);

        } else if (id == R.id.nav_manage) {
            control_check(item);

        } else if (id == R.id.nav_share) {
            control_check(item);

        } else if (id == R.id.nav_send) {
            control_check(item);

        }
        Log.d("CLICK", "test"+ id);
        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new Drawer_listener(item));

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void control_check(MenuItem item){
        /*if(item.isChecked())
            item.setChecked(false);*/
    }

    private void press_search(){
         searchView = view.findViewById(R.id.searchbar);
        if(searchView.getVisibility()!=View.VISIBLE) {
            searchView.setVisibility(View.VISIBLE);

            searchView.setAlpha(0.0f);
            searchView.setScaleX(1.4f);
            searchView.setScaleY(1.4f);
            searchView.animate().
                    scaleX(1).
                    scaleY(1)
                    .alpha(.8f);
        }
        else{

            searchView.performClick();
        }
    }

    private class Drawer_listener implements DrawerLayout.DrawerListener{
        private MenuItem item;

        private Drawer_listener(MenuItem item){
            this.item = item;
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if(searchView.isInTouchMode()){
                searchView.setQuery(searchView.getQuery(),false);
                searchView.setIconified(true);
            }
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            item.setChecked(false);
           // item.setCheckable(true);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
