package teamx.group.reminderapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter{
    //do we even need views in MVP then?
    //
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
    public View getView(int position, View convert_view, ViewGroup parent) {
        ViewHolder view_lists;
        if(convert_view==null){
            convert_view=this.inflated_layout.inflate(R.layout.list_row_layout,null);
            view_lists=new ViewHolder();
            view_lists.name_view=(TextView)convert_view.findViewById(R.id.title);
            view_lists.type_view=(TextView)convert_view.findViewById(R.id.date);
            view_lists.time_view=(TextView)convert_view.findViewById(R.id.time);
            convert_view.setTag(view_lists);
        }else{
            view_lists=(ViewHolder)convert_view.getTag();
        }
        //set the text of the views
        view_lists.name_view.setText(this.reminder_lists.get(position).get_reminder_name());
        view_lists.name_view.setTextColor(Color.BLACK);

        SimpleDateFormat date_format=new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date_format.getTimeZone();
        String date_string=date_format.format(this.reminder_lists.get(position).get_reminder_date_time().getTime());
        view_lists.time_view.setText(date_string);
        view_lists.time_view.setTextColor(Color.BLACK);

        view_lists.type_view.setText(this.reminder_lists.get(position).return_type());
        view_lists.type_view.setTextColor(Color.BLACK);

        return convert_view;
    }

    public void set_data_refresh(){
        notifyDataSetChanged();
    }

    static class ViewHolder{
        TextView name_view;
        TextView time_view;
        TextView type_view;
    }
}
