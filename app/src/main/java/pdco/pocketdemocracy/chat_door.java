package pdco.pocketdemocracy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Random;

public class chat_door extends AppCompatActivity implements View.OnClickListener{

    private String room_name;
    private String key;
    private Button deleteButton;

    public chat_door(String chatname){
        room_name = chatname;
        Random rand = new Random();
        String code = "";
        for(int i = 0; i < 5; i++){
            code = code + String.valueOf(rand.nextInt(9) + 1);
        }
        Log.i(key,key);
        key = code;
    }

    public String getRoom_name() {
        return room_name;
    }
    public void setRoom_name(String name){
        this.room_name = name;
    }
    public String getKey(){
        return key;
    }

    @Override
    public void onClick(View view) {
        if(view == deleteButton){
            //Delete this activity
        }
    }
}
