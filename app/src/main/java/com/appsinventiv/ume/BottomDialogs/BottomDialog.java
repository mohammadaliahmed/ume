package com.appsinventiv.ume.BottomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appsinventiv.ume.BottomDialogs.Adapters.LearningLanguageListAdapter;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;

import java.util.ArrayList;

public class BottomDialog {
    private BottomDialog() {
    }

    public static void showLanguagesDialog(Context context,ArrayList<String> countyList) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.languages_bottom_sheet_option, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context
        );
        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        Button cancel = customView.findViewById(R.id.cancel);
        Button ok = customView.findViewById(R.id.ok);

        recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        LearningLanguageListAdapter adapter=new LearningLanguageListAdapter(context,countyList);
        recyclerview.setAdapter(adapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showToast(search.getText().toString());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }
}
