package ir.icegroup.utilities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class Utility {


    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
    /**
     * this function get email that user entered and check that is correct or not return true/false
     *
     *
     * @param email input your email and this function check that are this Email correct!
     * @return if check result be ok return true or not return false
     * sample  : dsdsd        result ----> false
     * sample2 : ddd@ggg.ccc  result ----> true
     */
    public static boolean isValidEmail(String email) {
        if (email.length() == 0)
            return true;
        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }




    /**
     * convert english numbers to persian numbers
     *
     *
     * @param text your text
     * @return get back with persian numbers
     */
    public static String toPersianNumber(String text) {
        String out = "";
        if (text.length() == 0) {
            return out;
        }
            int length = text.length();
            for (int i = 0; i < length; i++) {
                char c = text.charAt(i);
                if ('0' <= c && c <= '9') {
                    int number = Integer.parseInt(String.valueOf(c));
                    out += persianNumbers[number];
                } else if (c == '٫') {
                    out += '،';
                } else {
                    out += c;
                }
            }
        return out;
    }




    /**
     * this function get an view and duration and target-height to expand to that size
     *
     * @param v your view that you want to do expanded on it
     * @param duration duration to expand
     * @param targetHeight max height you need to expand to
     */
    public static void expand(final View v, int duration, int targetHeight) {

        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }



    /**
     * this function get an view and duration and target-height to collapse to that size
     *
     * @param view your view that you want to do collapsed it
     * @param duration duration to collapse
     * @param targetHeight height of view to collapse to that value
     */
    public static void collapse(final View view, int duration, int targetHeight) {
        int prevHeight = view.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }
            @Override public void onAnimationStart(Animator animator) { }
            @Override public void onAnimationCancel(Animator animator) { }
            @Override public void onAnimationRepeat(Animator animator) { }
        });
    }




    /**
     * this function get an string and sey to you that was english type or persian word
     *
     * @param string your text
     * @return if your text was english words return true else return false
     */
    public static boolean isEnglishWord(String string) {
        for (int i = 0; i < Character.codePointCount(string, 0, string.length()); i++) {
            int c = string.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06FF || c == 0xFB8A || c == 0x067E || c == 0x0686 || c == 0x06AF)
                return false;
        }
        return true;
    }




    /**
     * this function has create html page for justify text . get string you want to set in web view
     *
     *
     * @param your_text your text
     * @param str_color if you want to set text color set this param with color code like this'#f5f5f5'
     *                  else let him with '' to get black color
     * @param font_size font size you want to set
     * @param font_name you need set font in directory 'asset/fonts/' of your project and set its name here
     *                  like 'Arial.ttf'
     * @return finally get back your html text to set in your webView
     */
    public static String getStyledFontForJustify(String your_text,String str_color, int font_size, String font_name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            String text;
            String str_message = your_text.trim();
            if (str_color == null)
                str_color = "";

            if (isEnglishWord(str_message)) {
                text = "<body><head><style>" +
                        "@font-face {" +
                        "font-family: CustomFont;" +
                        "src: url(\"file:///android_asset/fonts/"+font_name+"\")" +
                        "}" +
                        "body {" +
                        "font-family: CustomFont;" +
                        "font-size: "+font_size+"px;" +
                        "text-align: justify;" +
                        "color: " + str_color + ";" +
                        "}" +
                        "</style></head>" +
                        str_message +
                        "</body>";
            } else {
                text = "<body><head><style>" +
                        "@font-face {" +
                        "font-family: CustomFont;" +
                        "src: url(\"file:///android_asset/fonts/"+font_name+"\")" +
                        "}" +
                        "body {" +
                        "font-family: CustomFont;" +
                        "font-size: "+font_size+"px;" +
                        "text-align: justify;" +
                        "color: " + str_color + ";" +
                        "direction: rtl;" +
                        "}" +
                        "</style></head>" +
                        str_message +
                        "</body>";
            }
            return text.trim();
        }else
            return your_text.trim();
    }



    /**
     * this function check is google play service is install or not
     *
     *
     * @param activity need to your activity to get context
     * @return if Google Service was install in your device return true else false
     */
    public static boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }




    /**
     * this function get a view and return width and height of that at pixel
     *
     * @param view your view
     * @return return array with to digit. first digit is height and second digit width
     */
    public static int[] getViewHeightWidth(View view) {
        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int deviceWidth;
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        int[] a = new int[2];
        a[0] = view.getMeasuredHeight();
        a[1] = view.getMeasuredWidth();
        return a;
    }




    /**
     * this function will be restart your app
     *
     *
     * @param context set your app context
     */
    public static void RestartApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }




    /**
     * this function get string of money and separate by ',' between on three num and return money by separation like this
     *
     *
     * @param money input your money value like '2000000'
     * @return return to you '2,000,000'
     */
    public static String ShowMoney(String money) {
        return String.format("%,d", Long.parseLong(money));
    }





    /**
     * this function check network and return result ok as true or not as false
     * NOTE: you most be set permission of 'ACCESS_NETWORK_STATE' and 'INTERNET' in your application manifest
     *
     * @param context set application context
     * @return if network is access return true else false
     */
    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager cm1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo ni1 = cm1.getActiveNetworkInfo();
            if (ni1 == null)
                return false;
            else
                return true;
        }catch (NullPointerException e){
            return false;
        }

    }


    /**
     * this function convert dp to pixel
     *
     *
     * @param dp set your dp as digit
     * @return get back in px
     */
    public static int convertDpToPx(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }





    /**
     * this function convert pixel to dp
     *
     *
     * @param px set your px as digit
     * @return get back in dp
     */
    public static int convertPxToDp(int px) {
        return Math.round(px / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }




    /**
     * this function get back your device height and width as array with 2 digit int[0] and int[1]
     *
     *
     * @param context set your application context
     * @return get back int array with 2 digit. the first digit is height and second is width of your screen
     */
    public static int[] get_Device_height_width(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int[] a = new int[2];
        a[0] = displayMetrics.heightPixels;
        a[1] = displayMetrics.widthPixels;
        return a;
    }





    /**
     * get milli seconds to time
     *
     *
     * @param seconds set your second
     * @return get back time to you as '2:10:10' or '10:10'
     */
    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        if (h > 0) {
            return String.format("%d:%02d:%02d", h, m, s);
        } else {
            return String.format("%d:%02d", m, s);
        }
    }




    /**
     * get activity and state to show or hide status bar
     *
     *
     * @param state set true to show statusBar or to hide set false
     * @param activity set current activity here
     */
    public static void hide_Show_StatusBar(Boolean state, Activity activity) {
        if (state) {
            // Hide the status bar.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } else {
            // Show the status bar.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }




    /**
     * get a string and convert to integer
     * NOTE: if your string was 'null' return to you 0
     *
     *
     * @param value your string of digit
     * @return return to digit
     */
    public static int convert_To_Integer(String value) {
        try {
            if (value.contains("null") || value.contains("Null") || value.contains("NULL") || value.trim().length() == 0)
                return 0;
            else
                return Integer.parseInt(value);
        }catch (Exception e){
            return 0;
        }
    }




    /**
     * convert Byte to Hex
     *
     *
     * @param inArray get Bytes Array
     * @return get back Hex String
     */
    public static String ByteArrayToHexString(byte [] inArray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inArray.length ; ++j)
        {
            in = (int) inArray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }






    /**
     * convert Byte to DEC string
     *
     *
     * @param bArray get ByteArray
     * @return bet back Decimal String
     */
    public static String byteArrayToDecimal(byte[] bArray) {
        if (bArray == null) return "";
        long result = 0;
        for (int i = bArray.length - 1; i >= 0; --i) {
            result <<= 8;
            result |= bArray[i] & 0x0FF;
        }
        return Long.toString(result);
    }





    /**
     * this function get a time and then added to end of time her postfix like 'AM PM ب.ظ ق.ظ'
     * NOTE: if your language is farsi set 'language' param to 'fa' else 'en'
     *
     * @param time give to me time like '11:59'
     * @return i`ll be return to you '11:59 AM'
     */
    public static String TimeWithPostfix(String time, String language) {
        String postfix;
        String[] times = time.split(":");
        if (Integer.parseInt(times[0]) < 12) {
            if (language.equalsIgnoreCase("fa"))
                postfix = "ق.ظ";
            else
                postfix = "AM";
        } else {
            if (language.equalsIgnoreCase("fa"))
                postfix = "ب.ظ";
            else
                postfix = "PM";
        }
        if (language.equalsIgnoreCase("fa"))
            return toPersianNumber(times[0] + ":" + times[1] + " " + postfix);

        return times[0] + ":" + times[1] + " " + postfix;
    }


    /**
     * close keyboard
     *
     *
     * @param activity set current activity
     */
    public static void closeKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            try {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }





    /**
     * change miladi date to shamsi date
     *
     *
     * @param date your date like '2018/10/27'
     * @param splitter  your spliter like '/' or '-'
     * @return '1397/08/05'
     */
    public static String gregorian_to_jalali(String date, String splitter){
        int gy, gm, gd;

        String[] splitted_date = date.split(splitter);

        gy = Integer.parseInt(splitted_date[0]);
        gm = Integer.parseInt(splitted_date[1]);
        gd = Integer.parseInt(splitted_date[2]);


        int[] g_d_m = {0,31,59,90,120,151,181,212,243,273,304,334};
        int jy;
        if(gy>1600){
            jy=979;
            gy-=1600;
        }else{
            jy=0;
            gy-=621;
        }
        int gy2 = (gm > 2)?(gy + 1):gy;
        int days = (365 * gy) + ((int)((gy2 + 3) / 4)) - ((int)((gy2 + 99) / 100)) + ((int)((gy2 + 399) / 400)) - 80 + gd + g_d_m[gm - 1];
        jy += 33 * ((int)(days / 12053));
        days %= 12053;
        jy += 4 * ((int)(days / 1461));
        days %= 1461;
        if(days > 365){
            jy+=(int)((days-1)/365);
            days=(days-1)%365;
        }
        int jm = (days < 186)?1 + (int)(days / 31):7 + (int)((days - 186) / 30);
        int jd = 1 + ((days < 186)?(days % 31):((days - 186) % 30));

        String str_month = jm+"";
        if (str_month.length()==1)
            str_month = "0"+jm;

        String str_day = jd+"";
        if (str_day.length()==1)
            str_day = "0"+jd;

        return jy + "/" + str_month + "/" + str_day;
    }





    /**
     * change shamsi date to miladi date
     *
     *
     * @param date get date like as '1397/08/05'
     * @param splitter is an character like '/' or '-' or anythings like than
     * @return '2018/10/27'
     */
    public static String jalali_to_gregorian(String date, String splitter){
        int gy, jy, jm, jd;
        String[] splitted_date = date.split(splitter);

        jy = Integer.parseInt(splitted_date[0]);
        jm = Integer.parseInt(splitted_date[1]);
        jd = Integer.parseInt(splitted_date[2]);

        if(jy>979){
            gy=1600;
            jy-=979;
        }else{
            gy=621;
        }
        int days = (365 * jy) + (((int)(jy / 33)) * 8) + ((int)(((jy % 33) + 3) / 4)) + 78 + jd + ((jm < 7)?(jm - 1) * 31:((jm - 7) * 30) + 186);
        gy += 400 * ((int)(days / 146097));
        days %= 146097;
        if(days > 36524){
            gy += 100 * ((int)(--days / 36524));
            days %= 36524;
            if (days >= 365)days++;
        }
        gy += 4 * ((int)(days / 1461));
        days %= 1461;
        if(days > 365){
            gy += (int)((days - 1) / 365);
            days = (days - 1) % 365;
        }
        int gd = days + 1;
        int[] sal_a = {0,31,((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))?29:28,31,30,31,30,31,31,30,31,30,31};
        int gm;
        for(gm = 0;gm < 13;gm++){
            int v = sal_a[gm];
            if(gd <= v)break;
            gd -= v;
        }

        String str_month = gm+"";
        if (str_month.length()==1)
            str_month = "0"+gm;

        String str_day = gd+"";
        if (str_day.length()==1)
            str_day = "0"+gd;

        return gy + "/" + str_month + "/" + str_day;
    }

    /** show TurnOnGPS dialog */
    public static void turnGPSOn(final Activity activity, final int REQUEST_LOCATION) {
        GoogleApiClient googleApiClient =null;
        final GoogleApiClient finalGoogleApiClient = googleApiClient;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {}

                    @Override
                    public void onConnectionSuspended(int i) {
                        finalGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("Location error","Location error " + connectionResult.getErrorCode());

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                    }
                }).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        @SuppressWarnings("deprecation")
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(activity, REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent);
                }
            }
        });
    }

    /** get base64 string and calculate CRC32 checksum and return */
    public static String getChecksumBase64(String base64) {
        try {
            CRC32 crc = new CRC32();
            crc.update(base64.getBytes());
            return String.format("%08X", crc.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }

    /** converting bitmap image to base64 string */
    public static String encodeToBase64(Bitmap image){
        String encode = "";
        try {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
            encode = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }

    /** get base64 string and convert to bitmap type */
    public static Bitmap decodeFromBase64(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    /** @author get bytes as long and convert to MegaByte
     * @sample  -> `2MB`*/
    public static String getBytesToMBString(Long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

    /** add text in log */
    public static void log(String msg, String APP_NAME) {
        Log.e(APP_NAME, msg);
    }

    /** return list of storage's on device */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String[] getStorageDirectories(Context context) {
        List<String> results = new ArrayList<>();
        File[] externalDirs = context.getExternalFilesDirs(null);
        for (File file : externalDirs) {
            if (file.getPath().contains("/Android")){
                String path = file.getPath().split("/Android")[0];
                results.add(path);
            }
        }
        return results.toArray(new String[0]);
    }

}
