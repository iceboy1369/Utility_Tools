package ir.icegroup.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Toast {

    public enum ToastType {
        Warning, Done, Error, Info
    }

    public static void show(String msg, ToastType type, Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.toast_layout, null, false);

        ImageView image = view.findViewById(R.id.image);
        TextView text = view.findViewById(R.id.text);
        RelativeLayout background = view.findViewById(R.id.toast_layout_root);
        text.setText(msg);

        switch (type){
            case Done:
                image.setImageResource(R.drawable.mark_select);
                background.setBackgroundColor(context.getResources().getColor(R.color.green_pure_dark));
                break;
            case Info:
                image.setImageResource(R.drawable.ic_info);
                background.setBackgroundColor(context.getResources().getColor(R.color.blue));
                break;
            case Warning:
                image.setImageResource(R.drawable.ic_error);
                background.setBackgroundColor(context.getResources().getColor(R.color.orange));
                break;
            case Error:
                image.setImageResource(R.drawable.ic_close_red);
                background.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, 0, 0);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}
