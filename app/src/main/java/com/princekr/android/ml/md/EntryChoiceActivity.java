package com.princekr.android.ml.md;

import android.os.Bundle;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_entry_choice);

        RecyclerView entryRecyclerView = findViewById(R.id.entry_recycler_view);
        entryRecyclerView.setHasFixedSize(true);
        entryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class EntryItemAdapter extends RecyclerView.Adapter<EntryItemAdapter.EntryItemViewHolder> {

        private final EntryMode[] EntryModes;

        public EntryItemAdapter(EntryMode[] entryModes) {
            EntryModes = entryModes;
        }

        @NonNull
        @Override
        public EntryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull EntryItemViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        private class EntryItemViewHolder extends RecyclerView.ViewHolder {

            private final TextView titleView;
            private final TextView subtitleView;


            public EntryItemViewHolder(@NonNull View itemView, TextView titleView, TextView subtitleView) {
                super(itemView);
                this.titleView = titleView;
                this.subtitleView = subtitleView;
            }
        }
    }
}
