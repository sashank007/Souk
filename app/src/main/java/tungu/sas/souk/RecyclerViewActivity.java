package tungu.sas.souk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.util.Log;
import android.widget.Toolbar;


import java.util.ArrayList;

/**
 * Created by Student on 1/28/2018.
 */

public class RecyclerViewActivity extends Activity {
    RecyclerView rv;
    ProductsRecyclerAdapter pra;
    Toolbar mTopToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_products_layout);
        rv = (RecyclerView)findViewById(R.id.recycler_view);
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(mTopToolbar);

        mTopToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        //mTopToolbar.setLogo(R.mipmap.ic_shopping_cart_white_24dp);
        ;
       //mTopToolbar.setNavigationIcon(R.drawable.ic_lock);
       //mTopToolbar.setLogo(R.mipmap.ic_shopping_cart_white_24dp);



        Intent i = getIntent();
        ArrayList<Products> sumProducts = i.getParcelableArrayListExtra("sumProducts");
        Log.d("sumProducts",sumProducts.get(0).getProductName());

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        pra = new ProductsRecyclerAdapter(RecyclerViewActivity.this ,sumProducts);
       rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        rv.setAdapter(pra);
    }
}
