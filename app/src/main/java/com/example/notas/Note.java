package com.example.notas;

import android.widget.Toast;

import static com.example.notas.R.string.toast_long;

public class Note {

    private String mTitle;
    private String mDescription;

    private boolean mIdea;
    private boolean mImportant;
    private boolean mTodo;

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
