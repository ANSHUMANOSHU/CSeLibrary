package com.example.cselibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogAbout extends DialogFragment {

    private TextView weblink1,weblink2;
    private Button back;
    private AllList allList;

    public DialogAbout(AllList allList) {
        this.allList = allList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view =getActivity().getLayoutInflater().inflate(R.layout.about_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        weblink1=view.findViewById(R.id.weblink1);
        weblink2=view.findViewById(R.id.weblink2);
        back=view.findViewById(R.id.Back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        weblink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(allList,WebViewPDF.class);
                intent.putExtra("pdfurl",weblink2.getText().toString());
                intent.putExtra("type","url");
                allList.startActivity(intent);
            }
        });


        weblink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(allList,WebViewPDF.class);
                intent.putExtra("pdfurl",weblink1.getText().toString());
                intent.putExtra("type","url");
                allList.startActivity(intent);
            }
        });

        return builder.create();
    }
}

