package teamx.group.reminderapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class newBasicReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener,ListDialogFragment.OnDialogDismissListener{
    private TextView date_button,time_button;
    private TextInputLayout inserted_title;
    private Button create_button,edit_button,delete_button;
    private RemindersModel create_or_modify_reminder;
    private Calendar current_date;
    private int edit_int;
    private String[] date_array;
    public ListView list_view_basicreminders;

    public RemindersModel get_create_or_modify_reminder() {return create_or_modify_reminder;}

    public Intent create_finished_intent(String key, String[] data){
        Intent intent_finished=new Intent();
        intent_finished.putExtra(key,data);
        setResult(Activity.RESULT_OK,intent_finished);
        return intent_finished;
    }

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
        list_view_basicreminders=(ListView)findViewById(R.id.checkboxListView);

        setup_checkboxes();
    }

    public void setup_checkboxes(){
        final CustomListCheckBoxesListAdapter list_adapter=new CustomListCheckBoxesListAdapter(this,this.create_or_modify_reminder.get_checkbox_list());
        list_view_basicreminders.setAdapter(list_adapter);

    }

    public void check_edit_status(int edit_int){
        if(edit_int==Integer.MAX_VALUE){
            // do something here to fetch the reminder specified at position
            // also modify the visibility of the buttons
            this.create_button.setVisibility(View.VISIBLE);
            this.edit_button.setVisibility(View.INVISIBLE);
            this.delete_button.setVisibility(View.INVISIBLE);

            ArrayList<CheckBoxListSingle> checkbox_initialize=new ArrayList<CheckBoxListSingle>();
            checkbox_initialize.add(new CheckBoxListSingle(false,""));

            this.create_or_modify_reminder=new RemindersModel("",Calendar.getInstance(),checkbox_initialize);

            this.current_date=create_or_modify_reminder.get_reminder_date_time();
            this.set_calendar_text(this.current_date);

            //the previous implentation I did was to use some kind of sql function to zero in what the function is about
            //interface to transfer the reminder object in?
        } else {
            this.create_button.setVisibility(View.INVISIBLE);
            this.edit_button.setVisibility(View.VISIBLE);
            this.delete_button.setVisibility(View.VISIBLE);

            this.create_or_modify_reminder=MainActivity.reminder_transmission_holder.reminder_model;
            this.edit_int=MainActivity.reminder_transmission_holder.position_reminder;
            this.current_date=this.create_or_modify_reminder.get_reminder_date_time();
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
        if(date_ten/10>=1){
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
        finish();
    }

    public void create_button_onclick(View v){
        String title_determined=this.inserted_title.getEditText().getText().toString();
        this.create_or_modify_reminder.set_reminder_name(title_determined);
        this.create_or_modify_reminder.set_reminder_date_time(this.current_date);
        String[] commandArray=new String[2];
        commandArray[0]="FetchReminderModel";
        return_to_main();
        Intent create_button_intent=create_finished_intent("State",commandArray);
        finish();
        //use intent sent back to signal the fetch of data
    }

    public void delete_button_onclick(View v){
        // require remindermodel to be known at what position
        // require access to the reminder position
        String[] commandArray=new String[2];
        commandArray[0]="DeletThis";
        commandArray[1]=String.valueOf(this.edit_int);
        Intent delete_button_intent=create_finished_intent("State",commandArray);
        finish();
    }

    public void edit_button_onclick(View v){
        String title_determined=this.inserted_title.getEditText().getText().toString();
        this.create_or_modify_reminder.set_reminder_name(title_determined);
        this.create_or_modify_reminder.set_reminder_date_time(this.current_date);
        String[] commandArray=new String[2];
        commandArray[0]="FindAndReplace";
        commandArray[1]=String.valueOf(this.edit_int);
        return_to_main();
        Intent edit_button_intent=create_finished_intent("State",commandArray);
        finish();
    }

    public void return_to_main(){
        MainActivity.reminder_transmission_holder=null;
        MainActivity.reminder_transmission_holder=new MainActivity.ReminderItemPositions();
        MainActivity.reminder_transmission_holder.reminder_model=this.create_or_modify_reminder;
        MainActivity.reminder_transmission_holder.position_reminder=this.edit_int;
    }
    // find a way to insert remindersmodel in and out without ridiculous strats
}
