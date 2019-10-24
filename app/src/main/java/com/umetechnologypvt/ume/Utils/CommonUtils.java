package com.umetechnologypvt.ume.Utils;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.umetechnologypvt.ume.ApplicationClass;
import com.umetechnologypvt.ume.Models.Country;
import com.umetechnologypvt.ume.Models.Example;
import com.umetechnologypvt.ume.Models.LangaugeModel;
import com.umetechnologypvt.ume.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by AliAh on 14/05/2018.
 */

public class CommonUtils {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;


    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static void sendCustomerStatus(final String b) {
        if (SharedPrefs.getUserModel() != null) {
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if (mDatabase != null && SharedPrefs.getUserModel() != null && SharedPrefs.getUserModel().getUsername() != null && b != null) {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("status").setValue(b);
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("fcmKey").setValue(FirebaseInstanceId.getInstance().getToken());
            }
        }


    }
    public static String getNameFromUrl(String url){
        String abc = url.substring(url.length() - 10, url.length() - 1);
        return abc;
    }

    public static void shareUrl(Context context, String postType, String postId) {
        String append = "";
        if (postType.equalsIgnoreCase("post")) {
            append = "p";
        } else if (postType.equalsIgnoreCase("profile")) {
            append = "u";
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://umetechnology.com/" + append + "/" + postId);
        context.startActivity(Intent.createChooser(shareIntent, "Share post via.."));
    }

    public static void copyUrl(Context context, String postType, String postId) {
        String append = "";

        if (postType.equalsIgnoreCase("post")) {
            append = "p";
        } else if (postType.equalsIgnoreCase("profile")) {
            append = "u";
        }

        String url = "http://umetechnology.com/" + append + "/" + postId;


        ClipboardManager _clipboard = (ClipboardManager) ApplicationClass.getInstance().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        _clipboard.setText(url);
        CommonUtils.showToast("Copied");
    }


    public static String getFullAddress(Context context, Double lat, Double lon) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);

            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static Uri getVideoPic(String videoUrl) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //give YourVideoUrl below
        retriever.setDataSource(videoUrl, new HashMap<String, String>());
// this gets frame at 2nd second
        Bitmap image = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(ApplicationClass.getInstance().getApplicationContext().getContentResolver(), image, "Title", null);
            return Uri.parse(path);
        } catch (Exception e) {
            e.printStackTrace();
            return Uri.parse("");
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    public static String millisToLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append(" day").append(temp > 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append(" hour").append(temp > 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append(" minute");
            }

            if (!res.toString().equals("") && duration >= ONE_SECOND) {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append(" second").append(temp > 1 ? "s" : "");
            }
            return res.toString();
        } else {
            return "0 second";
        }
    }


    public static Uri getVideoPiac(Uri uri) {

        Bitmap image = ThumbnailUtils.createVideoThumbnail(getRealPathFromURI(uri), MediaStore.Images.Thumbnails.MINI_KIND);
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            String path = MediaStore.Images.Media.insertImage(ApplicationClass.getInstance().getApplicationContext().getContentResolver(), image, "Title", null);
            return Uri.parse(path);
        } catch (Exception e) {
            e.printStackTrace();
            return Uri.parse("");
        }
    }

    public static String uri2filename(Uri uri) {

        String ret = null;
        String scheme = uri.getScheme();

        if (scheme.equals("file")) {
            ret = uri.getLastPathSegment();
        } else if (scheme.equals("content")) {
            Cursor cursor = ApplicationClass.getInstance().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return ret;
    }

    public static String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ApplicationClass.getInstance().getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static List<Country> countryList() {
        String myJson = inputStreamToString(ApplicationClass.getInstance()
                .getApplicationContext().getResources().openRawResource(R.raw.count));
        Example myModel = new Gson().fromJson(myJson, Example.class);
        return myModel.getCountries();
    }

//    public static List<LangaugeModel> languageList() {
//        String myJson = inputStreamToString(ApplicationClass.getInstance().getApplicationContext().getResources().openRawResource(R.raw.finallangaugeslist));
//        ExampleLanguage myModel = new Gson().fromJson(myJson, ExampleLanguage.class);
//        return myModel.getLanguages();
//
//    }

    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    public static void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("WrongConstant")
            public void run() {
                Toast.makeText(ApplicationClass.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getFormattedPrice(Object price) {
        DecimalFormat formatter = new DecimalFormat("##,##,###");
        String formattedPrice = formatter.format(price);
        return formattedPrice;
    }

    public static String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd MMM ";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd MMM , h:mm aa", smsTime).toString();
        }
    }

    public static String getLocalTime(long smsTimeInMilis, String gmt) {
        Calendar smsTime = Calendar.getInstance(TimeZone.getTimeZone(gmt));
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd MMM ";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd MMM , h:mm aa", smsTime).toString();
        }
    }

    public static String getFormattedDateOnly(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("dd-MMM-yyyy", smsTime).toString();

    }


    public static String getFormattedTime(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        return DateFormat.format("h:mm aa", smsTime).toString();

    }

    public static boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) ApplicationClass.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static double screenSize() {

        double size = 0;
        try {

            // Compute screen size

            DisplayMetrics dm = ApplicationClass.getInstance().getApplicationContext().getResources().getDisplayMetrics();

            float screenWidth = dm.widthPixels / dm.xdpi;

            float screenHeight = dm.heightPixels / dm.ydpi;

            size = Math.sqrt(Math.pow(screenWidth, 2) +

                    Math.pow(screenHeight, 2));

        } catch (Throwable t) {

        }

        return size;

    }


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static ArrayList<String> interestList() {
        ArrayList<String> interestList = new ArrayList<>();
        interestList.add("Acting");
        interestList.add("Adventure");
        interestList.add("Animals");
        interestList.add("Anime");
        interestList.add("Archery");
        interestList.add("Architecutre");
        interestList.add("Art");
        interestList.add("Astrology");
        interestList.add("Athletics");
        interestList.add("Badminton");
        interestList.add("Baking");
        interestList.add("Baseball");
        interestList.add("Basketball");
        interestList.add("Beach");
        interestList.add("Blogging");
        interestList.add("Vlogging");
        interestList.add("Boating");
        interestList.add("Sailing");
        interestList.add("Bodybuilding");
        interestList.add("Boxing");
        interestList.add("Buisness");
        interestList.add("Camping");
        interestList.add("Card games");
        interestList.add("Car pentry");
        interestList.add("Cars");
        interestList.add("Clubbing");
        interestList.add("Collecting");
        interestList.add("Computers");
        interestList.add("Concerts");
        interestList.add("Cooking");
        interestList.add("Crafting");
        interestList.add("Cricket");
        interestList.add("Culture");
        interestList.add("Cycling");
        interestList.add("Dancing");
        interestList.add("Dating");
        interestList.add("Design");
        interestList.add("Education");
        interestList.add("Electronics");
        interestList.add("Entertainments");
        interestList.add("Extreme Sports");
        interestList.add("Fashion");
        interestList.add("Fishing");
        interestList.add("Fitness");
        interestList.add("Flying");
        interestList.add("Food");
        interestList.add("Football");
        interestList.add("Gambling");
        interestList.add("Gardening");
        interestList.add("Golf");
        interestList.add("Guitar");
        interestList.add("Gymnastics");
        interestList.add("Health");
        interestList.add("History");
        interestList.add("Hockey");
        interestList.add("Horse riding");
        interestList.add("Ice skating");
        interestList.add("Internet");
        interestList.add("Jogging");
        interestList.add("Languages");
        interestList.add("Magic");
        interestList.add("Makeup");
        interestList.add("Martial Arts");
        interestList.add("Meditation");
        interestList.add("Motor sports");
        interestList.add("Movies");
        interestList.add("Music");
        interestList.add("Nature");
        interestList.add("Pets");
        interestList.add("Philosophy");
        interestList.add("Photography");
        interestList.add("Piano");
        interestList.add("Poetry");
        interestList.add("Poker");
        interestList.add("Politics");
        interestList.add("Pottery");
        interestList.add("Programming");
        interestList.add("Psychology");
        interestList.add("Reading");
        interestList.add("Religion");
        interestList.add("Rugby");
        interestList.add("Science");
        interestList.add("Scuba diving");
        interestList.add("self improvment");
        interestList.add("Sweing");
        interestList.add("Shooting");
        interestList.add("Shopping");
        interestList.add("Singing");
        interestList.add("Skateboarding");
        interestList.add("Skiing");
        interestList.add("Soccer");
        interestList.add("Sports");
        interestList.add("Surfing");
        interestList.add("Swiming");
        interestList.add("Table tennis");
        interestList.add("Technology");
        interestList.add("Tennis");
        interestList.add("Theater");
        interestList.add("Travel");
        interestList.add("TV");
        interestList.add("Video games");
        interestList.add("Voilin");
        interestList.add("Volleyball");
        interestList.add("Volunteering");
        interestList.add("Wrestling");
        interestList.add("Writing");
        interestList.add("Yoga");
        return interestList;
    }

    public static ArrayList<LangaugeModel> languageList() {
        ArrayList<LangaugeModel> itemList = new ArrayList<>();

        itemList.add(new LangaugeModel("Afrikaans", R.drawable.flag_south_africa));
        itemList.add(new LangaugeModel("Albanian", R.drawable.flag_albania));
        itemList.add(new LangaugeModel("Amharic", R.drawable.flag_ethiopia));
        itemList.add(new LangaugeModel("Arabic", R.drawable.flag_saudi_arabia));
        itemList.add(new LangaugeModel("Armenian", R.drawable.flag_armenia));
        itemList.add(new LangaugeModel("Azerbaijani", R.drawable.flag_azerbaijan));
        itemList.add(new LangaugeModel("Basque", R.drawable.flag_macedonia));
        itemList.add(new LangaugeModel("Belarusian", R.drawable.flag_belarus));
        itemList.add(new LangaugeModel("Bengali", R.drawable.flag_bangladesh));
        itemList.add(new LangaugeModel("Bosnian", R.drawable.flag_bosnia));
        itemList.add(new LangaugeModel("Bulgarian", R.drawable.flag_bulgaria));
        itemList.add(new LangaugeModel("Burmese", R.drawable.flag_myanmar));
        itemList.add(new LangaugeModel("Catalan", R.drawable.flag_antigua_and_barbuda));
        itemList.add(new LangaugeModel("Cebuano", R.drawable.flag_philippines));
        itemList.add(new LangaugeModel("Chichewa", R.drawable.flag_zambia));
        itemList.add(new LangaugeModel("Chinese", R.drawable.flag_china));
        itemList.add(new LangaugeModel("Corsican", R.drawable.flag_france));
        itemList.add(new LangaugeModel("Croatian", R.drawable.flag_croatia));
        itemList.add(new LangaugeModel("Czech", R.drawable.flag_czech_republic));
        itemList.add(new LangaugeModel("Danish", R.drawable.flag_denmark));
        itemList.add(new LangaugeModel("Dutch", R.drawable.flag_netherlands));
        itemList.add(new LangaugeModel("English", R.drawable.flag_united_states_of_america));
        itemList.add(new LangaugeModel("Esperanto", R.drawable.flag_spain));
        itemList.add(new LangaugeModel("Estonian", R.drawable.flag_estonia));
        itemList.add(new LangaugeModel("Filipino", R.drawable.flag_philippines));
        itemList.add(new LangaugeModel("Finnish", R.drawable.flag_finland));
        itemList.add(new LangaugeModel("French", R.drawable.flag_france));
        itemList.add(new LangaugeModel("Frisian", R.drawable.flag_germany));
        itemList.add(new LangaugeModel("Galician", R.drawable.flag_spain));
        itemList.add(new LangaugeModel("Georgian", R.drawable.flag_georgia));
        itemList.add(new LangaugeModel("German", R.drawable.flag_germany));
        itemList.add(new LangaugeModel("Greek", R.drawable.flag_greece));
        itemList.add(new LangaugeModel("Gujarati", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Haitian Creole", R.drawable.flag_vietnam));
        itemList.add(new LangaugeModel("HaitianCreole", R.drawable.flag_haiti));
        itemList.add(new LangaugeModel("Hausa", R.drawable.flag_central_african_republic));
        itemList.add(new LangaugeModel("Hawaiian", R.drawable.flag_haiti));
        itemList.add(new LangaugeModel("Hebrew", R.drawable.flag_israel));
        itemList.add(new LangaugeModel("Hindi", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Hmong", R.drawable.flag_thailand));
        itemList.add(new LangaugeModel("Hungarian", R.drawable.flag_hungary));
        itemList.add(new LangaugeModel("Icelandic", R.drawable.flag_iceland));
        itemList.add(new LangaugeModel("Igbo", R.drawable.flag_nigeria));
        itemList.add(new LangaugeModel("Indonesian", R.drawable.flag_indonesia));
        itemList.add(new LangaugeModel("Irish", R.drawable.flag_ireland));
        itemList.add(new LangaugeModel("Italian", R.drawable.flag_italy));
        itemList.add(new LangaugeModel("Japanese", R.drawable.flag_japan));
        itemList.add(new LangaugeModel("Javanese", R.drawable.flag_indonesia));
        itemList.add(new LangaugeModel("Kannada", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Kazakh", R.drawable.flag_kazakhstan));
        itemList.add(new LangaugeModel("Khmer", R.drawable.flag_cambodia));
        itemList.add(new LangaugeModel("Korean", R.drawable.flag_south_korea));
        itemList.add(new LangaugeModel("Kurdish (Kurmanji)", R.drawable.flag_iraq));
        itemList.add(new LangaugeModel("Kyrgyz", R.drawable.flag_kyrgyzstan));
        itemList.add(new LangaugeModel("Lao", R.drawable.flag_vietnam));
        itemList.add(new LangaugeModel("Latin", R.drawable.flag_italy));
        itemList.add(new LangaugeModel("Latvian", R.drawable.flag_latvia));
        itemList.add(new LangaugeModel("Lithuanian", R.drawable.flag_lithuania));
        itemList.add(new LangaugeModel("Luxembourgish", R.drawable.flag_luxembourg));
        itemList.add(new LangaugeModel("Macedonian", R.drawable.flag_macedonia));
        itemList.add(new LangaugeModel("Malagasy", R.drawable.flag_madagascar));
        itemList.add(new LangaugeModel("Malay", R.drawable.flag_malaysia));
        itemList.add(new LangaugeModel("Malayalam", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Maltese", R.drawable.flag_malta));
        itemList.add(new LangaugeModel("Maori", R.drawable.flag_new_zealand));
        itemList.add(new LangaugeModel("Marathi", R.drawable.flag_thailand));
        itemList.add(new LangaugeModel("Mongolian", R.drawable.flag_mongolia));
        itemList.add(new LangaugeModel("Nepali", R.drawable.flag_nepal));
        itemList.add(new LangaugeModel("Norwegian (Bokmål)", R.drawable.flag_norway));
        itemList.add(new LangaugeModel("Pashto", R.drawable.flag_pakistan));
        itemList.add(new LangaugeModel("Persian", R.drawable.flag_iran));
        itemList.add(new LangaugeModel("Polish", R.drawable.flag_poland));
        itemList.add(new LangaugeModel("Portuguese", R.drawable.flag_portugal));
        itemList.add(new LangaugeModel("Punjabi", R.drawable.flag_pakistan));
        itemList.add(new LangaugeModel("Romanian", R.drawable.flag_romania));
        itemList.add(new LangaugeModel("Russian", R.drawable.flag_russian_federation));
        itemList.add(new LangaugeModel("Samoan", R.drawable.flag_samoa));
        itemList.add(new LangaugeModel("Scots Gaelic", R.drawable.flag_madagascar));
        itemList.add(new LangaugeModel("Serbian", R.drawable.flag_serbia));
        itemList.add(new LangaugeModel("Sesotho", R.drawable.flag_south_africa));
        itemList.add(new LangaugeModel("Shona", R.drawable.flag_zambia));
        itemList.add(new LangaugeModel("Sindhi", R.drawable.flag_pakistan));
        itemList.add(new LangaugeModel("Sinhala", R.drawable.flag_sri_lanka));
        itemList.add(new LangaugeModel("Slovak", R.drawable.flag_slovakia));
        itemList.add(new LangaugeModel("Slovenian", R.drawable.flag_slovenia));
        itemList.add(new LangaugeModel("Somali", R.drawable.flag_somalia));
        itemList.add(new LangaugeModel("Spanish", R.drawable.flag_spain));
        itemList.add(new LangaugeModel("Sundanese", R.drawable.flag_indonesia));
        itemList.add(new LangaugeModel("Swahili", R.drawable.flag_sri_lanka));
        itemList.add(new LangaugeModel("Swedish", R.drawable.flag_sweden));
        itemList.add(new LangaugeModel("Tajik", R.drawable.flag_tajikistan));
        itemList.add(new LangaugeModel("Tamil", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Telugu", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Thai", R.drawable.flag_thailand));
        itemList.add(new LangaugeModel("Turkish", R.drawable.flag_turkey));
        itemList.add(new LangaugeModel("Ukrainian", R.drawable.flag_ukraine));
        itemList.add(new LangaugeModel("Urdu", R.drawable.flag_pakistan));
        itemList.add(new LangaugeModel("Uzbek", R.drawable.flag_uzbekistan));
        itemList.add(new LangaugeModel("Vietnamese", R.drawable.flag_vietnam));
        itemList.add(new LangaugeModel("Welsh", R.drawable.flag_argentina));
        itemList.add(new LangaugeModel("Xhosa", R.drawable.flag_south_africa));
        itemList.add(new LangaugeModel("Yiddish", R.drawable.flag_germany));
        itemList.add(new LangaugeModel("Yoruba", R.drawable.flag_nigeria));
        itemList.add(new LangaugeModel("Zulu", R.drawable.flag_south_africa));

//        itemList.add(new LangaugeModel("Afrikaans", R.drawable.flag_south_africa));
//        itemList.add(new LangaugeModel("Albanian", R.drawable.flag_albania));
//        itemList.add(new LangaugeModel("Arabic", R.drawable.flag_saudi_arabia));
//        itemList.add(new LangaugeModel("Azerbaijani", R.drawable.flag_azerbaijan));
//        itemList.add(new LangaugeModel("Basque", R.drawable.flag_macedonia));
//        itemList.add(new LangaugeModel("Bengali", R.drawable.flag_bangladesh));
//        itemList.add(new LangaugeModel("Belarusian", R.drawable.flag_belarus));
//        itemList.add(new LangaugeModel("Bulgarian", R.drawable.flag_bulgaria));
//        itemList.add(new LangaugeModel("Catalan", R.drawable.flag_antigua_and_barbuda));
//        itemList.add(new LangaugeModel("Chinese", R.drawable.flag_china));
//        itemList.add(new LangaugeModel("Croatian", R.drawable.flag_croatia));
//        itemList.add(new LangaugeModel("Czech", R.drawable.flag_czech_republic));
//        itemList.add(new LangaugeModel("Danish", R.drawable.flag_denmark));
//        itemList.add(new LangaugeModel("Dutch", R.drawable.flag_netherlands));
//        itemList.add(new LangaugeModel("English", R.drawable.flag_united_states_of_america));
//        itemList.add(new LangaugeModel("Estonian", R.drawable.flag_estonia));
//        itemList.add(new LangaugeModel("Filipino", R.drawable.flag_philippines));
//        itemList.add(new LangaugeModel("Finnish", R.drawable.flag_finland));
//        itemList.add(new LangaugeModel("French", R.drawable.flag_france));
//        itemList.add(new LangaugeModel("Georgian", R.drawable.flag_georgia));
//        itemList.add(new LangaugeModel("German", R.drawable.flag_germany));
//        itemList.add(new LangaugeModel("Greek", R.drawable.flag_greece));
//        itemList.add(new LangaugeModel("Gujarati", R.drawable.flag_india));
//        itemList.add(new LangaugeModel("HaitianCreole", R.drawable.flag_haiti));
//        itemList.add(new LangaugeModel("Hebrew", R.drawable.flag_israel));
//        itemList.add(new LangaugeModel("Hindi", R.drawable.flag_india));
//        itemList.add(new LangaugeModel("Hungarian", R.drawable.flag_hungary));
//        itemList.add(new LangaugeModel("Icelandic", R.drawable.flag_iceland));
//        itemList.add(new LangaugeModel("Indonesian", R.drawable.flag_indonesia));
//        itemList.add(new LangaugeModel("Irish", R.drawable.flag_ireland));
//        itemList.add(new LangaugeModel("Italian", R.drawable.flag_italy));
//        itemList.add(new LangaugeModel("Japanese", R.drawable.flag_japan));
//        itemList.add(new LangaugeModel("Korean", R.drawable.flag_south_korea));
//        itemList.add(new LangaugeModel("Latin", R.drawable.flag_italy));
//        itemList.add(new LangaugeModel("Latvian", R.drawable.flag_latvia));
//        itemList.add(new LangaugeModel("Lithuanian", R.drawable.flag_lithuania));
//        itemList.add(new LangaugeModel("Macedonian", R.drawable.flag_macedonia));
//        itemList.add(new LangaugeModel("Malay", R.drawable.flag_malaysia));
//        itemList.add(new LangaugeModel("Maltese", R.drawable.flag_malta));
//        itemList.add(new LangaugeModel("Persian", R.drawable.flag_iran));
//        itemList.add(new LangaugeModel("Polish", R.drawable.flag_poland));
//        itemList.add(new LangaugeModel("Portuguese", R.drawable.flag_portugal));
//        itemList.add(new LangaugeModel("Romanian", R.drawable.flag_romania));
//        itemList.add(new LangaugeModel("Russian", R.drawable.flag_russian_federation));
//        itemList.add(new LangaugeModel("Serbian", R.drawable.flag_serbia));
//        itemList.add(new LangaugeModel("Slovak", R.drawable.flag_slovakia));
//        itemList.add(new LangaugeModel("Slovenian", R.drawable.flag_slovenia));
//        itemList.add(new LangaugeModel("Spanish", R.drawable.flag_spain));
//        itemList.add(new LangaugeModel("Swahili", R.drawable.flag_sri_lanka));
//        itemList.add(new LangaugeModel("Swedish", R.drawable.flag_sweden));
//        itemList.add(new LangaugeModel("Tamil", R.drawable.flag_india));
//        itemList.add(new LangaugeModel("Telugu", R.drawable.flag_india));
//        itemList.add(new LangaugeModel("Thai", R.drawable.flag_thailand));
//        itemList.add(new LangaugeModel("Turkish", R.drawable.flag_turkey));
//        itemList.add(new LangaugeModel("Ukrainian", R.drawable.flag_ukraine));
//        itemList.add(new LangaugeModel("Urdu", R.drawable.flag_pakistan));
//        itemList.add(new LangaugeModel("Vietnamese", R.drawable.flag_vietnam));
//
//
//
//
//        itemList.add(new LangaugeModel("Haitian Creole", R.drawable.flag_vietnam));
//        itemList.add(new LangaugeModel("Bosnian", R.drawable.flag_bosnia));
//        itemList.add(new LangaugeModel("Malagasy", R.drawable.flag_madagascar));
//        itemList.add(new LangaugeModel("Amharic", R.drawable.flag_ethiopia));
//        itemList.add(new LangaugeModel("Sindhi", R.drawable.flag_pakistan));
//        itemList.add(new LangaugeModel("Zulu", R.drawable.flag_south_africa));
//        itemList.add(new LangaugeModel("Scots Gaelic", R.drawable.flag_madagascar));
//        itemList.add(new LangaugeModel("Welsh", R.drawable.flag_argentina));
//        itemList.add(new LangaugeModel("Yiddish", R.drawable.flag_germany));
//        itemList.add(new LangaugeModel("Hawaiian", R.drawable.flag_haiti));
//        itemList.add(new LangaugeModel("Cebuano", R.drawable.flag_philippines));
//        itemList.add(new LangaugeModel("Hausa", R.drawable.flag_central_african_republic));
//        itemList.add(new LangaugeModel("Igbo", R.drawable.flag_nigeria));
//        itemList.add(new LangaugeModel("Mongolian", R.drawable.flag_mongolia));
//        itemList.add(new LangaugeModel("Hmong", R.drawable.flag_thailand));
//        itemList.add(new LangaugeModel("Chichewa", R.drawable.flag_zambia));
//        itemList.add(new LangaugeModel("Galician", R.drawable.flag_spain));
//        itemList.add(new LangaugeModel("Luxembourgish", R.drawable.flag_luxembourg));
//        itemList.add(new LangaugeModel("Armenian", R.drawable.flag_armenia));
//        itemList.add(new LangaugeModel("Pashto", R.drawable.flag_pakistan));
//        itemList.add(new LangaugeModel("Sesotho", R.drawable.flag_south_africa));
//        itemList.add(new LangaugeModel("Lao", R.drawable.flag_vietnam));
//        itemList.add(new LangaugeModel("Marathi", R.drawable.flag_thailand));
//        itemList.add(new LangaugeModel("Khmer", R.drawable.flag_cambodia));
//        itemList.add(new LangaugeModel("Nepali", R.drawable.flag_nepal));
//        itemList.add(new LangaugeModel("Sundanese", R.drawable.flag_indonesia));
//        itemList.add(new LangaugeModel("Kurdish (Kurmanji)", R.drawable.flag_iraq));
//        itemList.add(new LangaugeModel("Somali", R.drawable.flag_somalia));
//        itemList.add(new LangaugeModel("Kazakh", R.drawable.flag_kazakhstan));
//        itemList.add(new LangaugeModel("Javanese", R.drawable.flag_indonesia));
//        itemList.add(new LangaugeModel("Kyrgyz", R.drawable.flag_kyrgyzstan));
//        itemList.add(new LangaugeModel("Shona", R.drawable.flag_zambia));
//        itemList.add(new LangaugeModel("Xhosa", R.drawable.flag_south_africa));
//        itemList.add(new LangaugeModel("Norwegian (Bokmål)", R.drawable.flag_norway));
//        itemList.add(new LangaugeModel("Tajik", R.drawable.flag_tajikistan));
//        itemList.add(new LangaugeModel("Esperanto", R.drawable.flag_spain));
//        itemList.add(new LangaugeModel("Burmese", R.drawable.flag_myanmar));
//        itemList.add(new LangaugeModel("Sinhala", R.drawable.flag_sri_lanka));
//        itemList.add(new LangaugeModel("Malayalam", R.drawable.flag_india));
//        itemList.add(new LangaugeModel("Punjabi", R.drawable.flag_pakistan));
//        itemList.add(new LangaugeModel("Frisian", R.drawable.flag_germany));
//        itemList.add(new LangaugeModel("Uzbek", R.drawable.flag_uzbekistan));
//        itemList.add(new LangaugeModel("Corsican", R.drawable.flag_france));
//        itemList.add(new LangaugeModel("Kannada", R.drawable.flag_india));
//        itemList.add(new LangaugeModel("Maori", R.drawable.flag_new_zealand));
//        itemList.add(new LangaugeModel("Yoruba", R.drawable.flag_nigeria));
//        itemList.add(new LangaugeModel("Samoan", R.drawable.flag_samoa));


        return itemList;
    }

    public static ArrayList<LangaugeModel> languageListForTranslation() {
        ArrayList<LangaugeModel> itemList = new ArrayList<>();


        itemList.add(new LangaugeModel("Afrikaans", "af", "cf", R.drawable.flag_south_africa));
        itemList.add(new LangaugeModel("Albanian", "sq", "al", R.drawable.flag_albania));
        itemList.add(new LangaugeModel("Arabic", "ar", "sa", R.drawable.flag_saudi_arabia));
        itemList.add(new LangaugeModel("Azerbaijani", "az", "az", R.drawable.flag_azerbaijan));
        itemList.add(new LangaugeModel("Basque", "eu", "mk", R.drawable.flag_macedonia));
        itemList.add(new LangaugeModel("Bengali", "bn", "bd", R.drawable.flag_bangladesh));
        itemList.add(new LangaugeModel("Belarusian", "be", "by", R.drawable.flag_belarus));
        itemList.add(new LangaugeModel("Bulgarian", "bg", "bg", R.drawable.flag_bulgaria));
        itemList.add(new LangaugeModel("Catalan", "ca", "es", R.drawable.flag_antigua_and_barbuda));
        itemList.add(new LangaugeModel("Chinese", "zh-CN", "cn", R.drawable.flag_china));
        itemList.add(new LangaugeModel("Croatian", "hr", "hr", R.drawable.flag_croatia));
        itemList.add(new LangaugeModel("Czech", "cs", "cz", R.drawable.flag_czech_republic));
        itemList.add(new LangaugeModel("Danish", "da", "dk", R.drawable.flag_denmark));
        itemList.add(new LangaugeModel("Dutch", "nl", "nl", R.drawable.flag_netherlands));
        itemList.add(new LangaugeModel("English", "en", "us", R.drawable.flag_united_states_of_america));
        itemList.add(new LangaugeModel("Estonian", "et", "et", R.drawable.flag_estonia));
        itemList.add(new LangaugeModel("Filipino", "tl", "ph", R.drawable.flag_philippines));
        itemList.add(new LangaugeModel("Finnish", "fi", "fi", R.drawable.flag_finland));
        itemList.add(new LangaugeModel("French", "fr", "fr", R.drawable.flag_france));
        itemList.add(new LangaugeModel("Georgian", "ka", "ge", R.drawable.flag_georgia));
        itemList.add(new LangaugeModel("German", "de", "de", R.drawable.flag_germany));
        itemList.add(new LangaugeModel("Greek", "el", "gr", R.drawable.flag_greece));
        itemList.add(new LangaugeModel("Gujarati", "gu", "in", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Haitian Creole", "ht", "ht", R.drawable.flag_haiti));
        itemList.add(new LangaugeModel("Hebrew", "iw", "il", R.drawable.flag_israel));
        itemList.add(new LangaugeModel("Hindi", "hi", "in", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Hungarian", "hu", "hu", R.drawable.flag_hungary));
        itemList.add(new LangaugeModel("Icelandic", "is", "is", R.drawable.flag_iceland));
        itemList.add(new LangaugeModel("Indonesian", "id", "id", R.drawable.flag_indonesia));
        itemList.add(new LangaugeModel("Irish", "ga", "ie", R.drawable.flag_ireland));
        itemList.add(new LangaugeModel("Italian", "it", "it", R.drawable.flag_italy));
        itemList.add(new LangaugeModel("Japanese", "ja", "jp", R.drawable.flag_japan));
        itemList.add(new LangaugeModel("Korean", "ko", "kr", R.drawable.flag_south_korea));
        itemList.add(new LangaugeModel("Latin", "la", "it", R.drawable.flag_italy));
        itemList.add(new LangaugeModel("Latvian", "lv", "lv", R.drawable.flag_latvia));
        itemList.add(new LangaugeModel("Lithuanian", "lt", "lt", R.drawable.flag_lithuania));
        itemList.add(new LangaugeModel("Macedonian", "mk", "mk", R.drawable.flag_macedonia));
        itemList.add(new LangaugeModel("Malay", "ms", "my", R.drawable.flag_malaysia));
        itemList.add(new LangaugeModel("Maltese", "mt", "mt", R.drawable.flag_malta));
        itemList.add(new LangaugeModel("Norwegian", "no", "no", R.drawable.flag_norway));
        itemList.add(new LangaugeModel("Persian", "fa", "ir", R.drawable.flag_iran));
        itemList.add(new LangaugeModel("Polish", "pl", "pl", R.drawable.flag_poland));
        itemList.add(new LangaugeModel("Portuguese", "pt", "pt", R.drawable.flag_portugal));
        itemList.add(new LangaugeModel("Romanian", "ro", "ro", R.drawable.flag_romania));
        itemList.add(new LangaugeModel("Russian", "ru", "ru", R.drawable.flag_russian_federation));
        itemList.add(new LangaugeModel("Serbian", "sr", "rs", R.drawable.flag_serbia));
        itemList.add(new LangaugeModel("Slovak", "sk", "sk", R.drawable.flag_slovakia));
        itemList.add(new LangaugeModel("Slovenian", "sl", "si", R.drawable.flag_slovenia));
        itemList.add(new LangaugeModel("Spanish", "es", "es", R.drawable.flag_spain));
        itemList.add(new LangaugeModel("Swahili", "sw", "lk", R.drawable.flag_sri_lanka));
        itemList.add(new LangaugeModel("Swedish", "sv", "se", R.drawable.flag_sweden));
        itemList.add(new LangaugeModel("Tamil", "ta", "in", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Telugu", "te", "in", R.drawable.flag_india));
        itemList.add(new LangaugeModel("Thai", "th", "th", R.drawable.flag_thailand));
        itemList.add(new LangaugeModel("Turkish", "tr", "tr", R.drawable.flag_turkey));
        itemList.add(new LangaugeModel("Ukrainian", "uk", "ua", R.drawable.flag_ukraine));
        itemList.add(new LangaugeModel("Urdu", "ur", "pk", R.drawable.flag_pakistan));
        itemList.add(new LangaugeModel("Vietnamese", "vi", "vn", R.drawable.flag_vietnam));

        return itemList;
    }

    public static String getDuration(long seconds) {
        seconds = (seconds / 1000);
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%2d:%02d", m, s);
    }

}
