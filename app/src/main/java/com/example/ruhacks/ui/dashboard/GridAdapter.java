package com.example.ruhacks.ui.dashboard;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ruhacks.R;
import com.example.ruhacks.Reward;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    Context context;
    // private final String[] values;
    // private final int[] images;
    private ArrayList<Reward> rewards;
    View view;
    LayoutInflater layoutInflater;

    EventListener listener;
    ViewGroup container;

    public interface EventListener {
        int getPoints();
        void deductPoints(int deducted);
    }

    public GridAdapter(Context context, ArrayList<Reward> rewards, EventListener listener, ViewGroup container) {
        this.context = context;
        // this.values = values;
        // this.images = images;
        this.rewards = rewards;
        this.listener = listener;
        this.container = container;
    }

    @Override
    public int getCount() {
        return rewards.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            imageView.setImageResource(rewards.get(position).getImage());
            if (!rewards.get(position).getUnlocked()) {
                imageView.setImageAlpha(50);
            }

            Drawable myDrawable = imageView.getDrawable();
            BitmapDrawable bitmap = (BitmapDrawable) myDrawable;

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (rewards.get(position).getUnlocked()) {
                        Resources resources = context.getResources();
                        Uri uriToImage = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(rewards.get(position).getImage()) + '/' + resources.getResourceTypeName(rewards.get(position).getImage()) + '/' + resources.getResourceEntryName(rewards.get(position).getImage()));

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                        shareIntent.setType("image/png");
                        context.startActivity(Intent.createChooser(shareIntent, "uwu"));
                    } else if (listener.getPoints() >= 100) {
                        rewards.get(position).unlock();
                        listener.deductPoints(100);
                        // ViewGroup vg = findViewById(R.id.navigation_dashboard);
                        // vg.invalidate();
                    }
                }
            });
        }

        return view;
    }
}
