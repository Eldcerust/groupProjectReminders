package teamx.group.reminderapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RemindersModel {
    private String reminder_name;
    private Calendar reminder_date_time;
    private UUID reminder_UUID;
    private VoiceProfileModel reminder_voice_profile; //VoiceProfileModel used, pulled from VoiceProfilePresenter
    private ArrayList<CheckBoxListSingle> checkbox_list;

    public RemindersModel(String reminder_name,Calendar reminder_date_time,ArrayList<CheckBoxListSingle> bulletin_list){
        //this is the onCreate(constructor) of the class
        this.reminder_name=reminder_name;
        this.reminder_date_time=reminder_date_time;
        this.reminder_UUID=UUID.randomUUID();
        this.checkbox_list=bulletin_list;
    }

    public RemindersModel(String reminder_name,Calendar reminder_date_time){
        //this is the onCreate(constructor) of the class
        this.reminder_name=reminder_name;
        this.reminder_date_time=reminder_date_time;
        this.reminder_UUID=UUID.randomUUID();
        this.checkbox_list=new ArrayList<CheckBoxListSingle>();
        this.checkbox_list.add(new CheckBoxListSingle(false,""));
    }

    public void set_reminder_name(String string){this.reminder_name=string;}

    public void set_list(ArrayList<CheckBoxListSingle> list_set){this.checkbox_list=list_set;}

    public void set_reminder_date_time(Calendar date){this.reminder_date_time=date;}

    public void set_reminder_voice_profile(VoiceProfileModel voice_profile){this.reminder_voice_profile=voice_profile;}

    public void set_reminder_UUID(UUID uuid){this.reminder_UUID=uuid;}

    public String get_reminder_name(){return this.reminder_name;} //since the value of the model is a private model, it requires a logic to just access or change set by the model itself

    public Calendar get_reminder_date_time(){return this.reminder_date_time;}

    public UUID get_reminder_UUID(){return this.reminder_UUID;}

    public VoiceProfileModel get_reminder_voice_profile(){return this.reminder_voice_profile;}

    public String return_type(){return "Basic Reminders";}

    public ArrayList<CheckBoxListSingle> get_checkbox_list() {return this.checkbox_list;}
}
