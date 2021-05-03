package ir.icegroup.utility_tools;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import ir.icegroup.utilities.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.show("سلام دوستان", Toast.ToastType.Error, this);
    }
}