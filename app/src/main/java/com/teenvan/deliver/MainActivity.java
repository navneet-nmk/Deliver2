package com.teenvan.deliver;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.OnButtonClickListener;
import com.dexafree.materialList.card.provider.BasicImageButtonsCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declaration of member variables
    private MaterialListView mDeallList;
    private FloatingActionButton mAddDealsButton;
    private CoordinatorLayout mRootLayout;

    private Card card;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencing the UI elements

        mAddDealsButton = (FloatingActionButton)findViewById(R.id.mAddDealsButton);
        mRootLayout = (CoordinatorLayout)findViewById(R.id.mRootLayout);

        mAddDealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a snackbar
                Snackbar.make(mRootLayout,"Added a deal",Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });






    }

    public void getCardObject(){
        // Get the relevant data from the cloud
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Deals");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    // Success
                    for (ParseObject deal : list) {
                        final String title = deal.getString("DealTitle");
                        final String order = deal.getString("Order");
                        final String user = deal.getString("User");
                        // Creating the cards layout
                        card = new Card.Builder(MainActivity.this)
                                .setTag("BASIC_IMAGE_BUTTONS_CARD")
                                .withProvider(BasicImageButtonsCardProvider.class)
                                .setTitle(title)
                                .setDescription(order)
                                .setDrawable(R.mipmap.ic_launcher)
                                .setLeftButtonText("Accept Deal")
                                .setOnLeftButtonClickListener(new OnButtonClickListener() {
                                    @Override
                                    public void onButtonClicked(View view, Card card) {
                                        // Deal Accepted
                                        // Send a push message
                                        // Save the card info in local datastore
                                        ParseObject cardDeal = new ParseObject("Deal");
                                        cardDeal.put("Title", title);
                                        cardDeal.put("Order", order);
                                        cardDeal.put("User", user);
                                        cardDeal.pinInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    // Success
                                                    Log.d(TAG, "Success saving locally");
                                                } else {
                                                    // Failure
                                                    Log.e(TAG, "Failure saving locally", e);
                                                }
                                            }
                                        });
                                        ParsePush push = new ParsePush();
                                        push.setMessage("Deal Accpeted");
                                        push.sendInBackground(new SendCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    // Success
                                                    Log.d(TAG, "Push sent successfully");
                                                } else {
                                                    // Error
                                                    Log.e(TAG, "Failure sending push", e);
                                                }
                                            }
                                        });
                                    }
                                })
                                .endConfig()
                                .build();
                        mDeallList.add(card);
                    }

                } else {
                    // Error
                    Log.e(TAG, "Failure retrieving the objects", e);
                    Toast.makeText(MainActivity.this,"No deals currently",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
