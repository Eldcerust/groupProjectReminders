package teamx.group.reminderapp;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ReminderView{

    // this is unfortunately not the first activity, despite being the most important activity
    private VoiceProfilePresenter profiles_voice;
    private RemindersPresenter reminders_present;
    private ListView list_view;
    private AlarmManager alarm_mgr;
    private VoiceProfilePresenter voice_access;
    private newBasicReminder temporary_class_container_basic_reminder_creation;
    public static ReminderItemPositions reminder_transmission_holder;


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                Intent intent_fab=new Intent(MainActivity.this,newBasicReminder.class);
                intent_fab.putExtra("EDITMODE",Integer.MAX_VALUE);
                startActivityForResult(intent_fab,2);
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

    public void fetch_data(){
        profiles_voice=new VoiceProfilePresenter(this.getApplicationContext());
        //profiles_voice.load_sql_voice_profiles();
        reminders_present=new RemindersPresenter(this.getApplicationContext(),profiles_voice);
        //reminders_present.load_reminders_from_sql();
    }

    public void set_list_on_display() {
        CustomListAdapter adapter_for_list=new CustomListAdapter(this,reminders_present.get_reminder_list());
        list_view.setAdapter(adapter_for_list);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object list_view_object=list_view.getItemAtPosition(position);
                // how to get the type of the object without intentionally casting it first?
                // cast it and test for return type?
                reminder_transmission_holder.reminder_model=(RemindersModel)list_view_object;
                if(reminder_transmission_holder.reminder_model.return_type().equals("Basic Reminders")){
                    //the above if function serve to check if there are overrides to determine what type of object is the item. Child class overrides will be enforced even if cast.
                    //insert code for basic reminders editor
                    Intent reminder_edit_intent=new Intent(MainActivity.this,newBasicReminder.class);
                    reminder_edit_intent.putExtra("EDITMODE",position);
                    reminder_transmission_holder.position_reminder=position;
                    startActivityForResult(reminder_edit_intent,2);
                }else if(reminder_transmission_holder.reminder_model.return_type().equals("Recurring Reminders")){

                }else if(reminder_transmission_holder.reminder_model.return_type().equals("Time Boxed Reminders")) {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int request_code,int result_code,Intent data_received){
        if(request_code==2){
            if(result_code==RESULT_OK){
                if(data_received.getStringArrayExtra("State")[0].equals("FetchReminderModel")){
                    RemindersModel a=reminder_transmission_holder.reminder_model;
                    reminder_transmission_holder=null;
                    this.reminders_present.insert_reminder(a);
                }else if(data_received.getStringArrayExtra("State")[0].equals("FindAndReplace")){
                    RemindersModel a=reminder_transmission_holder.reminder_model;
                    reminder_transmission_holder=null;
                    this.reminders_present.change_reminder_position_wremindermodel(a,Integer.valueOf(data_received.getStringArrayExtra("State")[1]));
                }else if(data_received.getStringArrayExtra("State")[0].equals("DeletThis")){
                    this.reminders_present.delete_reminder_position(Integer.valueOf(data_received.getStringArrayExtra("State")[1]));
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

    public interface transferBasicReminders{
        public void transferBasicReminders(RemindersPresenter e);
        public void transferBasicReminders(RemindersPresenter e,int position);
    }

    static class ReminderItemPositions{
        RemindersModel reminder_model;
        int position_reminder;
    }
}
