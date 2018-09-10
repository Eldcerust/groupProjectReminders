package teamx.group.reminderapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;

public class newBasicReminder extends AppCompatActivity implements MainActivity.transferBasicReminders,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener,ListDialogFragment.OnDialogDismissListener{
    private TextView date_button,time_button;
    private TextInputLayout inserted_title;
    private Button create_button,edit_button,delete_button;
    private RemindersModel create_or_modify_reminder;
    private Calendar current_date;
    private int edit_int;
    private RemindersPresenter interact_reminder_edits;
    private String[] date_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_reminder);
        this.date_button=(TextView)findViewById(R.id.dateView);
        this.time_button=(TextView)findViewById(R.id.timeView);
        this.inserted_title=(TextInputLayout)findViewById(R.id.textInputLayout);
        this.create_button=(Button)findViewById(R.id.create_button);
        this.edit_button=(Button)findViewById(R.id.edit_button);
        this.delete_button=(Button)findViewById(R.id.delete_button);
        this.current_date=Calendar.getInstance();
        this.edit_int=getIntent().getIntExtra("EDITMODE",-1);

        this.check_edit_status(edit_int);
    }

    public void check_edit_status(int edit_int){
        if(edit_int==Integer.MAX_VALUE){
            // do something here to fetch the reminder specified at position
            // also modify the visibility of the buttons
            this.create_button.setVisibility(View.INVISIBLE);
            this.edit_button.setVisibility(View.VISIBLE);
            this.delete_button.setVisibility(View.VISIBLE);
            this.current_date=create_or_modify_reminder.get_reminder_date_time();
            this.set_calendar_text(this.current_date);

            //the previous implentation I did was to use some kind of sql function to zero in what the function is about
            //interface to transfer the reminder object in?
        } else {
            this.create_button.setVisibility(View.VISIBLE);
            this.edit_button.setVisibility(View.INVISIBLE);
            this.set_calendar_text(this.current_date);
        }
    }

    public String[] parse_calendar_to_stringarray(Calendar cal){
        String[] date_array=new String[5];

        date_array[0]=parse_zeroes_date(cal.get(Calendar.DAY_OF_MONTH));
        date_array[1]=parse_zeroes_date(cal.get(Calendar.MONTH));
        date_array[2]=parse_zeroes_date(cal.get(Calendar.YEAR));
        date_array[3]=parse_zeroes_date(cal.get(Calendar.HOUR_OF_DAY));
        date_array[4]=parse_zeroes_date(cal.get(Calendar.MINUTE));

        return date_array;
    }

    public String parse_zeroes_date(int date_ten){
        if(date_ten/10>1){
            return String.valueOf(date_ten);
        }else{
            return String.valueOf(0)+date_ten;
        }
    }

    public void set_calendar_text(Calendar cal){
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        String[] parsed_cal=parse_calendar_to_stringarray(cal);

        this.date_button.setText(parsed_cal[0]+"/"+parsed_cal[1]+"/"+parsed_cal[2]);
        this.time_button.setText(parsed_cal[3]+":"+parsed_cal[4]);
    }

    public void show_time_picker(View v){
        DialogFragment new_fragment=new MyTimePickerFragment();
        new_fragment.show(getSupportFragmentManager(),"time picker");
    }

    public void show_date_picker(View v){
        DialogFragment new_fragment=new MyDatePickerFragment();
        new_fragment.show(getSupportFragmentManager(),"date picker");
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        //set the date of the string array and also the calendar?
        this.current_date.set(Calendar.DAY_OF_MONTH,i);
        this.current_date.set(Calendar.MONTH,i1);
        this.current_date.set(Calendar.YEAR,i2);
        set_calendar_text(this.current_date);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        //set the time of the string aray and also the calendar object
        this.current_date.set(Calendar.HOUR_OF_DAY,i);
        this.current_date.set(Calendar.MINUTE,i1);
        set_calendar_text(this.current_date);
    }

    @Override
    public void onDialogDismissListener(int position) {

    }
    // find a way to insert remindersmodel in and out without ridiculous strats
}
