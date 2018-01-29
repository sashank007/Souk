package tungu.sas.souk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tungu.sas.souk.MainActivity;
import tungu.sas.souk.R;
import tungu.sas.souk.WebScraper;

import static android.content.ContentValues.TAG;

public class CameraCapture {


    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;


    Uri imageUri                      = null;
//    static TextView imageDetails      = null;
//    public  static ImageView showImg  = null;
//    CameraCapture CameraActivity = null;
//    FloatingActionButton fab;
//    String imageResults=null;
//



    public Uri captureImage(Context context) throws Exception
    {
        String fileName = "Camera_Example.jpg";

        // Create parameters for Intent with filename

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, fileName);

        values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");

        // imageUri is the current activity attribute, define and save it for later usage

        imageUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/


        // Standard Intent action that can be sent to have the camera
        // application capture an image and return it.
//
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        Log.d("imageCaptureUri" , imageUri.toString());
////        startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        return imageUri;
    }
//    @Override
//    protected void onActivityResult( int requestCode, int resultCode, Intent data)
//    {
//        if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//
//            if ( resultCode == RESULT_OK) {
//
//                /*********** Load Captured Image And Data Start ****************/
//
//                String imageId = convertImageUriToFile( imageUri,CameraActivity);
//
//
//                //  Create and excecute AsyncTask to load capture image
//
//                new LoadImagesFromSDCard().execute(""+imageId);
//
//                /*********** Load Captured Image And Data End ****************/
//
//
//            } else if ( resultCode == RESULT_CANCELED) {
//
//                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
//            } else {
//
//                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    /************ Convert Image Uri path to physical path **************/
//
//    public static String convertImageUriToFile ( Uri imageUri, Activity activity )  {
//
//        Cursor cursor = null;
//        int imageID = 0;
//
//        try {
//
//            /*********** Which columns values want to get *******/
//            String [] proj={
//                    MediaStore.Images.Media.DATA,
//                    MediaStore.Images.Media._ID,
//                    MediaStore.Images.Thumbnails._ID,
//                    MediaStore.Images.ImageColumns.ORIENTATION
//            };
//
//            cursor = activity.managedQuery(
//
//                    imageUri,         //  Get data for specific image URI
//                    proj,             //  Which columns to return
//                    null,             //  WHERE clause; which rows to return (all rows)
//                    null,             //  WHERE clause selection arguments (none)
//                    null              //  Order-by clause (ascending by name)
//
//            );
//
//            //  Get Query Data
//
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
//            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//            //int orientation_ColumnIndex = cursor.
//            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
//
//            int size = cursor.getCount();
//
//            /*******  If size is 0, there are no images on the SD Card. *****/
//
//            if (size == 0) {
//
//
//                imageDetails.setText("No Image");
//            }
//            else
//            {
//
//                int thumbID = 0;
//                if (cursor.moveToFirst()) {
//
//                    /**************** Captured image details ************/
//
//                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
//                    imageID     = cursor.getInt(columnIndex);
//
//                    thumbID     = cursor.getInt(columnIndexThumb);
//
//                    String Path = cursor.getString(file_ColumnIndex);
//
//                    //String orientation =  cursor.getString(orientation_ColumnIndex);
//
//                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
//                            +" ImageID :"+imageID+"\n"
//                            +" ThumbID :"+thumbID+"\n"
//                            +" Path :"+Path+"\n";
//
//                    // Show Captured Image detail on activity
//                    imageDetails.setText( CapturedImageDetails );
//
//                }
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )
//
//        return ""+imageID;
//    }
//
//
//    /**
//     * Async task for loading the images from the SD card.
//     *
//
//     *
//     */
//
//    // Class with extends AsyncTask class
//
//    public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
//
//        private ProgressDialog Dialog = new ProgressDialog(CameraCapture.this);
//
//        Bitmap mBitmap;
//
//        protected void onPreExecute() {
//            /****** NOTE: You can call UI Element here. *****/
//
//            // Progress Dialog
//            Dialog.setMessage(" Loading image from Sdcard..");
//            Dialog.show();
//        }
//
//
//        // Call after onPreExecute method
//        protected Void doInBackground(String... urls) {
//
//            Bitmap bitmap = null;
//            Bitmap newBitmap = null;
//            Uri uri = null;
//
//
//            try {
//
//
//
//                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);
//
//
//                /**************  Decode an input stream into a bitmap. *********/
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//
//                if (bitmap != null) {
//
//                    /********* Creates a new bitmap, scaled from an existing bitmap. ***********/
//
//                    newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);
//
//                    bitmap.recycle();
//
//                    if (newBitmap != null) {
//
//                        mBitmap = newBitmap;
//                    }
//                }
//            } catch (IOException e) {
//                // Error fetching image, try to recover
//
//                /********* Cancel execution of this task. **********/
//                cancel(true);
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void unused) {
//
//            // NOTE: You can call UI Element here.
//
//            // Close progress dialog
//            Dialog.dismiss();
//
//            if(mBitmap != null)
//            {
//                // Set Image to ImageView
//                try {
//                    callCloudVision(mBitmap);
//                    showImg.setImageBitmap(mBitmap);
//
//                }
//                catch (Exception e)
//                {}
//
//            }
//
//        }
//
//    }
//
//
//    public void uploadImage(Uri uri) {
//        if (uri != null) {
//            try {
//                // scale the image to save on bandwidth
//                Bitmap bitmap =
//                        scaleBitmapDown(
//                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
//                                1200);
//
//                callCloudVision(bitmap);
//                showImg.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                Log.d(TAG, "Image picking failed because " + e.getMessage());
//                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Log.d(TAG, "Image picker gave us a null image.");
//            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
//        }
//    }
//    public void callCloudVision(final Bitmap bitmap) throws IOException {
//        // Switch text to loading
//        imageDetails.setText(R.string.loading_message);
//
//        // Do the real work in an async task, because we need to use the network anyway
//        new AsyncTask<Object, Void, String>() {
//            @Override
//            protected String doInBackground(Object... params) {
//                try {
//                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
//                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//
//                    VisionRequestInitializer requestInitializer =
//                            new VisionRequestInitializer(MainActivity.CLOUD_VISION_API_KEY) {
//                                /**
//                                 * We override this so we can inject important identifying fields into the HTTP
//                                 * headers. This enables use of a restricted cloud platform API key.
//                                 */
//                                @Override
//                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
//                                        throws IOException {
//                                    super.initializeVisionRequest(visionRequest);
//
//                                    String packageName = getPackageName();
//                                    visionRequest.getRequestHeaders().set(MainActivity.ANDROID_PACKAGE_HEADER, packageName);
//
//                                    String sig = tungu.sas.souk.PackageManagerUtils.getSignature(getPackageManager(), packageName);
//
//                                    visionRequest.getRequestHeaders().set(MainActivity.ANDROID_CERT_HEADER, sig);
//                                }
//                            };
//
//                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
//                    builder.setVisionRequestInitializer(requestInitializer);
//
//                    Vision vision = builder.build();
//
//                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
//                            new BatchAnnotateImagesRequest();
//                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
//                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
//
//                        // Add the image
//                        Image base64EncodedImage = new Image();
//                        // Convert the bitmap to a JPEG
//                        // Just in case it's a format that Android understands but Cloud Vision
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
//                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
//
//                        // Base64 encode the JPEG
//                        base64EncodedImage.encodeContent(imageBytes);
//                        annotateImageRequest.setImage(base64EncodedImage);
//
//                        // add the features we want
//                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
//                            Feature labelDetection = new Feature();
//                            labelDetection.setType("LABEL_DETECTION");
//                            labelDetection.setMaxResults(10);
//                            add(labelDetection);
//                        }});
//
//                        // Add the list of one thing to the request
//                        add(annotateImageRequest);
//                    }});
//
//                    Vision.Images.Annotate annotateRequest =
//                            vision.images().annotate(batchAnnotateImagesRequest);
//                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
//                    annotateRequest.setDisableGZipContent(true);
//                    Log.d(TAG, "created Cloud Vision request object, sending request");
//
//                    BatchAnnotateImagesResponse response = annotateRequest.execute();
//                    return convertResponseToString(response);
//
//                } catch (GoogleJsonResponseException e) {
//                    Log.d(TAG, "failed to make API request because " + e.getContent());
//                } catch (IOException e) {
//                    Log.d(TAG, "failed to make API request because of other IOException " +
//                            e.getMessage());
//                }
//                return "Cloud Vision API request failed. Check logs for details.";
//            }
//
//            protected void onPostExecute(String result) {
//                imageDetails.setText(result);
//                imageResults=result;
//                Intent i = new Intent(CameraCapture.this,WebScraper.class);
//                i.putExtra("imageResults",imageResults);
//                startActivity(i);
//            }
//        }.execute();
//    }
//    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
//
//        int originalWidth = bitmap.getWidth();
//        int originalHeight = bitmap.getHeight();
//        int resizedWidth = maxDimension;
//        int resizedHeight = maxDimension;
//
//        if (originalHeight > originalWidth) {
//            resizedHeight = maxDimension;
//            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
//        } else if (originalWidth > originalHeight) {
//            resizedWidth = maxDimension;
//            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
//        } else if (originalHeight == originalWidth) {
//            resizedHeight = maxDimension;
//            resizedWidth = maxDimension;
//        }
//        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
//    }
//
//    private String convertResponseToString(BatchAnnotateImagesResponse response) {
//        String message = "I found these things:\n\n";
//
//        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
//        if (labels != null) {
//            for (EntityAnnotation label : labels) {
//                message += String.format(Locale.US, "%s",  label.getDescription());
//                message += "\n";
//            }
//        } else {
//            message += "nothing";
//        }
//
//        return message;
//    }

}