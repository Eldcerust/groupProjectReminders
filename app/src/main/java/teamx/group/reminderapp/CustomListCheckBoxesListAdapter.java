package teamx.group.reminderapp;

import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomListCheckBoxesListAdapter extends BaseAdapter{
    private ArrayList<CheckBoxListSingle> data_lists;
    private LayoutInflater layout_inflater;

    public CustomListCheckBoxesListAdapter(Context context_main,ArrayList<CheckBoxListSingle> list_of_checkboxes){
        this.data_lists=list_of_checkboxes;
        this.layout_inflater=LayoutInflater.from(context_main);
    }

    @Override
    public int getCount() {
        return data_lists.size();
    }

    public void addList(){
        this.data_lists.add(new CheckBoxListSingle(false,""));
        notifyDataSetChanged();
    }

    public void deleteList(){
        this.data_lists.remove(this.data_lists.size()-1);
        notifyDataSetChanged();
    }

    public ArrayList<CheckBoxListSingle> return_changed_list(){
        return this.data_lists;
    }

    @Override
    public Object getItem(int i) {
        return data_lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class EditTextOverrided extends CustomListAdapterEditText{
        public EditTextOverrided(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public EditTextOverrided(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public EditTextOverrided(Context context) {
            super(context);
        }

        @Override
        public void doSomethingOnInherition(){
            addList();
            setNextFocusDownId(R.id.CustomEditText);
        }

        @Override
        public void deleteAction(){
            deleteList();
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder layout_holder;
        if(view==null){
            view=this.layout_inflater.inflate(R.layout.list_row_checkboxes,null);
            layout_holder=new ViewHolder();
            layout_holder.checkBox=(CheckBox)view.findViewById(R.id.checkBox);
            layout_holder.list_name=(TextView)view.findViewById(R.id.textView);
            layout_holder.input_layout=(TextInputLayout)view.findViewById(R.id.textInputLayout2);
            layout_holder.edit_text=(EditTextOverrided)view.findViewById(R.id.CustomEditText);
        }else{
            layout_holder=(ViewHolder)view.getTag();
        }

        layout_holder.checkBox.setChecked(data_lists.get(i).get_state());
        String text_list=this.data_lists.get(i).get_name();
        layout_holder.list_name.setText(this.data_lists.get(i).get_name());

        layout_holder.list_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_holder.list_name.setVisibility(View.INVISIBLE);
            }
        });

        layout_holder.edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    layout_holder.edit_text.setVisibility(View.INVISIBLE);
                    layout_holder.list_name.setText(layout_holder.edit_text.getText());
                    layout_holder.list_name.setVisibility(View.VISIBLE);
                }
            }
        });

        layout_holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(layout_holder.edit_text.length()>0){
                    if(b==true){
                        layout_holder.list_name.setPaintFlags(layout_holder.list_name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    } else{
                        layout_holder.list_name.setPaintFlags(layout_holder.list_name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                } else {
                    // create snackbar showing that the operation cannot be done when the thing is empty
                    if(b==true){
                        layout_holder.checkBox.setChecked(false);
                    }
                }

            }
        });
        // if text view has text, edit text is hidden
        // edit text should not phase out if the user has not entered anything and tried to skip

        return view;
    }

    static class ViewHolder{
        //button,text
        TextView list_name;
        CheckBox checkBox;
        TextInputLayout input_layout;
        EditTextOverrided edit_text;
    }
}
