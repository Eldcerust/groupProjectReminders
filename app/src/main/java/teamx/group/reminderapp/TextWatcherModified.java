package teamx.group.reminderapp;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;

import java.util.ArrayList;

public class TextWatcherModified implements TextWatcher {

    private CustomListCheckBoxesListAdapter.ViewHolder layout_holder;
    private Integer position;

    public TextWatcherModified(CustomListCheckBoxesListAdapter.ViewHolder item, int position){
        super();
        this.layout_holder=item;
        this.position=position;
    }

    public CustomListCheckBoxesListAdapter.ViewHolder get_layout_holder() {
        return this.layout_holder;
    }

    public void set_layout_holder(CustomListCheckBoxesListAdapter.ViewHolder item){
        this.layout_holder=item;
    }

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
}
