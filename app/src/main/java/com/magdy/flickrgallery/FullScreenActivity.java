package com.magdy.flickrgallery;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import com.magdy.flickrgallery.Data.Contract;

import java.util.ArrayList;
import java.util.List;

public class FullScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    List<String> links ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
        links = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(this,links);
        viewPager.setAdapter(viewPagerAdapter);

        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Image.URI,
                Contract.Image.IMAGE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Image.COLUMN_ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        links.clear();
        if (data !=null)
        {
            for (int i = 0; i < data.getCount(); i++) {
                String s ;
                data.moveToPosition(i);
                s=data.getString(Contract.Image.POSITION_IMAGE_LINK);
                links.add(s);
            }
            viewPagerAdapter.notifyDataSetChanged();
        }
        Intent intent = getIntent();
        if (intent!=null){
            viewPager.setCurrentItem(intent.getIntExtra("pos",0));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
