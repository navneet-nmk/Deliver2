package com.teenvan.deliver;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.OnButtonClickListener;
import com.dexafree.materialList.card.provider.BasicImageButtonsCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonFloat;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    // Declaration of member variables
    private MaterialListView mDeallList;
    private ButtonFloat mAddDealsButton;
    private Card card;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencing the UI elements
        mDeallList = (MaterialListView)findViewById(R.id.dealsList);
        mAddDealsButton = (ButtonFloat)findViewById(R.id.addDealButton);

        // Setting up the on click listener
        mAddDealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add Deals
                // Show a dialog
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                final MaterialEditText mDealTitle = (MaterialEditText) dialog.
                        findViewById(R.id.orderEdit);
                final MaterialEditText mDealDescrp = (MaterialEditText) dialog
                        .findViewById(R.id.orderDescriptionEdit);
                Button mAddDeals = (Button) dialog.
                        findViewById(R.id.addDealButton);
                mAddDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the texts
                        String title = mDealTitle.getText().toString();
                        String descrp = mDealDescrp.getText().toString();
                        // Create a parse object
                        ParseObject dealObject = new ParseObject("Deals");
                        dealObject.put("DealTitle", title);
                        dealObject.put("Order", descrp);
                        dealObject.put("User", ParseUser.getCurrentUser().getUsername());
                        dealObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                dialog.dismiss();
                                if (e == null) {
                                    // Success
                                    Toast.makeText(MainActivity.this, "Successfully added deal",
                                            Toast.LENGTH_SHORT).show();
                                    getCardObject();
                                } else {
                                    // Failure
                                    Log.e(TAG, "Failure adding deal", e);
                                    Toast.makeText(MainActivity.this, "Failed adding deal",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
        getCardObject();






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
                                .setDrawable(R.drawable.ic_launcher)
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
