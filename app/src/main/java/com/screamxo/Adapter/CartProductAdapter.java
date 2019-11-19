package com.screamxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ItemDetailMedia;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.screamxo.Fragment.NewCartFragment;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.CustomItemClickListener;
import com.screamxo.R;
import com.screamxo.Utils.DialogFragmentForItemQuantity;
import com.screamxo.Utils.MyApplication;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SONY on 18-03-2018.
 */

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ProductHolder>{
    Activity context;
    ArrayList<ItemDetailResult> arrayListProduct;
    int oldposition, newPosition;
    boolean isSelecte = true;
    int curruntPosition = -1;
    public boolean isSaved = true;
    NewCartFragment fragment;
    String quintity;
   // TextView txtItemQuntity;

    public CartProductAdapter(Activity context, ArrayList<ItemDetailResult> arrayListProduct) {
        this.context = context;
        this.arrayListProduct = arrayListProduct;

    }

    public CartProductAdapter(Activity context, ArrayList<ItemDetailResult> arrayListProduct, NewCartFragment fragment) {
        this.context = context;
        this.arrayListProduct = arrayListProduct;
        this.fragment = fragment;
    }

    CommonMethod commonMethod;


    //    FragmentManager manager = context.getFragmentManager();
//    FragmentTransaction trans= manager.beginTransaction();
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_list_item, null);
        ProductHolder holder = new ProductHolder(view);
        return holder;
    }

    /*public CartProductAdapter(Context context, int id, NewCartFragment fragment) {
        this.fragment = fragment;
    }*/
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        ItemDetailResult product = arrayListProduct.get(position);
        String productName = product.getItemName();
        if (product != null) {
            holder.txtProduct.setText(productName.toString());
        }

        ArrayList<ItemDetailMedia> childItem = new ArrayList<>();
        childItem.addAll(product.getMedia());

        //
        CartChildAdapter childProductAdpter = new CartChildAdapter(context.getApplicationContext(), childItem, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (!isSelecte) {
                    isSelecte = true;
                    curruntPosition = position;
                    if (holder.rlProductDetail.getVisibility() == View.GONE) {
                        holder.rlProductDetail.setVisibility(View.VISIBLE);
                    } else
                        holder.rlProductDetail.setVisibility(View.GONE);
                } else if (position != curruntPosition) {
                    isSelecte = true;
                    curruntPosition = position;
                    holder.rlProductDetail.setVisibility(View.VISIBLE);
                } else {
                    isSelecte = false;
                    if (holder.rlProductDetail.getVisibility() == View.VISIBLE) {
                        holder.rlProductDetail.setVisibility(View.GONE);
                    }
                }
                holder.txtProductChildName.setText(childItem.get(position).getMediaName());
                Picasso.with(context).load(childItem.get(position).getMediaUrl()).into(holder.imgDetailProduct);
            }
        });
        holder.rvProduct.setHasFixedSize(true);
        holder.rvProduct.setLayoutManager(new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.rvProduct.setAdapter(childProductAdpter);

        if (!isSaved) {
            holder.txtSaveProductDetail.setText(context.getString(R.string.txt_save));
        } else {
            holder.txtSaveProductDetail.setText(context.getString(R.string.txt_add_to_cart));
        }

        holder.txtItemQuntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> qtyList = new ArrayList<>();
                for (int i = 1; i <= arrayListProduct.get(position).getItemQtyRemained(); i++) {
                    qtyList.add(String.valueOf(i));
                     //holder.txtItemQuntity.setText(String.valueOf(i));
                }
                DialogFragmentForItemQuantity dialogFragment = new DialogFragmentForItemQuantity(new CommonMethod() {
                    @Override
                    public void commonMethod(String type, File... files) {
                        holder.txtItemQuntity.setText(type);
                        ((MyApplication) context.getApplicationContext()).cartGlobalArray.get(position).setItemQuntity(holder.txtItemQuntity.getText().toString());
                    }
                }, qtyList, context);
                FragmentManager manager = context.getFragmentManager();
                dialogFragment.show(manager, "");

            }
        });
        /*commonMethod = new CommonMethod() {
            @Override
            public void commonMethod(String type, File... files) {
                if(!TextUtils.isEmpty(type)) {

                }
                Log.i("Quentity = ",type);
                quintity = type;
                holder.txtItemQuntity.setText(quintity);
            }
        };*/

    }

    @Override
    public int getItemCount() {
        if (arrayListProduct != null) {
            return arrayListProduct.size();
        } else {
            return 0;
        }
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView txtProduct, txtProductChildName, txtSaveProductDetail, txtItemQuntity;
        RecyclerView rvProduct;
        ImageView imgCrossProductDetail, imgDetailProduct;
        RelativeLayout rlProductDetail;

        public ProductHolder(View itemView) {
            super(itemView);

            txtProduct = itemView.findViewById(R.id.txtProductCart);
            rvProduct = itemView.findViewById(R.id.recycler_child_product);
            rlProductDetail = itemView.findViewById(R.id.rlProductDetail);
            txtProductChildName = itemView.findViewById(R.id.txtProductChildName);
            txtSaveProductDetail = itemView.findViewById(R.id.txtSaveProductDetail);
            txtItemQuntity = itemView.findViewById(R.id.txtItemQuntity);
            imgCrossProductDetail = itemView.findViewById(R.id.imgCrossProductDetail);
            imgDetailProduct = itemView.findViewById(R.id.imgDetailProduct);

           // txtItemQuntity.setText(quintity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}

