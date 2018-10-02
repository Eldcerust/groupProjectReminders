package teamx.group.reminderapp;

import android.view.View;
import android.widget.AdapterView;

public class OnLongClickMod implements AdapterView.OnItemLongClickListener {
    protected getListCustomListAdapter getterInteface;

    public OnLongClickMod(getListCustomListAdapter getListInterface){
        this.getterInteface=getListInterface;
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
