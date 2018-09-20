package teamx.group.reminderapp;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

public abstract class TextWatcherModified implements TextWatcher,CustomListCheckBoxesListAdapter.transfer_position{

    private ViewGroup layout_holder;
    private Integer position;
    private Integer position_holder;

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void afterTextChanged(Editable editable)
    {

    }

    @Override
    public void transfer_position(int position) {
        this.position=position;
    }

    @Override
    public void transfer_layoutholder(ViewGroup view_hold) {
        this.layout_holder=view_hold;
    }

    @Override
    public void transfer_static_position(int position) {
        this.position_holder=position;
    }

    public Integer get_position_holder() {
        return this.position_holder;
    }

    public ViewGroup getLayout_holder() {
        return this.layout_holder;
    }
}
