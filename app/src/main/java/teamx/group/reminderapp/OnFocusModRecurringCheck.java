package teamx.group.reminderapp;

import android.widget.CompoundButton;

public class OnFocusModRecurringCheck implements CompoundButton.OnCheckedChangeListener{
    private OnCheckedAddDays addInterface;
    private getDays getInterface;
    private OnCheckedDeleteDays deleteInterface;

    public OnCheckedAddDays getAddInterface() {
        return addInterface;
    }

    public void setAddInterface(OnCheckedAddDays addInterface) {
        this.addInterface = addInterface;
    }


    public OnCheckedDeleteDays getDeleteInterface() {
        return deleteInterface;
    }

    public void setDeleteInterface(OnCheckedDeleteDays deleteInterface) {
        this.deleteInterface = deleteInterface;
    }


    public getDays getGetInterface() {
        return getInterface;
    }

    public void setGetInterface(getDays getInterface) {
        this.getInterface = getInterface;
    }

    public OnFocusModRecurringCheck(OnCheckedAddDays add, OnCheckedDeleteDays delete, getDays getter){
        this.addInterface=add;
        this.deleteInterface=delete;
        this.getInterface=getter;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
