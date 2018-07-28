package com.rubixq.rubixkiosk.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.rubixq.rubixkiosk.R;
import com.rubixq.rubixkiosk.models.Queue;
import com.rubixq.rubixkiosk.ui.adapters.QueueListAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListView queueListView;
    QueueListAdapter queueListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.queueListView = this.findViewById(R.id.queue_list);
        this.setupQueueListView();
    }

    private void setupQueueListView(){
        List<Queue> queues = new ArrayList<>();
        queues.add(Queue.newInstance("1","New Passport","",""));
        queues.add(Queue.newInstance("2","Passport Renewal","",""));
        queues.add(Queue.newInstance("3","Help And Support","",""));

        this.queueListAdapter = new QueueListAdapter(getApplicationContext(), queues);
        this.queueListView.setAdapter(this.queueListAdapter);
    }
}
