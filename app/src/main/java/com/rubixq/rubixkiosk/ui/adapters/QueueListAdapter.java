package com.rubixq.rubixkiosk.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rubixq.rubixkiosk.R;
import com.rubixq.rubixkiosk.models.Queue;

import java.util.List;

public class QueueListAdapter extends ArrayAdapter<Queue> {
    private Context context;
    private List<Queue> queues;

    public QueueListAdapter(@NonNull Context context, List<Queue> queues) {
        super(context, R.layout.queue_item_template, queues);
        this.context = context;
        this.queues = queues;
    }

    @Override
    public int getCount() {
        return this.queues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setQueues(List<Queue> queues) {
        this.queues = queues;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Queue queue = this.queues.get(position);

        View view = LayoutInflater.from(this.context).inflate(R.layout.queue_item_template, null, false);
        TextView queueNameLabel = view.findViewById(R.id.queue_label);
        queueNameLabel.setText(queue.getName().toUpperCase());
        return view;
    }
}
