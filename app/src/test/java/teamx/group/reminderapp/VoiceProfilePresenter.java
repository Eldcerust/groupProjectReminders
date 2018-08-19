package teamx.group.reminderapp;

import android.graphics.Color;

import java.io.File;
import java.util.ArrayList;

public class VoiceProfilePresenter {
    ArrayList<VoiceProfileModel> voice_profile_lists=new ArrayList<VoiceProfileModel>();
    //store the list of voice profiles created by the user

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


}
