package teamx.group.reminderapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.security.auth.Subject;

public class CustomListCheckBoxesListAdapter extends BaseAdapter{
    private ArrayList<CheckBoxListSingle> data_lists;
    private LayoutInflater layout_inflater;
    private ArrayList<Thread> save_threads=new ArrayList<Thread>();
    private ArrayList<Thread> requestfocus_thread=new ArrayList<Thread>();
    private Boolean initial_bool=false;
    public volatile static boolean listview_change;
    public volatile static int position_holder;

    public CustomListCheckBoxesListAdapter(Context context_main,ArrayList<CheckBoxListSingle> list_of_checkboxes){
        this.data_lists=list_of_checkboxes;
        this.layout_inflater=LayoutInflater.from(context_main);
    }

    public void save_checkbox(){
        for(int a=0;a<save_threads.size();a++){
            save_threads.get(a).run();
        }
    }

    @Override
    public int getCount() {
        return data_lists.size();
    }

    public void addList(int position){
        if(position==getCount()-1 && position!=0) {
            System.out.println("Function is "+String.valueOf(getCount()-1)+" "+ String.valueOf(position)+ new Exception().getStackTrace()[0]);
            this.data_lists.add(new CheckBoxListSingle(false, ""));
        } else if(position!=0 && position!=getCount()-1) {
            System.out.println("Function is " + String.valueOf(getCount() - 1) + " " + String.valueOf(position) + new Exception().getStackTrace()[0]);
            this.data_lists.add(position, new CheckBoxListSingle(false, ""));
        } else if(position==0){
            if(!initial_bool) {
                this.data_lists.add(new CheckBoxListSingle(false, ""));
                initial_bool = true;
            } else {
                this.data_lists.add(1,new CheckBoxListSingle(false,""));
            }
        }
    }

    public void deleteList(int position){
        this.data_lists.remove(position);

}
    public ArrayList<CheckBoxListSingle> return_changed_list(){
        return this.data_lists;
    }

    public void set_array_checkbox(ArrayList<CheckBoxListSingle> check_box){
        this.data_lists=check_box;
        notifyDataSetChanged();
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewHolder layout_holder;
        System.out.println("Position of view just created "+position+ new Exception().getStackTrace()[0]);
        if(view==null) {
            view = this.layout_inflater.inflate(R.layout.list_row_checkboxes, null);
            layout_holder = new ViewHolder();
            layout_holder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxOfView);
            boolean state=this.data_lists.get(position).get_state();
            layout_holder.checkBox.setChecked(state);
            layout_holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                }
            });

            //null???
            layout_holder.input_layout = (TextInputLayout) view.findViewById(R.id.textInputLayoutInsideView);
            layout_holder.edit_text = (TextInputEditText) view.findViewById(R.id.customEditText);

            String list_text=this.data_lists.get(position).get_name();
            layout_holder.edit_text.setText(list_text);

            layout_holder.edit_text.setText(this.data_lists.get(position).get_name());
            layout_holder.edit_text.setFocusableInTouchMode(true);
            layout_holder.checkBox.setFocusableInTouchMode(true);

            layout_holder.edit_text.setTag(position);

            if(state){
                layout_holder.checkBox.setChecked(true);
                layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                layout_holder.checkBox.setChecked(false);
                layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            

            TextWatcherModified modified=new TextWatcherModified(layout_holder,position) {
                @Override
                public void afterTextChanged(Editable editable) {
                    int i = editable.toString().indexOf("\n");
                    if ( i != -1 ) {
                        if (editable.toString().endsWith("\n")&&editable.toString().length()>=1) {
                            System.out.println("Adding checkbox at position "+this.getPosition()+ new Exception().getStackTrace()[0]);
                            editable.delete(editable.length() - 1, editable.length());
                            addList((Integer)this.get_layout_holder().edit_text.getTag()+1);
                            position_holder=(Integer)this.get_layout_holder().edit_text.getTag()+1;
                            listview_change=true;
                            System.out.println("Int holder for view to be focused is "+position_holder);
                            notifyDataSetChanged();
                        } else {
                            editable.replace(i, i+1, "");
                        }
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    System.out.println("Integers are "+i+" "+i1+" "+i2+" ");
                    if(i==0 && i1>0 && i2==0 && data_lists.size()>1 && !this.get_layout_holder().checkBox.isFocused()) {
                        System.out.println("Value of data list,request thread array and save threads in delete function is "+data_lists.size()+" "+requestfocus_thread.size()+" "+save_threads.size()+ new Exception().getStackTrace()[0]);
                        System.out.println("Position to be deleted is "+this.getPosition()+ new Exception().getStackTrace()[0]);
                        deleteList((Integer)this.get_layout_holder().edit_text.getTag());
                        position_holder=(Integer)this.get_layout_holder().edit_text.getTag()-1;
                        System.out.println("Position to be run is "+(this.getPosition()-1)+ new Exception().getStackTrace()[0]);
                        listview_change=true;
                        notifyDataSetChanged();
                    }
                }};
            save_threads.add(new Thread(()->{
                CheckBoxListSingle a=new CheckBoxListSingle(layout_holder.checkBox.isChecked(),layout_holder.edit_text.getText().toString());
                this.data_lists.set(position,a);
            }));
            try{
                layout_holder.edit_text.removeTextChangedListener(modified);
                layout_holder.edit_text.addTextChangedListener(modified);
            } catch (Exception e){
                e.printStackTrace();
                layout_holder.edit_text.addTextChangedListener(modified);
            }
            /*layout_holder.edit_text.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN && layout_holder.edit_text.toString().length()>0) {
                        // Un-comment if you wish to cancel the backspace:
                        addList();
                        notifyDataSetChanged();
                        return false;

                        // require some sort of code that manipulates the arraylist into executing something
                    } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode()==KeyEvent.KEYCODE_DEL && layout_holder.edit_text.toString().length()==0){
                        deleteList();
                        notifyDataSetChanged();
                        return false;
                    }
                    return true;
                }

            });
            //why not override form here
        }else{
            layout_holder=(ViewHolder)view.getTag();
        }*/

        }


        // if text view has text, edit text is hidden
        // edit text should not phase out if the user has not entered anything and tried to skip
        return view;
    }

    static class ViewHolder{
        //button,text
        protected CheckBox checkBox;
        protected TextInputLayout input_layout;
        protected TextInputEditText edit_text;
    }
}
