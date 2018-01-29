package tungu.sas.souk;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Student on 1/28/2018.
 */

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.SimpleViewHolder> {
    private ArrayList<Products> mProducts = new ArrayList<>();
    ImageLoader imageLoader = ImageController.getInstance().getImageLoader();
    private Context mContext;

    public ProductsRecyclerAdapter(Context context, ArrayList<Products> products) {
        mContext = context;
        mProducts = products;
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SimpleViewHolder(view);
    }
    @Override
        public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Products p = mProducts.get(position);
        //setting the providers
        if (p.getProvider().equals("Amazon")) {
            holder.Provider.setImageDrawable(mContext.getResources().getDrawable(R.drawable.amazon_log));
        }
        if (p.getProvider().equals("Snapdeal")) {
            holder.Provider.setImageDrawable(mContext.getResources().getDrawable(R.drawable.snapdeal_logo));

        }
        if (p.getProvider().equals("Flipkart")) {
            holder.Provider.setImageDrawable(mContext.getResources().getDrawable(R.drawable.flipkart_logo));

        }



        holder.productName.setText(p.getProductName());
        holder.productPrice.setText(p.getProductPrice());
        imageLoader =ImageController.getInstance().getImageLoader();
        holder.imageView.setImageUrl(p.getUrlImg(), imageLoader);

    }

    @Override
    public int getItemCount() {
        return  mProducts.size();
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.list_item_plain;
    }
    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        final TextView productName;
        final ImageView Provider;
        final TextView productPrice;
        final NetworkImageView imageView;
        CardView cv;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            productName = (TextView) itemView.findViewById(R.id.tv_productname);
            productPrice = (TextView) itemView.findViewById(R.id.tv_productprice);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewProduct);
            Provider = (ImageView) itemView.findViewById(R.id.imageViewProvider);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }





    }
}
