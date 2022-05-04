package be.kuleuven.spot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class activity_login extends AppCompatActivity {

    private TextView DoNotHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onDoNotHaveAccount_Clicked(View caller) {

        Intent intent = new Intent(this, activity_register.class);
        startActivity(intent);
    }

    public void checkLoginData(){
        Connection c = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("mysql.studev.groept.be","a21pt215","secret");
            System.out.println("Opened database successfully");

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("");

            while(rs.next() == true){
                // write the action you wanna make with the data taken from the databse
            }
            rs.close();
            stmt.close();
            c.close();
        }
        catch(Exception e){
            System.err.println("ERROR : " + e.getMessage());
        }
    }
}