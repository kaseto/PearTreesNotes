package com.example.notas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Note mTempNote=new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTemp=(Button)findViewById(R.id.btnTemporal);
        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos una nueva intasncia de Dialog Show Note
                DialogShowNote dialog=new DialogShowNote();
                //Indico al dialogo qué nota es la que debe mostrar por pantalla.
                dialog.sendNoteSelected(mTempNote);
                //Mostramos el dialoggo por pantalla con un manager
                dialog.show(getSupportFragmentManager(),"note_show");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createNewNote(Note newNote){

        //Este método recibirá una nueva nota creada por el diálogo pertinente...
        mTempNote=newNote;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.action_add){
            dialog_new_note dialog =new dialog_new_note();
            dialog.show(getSupportFragmentManager(),"note_create");
        }
        return false;
    }
}
