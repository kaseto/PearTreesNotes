package com.example.notas;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.notas.R.string.toast_long;

public class Note {

    private String mTitle;
    private String mDescription;

    private boolean mIdea;
    private boolean mImportant;
    private boolean mTodo;

    //Variables para la clave de los JSON

    private static final String JSON_TITLE="title";
    private static final String JSON_DESCRIPTION="description";
    private static final String JSON_IDEA="idea";
    private static final String JSON_TODO="todo";
    private static final String JSON_IMPORTANT="important";

    //Constructor base VACIO:

    public Note(){

    }

    //Constructor para crear una nueva nota a partir de un fichero JSON:

    public Note(JSONObject jo)throws JSONException{

        mTitle=jo.getString(JSON_TITLE);
        mDescription=jo.getString((JSON_DESCRIPTION));
        mIdea=jo.getBoolean(JSON_IDEA);
        mTodo=jo.getBoolean(JSON_TODO);
        mImportant=jo.getBoolean(JSON_IMPORTANT);
    }

       /* EL siguente método toma las 5 varivles de la nota y las serializa
        en un objeto tiop JSON:
        (usando como clave las varibles creadas a tal efecto)
        */

    public  JSONObject convertNoteToJSON()throws JSONException{

        JSONObject jo=new JSONObject();

        jo.put(JSON_TITLE,mTitle);
        jo.put(JSON_DESCRIPTION,mDescription);
        jo.put(JSON_IDEA,mIdea);
        jo.put(JSON_TODO,mTodo);
        jo.put(JSON_IMPORTANT,mImportant);

        return jo;

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {

            this.mTitle = mTitle;

    }

    public String getDescription() {
            return mDescription;
    }

    public void setDescription(String mDescription) {

            this.mDescription = mDescription;
    }

    public boolean isIdea() {
        return mIdea;
    }

    public void setIdea(boolean mIdea) {
        this.mIdea = mIdea;
    }

    public boolean isImportant() {
        return mImportant;
    }

    public void setImportant(boolean mImportant) {
        this.mImportant = mImportant;
    }

    public boolean isTodo() {
        return mTodo;
    }

    public void setTodo(boolean mTodo) {
        this.mTodo = mTodo;
    }
}
