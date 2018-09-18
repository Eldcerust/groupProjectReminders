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
    private Context context_from_main;
    private ArrayList<Thread> save_threads=new ArrayList<Thread>();


    public CustomListCheckBoxesListAdapter(Context context_main,ArrayList<CheckBoxListSingle> list_of_checkboxes){
        this.data_lists=list_of_checkboxes;
        this.layout_inflater=LayoutInflater.from(context_main);
    }

    public void save_checkbox(){
        for(int a=0;a<save_threads.size();a++){
            save_threads.get(a).run();
        }
    }

    public void set_context(Context external){
        this.context_from_main=external;
    }

    @Override
    public int getCount() {
        return data_lists.size();
    }

    public void addList(){
        this.data_lists.add(new CheckBoxListSingle(false,""));
    }

    public void deleteList(){
        this.data_lists.remove(this.data_lists.size()-1);
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
    public View getView(int i, View view, ViewGroup viewGroup) {


        final ViewHolder layout_holder;
        if(view==null) {
            view = this.layout_inflater.inflate(R.layout.list_row_checkboxes, null);
            layout_holder = new ViewHolder();
            layout_holder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxOfView);
            boolean state=this.data_lists.get(i).get_state();
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
            String list_text=this.data_lists.get(i).get_name();
            layout_holder.edit_text.setText(list_text);
            layout_holder.edit_text.addTextChangedListener(new TextWatcherModified() {
                @Override
                public void afterTextChanged(Editable editable, boolean backSpace) {
                    int i = editable.toString().indexOf("\n");
                    if ( i != -1 ) {
                        if (editable.toString().endsWith("\n")) {
                            editable.delete(editable.length() - 1, editable.length());
                            addList();
                            addList();
                            notifyDataSetChanged();
                            layout_holder.edit_text.requestFocus(View.FOCUS_DOWN);
                            Log.i("Number of checkbox items is",String.valueOf(data_lists.size()));
                        } else {
                            editable.replace(i, i+1, "");
                            addList();
                            notifyDataSetChanged();
                            layout_holder.edit_text.requestFocus(R.id.customEditText);
                        }
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(i2==0 && data_lists.size()>1){
                        deleteList();
                        notifyDataSetChanged();
                    }
                }



                });
            layout_holder.edit_text.setText(this.data_lists.get(i).get_name());
            layout_holder.edit_text.setFocusableInTouchMode(true);

            if(state){
                layout_holder.checkBox.setChecked(true);
                layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                layout_holder.checkBox.setChecked(false);
                layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }
            save_threads.add(new Thread(()->{
                CheckBoxListSingle a=this.data_lists.get(i);
                a.set_name(layout_holder.edit_text.getText().toString());
                a.set_state(layout_holder.checkBox.isChecked());
                this.data_lists.set(i,a);
            }));
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
        CheckBox checkBox;
        TextInputLayout input_layout;
        TextInputEditText edit_text;
    }
}
