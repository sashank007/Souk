package tungu.sas.souk;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Student on 1/7/2018.
 */

public class Products implements Parcelable {
    private String ProductName;



    private String ProductPrice;
    private Bitmap bitmap;
    private String urlImg;

    private String productHref;

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
    public String getProductHref() {
        return productHref;
    }

    public void setProductHref(String productHref) {
        this.productHref = productHref;
    }



    public static Creator<Products> getCREATOR() {
        return CREATOR;
    }

    private String Provider;

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        this.Provider = provider;
    }

    protected Products(Parcel in) {
        ProductName = in.readString();
        ProductPrice = in.readString();
        Provider = in.readString();
        urlImg = in.readString();
        productHref = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());

    }

    public Products(String ProductName, String productPrice) {
        this.ProductName = ProductName;
        this.ProductPrice = productPrice;
    }

    public Products(String ProductName, String productPrice, String provider) {
        this.ProductName = ProductName;
        this.ProductPrice = productPrice;
        this.Provider = provider;

    }

    public Products(String ProductName, String productPrice, String provider, String productHref) {
        this.ProductName = ProductName;
        this.ProductPrice = productPrice;
        this.Provider = provider;
        this.productHref = productHref;

    }
    public Products(String ProductName,String productPrice,String provider,String productHref,String urlImage)
    {
        this.ProductName=ProductName;
        this.ProductPrice=productPrice;
        this.Provider=provider;
        this.urlImg=urlImage;
        this.productHref=productHref;

    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ProductName);
        parcel.writeString(ProductPrice);
        parcel.writeString(Provider);
        parcel.writeString(urlImg);
        parcel.writeString(productHref);
        parcel.writeParcelable(bitmap, i);
    }
}
