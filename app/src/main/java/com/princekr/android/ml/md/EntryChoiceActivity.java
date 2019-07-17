package com.princekr.android.ml.md;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EntryChoiceActivity extends AppCompatActivity {

    private enum EntryMode {
        ENTRY_JAVA(R.string.entry_java_title, R.string.entry_java_subtitle),
        ENTRY_KOTLIN(R.string.entry_kotlin_title, R.string.entry_kotlin_subtitle);

        private final int titleResId;
        private final int subtitleResId;

        EntryMode(int titleResId, int subtitleResId) {
            this.titleResId = titleResId;
            this.subtitleResId = subtitleResId;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_entry_choice);

        RecyclerView entryRecyclerView = findViewById(R.id.entry_recycler_view);
        entryRecyclerView.setHasFixedSize(true);
        entryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        entryRecyclerView.setAdapter(new EntryItemAdapter(EntryMode.values()));
    }

    private class EntryItemAdapter extends RecyclerView.Adapter<EntryItemAdapter.EntryItemViewHolder> {

        private final EntryMode[] entryModes;

        public EntryItemAdapter(EntryMode[] entryModes) {
            this.entryModes = entryModes;
        }

        @NonNull
        @Override
        public EntryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EntryItemViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.entry_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EntryItemViewHolder entryItemViewHolder, int position) {
            entryItemViewHolder.bindEntryMode(entryModes[position]);
        }

        @Override
        public int getItemCount() {
            return entryModes.length;
        }

        private class EntryItemViewHolder extends RecyclerView.ViewHolder {

            private final TextView titleView;
            private final TextView subtitleView;


            public EntryItemViewHolder(@NonNull View view) {
                super(view);
                this.titleView = view.findViewById(R.id.entry_title);
                this.subtitleView = view.findViewById(R.id.entry_subtitle);
            }

            void bindEntryMode(EntryMode entryMode) {
                titleView.setText(entryMode.titleResId);
                subtitleView.setText(entryMode.subtitleResId);
                itemView.setOnClickListener(view -> {
                    Activity activity = EntryChoiceActivity.this;
                    switch (entryMode) {
                        case ENTRY_JAVA:
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            break;
                        case ENTRY_KOTLIN:
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            break;
                    }
                });
            }
        }
    }
}
