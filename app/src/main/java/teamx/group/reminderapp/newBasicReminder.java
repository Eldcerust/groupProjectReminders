package teamx.group.reminderapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
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
    private RecyclerView r_view;
    private MyRecyclerViewAdapter r_adapter;
    private RecyclerView.LayoutManager r_layoutmanager;

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
        this.r_view=(RecyclerView)findViewById(R.id.checkboxListView);

        setup_checkboxes();
    }

    public void setup_checkboxes(){
        this.r_adapter=new MyRecyclerViewAdapter(this,this.create_or_modify_reminder.get_checkbox_list(),this.r_view);
        this.r_view.setAdapter(this.r_adapter);

        this.r_layoutmanager=new LinearLayoutManager(this);
        this.r_view.setLayoutManager(this.r_layoutmanager);


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

            this.create_or_modify_reminder=MainActivity.reminder_transmission_holder;
            this.edit_int=MainActivity.reminder_position;
            this.current_date=this.create_or_modify_reminder.get_reminder_date_time();
            this.inserted_title.getEditText().setText(this.create_or_modify_reminder.get_reminder_name());
        }
    }

    public String[] parse_calendar_to_stringarray(Calendar cal){
        String[] date_array=new String[2];

        SimpleDateFormat date_format=new SimpleDateFormat("EEE, dd MMM yyyy");
        SimpleDateFormat time_format=new SimpleDateFormat("HH:mm");

        date_array[0]=date_format.format(cal.getTime());
        date_array[1]=time_format.format(cal.getTime());

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

        this.date_button.setText(parsed_cal[0]);
        this.time_button.setText(parsed_cal[1]);
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
        this.current_date.set(Calendar.DAY_OF_MONTH,i2);
        this.current_date.set(Calendar.MONTH,i1);
        this.current_date.set(Calendar.YEAR,i);
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
        if(title_determined.length()>0){
            System.out.println(title_determined);
            this.create_or_modify_reminder.set_reminder_name(title_determined);
            this.create_or_modify_reminder.set_reminder_date_time(this.current_date);
            //this.list_adapter.save_checkbox();
            this.create_or_modify_reminder.set_list(this.r_adapter.getDataLists());
            String[] commandArray=new String[2];
            commandArray[0]="FetchReminderModel";
            return_to_main();
            Intent create_button_intent=create_finished_intent("State",commandArray);
            finish();
        } else {
            Toast.makeText(this.getApplicationContext(),"Reminder name should not be zero. Please try again.",Toast.LENGTH_LONG).show();
        }
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
        if(title_determined.length()>0) {
            this.create_or_modify_reminder.set_reminder_name(title_determined);
            this.create_or_modify_reminder.set_reminder_date_time(this.current_date);
            //this.list_adapter.save_checkbox();
            this.create_or_modify_reminder.set_list(this.r_adapter.getDataLists());
            String[] commandArray=new String[2];
            commandArray[0]="FindAndReplace";
            commandArray[1]=String.valueOf(this.edit_int);
            return_to_main();
            Intent edit_button_intent=create_finished_intent("State",commandArray);
            finish();
        } else {
            Toast.makeText(this.getApplicationContext(),"Reminder name should not be zero. Please try again.",Toast.LENGTH_LONG).show();
        }
    }

    public void return_to_main(){
        MainActivity.reminder_transmission_holder=null;
        MainActivity.reminder_transmission_holder=this.create_or_modify_reminder;
        MainActivity.reminder_position=this.edit_int;
    }
    // find a way to insert remindersmodel in and out without ridiculous strats
}
