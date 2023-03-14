package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RingViewModel extends RecyclerView.ViewHolder {

    private final TextView uidView;

    public RingViewModel(@NonNull View itemView) {
        super(itemView);
        this.uidView = itemView.findViewById(R.id.uidView);
    }

    public void updateUID() {

    }
}
