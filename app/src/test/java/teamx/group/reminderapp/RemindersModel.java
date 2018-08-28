package teamx.group.reminderapp;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RemindersModel {
    private String reminder_name;
    private Calendar reminder_date_time;
    private UUID reminder_UUID;
    private VoiceProfileModel reminder_voice_profile; //VoiceProfileModel used, pulled from VoiceProfilePresenter

    public RemindersModel(String reminder_name,Calendar reminder_date_time){
        //this is the onCreate(constructor) of the class
        this.reminder_name=reminder_name;
        this.reminder_date_time=reminder_date_time;
        this.reminder_UUID=UUID.randomUUID();
    }

    public void set_reminder_name(String string){this.reminder_name=string;}

    public void set_reminder_date_time(Calendar date){this.reminder_date_time=date;}

    public void set_reminder_voice_profile(VoiceProfileModel voice_profile){this.reminder_voice_profile=voice_profile;}

    public String get_reminder_name(){return this.reminder_name;} //since the value of the model is a private model, it requires a logic to just access or change set by the model itself

    public Calendar get_reminder_date_time(){return this.reminder_date_time;}

    public UUID get_reminder_UUID(){return this.reminder_UUID;}

    public VoiceProfileModel get_reminder_voice_profile(){return this.reminder_voice_profile;}
}
