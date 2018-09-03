package teamx.group.reminderapp;

import android.graphics.Color;

import java.io.File;
import java.util.UUID;

public class VoiceProfileModel {
    private Color color_profile;
    private Boolean tts_boolean;
    private File media_file;
    private UUID profile_UUID;

    public VoiceProfileModel(Color color_profile,Boolean tts_boolean,File media_file){
        this.color_profile=color_profile;
        this.tts_boolean=tts_boolean;
        this.media_file=media_file;
        this.profile_UUID= UUID.randomUUID();
    }

    public Color get_color_profile_name(){return this.color_profile;}

    public Boolean get_tts_boolean(){return this.tts_boolean;}

    public File get_media_file(){return this.media_file;}

    public UUID get_name(){return this.profile_UUID;}

    public void set_color_profile_name(Color color){this.color_profile=color;}

    public void set_UUID(UUID uuid_external){this.profile_UUID=uuid_external;}

    public void set_media_file(File media_file){this.media_file=media_file;}

    public void set_tts_boolean(Boolean tts_boolean){this.tts_boolean=tts_boolean;}
}
