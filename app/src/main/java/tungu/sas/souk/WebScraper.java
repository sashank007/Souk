package tungu.sas.souk;
//created by Sashank Tungaturthi

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;

import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toolbar;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.dd.morphingbutton.impl.LinearProgressButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import tungu.sas.souk.MainActivity;
import tungu.sas.souk.R;

import static android.content.ContentValues.TAG;
import static com.google.common.primitives.Ints.max;
import static java.lang.Math.min;

public class WebScraper extends Activity {


    final static int REQUEST_CODE_PERMISSIONS = 151;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    static ProgressDialog mProgressDialog;
    static Switch Popularity;
    Switch Sort,strictSearchSwitch;
    WebScraper webScraper;
    TextView sorter, popular,strictSearch;
    static TextView imageDetails;
    Button imageSearch, Search;
    static Boolean pops = false, sorting = false ,strictSearchBool=false;
    private static int Size = 0;
    static final ArrayList<tungu.sas.souk.Products> sumProducts = new ArrayList<>();

    TextInputLayout til_product;
    Toolbar mTopToolbar;
    String strNoSpaces="";
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
    RecyclerView mRecyclerView;
    private ProductsRecyclerAdapter pra;
    Uri imageUri = null;
    EditText productNameQuery;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webscraper_layout);
        checkPermissions();
//        mGoogleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        imageDetails = (TextView) findViewById(R.id.image_details);
        //imageSearch = (Button) findViewById(R.id.image_search);
        Popularity = (Switch) findViewById(R.id.switch_popularity);
        popular = (TextView) findViewById(R.id.tv_popularity);
        Sort = (Switch) findViewById(R.id.switch_sort);
        sorter = (TextView) findViewById(R.id.tv_sort);
        til_product = (TextInputLayout) findViewById(R.id.et_product);
        productNameQuery = (EditText)findViewById(R.id.et_productText);
        productNameQuery.addTextChangedListener(new MyTextWatcher(productNameQuery));
        strictSearch=(TextView)findViewById(R.id.tv_strictsearch);
        strictSearchSwitch =(Switch)findViewById(R.id.switch_strictsearch);
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(mTopToolbar);

        mTopToolbar.setLogo(R.mipmap.ic_shopping_cart_white_24dp);



        mTopToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WebScraper.this, "Click", Toast.LENGTH_LONG).show();
            }
        });


        Search = (Button) findViewById(R.id.search_btn);
        Search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               Search.setAnimation(buttonClick);
                if (!productNameQuery.getText().toString().isEmpty()) {
                    new Title(WebScraper.this).execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Please put in a product!", Toast.LENGTH_LONG).show();
                }

            }
        });
//
//        imageSearch.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
////               Intent i = new Intent(WebScraper.this,CameraCapture.class);
////               startActivity(i);
//                CameraCapture cc = new CameraCapture();
//                try {
////                    imageUri = cc.captureImage(getApplicationContext());
////                    Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
////
////                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////
////                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
////
////                    startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//                    captureImage();
//                } catch (Exception e) {
//                    Log.e("exception", e.toString());
//
//                }
//
//            }
//        });


        Popularity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (!isChecked) {
                    popular.setText(R.string.Off);
                    pops = false;
                }
                if (isChecked) {
                    popular.setText(R.string.On);
                    pops = true;
                }
            }
        });
        strictSearchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (!isChecked) {
                    strictSearch.setText(R.string.Off);
                   strictSearchBool = false;
                }
                if (isChecked) {
                   strictSearch.setText(R.string.On);
                   strictSearchBool = true;
                }
            }
        });

        Sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    sorter.setText(R.string.ascending);
                    sorting = false;
                }
                if (b) {
                    sorter.setText(R.string.descending);
                    sorting = true;
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

//                /*********** Load Captured Image And Data Start ****************/

                String imageId = convertImageUriToFile(imageUri, webScraper);


                //  Create and excecute AsyncTask to load capture image

                new LoadImagesFromSDCard().execute("" + imageId);

//                /*********** Load Captured Image And Data End ****************/


            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validateProduct()
    {
        if(productNameQuery.getText().toString().trim().isEmpty())
        {
            til_product.setErrorEnabled(true);
            til_product.setError(getString(R.string.error_notext));
            requestFocus(productNameQuery);
            return false;
        }
        else if (productNameQuery.getText().length()>25)
        {
            til_product.setError(getString(R.string.error_text_too_long));
            return false;
        }
        else {
            til_product.setErrorEnabled(false);
            return true;
        }

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;

        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_productText:
                    validateProduct();
                    break;

            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    public void captureImage() throws Exception {
        String fileName = "Camera_Example.jpg";

        // Create parameters for Intent with filename

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, fileName);

        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

        // imageUri is the current activity attribute, define and save it for later usage

        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/


        // Standard Intent action that can be sent to have the camera
        // application capture an image and return it.
//
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        Log.d("imageCaptureUri", imageUri.toString());
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }



    public static String convertImageUriToFile(Uri imageUri, Activity activity) {

        Cursor cursor = null;
        int imageID = 0;

        try {

            /*********** Which columns values want to get *******/
            String[] proj = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.managedQuery(

                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);


            int size = cursor.getCount();

            /*******  If size is 0, there are no images on the SD Card. *****/

            if (size == 0) {


                imageDetails.setText("No Image");
            } else {

                int thumbID = 0;
                if (cursor.moveToFirst()) {

                    /**************** Captured image details ************/

                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID = cursor.getInt(columnIndex);

                    thumbID = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);

                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                            + " ImageID :" + imageID + "\n"
                            + " ThumbID :" + thumbID + "\n"
                            + " Path :" + Path + "\n";

                    // Show Captured Image detail on activity
                    imageDetails.setText(CapturedImageDetails);

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return "" + imageID;
    }

    public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(WebScraper.this);

        Bitmap mBitmap;

        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/

            // Progress Dialog
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }


        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;


            try {


                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);


//                /**************  Decode an input stream into a bitmap. *********/
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                if (bitmap != null) {

                    /********* Creates a new bitmap, scaled from an existing bitmap. ***********/

                    newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);

                    bitmap.recycle();

                    if (newBitmap != null) {

                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {
                // Error fetching image, try to recover

                /********* Cancel execution of this task. **********/
                cancel(true);
            }

            return null;
        }


        protected void onPostExecute(Void unused) {

            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (mBitmap != null) {
                // Set Image to ImageView
                try {
                    callCloudVision(mBitmap);
                    //showImg.setImageBitmap(mBitmap);

                } catch (Exception e) {
                }

            }

        }

    }

    private void simulateProgress1(@NonNull final IndeterminateProgressButton button) {
        int progressColor1 = getColor(R.color.holo_blue_bright);
        int progressColor2 = getColor(R.color.holo_green_light);
        int progressColor3 = getColor(R.color.holo_orange_light);
        int progressColor4 = getColor(R.color.holo_red_light);
        int color = getColor(R.color.mb_gray);
        int progressCornerRadius = (int) getResources().getDimension(R.dimen.mb_corner_radius_4);
        int width = (int) getResources().getDimension(R.dimen.mb_width_200);
        int height = (int) getResources().getDimension(R.dimen.mb_height_8);
        int duration = (int) getResources().getInteger(R.integer.mb_animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //morphToSuccess(button);
                button.unblockTouch();
            }
        }, 4000);
        button.blockTouch(); // prevent user from clicking while button is in progress
        button.blockTouch(); // prevent user from clicking while button is in progress
        button.morphToProgress(color, progressCornerRadius, width, height, duration, progressColor1, progressColor2,
                progressColor3, progressColor4);
    }

    // AsyncTask to fetch from all ecommerce websites

    private  class Title extends AsyncTask<Void, Void, Void> {
        String title;
        String url = "";
        final int maxItems = 15;
        private Context context;
        private Title(Context ctx)
        {
            context=ctx;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //onMorphButton1Clicked(linearProgressButton);
            mProgressDialog = new ProgressDialog(context);
            sumProducts.clear();
            pops = Popularity.isChecked();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site

                strNoSpaces = productNameQuery.getText().toString().trim().toLowerCase().replace(" ", "+");

//                    if (!document.select("div.product-tuple-description").isEmpty())
                {


                    if (pops) {
                        url =getResources().getString(R.string.urlSnapdealPop)+strNoSpaces;


                    } else {
                        url =getResources().getString(R.string.urlSnapdeal)+strNoSpaces;

                    }

                    Log.d("urlProductName", url);
                    try {
                        {
                            Document document = Jsoup.connect(url).get();
                            String snapdealImgUrl = "";
                            Elements links = document.select("div.product-tuple-description");
                            Elements productHrefs = document.select("a.dp-widget-link hashAdded");
                            //Elements prices = document.select("span.lfloat product-price");
                            //Elements productNames = document.select("p.product-title");
                            Elements imgs = document.select("img.product-image");

                            Size = min(links.size(), maxItems);
                            if (!links.isEmpty()) {
                                for (int i = 0; i < Size; i++) {
                                    Bitmap bitmap;
                                    String productHrefUrl;
                                    Element link = links.get(i);
                                    Element productHref = link.getElementsByTag("a").first();
                                    productHrefUrl = productHref.absUrl("href");
//                                    Element productName = productNames.get(i);
//                                    //Element productPrice = link.getElementsByTag("span").get(1);
//                                    Element productPrice = prices.get(i);
                                    snapdealImgUrl=imgs.get(i).absUrl("src");
                                    Element productName = link.getElementsByTag("p").get(0);
                                    Element productPrice = link.getElementsByTag("span").get(0);
                                    Log.d("productPriceSnapdeal", productPrice.text());
                                    Log.d("productNameSnapdeal", productName.text());
                                    //Elements img = link.getElementsByTag("a");
                                    Log.d("imgSnapdeal", snapdealImgUrl);
                                    if (strictSearchBool) {
                                        if (productName.text().toLowerCase().contains(strNoSpaces) && link.hasText()) {
                                            sumProducts.add((new tungu.sas.souk.Products(productName.text(), productPrice.text(), context.getString(R.string.Snapdeal), productHrefUrl,snapdealImgUrl)));

                                        }
                                    } else {
                                        if (link.hasText() && productName.hasText()) {
                                            sumProducts.add((new tungu.sas.souk.Products(productName.text(), productPrice.text(), context.getString(R.string.Snapdeal), productHrefUrl,snapdealImgUrl)));

                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                {
                    String url = "";

                    if (pops) {
                        url =getResources().getString(R.string.urlFlipkartPop)+strNoSpaces;


                    } else {
                        url = getResources().getString(R.string.urlFlipkart)+strNoSpaces;

                    }
                    Log.d("urlProductName", url);
                    try {
                      Document document = Jsoup.connect(url).get();
                        Elements links = document.select("div._3liAhj");
                        Elements prices = document.select("div._1vC4OE");
                        //Elements images = document.select("img");
                        Elements imgs = document.select("div._3BTv9X");
                        //Elements imgs = document.select("img._1Nyybr");
                        Log.d("pricesFlipkart",prices.text());
                        //Elements imgs = document.select("div._3BTv9X");
                        Size = min(links.size(), maxItems);
                        if (!links.isEmpty()) {
                            for (int i = 0; i < Size; i++) {

                                Element link = links.get(i);
                                Element price = prices.get(i);
                                Element productName = link.getElementsByClass("_2cLu-l").get(0);
                                Element productHref = link.getElementsByClass("Zhf2z-").get(0);
                                //Element img  = imgs.get(i).getElementsByAttribute("src").get(0);
                                //String imgUrlFlipkart = img.absUrl("src");
                                //Log.d("imgFlipkart",imgUrlFlipkart);
                                String hrefUrl = productHref.absUrl("href");
                                Log.d("flipkartProduct", price.toString());
                                if (strictSearchBool) {
                                    if (productName.text().toLowerCase().contains(strNoSpaces) && productName.hasText() && price.hasText()) {
                                        sumProducts.add((new tungu.sas.souk.Products(productName.text(), price.text(), context.getString(R.string.Flipkart),hrefUrl)));


                                    }

                                } else {
                                    if (productName.hasText() && price.hasText()) {
                                        sumProducts.add((new tungu.sas.souk.Products(productName.text(), price.text(), context.getString(R.string.Flipkart), hrefUrl)));

                                    }
                                }
                            }
                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }

                {
                    String url = "";
                    if (pops) {
                        url =getResources().getString(R.string.urlAmazonPop) +strNoSpaces;

                    } else {
                        url = getResources().getString(R.string.urlAmazon) +strNoSpaces;

                    }
                    {

                    Log.d("urlProductName", url);
                   Element document = Jsoup.connect(url).get();

                        Elements links = document.select("div.s-item-container");
                        Elements prices = document.select("span.a-price-whole");
                        //Elements names = document.select("h2.a-size-medium s-inline  s-access-title  a-text-normal");


                        String hrefAmazonUrl;
                        Size = min(links.size(), maxItems);
                        if (!links.isEmpty() && !prices.isEmpty()) {
                            for (int i = 0; i < Size; i++) {
                                Element link = links.get(i);
                                Element price = prices.get(i);
                                //Elements price = link.getElementsByClass("a-size-base a-color-price s-price a-text-bold");
                                Elements hrefAmazon = link.getElementsByAttribute("href");
                                hrefAmazonUrl = hrefAmazon.get(0).absUrl("href");

                                Element productName = link.getElementsByTag("h2").get(0);
                                Log.d("productNameAmazon",productName.toString());
                                Elements imagesAmazon = link.getElementsByTag("img");
                                Element im = imagesAmazon.get(0);
                                String amazonImgUrl = im.absUrl("src");
                                if(strictSearchBool)
                                {
                                if (productName.text().toLowerCase().contains(strNoSpaces) && productName.hasText() && price.text().matches(".*\\d+.*")) {

                                    Log.d("productName", productName.text());
                                    Log.d("productPrice", price.text());
                                    Log.d("imgUrl", amazonImgUrl);
                                    String priceRupees = "Rs." + " " + price.text();
                                    sumProducts.add((new tungu.sas.souk.Products(productName.text(), priceRupees, context.getString(R.string.Amazon), hrefAmazonUrl,amazonImgUrl)));

                                }
                            }
                            else
                                {
                                    if ( productName.hasText() && price.text().matches(".*\\d+.*")) {

                                        Log.d("productName", productName.text());
                                        Log.d("productPrice", price.text());
                                        Log.d("imgUrl", amazonImgUrl);
                                        String priceRupees = "Rs." + " " + price.text();
                                        sumProducts.add((new tungu.sas.souk.Products(productName.text(), priceRupees, context.getString(R.string.Amazon), hrefAmazonUrl,amazonImgUrl)));

                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            if (sumProducts.size() != 0) {
//                Intent i = new Intent(context, tungu.sas.souk.RecyclerViewProducts.class);

                if (!sorting) {
                    Collections.sort(sumProducts, new Comparator<tungu.sas.souk.Products>() {
                        @Override
                        public int compare(tungu.sas.souk.Products p1, tungu.sas.souk.Products p2) {
                            String p1string = p1.getProductPrice().split("\\W*[.][0-9]+$", 2)[0];
                            String p2string = p2.getProductPrice().split("\\W*[.][0-9]+$", 2)[0];
                            Log.d("p1String", p1string);
                            int p1Int = Math.round(Integer.parseInt(p1string.replaceAll("\\D", "")));
                            int p2Int = Math.round(Integer.parseInt(p2string.replaceAll("\\D", "")));
                            return p1Int - p2Int; // Descending
                        }
                    });
                }
                if (sorting) {
                    Collections.sort(sumProducts, new Comparator<tungu.sas.souk.Products>() {
                        @Override
                        public int compare(tungu.sas.souk.Products p1, tungu.sas.souk.Products p2) {
                            String p1string = p1.getProductPrice().split("\\W*[.][0-9]+$", 2)[0];
                            String p2string = p2.getProductPrice().split("\\W*[.][0-9]+$", 2)[0];
                            Log.d("p1String", p1string);
                            int p1Int = Math.round(Integer.parseInt(p1string.replaceAll("\\D", "")));
                            int p2Int = Math.round(Integer.parseInt(p2string.replaceAll("\\D", "")));
                            return p2Int - p1Int; // Descending
                        }
                    });
                }
                //i.putParcelableArrayListExtra("products", sumProducts);
                mProgressDialog.dismiss();
                Log.d("productNameQueryEnding",productNameQuery.getText().toString());
                productNameQuery.setText(null);
                Log.d("productNameQueryEnding",productNameQuery.getText().toString());
                //recreate();
                //context.startActivity(i);
                //productNameQuery.getText().clear();
//
//                pra= new ProductsRecyclerAdapter(getApplicationContext(),sumProducts);
//                mRecyclerView.setAdapter(pra);
//                RecyclerView.LayoutManager layoutManager =
//                        new LinearLayoutManager(WebScraper.this);
//                mRecyclerView.setLayoutManager(layoutManager);
//                mRecyclerView.setHasFixedSize(true);

                Intent i = new Intent(WebScraper.this,RecyclerViewActivity.class);
                i.putParcelableArrayListExtra("sumProducts",sumProducts);
                startActivity(i);

                //til_product.invalidate();
            } else {
                mProgressDialog.dismiss();
                //morphToNormal(linearProgressButton);
//                recreate();
                //productNameQuery.getText().clear();
                productNameQuery.setText(null);
                Log.d("productNameQuery",productNameQuery.getText().toString());
                Toast.makeText(context, "Oops...Looks like we could not find anything related to this.", Toast.LENGTH_LONG).show();
                //til_product.invalidate();
            }
        }
    }

    //Options Menu


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
                Intent i = new Intent(WebScraper.this, tungu.sas.souk.About.class);
                startActivity(i);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        imageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(getString(R.string.CLOUD_VISION_API_KEY)) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(getString(R.string.ANDROID_PACKAGE_HEADER), packageName);

                                    String sig = tungu.sas.souk.PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(getString(R.string.ANDROID_CERT_HEADER), sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response).toString();

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                imageDetails.setText(result);
            }
        }.execute();
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private StringBuilder convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder();

        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.US, "%s", label.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing");
        }

        return message;
    }


    public void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        }


    }
}