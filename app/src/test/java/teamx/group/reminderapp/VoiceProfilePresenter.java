package teamx.group.reminderapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class VoiceProfilePresenter {
    private ArrayList<VoiceProfileModel> voice_profile_lists=new ArrayList<VoiceProfileModel>();
    private Context context_from_main;
    //store the list of voice profiles created by the user

    public VoiceProfilePresenter(){
        //load from sql
    }

    public ArrayList<VoiceProfilePresenter> load_sql_voice_profiles(){
        SQLiteDatabase my_database=this.context_from_main.openOrCreateDatabase("VoiceProfiles", Context.MODE_PRIVATE,null);
        Cursor c = my_database.rawQuery("SELECT * FROM Profiles ORDER BY fileName,color,booleanTTS",null);
        ArrayList<RemindersModel> reminders_list_for_save=new ArrayList<RemindersModel>();
        //determine structure
        //title,year,month,date,day,hour,minute,
        //create reminder and insert one by one?
        int count=0;

        int color_index=c.getColumnIndex("color");
        int file_name_index=c.getColumnIndex("fileName");
        int boolean_tts_index=c.getColumnIndex("booleanTTS");
        int voice_profile_UUID=c.getColumnIndex("profileUUID");

        c.moveToFirst();

        while(c!=null) {

            // how to determine which voice profile is this? Use UUID
        }

        return(reminders_list_for_save);
    }

    public interface receive_voice_profiles{
        public void receive_voice_profiles(VoiceProfilePresenter voice_profiles);
    }

    public void create_voice_profile(Color color_profile, File file_media, Boolean tts_boolean){
        this.voice_profile_lists.add(new VoiceProfileModel(color_profile,tts_boolean,file_media));
    }

    public void enable_tts(int element_wanted){
        VoiceProfileModel temporary_reminder_model=this.voice_profile_lists.get(element_wanted);
        temporary_reminder_model.set_tts_boolean(Boolean.TRUE);
        temporary_reminder_model.set_media_file(null);
        this.voice_profile_lists.set(element_wanted,temporary_reminder_model);
    }

    public void disable_tts(int element_wanted,File file_of_ringtone){
        VoiceProfileModel temporary_reminder_model=this.voice_profile_lists.get(element_wanted);
        temporary_reminder_model.set_tts_boolean(Boolean.FALSE);
        temporary_reminder_model.set_media_file(file_of_ringtone);
        this.voice_profile_lists.set(element_wanted,temporary_reminder_model);
    }

    public void change_media(int element_wanted,File file_of_ringtone){
        VoiceProfileModel temporary_reminder_model=this.voice_profile_lists.get(element_wanted);
        temporary_reminder_model.set_media_file(file_of_ringtone);
        this.voice_profile_lists.set(element_wanted,temporary_reminder_model);
    }

    public void set_profile_color(int element_wanted,Color color){
        VoiceProfileModel temporary_reminder_model=this.voice_profile_lists.get(element_wanted);
        temporary_reminder_model.set_color_profile_name(color);
        this.voice_profile_lists.set(element_wanted,temporary_reminder_model);
    }

    public VoiceProfileModel search_profile(UUID uuid_recorded){
        for(int i=0;i<voice_profile_lists.size();i++){
            if(voice_profile_lists.get(i).get_name()==uuid_recorded){
                return voice_profile_lists.get(i);
            }
        }
        return null;
    }


}
