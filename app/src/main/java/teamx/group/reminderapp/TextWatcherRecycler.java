package teamx.group.reminderapp;

import android.text.Editable;
import android.text.TextWatcher;

public class TextWatcherRecycler implements TextWatcher,MyRecyclerViewAdapter.transfer_position {
    protected CheckBoxListSingle check_box;
    protected addListInterface adding_interface;
    protected deleteListInterface deleting_interface;
    protected int position;

    public TextWatcherRecycler(addListInterface b,deleteListInterface c){
        this.adding_interface=b;
        this.deleting_interface=c;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void transfer_position(int position) {
        this.position=position;
    }

    @Override
    public void transfer_checkbox(CheckBoxListSingle check_box) {
        this.check_box=check_box;
    }
}
