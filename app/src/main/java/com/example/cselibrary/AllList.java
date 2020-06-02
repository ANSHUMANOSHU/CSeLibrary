package com.example.cselibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class AllList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout dl;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<BookItem> books = new ArrayList<>();
    Handler handler =new Handler();
    Thread thread;
    ConnectivityManager connectivityManager;
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dl = findViewById( R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,dl,
                toolbar,R.string.app_name,R.string.app_name);
       dl.addDrawerListener(actionBarDrawerToggle);
       actionBarDrawerToggle.syncState();
       NavigationView navigationView =findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(this);

        fetchData();

        if(!running)
        startThread();
        else{
            running = false;
            startThread();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }

    private void startThread() {
        final Dialog builder = new Dialog(this);
        TextView text = new TextView(this);
        text.setText("Please Connect To Internet\n\n");
        text.setGravity(Gravity.CENTER);
        builder.setContentView(text);
        builder.setTitle("Error");
        builder.setCancelable(false);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        running = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(!builder.isShowing())
                                        builder.show();
                                }
                                catch (WindowManager.BadTokenException e) {
                                }
                            }

                        });
                    }else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(builder.isShowing())
                                        builder.dismiss();
                                }
                                catch (WindowManager.BadTokenException e) {
                                }
                            }
                        });
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, ""+Thread.activeCount(), Toast.LENGTH_SHORT).show();
    }

    private void fetchData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/library");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    books.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        books.add(snapshot.getValue(BookItem.class));
                    }


                    recyclerView =findViewById(R.id.recyclerView);
                    adapter=new RecyclerViewAdapter(AllList.this,books);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AllList.this));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }catch (Exception e){
                    Toast.makeText(AllList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllList.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.programming){
            ArrayList<BookItem> arrayList = filter("programming");
            adapter.setItems(arrayList);
            adapter.notifyDataSetChanged();
        }

        if(id == R.id.database){
            ArrayList<BookItem> arrayList = filter("database");
            adapter.setItems(arrayList);
            adapter.notifyDataSetChanged();
        }
        if(id==R.id.mobiledev){
            ArrayList<BookItem> arrayList = filter("mobile development");
            adapter.setItems(arrayList);
            adapter.notifyDataSetChanged();
        }
        if(id==R.id.webdev){
            ArrayList<BookItem> arrayList = filter("web development");
            adapter.setItems(arrayList);
            adapter.notifyDataSetChanged();
        }
        if(id==R.id.share){
            Intent intent =new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String url="hello";
            intent.putExtra(Intent.EXTRA_TEXT,url);
            startActivity(Intent.createChooser(intent,"Share..."));
        }
        if(id==R.id.about){
            DialogAbout about =new DialogAbout(this);
            about.show(getSupportFragmentManager(),"about dialog");
        }

        dl.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<BookItem> filter(String cat) {
        ArrayList<BookItem> items = new ArrayList<>();
        for(BookItem i : books){
            if(i.category.toLowerCase().equals(cat.toLowerCase())){
                items.add(i);
            }
        }
        return items;
    }
}
