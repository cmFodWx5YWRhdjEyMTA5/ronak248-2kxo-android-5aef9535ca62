package com.screamxo.Adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apimodule.ApiBase.ApiBean.AddItemreviewBean;
import com.example.apimodule.ApiBase.ApiBean.PurchaseOrderdetail;
import com.example.apimodule.ApiBase.ApiBean.ShippingAddress;
import com.example.apimodule.ApiBase.BaseApiBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.gson.Gson;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Activity.PurchaseHistoryActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;

public class PurchaseItemListAdapter extends RecyclerView.Adapter<PurchaseItemListAdapter.PurchaseItemHolder> {

    String trakingid;
    private Call<AddItemreviewBean> additemreviewcall;
    private PurchaseHistoryActivity context;
    private ArrayList<PurchaseOrderdetail> puchaseitemlist;
    private Validations mvalidations;
    private String userid, review,shippingaddress;
    private FetchrServiceBase mservice;
    private Preferences preferences;

    public PurchaseItemListAdapter(PurchaseHistoryActivity context, ArrayList<PurchaseOrderdetail> puchaseitemlist, String userid, String shippingaddress) {
        this.context = context;
        this.puchaseitemlist = puchaseitemlist;
        this.userid = userid;
        this.shippingaddress = shippingaddress;

        mvalidations = new Validations();
        mservice = new FetchrServiceBase();
        preferences = new Preferences(context);
    }

    @Override
    public PurchaseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dummy2, parent, false);
        return new PurchaseItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PurchaseItemHolder holder, final int position) {

        if (puchaseitemlist.get(position).isOpened()) {
            holder.li_l.setVisibility(View.VISIBLE);
            holder.txt_view_more.setText("View Less");
        } else {
            holder.li_l.setVisibility(View.GONE);
            holder.txt_view_more.setText("View More");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ItemDetailsAcitvity.class);
                i.putExtra("itemid", "" + puchaseitemlist.get(position).getItemId());
                ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
            }
        });

//        if (puchaseitemlist.size() != 0) {
//            if (puchaseitemlist.get(position).isIsreview()) {
//                holder.txtreview.setVisibility(View.GONE);
//                //   holder.txtReviewDummy.setVisibility(View.GONE);
//            } else {
//                holder.txtreview.setVisibility(View.VISIBLE);
//                //  holder.txtReviewDummy.setVisibility(View.VISIBLE);
//                holder.txtreview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        openAlertDialog(position);
//                    }
//                });
//            }
//        }
        holder.tv_basic_address.setText(shippingaddress);
        if (puchaseitemlist.get(position).getItemTrackingDataNew() != null && puchaseitemlist.get(position).getItemTrackingDataNew().size() > 0) {
            holder.tv_tracking.setText(puchaseitemlist.get(position).getItemTrackingDataNew().get(0).getTrackingNumber());
            holder.tv_estimate.setText(puchaseitemlist.get(position).getItemTrackingDataNew().get(0).getEstimatedDelivery());
            holder.tv_postal.setText(puchaseitemlist.get(position).getItemTrackingDataNew().get(0).getPostalCarrier());
        }
        else{
            holder.tv_tracking.setHint("Pending");
            holder.tv_estimate.setHint("Pending");
            holder.tv_postal.setHint("Pending");
        }
        Glide.with(context).load("https://s3-us-west-2.amazonaws.com/scremax/profile/" + puchaseitemlist.get(position).getSeller().getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.img_buyer);
        holder.iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("otherUid", puchaseitemlist.get(position).getOrderId() + "");
                intent.putExtra("username", puchaseitemlist.get(position).getSeller().getUsername());
                intent.putExtra("userProfile", "https://s3-us-west-2.amazonaws.com/scremax/profile/" + puchaseitemlist.get(position).getSeller().getPhoto());
                intent.putExtra("fullname", puchaseitemlist.get(position).getSeller().getFname());
                intent.putExtra("itemId", puchaseitemlist.get(position).getSeller().getId() + "");
                context.startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
            }
        });

        holder.txtreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialog(position);
            }
        });


        String itemname = "";
        if (puchaseitemlist.size() != 0)
            itemname = puchaseitemlist.get(position).getDetails().get(0).getItemName();
        String itemprice = "";
        String date = mvalidations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss",
                puchaseitemlist.get(position).getPurchaseDate());

        if (date != null)
            holder.txt_purchase_date.setText(date);

        String username = "";
        if (puchaseitemlist.size() != 0)
            username = puchaseitemlist.get(position).getSeller().getFname()+" "+puchaseitemlist.get(position).getSeller().getLname();

        StringBuilder finalString = new StringBuilder();

        if (puchaseitemlist.size() != 0) {
            /*finalString.append("Purchased" + " ").append(puchaseitemlist.get(position).getDetails()
                    .get(0).getItemQty()).append(" ")
                    .append(itemname);*/


            finalString.append("Purchased" + " ").append(puchaseitemlist.get(position).getQty().getProductqty()).append(" ")
                    .append(itemname);
            holder.txtitemname.setText(finalString);

            if (Utils.getFormattedPrice(puchaseitemlist.get(position).getDetails()
                    .get(0).getItemPrice()) != null) {
                itemprice = Utils.getFormattedPrice(puchaseitemlist.get(position).getDetails().get(0).getItemPrice() * puchaseitemlist.get(position).getQty().getProductqty());
            }
        }

        StringBuilder finalPriceString = new StringBuilder();

        if (itemprice != null) {
            finalPriceString.append(itemprice).append(" From ").append(username);
            holder.item_price.setText(finalPriceString);
        }

        /*if (purchaseOrderdetails.size() != 0) {
            if (purchaseOrderdetails.get(0).getHastrackingdetail() == 1
                    && !puchaseitemlist.get(position).getTrackingId().isEmpty()) {
                finalString.append("Tracking id : ").append(puchaseitemlist.get(position).getTrackingId());
            }
        }*/
        /*finalString.append("\n");
        SpannableString ss = new SpannableString(finalString);

        try {
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), ("Purchased" + " ").length(), finalString.indexOf(itemname), 0);
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), finalString.indexOf(itemprice), finalString.indexOf(itemprice) + (itemprice).length(), 0);
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), finalString.indexOf(username), finalString.indexOf(username) + (username).length(), 0);
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), finalString.indexOf(puchaseitemlist.get(position).getTrackingId())
                    , finalString.indexOf(puchaseitemlist.get(position).getTrackingId()) + (puchaseitemlist.get(position).getTrackingId()).length(), 0);
        } catch (Exception ignored) {
// ignoring the issue caused when there is no tracking id or spanning issues...
        }
*/
        if (puchaseitemlist.size() != 0) {
           /* if (purchaseOrderdetails.get(0).getDetails().get(0).getItemName() != null
                    || puchaseitemlist.get(position).getItemOrderDate() != null) {
                //  holder.txtitemname.setText(ss);
            }*/

            if (!TextUtils.isEmpty(puchaseitemlist.get(position).getMedia().get(0).getMediaThumb())) {
                Picasso.with(context)
                        .load(puchaseitemlist.get(position).getMedia().get(0).getMediaThumb())
                        .placeholder(R.drawable.ico_img_new_placeholder)
                        .error(R.drawable.ico_img_new_placeholder)
                        .into(holder.imgitem);
            }

           /* holder.txt_view_more.setText(purchaseOrderdetails.get(0).getHastrackingdetail() == 0
                    ? "Request Tracking" : "View Tracking");*/

            holder.txt_view_more.setVisibility(puchaseitemlist.get(position).getHastrackingdetail() == 0 ? View.GONE : View.VISIBLE);
        }

        if (puchaseitemlist.get(position).getHastrackingdetail() == 1) {
            String trakingdata = puchaseitemlist.get(position).getItemTrackingData();
            if (!TextUtils.isEmpty(trakingdata)) {
                try {
                    trakingid = (String) new JSONObject(trakingdata).get("trackingid");

//                    if (!TextUtils.isEmpty(trakingid))
//                        holder.txt_view_more.setVisibility(View.VISIBLE);
//                    else
//                        holder.txt_view_more.setVisibility(View.GONE);
//                     holder.txt_add_Tracking.setText("TrakingId:"+" "+trakingid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //  merge.append("Tracking Id : ").append(puchaseitemlist.get(position).getTrackingdetail().getTrackingid());
        }

//        if (!TextUtils.isEmpty(trakingid))
//            holder.txt_view_more.setVisibility(View.VISIBLE);
//        else
//            holder.txt_view_more.setVisibility(View.GONE);

        holder.txt_view_more.setOnClickListener(view -> {
            context.setChanged(!puchaseitemlist.get(position).isOpened(), position);


       /*     if (!TextUtils.isEmpty(trakingid)) {
                DialogBox.showDialog(context, context.getString(R.string.app_name), "Tracking Id : " + trakingid, DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                    @Override
                    public void dialogAction() {

                    }
                });
            } else {
                DialogBox.showDialog(context, context.getString(R.string.app_name),
                        context.getString(R.string.msg_no_tracking_id), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                            @Override
                            public void dialogAction() {

                            }
                        });
            }*/
        });
        /*holder.txt_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (puchaseitemlist.size()!=0 && puchaseitemlist.get(holder.getAdapterPosition()).getOrderdetails().size()!=0){
                    if (puchaseitemlist.get(holder.getAdapterPosition()).getOrderdetails().get(0).getHastrackingdetail() == 0) {
                        notifyItemOwnerForTrackingUpdate(holder.getAdapterPosition());
                    } else {
                        DialogBox.showTrackingDetails(context, puchaseitemlist.get(position).getCourierCompanyName(), puchaseitemlist.get(position).getTrackingId());
                    }
                }

            }
        });*/

       /* holder.img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("otherUid", puchaseitemlist.get(position).getDetails().get(0).getId() + "");
//                intent.putExtra("username", puchaseitemlist.get(position).getSeller().getUsername());
//                intent.putExtra("userProfile", puchaseitemlist.get(position).getSeller().getUsername());
//                intent.putExtra("fullname", puchaseitemlist.get(position).getOrderdetails().get(0).getDetails().get(0).getFname() + " " + puchaseitemlist.get(position).getOrderdetails().get(0).getDetails().get(0).getLname());
//                intent.putExtra("itemId", puchaseitemlist.get(position).getItemid() + "");
                context.startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
            }
        });*/


        String finalUsername = username;
        holder.img_name_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("User Name", finalUsername);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAlertDialog(int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.add_item_review_dialog, null);
        builder.setView(v);
        final AlertDialog ad = builder.show();

        TextView cancle = v.findViewById(R.id.txt_cancel);
        EditText edtreview = v.findViewById(R.id.edt_review);
        TextView mtxtdone = v.findViewById(R.id.txt_done);

        ProgressBar progress = v.findViewById(R.id.progreessbar);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                edtreview.setFocusable(true);
                edtreview.setFocusableInTouchMode(true);
                edtreview.requestFocus();
            }
        });
        ad.show();
        mtxtdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtreview.getText().toString().equals("")) {
                    review = edtreview.getText().toString();
                    callAddItemReviewApi(position);
                    ad.dismiss();
                } else {
                    Utils.showToast(context, context.getString(R.string.msg_empty_review));
                }
            }
        });
    }


    public void notifyItemOwnerForTrackingUpdate(int position) {
        if (Utils.isInternetOn(context)) {
            Map<String, String> map = new HashMap<>();
            map.put("uid", userid);
            map.put("itemid", String.valueOf(puchaseitemlist.get(position).getItemId()));
            map.put("orderid", String.valueOf(puchaseitemlist.get(position).getOrderId()));

            ((PurchaseHistoryActivity) context).progreessbar.setVisibility(View.VISIBLE);
            mservice.getFetcherService(context).notifyItemOwnerForTrackingUpdate(map).enqueue(new Callback<BaseApiBean>() {
                @Override
                public void onResponse(Call<BaseApiBean> call, Response<BaseApiBean> response) {
                    ((PurchaseHistoryActivity) context).progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_SUCESS, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseApiBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                    ((PurchaseHistoryActivity) context).progreessbar.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void callAddItemReviewApi(int position) {
        if (Utils.isInternetOn(context)) {
            Map<String, String> map = new HashMap<>();
            map.put("uid", userid);
            map.put("itemid", String.valueOf(puchaseitemlist.get(position).getItemId()));
            map.put("description", review);

            ((PurchaseHistoryActivity) context).progreessbar.setVisibility(View.VISIBLE);
            additemreviewcall = mservice.getFetcherService(context).AddItemReview(map);
            additemreviewcall.enqueue(new Callback<AddItemreviewBean>() {
                @Override
                public void onResponse(Call<AddItemreviewBean> call, Response<AddItemreviewBean> response) {
                    ((PurchaseHistoryActivity) context).progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            puchaseitemlist.get(position).setIsreview(true);
                            notifyItemChanged(position);
                            Utils.showToast(context, response.body().getMsg());
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddItemreviewBean> call, Throwable t) {
                    Utils.showToast(context, t.toString());
                    ((PurchaseHistoryActivity) context).progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public int getItemCount() {
        return puchaseitemlist.size();
    }

    public class PurchaseItemHolder extends RecyclerView.ViewHolder {
        private TextView txtitemname, txtreview, txt_view_more, item_price, txt_purchase_date, txtReviewDummy, tv_postal, tv_estimate, tv_tracking,tv_basic_address;
        private ImageView imgitem, iv_chat, img_buyer,img_name_copy;
        private LinearLayout li_l;

        public PurchaseItemHolder(View itemView) {
            super(itemView);
            txtitemname = itemView.findViewById(R.id.txt_purchased_item_name);
            tv_basic_address = itemView.findViewById(R.id.tv_basic_address);
            item_price = itemView.findViewById(R.id.txt_purchased_item_price);
            txt_purchase_date = itemView.findViewById(R.id.txt_purchased_date);
            imgitem = itemView.findViewById(R.id.img_item);
            img_buyer = itemView.findViewById(R.id.img_buyer);
            iv_chat = itemView.findViewById(R.id.iv_chat);
            txtreview = itemView.findViewById(R.id.txt_review);
            tv_postal = itemView.findViewById(R.id.tv_postal);
            tv_estimate = itemView.findViewById(R.id.tv_estimate);
            img_name_copy = itemView.findViewById(R.id.img_name_copy);
            tv_tracking = itemView.findViewById(R.id.tv_tracking);
            //txtReviewDummy = itemView.findViewById(R.id.txt_add_review_dummy);
            txt_view_more = itemView.findViewById(R.id.txt_view_more);
            li_l = itemView.findViewById(R.id.li_l);
        }
    }
}
