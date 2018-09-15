package teamx.group.reminderapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
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

import java.util.ArrayList;

public class CustomListCheckBoxesListAdapter extends BaseAdapter{
    private ArrayList<CheckBoxListSingle> data_lists;
    private LayoutInflater layout_inflater;
    private Context context_from_main;

    public CustomListCheckBoxesListAdapter(Context context_main,ArrayList<CheckBoxListSingle> list_of_checkboxes){
        this.data_lists=list_of_checkboxes;
        this.layout_inflater=LayoutInflater.from(context_main);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        final ViewHolder layout_holder;
        if(view==null) {
            view = this.layout_inflater.inflate(R.layout.list_row_checkboxes, null);
            layout_holder = new ViewHolder();
            layout_holder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxOfView);
            //null???
            layout_holder.input_layout = (TextInputLayout) view.findViewById(R.id.textInputLayoutInsideView);
            layout_holder.edit_text = (TextInputEditText) view.findViewById(R.id.customEditText);

            layout_holder.edit_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(i2==0){
                        deleteList();
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().endsWith("\n")) {
                        editable.delete(editable.length()-1,editable.length());
                        addList();
                        addList();
                        notifyDataSetChanged();
                    }
                }
            });
            layout_holder.edit_text.setFocusableInTouchMode(true);
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
