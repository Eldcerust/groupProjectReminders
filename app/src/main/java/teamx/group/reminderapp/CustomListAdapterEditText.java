package teamx.group.reminderapp;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

public class CustomListAdapterEditText extends android.support.design.widget.TextInputEditText {

    public CustomListAdapterEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomListAdapterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListAdapterEditText(Context context) {
        super(context);
    }

    public void doSomethingOnInherition(){}

    public void deleteAction(){}

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType= InputType.TYPE_NULL;
        return new CustomListAdapterInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class CustomListAdapterInputConnection extends InputConnectionWrapper {

        public CustomListAdapterInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                // Un-comment if you wish to cancel the backspace:
                doSomethingOnInherition();
                return false;

                // require some sort of code that manipulates the arraylist into executing something
            } else if (event.getAction()==KeyEvent.ACTION_DOWN && event.getKeyCode()==KeyEvent.KEYCODE_DEL&&length()==0){
                deleteAction();
                return false;
            }
            return super.sendKeyEvent(event);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (beforeLength == 1 && afterLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }

    }

}
