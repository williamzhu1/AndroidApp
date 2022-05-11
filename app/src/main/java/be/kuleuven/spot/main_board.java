package be.kuleuven.spot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class main_board extends AppCompatActivity {

    Connection connect;
    String connectionResult="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);
    }

    public void GetDataToTextView(View v){

    }
}