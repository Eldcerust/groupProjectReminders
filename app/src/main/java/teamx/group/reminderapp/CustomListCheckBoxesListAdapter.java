package teamx.group.reminderapp;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomListCheckBoxesListAdapter extends BaseAdapter {
    private ArrayList<CheckBoxListSingle> data_lists;
    private LayoutInflater layout_inflater;

    public CustomListCheckBoxesListAdapter(Context context_main,ArrayList<CheckBoxListSingle> list_of_checkboxes){
        this.data_lists=list_of_checkboxes;
        this.layout_inflater=LayoutInflater.from(context_main);
    }

    @Override
    public int getCount() {
        return return data_lists.size();
    }

    public void addEmptyList(){
        this.data_lists.add(new CheckBoxListSingle(false,""));
    }

    @Override
    public Object getItem(int i) {
        return data_lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder layout_holder;
        if(view==null){
            view=this.layout_inflater.inflate(R.layout.list_row_checkboxes,null);
            layout_holder=new ViewHolder();
            layout_holder.checkBox=(CheckBox)view.findViewById(R.id.checkBox);
            layout_holder.list_name=(TextView)view.findViewById(R.id.textView);
            layout_holder.input_layout=(TextInputLayout)view.findViewById(R.id.textInputLayout2);
        }else{
            layout_holder=(ViewHolder)view.getTag();
        }

        layout_holder.checkBox.setChecked(data_lists.get(i).get_state());
        String text_list=this.data_lists.get(i).get_name();
        layout_holder.list_name.setText(this.data_lists.get(i).get_name());
        // if text view has text, edit text is hidden
        // edit text should not phase out if the user has not entered anything and tried to skip
    }

    static class ViewHolder{
        //button,text
        TextView list_name;
        CheckBox checkBox;
        TextInputLayout input_layout;
    }
}
