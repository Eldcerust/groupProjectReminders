package teamx.group.reminderapp;

import android.view.View;
import android.view.ViewGroup;

public class ViewOnFocusChangeListenerMod implements CustomListCheckBoxesListAdapter.transfer_position, View.OnFocusChangeListener {
    private int position;
    private CustomListCheckBoxesListAdapter.ViewHolder layout_holder;
    private int position_holder;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void transfer_position(int position) {
        this.position=position;
    }

    @Override
    public void transfer_layoutholder(ViewGroup view_hold) {

    }

    @Override
    public void transfer_static_position(int position) {
        this.position_holder=position;
    }

    public int getPosition() {
        return this.position;
    }

    public int get_position_holder(){
        return this.position_holder;
    }

    public CustomListCheckBoxesListAdapter.ViewHolder getLayout_holder() {
        return this.layout_holder;
    }
}
