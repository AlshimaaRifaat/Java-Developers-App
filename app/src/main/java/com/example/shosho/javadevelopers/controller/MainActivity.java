package com.example.shosho.javadevelopers.controller;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shosho.javadevelopers.api.Client;
import com.example.shosho.javadevelopers.api.Service;
import com.example.shosho.javadevelopers.ItemAdapter;
import com.example.shosho.javadevelopers.model.Item;
import com.example.shosho.javadevelopers.model.ItemResponse;
import com.example.shosho.javadevelopers.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
   private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer ;
    //private Item item;
    TextView textView_disconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initViews();
        swipeContainer =findViewById( R.id.id_swip_container );
        swipeContainer .setColorSchemeResources( android.R.color.holo_orange_dark );

        swipeContainer .setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJson();
                Toast.makeText( MainActivity.this, "Githup Users Refreshed", Toast.LENGTH_SHORT ).show();
            }
        } );

    }
    public void initViews()
    {
        progressDialog=new ProgressDialog( this );
        progressDialog.setMessage( "Fetching Githup Users ..." );
        progressDialog.setCancelable( false );
        progressDialog.show();
        recyclerView=findViewById( R.id.id_recyclerView );
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        recyclerView.smoothScrollToPosition( 0 );
        loadJson();
    }

    public void loadJson()
    { textView_disconnected = (TextView) findViewById(R.id.id_disconnected);
        try{
            //Client Client = new Client();
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<ItemResponse> call = apiService.getitems();
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    List<Item> items = response.body().getItems();
                    recyclerView.setAdapter(new ItemAdapter(getApplicationContext(), items));
                    recyclerView.smoothScrollToPosition(0);
                    swipeContainer .setRefreshing(false);
                    progressDialog.hide();
                }

                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    textView_disconnected.setVisibility(View.VISIBLE);
                    progressDialog.hide();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}