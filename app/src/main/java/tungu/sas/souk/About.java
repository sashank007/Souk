package tungu.sas.souk;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toolbar;

/**
 * Created by Student on 1/16/2018.
 */

public class About extends Activity {
    private Toolbar mTopToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar_about);
//        setActionBar(mTopToolbar);
//        mTopToolbar.setLogo(R.mipmap.ic_shopping_cart_white_24dp);
        setContentView(R.layout.about_layout);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
