package com.project.an;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        for (int i = 0; i < videoCards.size(); i++) {
            Card card = new Card(videoCards.get(i).getTitle(), videoCards.get(i).getDescription(), videoCards.get(i).getUrl());
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);


    }

    public static void setupCardModels(){

        String Tag = "vhagar";
        Log.i(Tag, "Starting");
        Call<List<Video>> call = RetrofitClient.getInstance().getAPI().getVideos();
        Log.i(Tag, "call ");
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Log.i(Tag, "Got data");
                List<Video> videoList = response.body();
                Log.i(Tag, videoList.get(0).getTitle());
                Log.i(Tag, videoList.get(0).getDescription());
                for (int i = 0; i < videoList.size(); i++) {
                    Log.i(Tag, "xxxx");
                    videoCards.add(new Card(videoList.get(i).getTitle(), videoList.get(i).getDescription(),
                                    BuildConfig.FILE_SYSTEM_URL+videoList.get(i).getID()+"/"+videoList.get(i).getID()+"_out.mpd")
                            );
                }
                Log.i(Tag, videoCards.toString());
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {

                Log.e(Tag, "Mission Faileddddd");
                Log.e(Tag, t.getMessage());

            }

        });

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