package com.example.cselibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Context context;
    ArrayList<BookItem> items =new ArrayList<>();

    public RecyclerViewAdapter(Context context, ArrayList<BookItem> items) {
        this.context = context;
        this.items=items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_item_view,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),String.format(Locale.US,"text.ttf"));
        holder.title.setTypeface(typeface);
        holder.title.setText(items.get(position).title);
        Glide.with(context).load(items.get(position).image).into(holder.bookImage);
        holder.title.setSelected(true);
        holder.bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,WebViewPDF.class);
                intent.putExtra("pdfurl",items.get(position).pdf);
                intent.putExtra("type","pdf");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<BookItem> items){
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView bookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            bookImage=itemView.findViewById(R.id.image_book);
        }
    }
}
