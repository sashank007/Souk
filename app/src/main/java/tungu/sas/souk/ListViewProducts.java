package tungu.sas.souk;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import java.util.ArrayList;

/**
 * Created by Student on 10/21/2017.
 */

public class ListViewProducts extends Activity {

    ListView list;

    ArrayList<Products> products, productsFlipkart;
    ArrayList<String> labels = new ArrayList<>();
    PopupWindow popUpmessage;
    RecyclerView rv;
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar_list);
//        setActionBar(mTopToolbar);
//        mTopToolbar.setLogo(R.mipmap.ic_shopping_cart_white_24dp);

        Intent i = getIntent();
        //labels=i.getStringArrayListExtra("productsname");
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar_list);
        setActionBar(mTopToolbar);
        //mTopToolbar.setLogo(R.mipmap.ic_shopping_cart_white_24dp);

        setContentView(R.layout.listview_products_layout);
        //setContentView(R.layout.recycler_view_products_layout);
        list = (ListView) findViewById(R.id.listViewProducts1);

//        rv.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
//        rv.setLayoutManager(llm);

        try {


            products = this.getIntent().getParcelableArrayListExtra("products");
//
//
//            InputStream input = new java.net.URL(urlWatch).openStream();
//            // Decode Bitmap
//            bitmap = BitmapFactory.decodeStream(input);
            ;
        } catch (Exception e) {
        }

        tungu.sas.souk.CustomAdapter ca = new tungu.sas.souk.CustomAdapter(this, products);
        list.setAdapter(ca);
//        RecyclerViewProducts rvp= new RecyclerViewProducts(this,products);
//        rv.setAdapter(rvp);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                String keyword=labels.get(position);
//
//                Log.d("keyword",keyword);
//                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.flipkart.android");
//                if (launchIntent != null) {
//                    startActivity(launchIntent);//null pointer check in case package name was not found
//                }

            }
        });

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent i = new Intent(ListViewProducts.this, tungu.sas.souk.About.class);
                startActivity(i);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}

