package teamx.group.reminderapp;

import android.view.View;

public class OnFocusMod implements View.OnFocusChangeListener {
    private setListItem interface_this;
    private requestState boolean_state;
    private boolean boolean_initialized=false;

    public OnFocusMod(setListItem interface_add,requestState boolean_req){
        this.interface_this=interface_add;
        this.boolean_state=boolean_req;
    }

    public setListItem getInterface_this() {
        return interface_this;
    }

    public boolean isBoolean_initialized() {
        return boolean_initialized;
    }

    public void setBoolean_state(Boolean a){
        this.boolean_initialized=a;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    public requestState getBoolean_state() {
        return boolean_state;
    }
}
