package teamx.group.reminderapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.speech.tts.Voice;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class VoiceProfilePresenter{
    private ArrayList<VoiceProfileModel> voice_profile_lists=new ArrayList<VoiceProfileModel>();
    private Context context_from_main;
    //store the list of voice profiles created by the user

    public VoiceProfilePresenter(Context context){
        this.context_from_main=context;
        //load from sql
    }

    public ArrayList<VoiceProfileModel> load_sql_voice_profiles(){
        SQLiteDatabase my_database=this.context_from_main.openOrCreateDatabase("VoiceProfiles", Context.MODE_PRIVATE,null);
        ArrayList<VoiceProfileModel> voice_profiles=new ArrayList<VoiceProfileModel>();
        try{
            Cursor c = my_database.rawQuery("SELECT * FROM Profiles ORDER BY fileName,color,booleanTTS",null);
        //determine structure
        //title,year,month,date,day,hour,minute,
        //create reminder and insert one by one?
            int color_index = c.getColumnIndex("color");
            int file_name_index = c.getColumnIndex("fileName");
            int boolean_tts_index = c.getColumnIndex("booleanTTS");
            int voice_profile_UUID = c.getColumnIndex("profileUUID");

            c.moveToFirst();

            while (c != null) {
                Color color_fetched=return_color(c.getString(color_index));

                File file = new File(c.getString(file_name_index));

                boolean boolean_tts = Boolean.getBoolean(c.getString(boolean_tts_index));

                UUID uuid_profile = UUID.fromString(c.getString(voice_profile_UUID));

                VoiceProfileModel iteration_model=new VoiceProfileModel(color_fetched,boolean_tts,file);
                iteration_model.set_UUID(uuid_profile);

                voice_profiles.add(iteration_model);

                c.moveToNext();
            }
        } catch (Exception e){
            if(voice_profiles.size()>0 && voice_profiles!=null){
                return voice_profiles;
            } else if(voice_profiles.size()==0 || voice_profiles==null){
                ArrayList<VoiceProfileModel> new_empty_model=new ArrayList<VoiceProfileModel>();
                new_empty_model.add(new VoiceProfileModel(new Color(),false,null));
                return new_empty_model;
            }
        }
        return voice_profiles;
    }

    public void save_sql(Context context,ArrayList<VoiceProfileModel> voice_profiles_to_save){
        //require context to actually save the sql
        for(int i=0;i<voice_profiles_to_save.size();i++){
            VoiceProfileModel iterated_profiles=voice_profiles_to_save.get(i);
            SQLiteDatabase my_database=context.openOrCreateDatabase("Voice Profiles",Context.MODE_PRIVATE,null);
            Cursor cursor = my_database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + "Profiles" + "'", null);
            if(cursor.getCount()>0){
                my_database.execSQL("DROP TABLE Profiles");
            }
            my_database.execSQL("CREATE TABLE IF NOT EXISTS Profiles(color VARCHAR, fileName VARCHAR,booleanTTS BOOLEAN,profileUUID VARCHAR)");
            my_database.execSQL("INSERT INTO Profiles(color,fileName,booleanTTS,profileUUID) values (\'"+this.color_to_string(iterated_profiles.get_color_profile_name())+"\',\'"+iterated_profiles.get_media_file().toPath().toString()+"\',\'"+String.valueOf(iterated_profiles.get_tts_boolean())+"\',\'"+iterated_profiles.get_name().toString()+"\')");
        }
    }

    public void create_voice_profile(Color color_profile, File file_media, Boolean tts_boolean){
        this.voice_profile_lists.add(new VoiceProfileModel(color_profile,tts_boolean,file_media));
    }

    public String color_to_string(Color color) {
        String color_return = String.valueOf(color.alpha()) + "," + String.valueOf(color.red()) + "," + String.valueOf(color.green()) + "," + String.valueOf(color.blue());
        return color_return;
    }

    public Color return_color(String raw_float_values){
        float[] color_array=new float[4];
        String[] split_color=raw_float_values.split(",");
        for(int i=0;i<color_array.length;i++){
            color_array[i]=Float.valueOf(split_color[i]);
        }
        Color color_to_return = Color.valueOf(color_array[0],color_array[1],color_array[2],color_array[3]);
        return color_to_return;
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
