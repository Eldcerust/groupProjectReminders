package teamx.group.reminderapp;

        import android.content.Context;
        import android.database.DataSetObserver;
        import android.graphics.Paint;
        import android.support.design.widget.TextInputEditText;
        import android.support.design.widget.TextInputLayout;
        import android.text.Editable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.ViewParent;
        import android.widget.BaseAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;

        import org.w3c.dom.Text;

        import java.util.ArrayList;

public class CustomListCheckBoxesListAdapter extends BaseAdapter{
    private ArrayList<CheckBoxListSingle> data_lists;
    private LayoutInflater layout_inflater;
    private ArrayList<Thread> save_threads=new ArrayList<Thread>();
    private int position_holder;
    private Context context_from_main;
    public static ViewGroup view_group_holder;

    public CustomListCheckBoxesListAdapter(Context context_main,ArrayList<CheckBoxListSingle> list_of_checkboxes){
        this.data_lists=list_of_checkboxes;
        this.layout_inflater=LayoutInflater.from(context_main);
        notifyDataSetChanged();
    }

    public void save_checkbox(){
        for(int a=0;a<save_threads.size();a++){
            save_threads.get(a).run();
        }
    }

    public int getPosition_holder() {
        return position_holder;
    }

    public void setPosition_holder(int position_holder) {
        this.position_holder = position_holder;
    }

    @Override
    public int getCount() {
        return data_lists.size();
    }

    public void addList(int position,ViewHolder layout_holder){
        /*if(position==getCount()-1 && position!=0) {
            System.out.println("Function is first addition:"+String.valueOf(getCount()-1)+" "+ String.valueOf(position)+ new Exception().getStackTrace()[0]);
            this.data_lists.add(new CheckBoxListSingle(false, ""));
        } else if(position!=0 && position!=getCount()-1) {
            System.out.println("Function is second addition:" + String.valueOf(getCount() - 1) + " " + String.valueOf(position) + new Exception().getStackTrace()[0]);
            this.data_lists.add(position, new CheckBoxListSingle(false, ""));
        } else if(position==0){
            System.out.println("Function is going through initial bool:" + String.valueOf(getCount() - 1) + " " + String.valueOf(position) + new Exception().getStackTrace()[0]);
            if(getCount()==1) {
                this.data_lists.add(new CheckBoxListSingle(false, ""));
            } else {
                this.data_lists.add(1,new CheckBoxListSingle(false,""));
            }
        }*/
        System.out.println("Adding");
        System.out.println(position+" "+getCount()+" "+this.save_threads.size());
        this.data_lists.add(position,new CheckBoxListSingle(false,""));

        try {
            this.save_threads.add(position,new Thread(()->{
                CheckBoxListSingle a=new CheckBoxListSingle(layout_holder.checkBox.isChecked(),layout_holder.edit_text.getText().toString());
                this.data_lists.set(position,a);
            }));
        }catch(Exception e){
            System.out.println("Error is on thread addition,"+e.toString());
        }
        System.out.println(position+" "+getCount()+" "+this.save_threads.size());
    }



    public void deleteList(int position){
        System.out.println("Deleting");
        System.out.println(position+" "+getCount()+" "+this.save_threads.size());
        try {
            this.save_threads.remove(position);
        }catch(Exception e){
            System.out.println("Error is on thread deletion,"+e.toString());
        }
        this.data_lists.remove(position);
        System.out.println(position+" "+getCount()+" "+this.save_threads.size());
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
    public View getView(int position, View view, ViewGroup list) {
        final ViewHolder layout_holder;
        System.out.println("Position of view just created "+position+ new Exception().getStackTrace()[0]);
        if(view==null) {
            view = this.layout_inflater.inflate(R.layout.list_row_checkboxes, null);
            layout_holder = new ViewHolder();

            view.setTag(layout_holder);

            //null???
        }else{
            layout_holder=(ViewHolder)view.getTag();
        }

        layout_holder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxOfView);
        boolean state=this.data_lists.get(position).get_state();
        layout_holder.checkBox.setChecked(state);
        layout_holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });
        layout_holder.input_layout = (TextInputLayout) view.findViewById(R.id.textInputLayoutInsideView);
        layout_holder.edit_text = (TextInputEditText) view.findViewById(R.id.customEditText);

        String list_text=this.data_lists.get(position).get_name();
        layout_holder.edit_text.setText(list_text);

        layout_holder.edit_text.setText(this.data_lists.get(position).get_name());
        layout_holder.edit_text.setFocusableInTouchMode(true);
        layout_holder.checkBox.setFocusableInTouchMode(true);

        layout_holder.checkBox.setFocusedByDefault(true);
        layout_holder.checkBox.requestFocus();
        layout_holder.edit_text.requestFocus();
        layout_holder.edit_text.setTag(position);

        if(state){
            layout_holder.checkBox.setChecked(true);
            layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            layout_holder.checkBox.setChecked(false);
            layout_holder.edit_text.setPaintFlags(layout_holder.edit_text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        view_group_holder=list;
            /*ViewOnFocusChangeListenerMod onFocusModded=new ViewOnFocusChangeListenerMod(){
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!getLayout_holder().edit_text.hasFocus() && this.getPosition()==this.get_position_holder()){
                        System.out.println("onFocusChange at "+String.valueOf(!getLayout_holder().edit_text.hasFocus())+" , position and positionholder at "+this.getPosition()+" "+getPosition_holder());
                        getLayout_holder().edit_text.requestFocus();
                    }else if(getLayout_holder().edit_text.hasFocus() && this.getPosition()==this.get_position_holder()){

                    }
                    this.transfer_position((Integer)v.getTag());
                    this.transfer_static_position(getPosition_holder());
                }
            };
            onFocusModded.transfer_position(position);
            onFocusModded.transfer_layoutholder(layout_holder);
            onFocusModded.transfer_static_position(this.getPosition_holder());
            layout_holder.edit_text.setOnFocusChangeListener(onFocusModded);*/
        TextWatcherModified modified=new TextWatcherModified() {
            @Override public void afterTextChanged(Editable editable) {
                int i = editable.toString().indexOf("\n");
                if ( i != -1 ) {
                    if (editable.toString().endsWith("\n")&&editable.toString().length()>=1) {
                            /*System.out.println("Adding checkbox at position "+this.getPosition()+ new Exception().getStackTrace()[0]);
                            editable.delete(editable.length() - 1, editable.length());
                            addList(this.getPosition()+1);
                            this.setPosition(this.getPosition()+1);
                            setPosition_holder(this.getPosition()+1);
                            this.transfer_layoutholder(layout_holder);
                            this.transfer_static_position(getPosition_holder());
                            System.out.println("Int holder for view to be focused is "+getPosition_holder());*/


                        /*editable.delete(editable.length() - 1, editable.length());
                        System.out.println("Before adding, value is "+this.getLayout_holder().getChildCount()+" "+getCount());
                        addList(position);
                        this.transfer_layoutholder(list);
                        notifyDataSetChanged();
                        System.out.println("After adding, value is "+this.getLayout_holder().getChildCount()+" "+getCount());
                        request_focus(this.getLayout_holder(),this.getPosition()+1);
                        this.setPosition(position+1);*/
                        editable.delete(editable.length() - 1, editable.length());
                        addList(this.getPosition(),layout_holder);
                        notifyDataSetChanged();
                        layout_holder.edit_text.removeTextChangedListener(this);
                    } else {
                        editable.replace(i, i+1, "");
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("Integers are "+i+" "+i1+" "+i2+" ");
                System.out.println("Boolean stat isFocusedCheckox and editTextIsfocused is"+layout_holder.edit_text.isFocused()+" "+layout_holder.checkBox.isFocused());
                if(i==0 & i1>0 & i2==0 & data_lists.size()>1) {
                        /*System.out.println("Value of data list,request thread array and save threads in delete function is "+data_lists.size()+" "+requestfocus_thread.size()+" "+save_threads.size()+ new Exception().getStackTrace()[0]);
                        System.out.println("Position to be deleted is "+this.getPosition()+ new Exception().getStackTrace()[0]);
                        deleteList(this.getPosition());
                        System.out.println("Position to be run is "+(this.getPosition()-1)+ new Exception().getStackTrace()[0]);
                        this.setPosition(this.getPosition()-1);
                        setPosition_holder(this.getPosition()-1);
                        notifyDataSetChanged();
                        this.transfer_layoutholder(layout_holder);
                        this.transfer_static_position(getPosition_holder());
                        deleteList(position);
                        System.out.println("Before deleting, value is "+this.getLayout_holder().getChildCount()+" "+getCount());
                    ViewParent group=this.getLayout_holder().getParent();
                    notifyDataSetChanged();
                    System.out.println("After deleting, value is "+this.getLayout_holder()+" "+getCount());
                    request_focus(this.getLayout_holder(),this.getPosition()-1);*/

                    System.out.println(this.getPosition()+" "+getCount()+" "+save_threads.size());
                    deleteList(this.getPosition());
                    this.transfer_position(this.getPosition()-1);
                    layout_holder.edit_text.removeTextChangedListener(this);
                    this.transfer_layoutholder(request_focus(this.getLayout_holder(),this.getPosition()));
                } else if(i>0 & i1>0 & i2==0 & data_lists.size()>1 & layout_holder.checkBox.isFocused()) {
                    layout_holder.edit_text.requestFocus();
                    System.out.println("Edit text requested focus");
            }
            }
        };
        modified.transfer_position(position);
        modified.transfer_layoutholder(list);

        layout_holder.edit_text.setTag(modified);
        layout_holder.checkBox.setTag(list);
        layout_holder.edit_text.addTextChangedListener(modified);
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

        // if text view has text, edit text is hidden
        // edit text should not phase out if the user has not entered anything and tried to skip
        if(this.data_lists.size()==1&&this.save_threads.size()==0){
            this.save_threads.add(new Thread(()->{
                CheckBoxListSingle a=new CheckBoxListSingle(layout_holder.checkBox.isChecked(),layout_holder.edit_text.getText().toString());
                this.data_lists.set(position,a);
            }));
        }
        return view;
    }

    public ViewGroup request_focus(ViewGroup vgroup, int child_position){
        View v=vgroup.getChildAt(child_position);
        TextInputEditText edit=(TextInputEditText)v.findViewById(R.id.customEditText);
        ViewGroup second_view=(ViewGroup)v.findViewById(R.id.checkBoxOfView).getTag();
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        TextWatcherModified text_watcher=(TextWatcherModified)edit.getTag();
        text_watcher.transfer_position(child_position);
        edit.addTextChangedListener(text_watcher);
        return second_view;
    }

    static class ViewHolder{
        //button,text
        protected CheckBox checkBox;
        protected TextInputLayout input_layout;
        protected TextInputEditText edit_text;
    }

    public interface transfer_position{
        public void transfer_position(int position);
        public void transfer_layoutholder(ViewGroup view_hold);
        public void transfer_static_position(int position);
    }
}
