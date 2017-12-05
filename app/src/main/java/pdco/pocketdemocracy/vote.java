package pdco.pocketdemocracy;

/**
 * Created by kianb on 11/29/2017.
 */

public class vote {

    private String title;
    private String description;

    public vote(){
        title = null;
        description = null;
    }

    public vote(String title, String description){
        this.title = title;
        this.description = description;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setDescription(){
        this.description = description;
    }

    public String getDescritpion(){
        return description;
    }

}
