package ru.customerapp.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import ru.customerapp.R;
import ru.customerapp.data.DataAccess;

/**
 * Created by rash on 06.02.2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvPrice;
        TextView tvDiscount;
        ImageView ivAddToCart;

        public ProductViewHolder(View view) {
            super(view);

            ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            tvProductName = (TextView) view.findViewById(R.id.tvProductName);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
            ivAddToCart = (ImageView) view.findViewById(R.id.ivAddToCart);
        }
    }
    private Context context;
    private DataAccess dataAccess;
    private AppContext appContext;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        dataAccess = DataAccess.getInstance(context);
        appContext = AppContext.getInstance(context);
    }

    ViewGroup parent;

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        if (product.Picture == null) {
            DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(product, holder.ivProductImage);
            downloadAsyncTask.execute();
        } else {
            int productPictureLength = product.Picture.length;
            //Log.d("TAG", "onBindViewHolder: " + productPictureLength);
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.Picture, 0, product.Picture.length);
            holder.ivProductImage.setImageBitmap(bitmap);
        }

        holder.tvProductName.setText(product.Name);
        holder.tvProductName.setTag(product.Id);
        String productPrice = appContext.getOriginalPrice(product);
        holder.tvPrice.setText(productPrice);
        holder.tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String discountedPrice = appContext.getDiscountedPrice(product);
        holder.tvDiscount.setText(discountedPrice);
    }

    private class DownloadAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private Bitmap bitmap;
        private ImageView imageView;
        private Product product;

        public DownloadAsyncTask(Product product, ImageView imageView) {
            this.product = product;
            this.imageView = imageView;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String url = String.format("http://10.0.1.99/api/pictures?value=%s", product.ImageUrl);
                URL imageURL = new URL(url);
                bitmap = BitmapFactory.decodeStream(imageURL.openStream());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                product.Picture = byteArray;
                int productPictureLength = product.Picture.length;
                //Log.d("TAG", "DownloadAsyncTask: " + productPictureLength);
                dataAccess.updateProductPicture(product);
            } catch (IOException e) {
                bitmap = null;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } else {

            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}