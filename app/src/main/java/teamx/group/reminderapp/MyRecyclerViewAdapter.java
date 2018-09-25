package teamx.group.reminderapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements addListInterface,deleteListInterface,setListItem,requestState {
    private ArrayList<CheckBoxListSingle> data_lists;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    protected RecyclerView mRecyclerView;
    private volatile boolean status_add=false;
    private volatile boolean save_state=false;


    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<CheckBoxListSingle> data, RecyclerView recycler_view) {
        this.mInflater = LayoutInflater.from(context);
        this.data_lists=data;
        this.mRecyclerView=recycler_view;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_checkboxes, parent,false);

        return new ViewHolder(view);
    }

    public void setStatus_add(boolean status_add) {
        this.status_add = status_add;
    }

    public void setSaveState(Boolean state){
        this.save_state=state;
    }

    public Boolean getSaveState(){
        return this.save_state;
    }

    public ArrayList<CheckBoxListSingle> getDataLists(){
        try {
            mRecyclerView.getFocusedChild().clearFocus();
        } catch (NullPointerException e){
            Log.i("No focus on child","Skip");
        }
        return this.data_lists;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*String animal = mData.get(position);
        holder.myTextView.setText(animal);*/
        CheckBoxListSingle check_box= getList().get(position);
        String checkboxTitle=check_box.get_name();
        Boolean checkBoxListState=check_box.get_state();
        holder.editText.setText(checkboxTitle);
        holder.editText.setTag(position);
        holder.checkBox.setChecked(checkBoxListState);
        holder.textWatcherRecycler=new TextWatcherRecycler(this::addCheckbox,this::deleteList){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start==0 && before>0 &&count==0 &&getItemCount()>1&&!requestState()){
                    this.deleting_interface.deleteList((int)holder.editText.getTag(),holder.editText,this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.toString().indexOf("\n");
                if(i!=-1){
                    if(s.toString().endsWith("\n") && s.toString().length()>=1 && !requestState()){
                        s.delete(s.length()-1,s.length());
                        CheckBoxListSingle a =new CheckBoxListSingle(holder.checkBox.isChecked(),s.toString());
                        this.adding_interface.addCheckbox(new CheckBoxListSingle(holder.checkBox.isChecked(),s.toString()),(int)holder.editText.getTag()+1,holder.editText,this);
                        //holder.layoutView.getEditText().removeTextChangedListener(this);
                    } else {
                        s.replace(i+1,i,"");
                    }
                }
            }
        };
        holder.editText.addTextChangedListener(holder.textWatcherRecycler);
        holder.onFocusChangeListener=new OnFocusMod(this::setListItem,this::requestState) {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (!requestState()) {
                        CheckBoxListSingle a = new CheckBoxListSingle(holder.checkBox.isChecked(), holder.editText.getText().toString());
                        this.getInterface_this().setListItem(a, (int) holder.editText.getTag());
                    }
                }
            }
        };
        holder.editText.setFocusableInTouchMode(true);
        holder.editText.setOnFocusChangeListener(holder.onFocusChangeListener);
        holder.editText.requestFocus();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return (null != data_lists ? data_lists.size() : 0);
    }

    @Override
    public void addCheckbox(CheckBoxListSingle check_box,int position, TextInputEditText editText,TextWatcherRecycler recycler) {
        ArrayList<CheckBoxListSingle> temp=this.getList();
        temp.set(position-1,check_box);
        temp.add(position,new CheckBoxListSingle(false,""));
        this.setList(temp);
        if(!requestState()){
            setStatus_add(true);
        }

        mRecyclerView.post(new Runnable() {
            @Override public void run() {
                notifyItemInserted(position);
                notifyItemRangeChanged(position,temp.size());
                mRecyclerView.getViewTreeObserver()
                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                setStatus_add(false);
                                mRecyclerView.getChildAt(position).findViewById(R.id.customEditText).requestFocus();
                                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
            }
        });

    }

    @Override
    public void deleteList(int position, TextInputEditText editText,TextWatcherRecycler recycler) {
        if(position!=-1&&position<getItemCount()) {
        ArrayList<CheckBoxListSingle> temp=this.getList();
        int count=Integer.class.cast(temp.size());
        if(!requestState()){
            setStatus_add(true);}
        mRecyclerView.post(new Runnable() {
            @Override public void run() {
                notifyItemRangeChanged(position,temp.size());
                mRecyclerView.getViewTreeObserver()
                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                setStatus_add(false);
                                mRecyclerView.getChildAt(position-1).findViewById(R.id.customEditText).requestFocus();
                                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
            }
        });
        temp.remove(position);
        this.setList(temp);
        }
}

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void setListItem(CheckBoxListSingle a, int b) {
        this.data_lists.set(b,a);
    }

    @Override
    public synchronized boolean requestState() {

        return this.status_add;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextInputLayout layoutView;
        private CheckBox checkBox;
        private TextInputEditText editText;
        private TextWatcherRecycler textWatcherRecycler;
        private OnFocusMod onFocusChangeListener;

        ViewHolder(View itemView) {
            super(itemView);
            this.layoutView=itemView.findViewById(R.id.textInputLayoutInsideView);
            this.editText=itemView.findViewById(R.id.customEditText);
            this.checkBox=itemView.findViewById(R.id.checkBoxOfView);
            /*this.textWatcherRecycler=modified_twatch;
            this.layoutView.getEditText().addTextChangedListener(this.textWatcherRecycler);
            layoutView.getEditText().addTextChangedListener(new TextWatcherRecycler(MyRecyclerViewAdapter.this::addCheckbox,MyRecyclerViewAdapter.this::deleteList){

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(start==0&&before>0&&count==0 & getItemCount()>1){
                        this.deleting_interface.deleteList(this.position);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int i = s.toString().indexOf("\n");
                    if(i!=-1){
                        if(s.toString().endsWith("\n") && s.toString().length()>=1){
                            s.delete(s.length()-1,s.length());
                            this.adding_interface.addCheckbox(new CheckBoxListSingle(false,""),this.position);
                        }
                    }
                }
            });*/
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    CheckBoxListSingle getItem(int id) {
        return data_lists.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface transfer_position{
        public void transfer_position(int position);
        void transfer_checkbox(CheckBoxListSingle check_box);
    }

    public void setList(ArrayList<CheckBoxListSingle> checkBoxes){
        this.data_lists=checkBoxes;
    }

    public ArrayList<CheckBoxListSingle> getList(){
        return this.data_lists;
    }

    public void setListPositionWise(CheckBoxListSingle a,Integer transfer_position){
        this.data_lists.set(transfer_position,a);
    }


}
