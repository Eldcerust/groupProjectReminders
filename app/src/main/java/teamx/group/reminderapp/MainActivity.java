package teamx.group.reminderapp;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ReminderView,ListDialogFragment.OnDialogDismissListener{

    // this is unfortunately not the first activity, despite being the most important activity
    private VoiceProfilePresenter profiles_voice;
    private volatile RemindersPresenter reminders_present;
    private volatile RecurringReminderPresenter recurringReminderPresenter;
    private volatile TimeBoxedReminderPresenter timebox_presenter;
    private ListView list_view;
    private CustomListAdapter list_adapter;
    private AlarmManager alarm_mgr;
    private VoiceProfilePresenter voice_access;
    private newBasicReminder temporary_class_container_basic_reminder_creation;
    public static RemindersModel reminder_transmission_holder,reminder_transmission_holder_original;
    public static RecurringRemindersModel recurringRemindersModel_transmission_holder,recurringRemindersModel_transmission_holder_original;
    public static TimeBoxedReminderModel timeBoxed_transmission_holder,timeBoxed_transmission_holder_original;
    public static int reminder_position;
    public static int reminder_creation_type;
    public static ArrayList<RemindersModel> temp_lost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //not main activity, probably more a view or something
        // let's call it another presenter
        // that attaches presenters to views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.list_view=(ListView)findViewById(R.id.listView);

        fetch_data();
        set_list_on_display();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                DialogFragment reminderChoice=new ListDialogFragment();
                reminderChoice.show((getSupportFragmentManager()),"Reminder Picker");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public RemindersPresenter check_reminder_presenter_initial(RemindersPresenter reminders_present) {
        if(reminders_present.get_reminder_list().size()==1 && reminders_present.get_reminder_list().get(0).get_reminder_name().equals("")){
            reminders_present.delete_reminder_position(0);
            return reminders_present;
        } else {
            return reminders_present;
        }
    }

    public void setReminders_present(RemindersPresenter reminders_present) {
        this.reminders_present = reminders_present;
    }

    public void setRecurringReminderPresenter(RecurringReminderPresenter recurringReminderPresenter) {
        this.recurringReminderPresenter = recurringReminderPresenter;
    }

    public void setTimebox_presenter(TimeBoxedReminderPresenter timebox_presenter) {
        this.timebox_presenter = timebox_presenter;
    }

    public RecurringReminderPresenter check_reminder_presenter_initial(RecurringReminderPresenter reminders_present) {
        if(reminders_present.get_reminder_list().size()==1 && reminders_present.get_reminder_list().get(0).get_reminder_name().equals("")){
            reminders_present.delete_reminder_position(0);
            return reminders_present;
        } else {
            return reminders_present;
        }
    }

    public TimeBoxedReminderPresenter check_reminder_presenter_initial(TimeBoxedReminderPresenter reminders_present) {
        if(reminders_present.get_reminder_list().size()==1 && reminders_present.get_reminder_list().get(0).get_reminder_name().equals("")){
            reminders_present.delete_reminder_position(0);
            return reminders_present;
        } else {
            return reminders_present;
        }
    }

    public void fetch_data(){
        this.reminders_present=new RemindersPresenter(this.getApplicationContext(),profiles_voice);
        this.reminders_present.load_reminders_from_sql();
        this.reminders_present=check_reminder_presenter_initial(this.reminders_present);

        this.recurringReminderPresenter=new RecurringReminderPresenter(this.getApplicationContext(),profiles_voice);
        this.recurringReminderPresenter.load_reminders_from_sql();
        this.recurringReminderPresenter=check_reminder_presenter_initial(this.recurringReminderPresenter);

        this.timebox_presenter=new TimeBoxedReminderPresenter(this.getApplicationContext(),profiles_voice);
        this.timebox_presenter.load_reminders_from_sql();
        this.timebox_presenter=check_reminder_presenter_initial(this.timebox_presenter);

        this.profiles_voice=new VoiceProfilePresenter(this.getApplicationContext());
        this.profiles_voice.load_sql_voice_profiles();
    }

    public void addReminders(RemindersModel a,ArrayList<RemindersModel> b){
        b.add(a);
    }

    private ArrayList<RemindersModel> initialize_display(){
        ArrayList<RemindersModel> temp_lost=null;
        temp_lost=(ArrayList<RemindersModel>)this.reminders_present.get_reminder_list().clone();
        ArrayList<RecurringRemindersModel> temp_recurring=null;
        temp_recurring=(ArrayList<RecurringRemindersModel>)this.recurringReminderPresenter.get_reminder_list().clone();
        ArrayList<TimeBoxedReminderModel> temp_timebox=null;
        temp_timebox=(ArrayList<TimeBoxedReminderModel>)this.timebox_presenter.get_reminder_list().clone();

        for(int a=0;a<temp_recurring.size();a++){
            if(temp_lost.indexOf((RemindersModel)temp_recurring.get(a))==-1)temp_lost.add((RemindersModel)temp_recurring.get(a));
        }

        for(int a=0;a<temp_timebox.size();a++){
            if(temp_lost.indexOf((RemindersModel)temp_timebox.get(a))==-1)temp_lost.add((RemindersModel)temp_timebox.get(a));
        }

        temp_lost =this.reminders_present.sort_reminders(temp_lost);
        return temp_lost;
    }

    public void set_list_on_display() {
        ArrayList<RemindersModel> initialized=this.initialize_display();
        this.list_adapter=new CustomListAdapter(this, initialized);
        list_view.setAdapter(this.list_adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object list_view_object=list_view.getItemAtPosition(position);
                System.out.println(list_view_object);
                // how to get the type of the object without intentionally casting it first?
                // cast it and test for return type?
                reminder_transmission_holder=initialized.get(position);
                if(reminder_transmission_holder.return_type().equals("Basic Reminders")){
                    //the above if function serve to check if there are overrides to determine what type of object is the item. Child class overrides will be enforced even if cast.
                    //insert code for basic reminders editor
                    Intent reminder_edit_intent=new Intent(MainActivity.this,newBasicReminder.class);
                    reminder_edit_intent.putExtra("EDITMODE",position);
                    reminder_position=position;
                    reminder_transmission_holder_original=reminder_transmission_holder;
                    startActivityForResult(reminder_edit_intent,2);
                }else if(reminder_transmission_holder.return_type().equals("Recurring Reminders")){
                    Intent reminder_edit_intent=new Intent(MainActivity.this,newRecurringReminder.class);
                    reminder_edit_intent.putExtra("EDITMODE",position);
                    reminder_position=position;
                    recurringRemindersModel_transmission_holder=(RecurringRemindersModel)reminder_transmission_holder;
                    recurringRemindersModel_transmission_holder_original=(RecurringRemindersModel)reminder_transmission_holder;
                    reminder_transmission_holder=null;
                    startActivityForResult(reminder_edit_intent,2);
                }else if(reminder_transmission_holder.return_type().equals("Time Boxed Reminders")) {
                    Intent reminder_edit_intent=new Intent(MainActivity.this,newTimeBoxedReminder.class);
                    reminder_edit_intent.putExtra("EDITMODE",position);
                    reminder_position=position;
                    timeBoxed_transmission_holder=(TimeBoxedReminderModel) reminder_transmission_holder;
                    timeBoxed_transmission_holder_original=(TimeBoxedReminderModel) reminder_transmission_holder;
                    reminder_transmission_holder=null;
                    startActivityForResult(reminder_edit_intent,2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int request_code,int result_code,Intent data_received){
        if(request_code==2){
            if(result_code==RESULT_OK){
                String type="";
                if(data_received.getStringArrayExtra("State")[0].equals("FetchReminderModel")){
                    RemindersModel a=reminder_transmission_holder;
                    reminder_transmission_holder=null;
                    reminder_transmission_holder_original=null;
                    this.reminders_present.insert_reminder(a);
                    type="BasicReminder";
                }else if(data_received.getStringArrayExtra("State")[0].equals("FindAndReplace")){
                    RemindersModel a=reminder_transmission_holder;
                    RemindersModel b=reminder_transmission_holder_original;
                    reminder_transmission_holder=null;
                    reminder_transmission_holder_original=null;
                    this.reminders_present.change_reminder_similar_object(b,a);
                    type="BasicReminder";
                }else if(data_received.getStringArrayExtra("State")[0].equals("DeletThis")){
                    this.reminders_present.delete_reminder(reminder_transmission_holder_original);
                    reminder_transmission_holder=null;
                    reminder_transmission_holder_original=null;
                    type="BasicReminder";
                } else if(data_received.getStringArrayExtra("State")[0].equals("FetchRecurringReminderModel")){
                    RecurringRemindersModel a=recurringRemindersModel_transmission_holder;
                    recurringRemindersModel_transmission_holder=null;
                    recurringRemindersModel_transmission_holder_original=null;
                    this.recurringReminderPresenter.insert_reminder(a);
                    type="RecurringReminderModel";
                }else if(data_received.getStringArrayExtra("State")[0].equals("FindAndReplaceRecurringReminder")){
                    RecurringRemindersModel a=recurringRemindersModel_transmission_holder;
                    RecurringRemindersModel b=recurringRemindersModel_transmission_holder_original;
                    recurringRemindersModel_transmission_holder=null;
                    recurringRemindersModel_transmission_holder_original=null;
                    this.recurringReminderPresenter.change_reminder_similar_object(b,a);
                    type="RecurringReminderModel";
                }else if(data_received.getStringArrayExtra("State")[0].equals("DeletThisRecurringReminder")){
                    this.recurringReminderPresenter.delete_object_reminderModel(recurringRemindersModel_transmission_holder_original);
                    recurringRemindersModel_transmission_holder=null;
                    recurringRemindersModel_transmission_holder_original=null;
                    type="RecurringReminderModel";
                }else if(data_received.getStringArrayExtra("State")[0].equals("FetchTimeBoxedModel")){
                    TimeBoxedReminderModel a=timeBoxed_transmission_holder;
                    timeBoxed_transmission_holder=null;
                    timeBoxed_transmission_holder_original=null;
                    this.timebox_presenter.insert_reminder(a);
                    type="TimeBoxedReminderModel";
                }else if(data_received.getStringArrayExtra("State")[0].equals("FindAndReplaceTimeBoxed")){
                    TimeBoxedReminderModel a=timeBoxed_transmission_holder;
                    TimeBoxedReminderModel b=timeBoxed_transmission_holder_original;
                    timeBoxed_transmission_holder=null;
                    timeBoxed_transmission_holder_original=null;
                    this.timebox_presenter.change_reminder_similar_object(b,a);
                    type="TimeBoxedReminderModel";
                }else if(data_received.getStringArrayExtra("State")[0].equals("DeletThisTimeBoxed")){
                    this.timebox_presenter.delete_object_reminderModel(timeBoxed_transmission_holder_original);
                    timeBoxed_transmission_holder=null;
                    timeBoxed_transmission_holder_original=null;
                    type="TimeBoxedReminderModel";
                }

                if(type.equals("BasicReminder")) {
                    setReminders_present(this.reminders_present);
                    this.reminders_present.reminder_list.size();
                    this.reminders_present.save_reminders_sql(this.reminders_present.get_reminder_list());
                    this.list_adapter.set_data_refresh(this.initialize_display());
                    this.list_adapter.notifyDataSetChanged();
                } else if(type.equals("RecurringReminderModel")){
                    setRecurringReminderPresenter(this.recurringReminderPresenter);
                    this.recurringReminderPresenter.reminder_list.size();
                    this.recurringReminderPresenter.save_reminders_sql(this.recurringReminderPresenter.get_reminder_list());
                    this.list_adapter.set_data_refresh(this.initialize_display());
                    this.list_adapter.notifyDataSetChanged();
                } else if(type.equals("TimeBoxedReminderModel")){
                    setTimebox_presenter(this.timebox_presenter);
                    this.timebox_presenter.reminder_list.size();
                    this.timebox_presenter.save_reminders_sql(this.timebox_presenter.get_reminder_list());
                    this.list_adapter.set_data_refresh(this.initialize_display());
                    this.list_adapter.notifyDataSetChanged();
                }
                //create function on reminderpresenter to edit sql
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public RemindersModel receive_reminders_data(RemindersModel reminder_item) {
        return reminder_item;
        // how to properly return an item?
        // call a function that will eat the item back?
        // implementation cyclic error

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDialogDismissListener(int position) {
        Intent intent_fab;
        switch(position){
            case 0:
                intent_fab=new Intent(MainActivity.this,newBasicReminder.class);
                intent_fab.putExtra("EDITMODE",Integer.MAX_VALUE);
                startActivityForResult(intent_fab,2);
                break;
            case 1:
                intent_fab=new Intent(MainActivity.this,newRecurringReminder.class);
                intent_fab.putExtra("EDITMODE",Integer.MAX_VALUE);
                startActivityForResult(intent_fab,2);
                break;
            case 2:
                intent_fab=new Intent(MainActivity.this,newTimeBoxedReminder.class);
                intent_fab.putExtra("EDITMODE",Integer.MAX_VALUE);
                startActivityForResult(intent_fab,2);
                break;
        }
    }
}
