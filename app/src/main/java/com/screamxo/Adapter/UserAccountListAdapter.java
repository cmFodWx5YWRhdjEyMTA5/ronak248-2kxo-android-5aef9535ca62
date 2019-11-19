package com.screamxo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.GetAccountResult;
import com.screamxo.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Shubham.Agarwal on 24/11/16.
 */
@Deprecated
public class UserAccountListAdapter extends RecyclerView.Adapter<UserAccountListAdapter.Accountholder> {

    private Context context;
    private GetAccountResult accontlist;

    public UserAccountListAdapter(Context context, GetAccountResult accountlist) {
        this.context = context;
        this.accontlist = accountlist;
    }

    @Override
    public Accountholder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_account_recycle, parent, false);
        return new UserAccountListAdapter.Accountholder(v);
    }

    @Override
    public void onBindViewHolder(Accountholder holder, int position) {

        if (accontlist.getPaypal() != null) {
            holder.txtemail.setText(accontlist.getPaypal());
            Picasso.with(context)
                    .load(R.mipmap.logo_paypal)
                    .placeholder(R.mipmap.logo_paypal)
                    .error(R.mipmap.logo_paypal)
                    .into((holder.imgaccount));
        }

        if (accontlist.getBitcoin() != null) {
            holder.txtemail.setText(accontlist.getBitcoin());
            Picasso.with(context)
                    .load(R.mipmap.logo_bitcoin)
                    .placeholder(R.mipmap.logo_bitcoin)
                    .error(R.mipmap.logo_bitcoin)
                    .into((holder.imgaccount));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Accountholder extends RecyclerView.ViewHolder {
        ImageView imgaccount;
        TextView txtemail;

        public Accountholder(View itemView) {
            super(itemView);
            imgaccount = itemView.findViewById(R.id.img_accont);
            txtemail = itemView.findViewById(R.id.txt_email);
        }
    }
}
