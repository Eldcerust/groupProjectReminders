package teamx.group.reminderapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements addListInterface,deleteListInterface {
    private ArrayList<CheckBoxListSingle> data_lists;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private RecyclerView mRecyclerView;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<CheckBoxListSingle> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data_lists=data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row_checkboxes, parent, false);
        return new ViewHolder(view,new TextWatcherRecycler(this::addCheckbox,this::deleteList){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start==0 && before>0 &&count==0 &&getItemCount()>1){
                    this.deleting_interface.deleteList(this.position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.toString().indexOf("\n");
                if(i!=-1){
                    if(s.toString().endsWith("\n") && s.toString().length()>=1){
                        s.delete(s.length()-1,s.length());
                        this.adding_interface.addCheckbox(new CheckBoxListSingle(false,""),this.position+1);
                    } else {
                        s.replace(i+1,i,"");
                }
                }
            }
        });
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*String animal = mData.get(position);
        holder.myTextView.setText(animal);*/
        CheckBoxListSingle check_box= data_lists.get(position);
        String checkboxTitle=check_box.get_name();
        Boolean checkBoxListState=check_box.get_state();
        holder.layoutView.getEditText().setText(checkboxTitle);
        holder.checkBox.setChecked(checkBoxListState);
        holder.textWatcherRecycler.transfer_position(position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data_lists.size();
    }

    @Override
    public void addCheckbox(CheckBoxListSingle check_box,int position) {
        data_lists.add(position,new CheckBoxListSingle(false,""));
        mRecyclerView.post(new Runnable() {
            @Override public void run() {
                notifyItemChanged(position-1);
            }
        });
    }

    @Override
    public void deleteList(int position) {
        data_lists.remove(position);
        mRecyclerView.post(new Runnable() {
            @Override public void run() {
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextInputLayout layoutView;
        protected CheckBox checkBox;
        protected TextWatcherRecycler textWatcherRecycler;


        ViewHolder(View itemView,TextWatcherRecycler modified_twatch) {
            super(itemView);
            this.layoutView=itemView.findViewById(R.id.textInputLayoutInsideView);
            this.checkBox=itemView.findViewById(R.id.checkBoxOfView);
            this.textWatcherRecycler=modified_twatch;
            this.layoutView.getEditText().addTextChangedListener(this.textWatcherRecycler);
            /*layoutView.getEditText().addTextChangedListener(new TextWatcherRecycler(MyRecyclerViewAdapter.this::addCheckbox,MyRecyclerViewAdapter.this::deleteList){

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
}
