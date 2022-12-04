package com.project.an;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class WatchVideosActivity extends AppCompatActivity {

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    private static final ArrayList<Card> videoCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_watch_videos);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.card_listView);
        setupCardModels();
        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card, this);

        for (int i = 0; i < 10; i++) {
            Card card = new Card("Tile", "Description", "URl");
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);


    }

    public static void setupCardModels(){
        String[] titles = {"Video 5", "Video 6", "Video 7", "Video 8", "Video 9", "Video 10", "Video 15", "Video 16"};
        String[] descriptions = {
                "This is a description for video 5",
                "This is a description for video 6",
                "This is a description for video 7",
                "This is a description for video 8",
                "This is a description for video 9",
                "This is a description for video 10",
                "This is a description for video 15",
                "This is a description for video 16"
        };

        String[] associatedUrls = {
                "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd",
                "http://ftp.itec.aau.at/datasets/DASHDataset2014/BigBuckBunny/2sec/BigBuckBunny_2s_onDemand_2014_05_09.mpd",
                "http://media.developer.dolby.com/DolbyVision_Atmos/profile8.1_DASH/p8.1.mpd",
                "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd",
                "http://ftp.itec.aau.at/datasets/DASHDataset2014/BigBuckBunny/2sec/BigBuckBunny_2s_onDemand_2014_05_09.mpd",
                "http://media.developer.dolby.com/DolbyVision_Atmos/profile8.1_DASH/p8.1.mpd",
                "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd",
                "http://ftp.itec.aau.at/datasets/DASHDataset2014/BigBuckBunny/2sec/BigBuckBunny_2s_onDemand_2014_05_09.mpd"
        };
        for(int i = 0; i < titles.length; i++){
            videoCards.add(new Card(titles[i], descriptions[i], associatedUrls[i]));
        }
    }


    public void onCardClick(int position) {

        Log.i("aemond", "Clicked in watch vide activity");
        try {
            Intent intent = new Intent(this, PlayerActivity.class);
            Log.i("aemond", "Intent created");

            intent.putExtra("VIDEO_URL", videoCards.get(position).getUrl());
            Log.i("aemond", "Url put to intent");
            intent.putExtra("VIDEO_TITLE", videoCards.get(position).getTitle());
            Log.i("aemond", "Title put to intent");
            startActivity(intent);
            Log.i("aemond", "play Activity started");
        }catch(Exception e){
            Log.e("aemond", e.getMessage());
        }
    }


}