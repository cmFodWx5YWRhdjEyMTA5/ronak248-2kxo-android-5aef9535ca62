package com.screamxo.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.ShippingAddress;
import com.example.apimodule.ApiBase.ApiBean.SoldItemDetail;
import com.google.gson.Gson;
import com.screamxo.Activity.ChatActivity;
import com.screamxo.Activity.RahulWork.ItemDetailsAcitvity;
import com.screamxo.Activity.SoldHistoryActivity;
import com.screamxo.R;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_CHAT_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS;

public class SoldHistoryAdapter extends RecyclerView.Adapter<SoldHistoryAdapter.SoldHistoryHolder> {
    private SoldHistoryActivity context;
    private ArrayList<SoldItemDetail> soldItemDetailArrayList;
    private Validations mvalidations;
    private String trakingid = "";

    public SoldHistoryAdapter(SoldHistoryActivity context, ArrayList<SoldItemDetail> soldItemDetailArrayList) {
        this.context = context;
        this.soldItemDetailArrayList = soldItemDetailArrayList;
        mvalidations = new Validations();
    }

    @Override
    public SoldHistoryAdapter.SoldHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dummy, parent, false);
        return new SoldHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(SoldHistoryHolder holder, int position) {

        if (soldItemDetailArrayList.get(position).isOpened()) {
            holder.li_l.setVisibility(View.VISIBLE);
            holder.txt_view_more.setText("Save");
        } else {
            holder.li_l.setVisibility(View.GONE);
            holder.txt_view_more.setText("View More");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ItemDetailsAcitvity.class);
                i.putExtra("itemid", "" + soldItemDetailArrayList.get(position).getItemid());
                ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
            }
        });

        String itemname = soldItemDetailArrayList.get(position).getItemName();
        String itemprice = String.valueOf(soldItemDetailArrayList.get(position).getItemPrice());
        String date = mvalidations.dateFormationUTCtoLocal("yyyy-MM-dd'T'HH:mm:ss",
                soldItemDetailArrayList.get(position).getCreatedDate());
        String username = soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getFname() + " " + soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getLname();

        int quantity = 1;
        for (int k = 0; k < soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemQty().size(); k++) {
            {
                if (soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemQty().get(k).getItemId() == soldItemDetailArrayList.get(position).getItemid()) {
                    quantity = soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemQty().get(k).getProductqty();
                    break;
                }
            }
        }

        StringBuilder merge = new StringBuilder().append("Sold").append(" ")
                .append(quantity)
                .append(" ").append(itemname);

        holder.txtitemname.setText(merge);

        StringBuilder itemPrice = new StringBuilder();
        if (itemPrice != null) {
            itemPrice.append("$").append(Double.parseDouble(itemprice) * quantity).append(" ").append("By").append(" ").append(username);
            holder.item_price.setText(itemPrice);
        }

        if (date != null)
            holder.txt_purchase_date.setText(date);

        if (soldItemDetailArrayList.get(position).getOrderdetails().get(0).getHastrackingdetail() == 1) {
            String trakingdata = soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemTrackingData();
            try {
                trakingid = (String) new JSONObject(trakingdata).get("trackingid");
                holder.txt_view_more.setVisibility(View.VISIBLE);
                //  holder.txt_view_more.setText("TrakingId:"+" "+trakingid);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            //  merge.append("Tracking Id : ").append(soldItemDetailArrayList.get(position).getTrackingdetail().getTrackingid());
        }


        //merge.append("\n\n").append(date).append("\n");

        /*SpannableString ss = new SpannableString(merge);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, username.length(), 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), username.length() + 11, username.length() + 11
                + String.valueOf(soldItemDetailArrayList.get(position).getItemQty()).length(), 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK)
                , username.length() + 11 + String.valueOf(soldItemDetailArrayList.get(position).getItemQty()).length() + 10
                , username.length() + 11 + String.valueOf(soldItemDetailArrayList.get(position).getItemQty()).length() + 10 + 1 + itemprice.length(), 0);*/

        if (soldItemDetailArrayList.get(position).getItemName() != null
                || soldItemDetailArrayList.get(position).getCreatedDate() != null) {
            // holder.txtitemname.setText(ss);
        }

        if (soldItemDetailArrayList.get(position).getMedia().getMediaThumb() != null
                && !soldItemDetailArrayList.get(position).getMedia().getMediaThumb().equals("")) {
            Picasso.with(context)
                    .load(soldItemDetailArrayList.get(position).getMedia().getMediaThumb())
                    .placeholder(R.drawable.ico_img_new_placeholder)
                    .error(R.drawable.ico_img_new_placeholder)
                    .into(holder.imgitem);
        }

//        try {
//            holder.txt_view_more.setText(soldItemDetailArrayList.get(position).getOrderdetails().get(0).getHastrackingdetail() == 0
//                    ? context.getString(R.string.txt_request_tracking) : "View Tracking");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

       /* holder.txt_view_more.setVisibility(soldItemDetailArrayList.get(position).getOrderdetails().get(0).getHastrackingdetail() == 0
                ? View.VISIBLE : View.GONE);*/

        holder.txt_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (soldItemDetailArrayList.get(position).isOpened()) {
                    //hit api
//                    soldItemDetailArrayList.get(position).getItemid()
//                    soldItemDetailArrayList.get(position).getOrderdetails().get(0).getOrderId();

                    context.deliveryUpdate(soldItemDetailArrayList.get(position).getItemid(), soldItemDetailArrayList.get(position).getOrderdetails().get(0).getOrderId(), holder.ed_epc.getText().toString(), holder.ed_edd.getText().toString(), holder.ed_etn.getText().toString());
                }

                context.setChanged(!soldItemDetailArrayList.get(position).isOpened(), position);
//             commented for visibility handling
//                if (soldItemDetailArrayList.get(position).getOrderdetails().get(0).getHastrackingdetail() == 0) {
//                    Intent i = new Intent(context, ItemDetailsAcitvity.class);
//                    i.putExtra("itemid", "" + soldItemDetailArrayList.get(position).getItemid());
//                    i.putExtra("ADD_TRACKING", true);
//                    i.putExtra("ORDER_ID", soldItemDetailArrayList.get(position).getOrderdetails().get(0).getOrderId());
//                    ((Activity) context).startActivityForResult(i, REQ_CODE_ITEM_DETAILS_ACTIVITY_RESULTS);
//                } else {
//                    if (!TextUtils.isEmpty(trakingid)) {
//                        DialogBox.showDialog(context, context.getString(R.string.app_name), "Tracking Id : " + trakingid, DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
//                            @Override
//                            public void dialogAction() {
//dumm
//                            }
//                        });
//                    } else {
//                        DialogBox.showDialog(context, context.getString(R.string.app_name),
//                                context.getString(R.string.msg_no_tracking_id), DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
//                                    @Override
//                                    public void dialogAction() {
//
//                                    }
//                                });
//                    }
//                }


            }
        });
        if (soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemTrackingDataNew() != null && soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemTrackingDataNew().size() > 0) {
            holder.ed_epc.setText(soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemTrackingDataNew().get(0).getPostalCarrier());
            holder.ed_edd.setText(soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemTrackingDataNew().get(0).getEstimatedDelivery());
            holder.ed_etn.setText(soldItemDetailArrayList.get(position).getOrderdetails().get(0).getItemTrackingDataNew().get(0).getTrackingNumber());
        } else {
            holder.ed_epc.setHint("Carrier*");
            holder.ed_edd.setHint("00/00/00");
            holder.ed_etn.setHint("00000000*");
        }
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                holder.ed_edd.setText(updateLabel(myCalendar));
            }

        };
        holder.ed_edd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, dateDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        holder.txt_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ItemDetailsAcitvity.class);
                i.putExtra("itemid", "" + soldItemDetailArrayList.get(position).getItemid());
                i.putExtra("openReviews", true);
                context.startActivity(i);
            }
        });
        ShippingAddress shippingAddress = new Gson().fromJson(soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getShippingAddress(), ShippingAddress.class);
        String address = shippingAddress.getStreet() + ", " + shippingAddress.getZipcode() + ", " + shippingAddress.getCity();

        holder.tv_basic_address.setText(address);
        holder.tv_country.setText(shippingAddress.getCountry());

        holder.img_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Shipping Address", address + ", " + shippingAddress.getCountry());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Address copied", Toast.LENGTH_SHORT).show();
            }
        });
        holder.img_name_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("User Name", username);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.with(context)
                .load("https://s3-us-west-2.amazonaws.com/scremax/profile/" + soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getPhoto())
                .placeholder(R.drawable.ico_user_new)
                .error(R.drawable.ico_user_new)
                .into(holder.img_buyer);


        holder.img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("otherUid", soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getId() + "");
                intent.putExtra("username", soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getUsername());
                intent.putExtra("userProfile", "https://s3-us-west-2.amazonaws.com/scremax/profile/" + soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getPhoto());
                intent.putExtra("fullname", soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getFname() + " " + soldItemDetailArrayList.get(position).getOrderdetails().get(0).getDetails().get(0).getLname());
                intent.putExtra("itemId", soldItemDetailArrayList.get(position).getItemid() + "");
                context.startActivityForResult(intent, REQ_CODE_CHAT_ACTIVITY_RESULTS);
            }
        });
    }

    @Override
    public int getItemCount() {
        return soldItemDetailArrayList.size();
    }

    class SoldHistoryHolder extends RecyclerView.ViewHolder {
        private TextView txtitemname, txt_view_more, item_price, txt_purchase_date, txt_review, tv_basic_address, tv_country;
        private ImageView imgitem, img_copy, img_buyer, img_chat, img_name_copy;
        private LinearLayout li_l;

        private EditText ed_epc, ed_etn;
        private TextView ed_edd;

        SoldHistoryHolder(View itemView) {
            super(itemView);
            txt_review = itemView.findViewById(R.id.txt_review);
            li_l = itemView.findViewById(R.id.li_l);
            txtitemname = itemView.findViewById(R.id.txt_purchased_item_name);
            item_price = itemView.findViewById(R.id.txt_purchased_item_price);
            txt_purchase_date = itemView.findViewById(R.id.txt_purchased_date);
            imgitem = itemView.findViewById(R.id.img_item);
            txt_view_more = itemView.findViewById(R.id.txt_view_more);
            tv_basic_address = itemView.findViewById(R.id.tv_basic_address);
            tv_country = itemView.findViewById(R.id.tv_country);
            img_copy = itemView.findViewById(R.id.img_copy);
            img_name_copy = itemView.findViewById(R.id.img_name_copy);
            img_buyer = itemView.findViewById(R.id.img_buyer);
            ed_epc = itemView.findViewById(R.id.ed_epc);
            ed_edd = itemView.findViewById(R.id.ed_edd);
            ed_etn = itemView.findViewById(R.id.ed_etn);
            img_chat = itemView.findViewById(R.id.img_chat);
        }
    }

    private String updateLabel(Calendar myCalendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        return (sdf.format(myCalendar.getTime()));
    }

}