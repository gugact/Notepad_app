package br.usp.icmc.gustavo.notepad_app.model;

/**
 * Created by Gustavo on 05/07/2016.
 */
public class Model{
    String name;
    boolean checked;

    Model(String name, boolean value){
        this.name = name;
        this.checked = value;
    }
    public String getName(){
        return this.name;
    }
    public boolean isChecked(){
        return this.checked;
    }

}
