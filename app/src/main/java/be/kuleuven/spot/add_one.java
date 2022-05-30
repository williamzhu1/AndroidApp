package be.kuleuven.spot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class add_one extends AppCompatActivity {

    Button btn_publish, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one);

        btn_publish = findViewById(R.id.btn_publish);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_one.this, HomeFragment.class );
                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_one.this, HomeFragment.class );
                startActivity(intent);
            }
        });
    }
}