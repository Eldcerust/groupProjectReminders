package teamx.group.reminderapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter{
    private ArrayList<RemindersModel> reminder_lists;
    private LayoutInflater inflated_layout;
    private ArrayList<Integer> reminder_timepresent;

    public CustomListAdapter(Context context_required,ArrayList<RemindersModel> reminder_listed){
        this.reminder_lists=reminder_listed;
        this.inflated_layout=LayoutInflater.from(context_required.getApplicationContext());
    }

    @Override
    public int getCount() {
        return this.reminder_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return this.reminder_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view_lists;
        if(convertView==null){
            convertView=this.inflated_layout.inflate(R.layout.list_row_layout,null);
            view_lists=new ViewHolder();
        }
    }

    static class ViewHolder{
        TextView name_view;
        TextView time_view;
        TextView date_view;
    }
}
