package com.screamxo.Utils;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Krunal.Kevadiya on 29-01-2016.
 */
public class StaticConstant {

    public static  String from_ = "";
    public static String from_url = "";
    public static String from_type = "";
    public static String from_posi = "";

    public static final String LIMIT = "5000";
    public static final String STATUS_1 = "1";
    public static final String STATUS_0 = "0";
    public static final int REQUEST_CAPTURE = 103;
    public static final int REQUEST_GALLERY = 104;
    public static final int REQUEST_CROP_IMAGE = 105;
    public static final int REQUEST_VIDEO_CAPTURE = 106;
    public static final int REQUEST_VIDEO = 112;
    public static final int RESULT_OK = 200;

    /*USAGE: BOOKMARK*/
    public static final int LIKE = 0;
    public static final int UNLIKE = 1;

    /*REQUEST CODES... */
    public static final int REQUEST_POST_DETAIL = 300;
    public static final int REQUEST_POST_MEDIA = 301;
    public static final int REQUEST_SELL_ITEM = 302;
    public static final int REQUEST_POST_DETAIL_MORE = 304;
    //    public static final int REQUEST_POST_STREAM = 305; not using @shubham agarwal
    public static final int REQUEST_PAYMENT_DETAIL = 305;
    public static final int REQUEST_ITEM_CREATE = 306;

    public static final int UNAUTHORIZE=401;
    public static final int BAD_REQUEST=400;

    public static String CONFIG_CLIENT_ID = "";
    public static String SECRET_KEY = "";
    public static int GUESSCHECK = 0;
    public static Boolean CHAT_SCREEN = false;
    public static String SEARCHSTRING = "";
    public static String WORLDSEARCH = "";
    public static Boolean REQUESTFLAG = false;
    public static final String STRIPEKEY = "pk_test_52XRGhakXWpAPlAMKegKSlkX";
    public static String SELL_CONDITION = "";

    public static final String MEDIA_0 = "media";
    /*MEDIA POST TYPE*/
    public static final String POST_TYPE_RECORD = "3";
    public static final String POST_TYPE_MUSIC = "4";
    public static final String POST_TYPE_VIDEO = "1";
    public static final String POST_TYPE_IMAGE = "2";

    /**
     * Constant user to show which time is selected on 'control' timer.
     */
    public static int selectedTimePos = -1;

    /*PAYMENT PROCESSOR TYPE*/
    public static int TYPE_PAYPAL = 1;
    public static int TYPE_BITCOIN = 2;
    public static int TYPE_ALIPAY = 3;
    public static int TYPE_WECHAT = 4;

    public static File[] imageFiles_new;
    public static int BOOST_TYPE_SHOP_ITEM = 1;
    public static int BOOST_TYPE_MEDIA_ITEM = 2;


    public static int BOOST_TYPE_DAYS = 0;
    public static int BOOST_TYPE_PRICE = 1;

    public static int CHAT_SENDER_TEXT = 1;
    public static int CHAT_SENDER_IMAGE = 2;
    public static int CHAT_SENDER_AUDIO = 5;
    public static int CHAT_RECEIVER_AUDIO = 6;
    public static int CHAT_RECIEVER_TEXT = 3;
    public static int CHAT_RECIEVER_IMAGE = 4;

    public static int count;
    public static Bitmap photo = null;

    public static ArrayList<Category> categoryList= new ArrayList<>();
    public static File saveImage(Bitmap photo) {

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/req_images");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".jpg";
            File file = new File(myDir, fname);
            //  Log.i(TAG, "" + file);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

       return  file;



    }

    public static String TEMP_PATH = Environment.getExternalStorageDirectory() + "/scremxo/tempmedia/";
}