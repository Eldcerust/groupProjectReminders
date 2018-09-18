package teamx.group.reminderapp;

public class CheckBoxListSingle {
    private Boolean finished_list;
    private String checkbox_name;

    public CheckBoxListSingle(Boolean state,String title){
        this.finished_list=state;
        this.checkbox_name=title;
    }

    public void toggle_checkbox(){
        if(this.finished_list==false){
            this.finished_list=true;
        }else if(this.finished_list==true){
            this.finished_list=false;
        }
    }

    public void set_name(String title){
        this.checkbox_name=title;
    }

    public void set_state(Boolean state){this.finished_list=state;}

    public String get_name(){
        return this.checkbox_name;
    }

    public Boolean get_state(){
        return this.finished_list;
    }
}
