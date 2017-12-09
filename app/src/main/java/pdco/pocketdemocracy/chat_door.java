package pdco.pocketdemocracy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Random;

public class chat_door  {

    private String room_name;
    private String key;
    private Button joinButton;
   // private ArrayList<String> guest_list;
    public chat_door(String chatname, String guest){
        room_name = chatname;
        Random rand = new Random();
        //guest_list = new ArrayList<String>();
        //guest_list.add(guest);
        String code = "";
        for(int i = 0; i < 5; i++){
            code = code + String.valueOf(rand.nextInt(9) + 1);
        }
        Log.i("key",code);
        key = code;

    }
    public chat_door(){

    }
    //Must have getters for Firebase to retrieve information about an object
    public String getRoom_name() {return room_name;}
    public void setRoom_name(String name){
        this.room_name = name;
    }
    public String getKey(){
        return key;
    }
    public Button getJoinButton(){return joinButton;}
//    public ArrayList<String> getGuest_list(){return guest_list;}

}
