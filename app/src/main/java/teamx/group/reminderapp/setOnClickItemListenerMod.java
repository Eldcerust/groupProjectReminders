package teamx.group.reminderapp;

import android.view.View;
import android.widget.AdapterView;

public class setOnClickItemListenerMod implements AdapterView.OnItemClickListener{
    protected getListCustomListAdapter getterInteface;

    public setOnClickItemListenerMod(getListCustomListAdapter getListInterface){
        this.getterInteface=getListInterface;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
