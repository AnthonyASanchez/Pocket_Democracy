package pdco.pocketdemocracy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class info extends AppCompatActivity {

    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        message = (TextView) findViewById(R.id.message);
        message.setMovementMethod(new ScrollingMovementMethod());
        message.setText("Thank you for trying PocketDemocracy.\n" +
                "-Tap Create Room button to create a chat room. This will generate a chat room with a key code. Give this code to the people you would like to chat and vote with.\n " +
                "-Tap Join Room to enter an existing chat room. You will need to use a given key code from an already created chat room to add it to your directory.\n" +
                "-Tap on a room of your choice in the directory to join it.\n\n" +
                "In Chat:\n" +
                "-Tap the top right icon in order to create a vote. You will be asked to enter a vote title. Press the Cast button in order to enter that vote.\n" +
                "-Once a vote is entered, a dialog will pop up asking for the users choice. Another notification will also appear at the top of the screen, saying how long the vote will last.\n" +
                "-Votes last for 15 seconds.\n" +
                "-At the end of those 15 seconds, a notification will appear saying the result of the vote.\n\n" +
                "-PocketDemocracy is a work in progress. There are many bugs, and it is far from finished." +
                "-List of bugs include: \n" +
                "   1. Sometimes a vote will be permenatley set, which prevents users from adding new votes, essentially creating a chat room with no voting features.\n" +
                "   2. Users can sometimes experience different vote results (User 1 will see a vote passed while User 2 will see a vote failed).\n" +
                "   3. Users sometimes will not be notified of a vote in progress, while others will.\n" +
                "   4. When a chat is first created, we have experienced that it is prone to crashing.\n" +
                "-There are more bugs as well.\n\n" +
                "" +
                "-We would like to say thank you for your interest in this application. We plan to improve the current features and to remove all bugs in order to give a great user experience.\n" +
                "-We apologize for any inconvience or frustration.\n" +
                "-Thank you.");
    }
}
