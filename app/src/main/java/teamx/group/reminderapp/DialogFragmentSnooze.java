package teamx.group.reminderapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogFragmentSnooze extends DialogFragment {
    protected ListDialogSnooze.OnDialogDismissSnoozeListener callingBack;

    public void setCallingBack(ListDialogSnooze.OnDialogDismissSnoozeListener callingBack) {
        this.callingBack = callingBack;
    }

    public DialogFragmentSnooze() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,
                                 android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        return dialog;
    }


}
