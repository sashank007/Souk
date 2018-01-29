package tungu.sas.souk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class CustomAdapter extends BaseAdapter {
    final Context context;
    final List<Products> rowItems;
    NetworkImageView imageView;
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
    ImageLoader imageLoader = ImageController.getInstance().getImageLoader();

    public CustomAdapter(Context context, List<Products> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {

        TextView productName;
        ImageView Provider;
        TextView productPrice, provider, url;
        boolean isSet;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(context);

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.list_item_single, null);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.tv_productname);
            holder.productPrice = (TextView) convertView.findViewById(R.id.tv_productprice);
            imageView = (NetworkImageView) convertView.findViewById(R.id.imageViewProduct);
            holder.Provider = (ImageView) convertView.findViewById(R.id.imageViewProvider);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        {
            if (imageLoader == null)
                imageLoader =ImageController.getInstance().getImageLoader();

        }
        Products m = rowItems.get(position);


        if(sp.getBoolean("position="+position, false)){

         imageView.setImageUrl(m.getUrlImg(), imageLoader);

        }
        imageView.setImageUrl(m.getUrlImg(), imageLoader);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("position="+position, true);
        edit.apply();


        final Products products = (Products) getItem(position);
        if (products.getProvider().equals("Amazon")) {
            holder.Provider.setImageDrawable(context.getResources().getDrawable(R.drawable.amazon_log));
        }
        if (products.getProvider().equals("Snapdeal")) {
            holder.Provider.setImageDrawable(context.getResources().getDrawable(R.drawable.snapdeal_logo));

        }
        if (products.getProvider().equals("Flipkart")) {
            holder.Provider.setImageDrawable(context.getResources().getDrawable(R.drawable.flipkart_logo));

        }

        holder.productName.setText(products.getProductName());
        holder.productPrice.setText(products.getProductPrice());

        //new GetXMLTask().execute(products.getUrlImg());

        //set oncliclistener for the view
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(buttonClick);
                String url = "";
                Uri uriUrl;
                if (products.getProvider().equals("Amazon")) {
                    String productName = holder.productName.getText().toString();
                    if (productName.length() > 50) {
                        productName = holder.productName.getText().toString().substring(0, 50);
                    }
                    String strNoSpaces = productName.replace(" ", "+");

                    url = products.getProductHref();
                    //Toast.makeText(context,url,Toast.LENGTH_LONG).show();
                    Log.d("productUrl", url);



                }
                if (products.getProvider().equals("Snapdeal")) {
                    String productName = holder.productName.getText().toString();
                    if (productName.length() > 50) {
                        productName = holder.productName.getText().toString().substring(0, 50);
                    }

                    String strNoSpaces = productName.replace(" ", "+");
                    url = products.getProductHref();
                    //Toast.makeText(context,url,Toast.LENGTH_LONG).show();
                    Log.d("productUrl", url);

                }
                if (products.getProvider().equals("Flipkart")) {
                    url = products.getProductHref();
//                    Toast.makeText(context,url,Toast.LENGTH_LONG).show();
                    Log.d("productUrl", url);

                }
                uriUrl = Uri.parse(url);


                Intent i = new Intent(Intent.ACTION_VIEW, uriUrl);
                context.startActivity(i);


                //Toast.makeText(context,holder.productPrice.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }


    public class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

}
