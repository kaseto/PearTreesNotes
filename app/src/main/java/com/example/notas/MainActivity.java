package com.example.notas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

 private  NoteAdapter mNoteAdapter;

 private Animation mAnimFlash;
 private Animation mAnimFadeIn;

 int mIdBeep=-1;
 SoundPool mSp;


 private boolean mSound;
 private int mAnimOption;
 private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoteAdapter=new NoteAdapter();
        ListView listNote=(ListView)findViewById(R.id.list_view);
        listNote.setAdapter(mNoteAdapter);

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if( mSound){
                    mSp.play(mIdBeep,1,1,0,0,1);
                }

                //Recupermos la nota de la posición pulsada por el usuario
                Note tempNote = mNoteAdapter.getItem(position);
                //creamos una instancia de show note
                DialogShowNote dialog = new DialogShowNote();
                dialog.sendNoteSelected(tempNote);
                dialog.show(getSupportFragmentManager(), "");
            }
        });

        listNote.setLongClickable(true);

        listNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setMessage(R.string.delete_note_text)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNoteAdapter.deleteNote(position);
                            }
                        })
                        .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog title=alert.create();
                title.setTitle("");
                title.show();



               return true;
            }
        });

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes attr=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSp=new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(attr)
                    .build();

        }else{
            mSp=new SoundPool(5, AudioManager.STREAM_MUSIC,0);

        }
        try {
            AssetManager manager=this.getAssets();
            AssetFileDescriptor desc=manager.openFd("beep.ogg");
            mIdBeep=mSp.load(desc,0);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
        //el método onREsume se llama despues del onCreate y al volver a la actividad
    // después de pasar por otra
    protected void onResume() {

        //Se cargan aquí las preferencias:
        mPrefs=getSharedPreferences("PearTrees Notes",MODE_PRIVATE);
        mSound=mPrefs.getBoolean("sound",true);
        mAnimOption=mPrefs.getInt("anim option",SettingsActivity.FAST);

        mAnimFlash= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.flash);
        mAnimFadeIn= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        if (mAnimOption==SettingsActivity.FAST){
            mAnimFlash.setDuration(100);
        }else if(mAnimOption==SettingsActivity.SLOW){
            mAnimFlash.setDuration(1000);
        }
        mNoteAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createNewNote(Note newNote){

        //Este método recibirá una nueva nota creada por el diálogo pertinente...
        mNoteAdapter.addNote(newNote);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.action_add){
            dialog_new_note dialog =new dialog_new_note();
            dialog.show(getSupportFragmentManager(),"note_create");
        }
        if(item.getItemId()==R.id.action_settings){
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mNoteAdapter.saveNotes();

    }




    public  class NoteAdapter extends BaseAdapter{

        List<Note> notelist= new ArrayList<Note>();
        private  JSONSerializer mSerializer;

        public NoteAdapter(){
            mSerializer=new JSONSerializer("PearTrees.json",MainActivity.this.getApplicationContext());

            try{
                notelist=mSerializer.load();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public  void saveNotes() {
            try {
                mSerializer.save(notelist);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        @Override
        public int getCount() {
            return notelist.size();
        }

        @Override
        public Note getItem(int position) {
            return notelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            //Aquí va la lógica de las celdas de la lista

            if (view==null){
                /*en este if la vista no ha sido accedida anteriormente
                asi que lo primero que hay que hacer es inflarla
                a partir del layout lis_item.xml
                 */

                LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view =inflater.inflate(R.layout.list_item,viewGroup,false);

            }

            /*Vista bien definida y procedemos a ocultar las imagenes que sobren de layout...
            1º cargamos los widgewts del ayout
            * y rellenar titulo y descripción de la tarea
            */
            TextView texviewTitle =(TextView)view.findViewById(R.id.text_view_title);
            TextView texviewDescription =(TextView)view.findViewById(R.id.text_view_description);

            ImageView ivImportant = (ImageView)view.findViewById(R.id.image_vew_important);
            ImageView ivTodo=(ImageView)view.findViewById(R.id.image_view_todo);
            ImageView ivIdea=(ImageView)view.findViewById(R.id.image_view_idea);

            Note currentNote=notelist.get(position);

            if (currentNote.isImportant()&&mAnimOption!=SettingsActivity.NONE){
                view.setAnimation(mAnimFlash);
            }else{
                view.setAnimation(mAnimFadeIn);
            }


            if(!currentNote.isImportant()){
                ivImportant.setVisibility(View.GONE);
            }
            if(!currentNote.isIdea()){
                ivIdea.setVisibility(View.GONE);
            }
            if(!currentNote.isTodo()){
                ivTodo.setVisibility(View.GONE);
            }

            texviewTitle.setText(currentNote.getTitle());
            texviewDescription.setText(currentNote.getDescription());

            return view;
        }

        public  void addNote(Note n){
            notelist.add(n);
            notifyDataSetChanged();
        }

       public  void deleteNote(int n){
            notelist.remove(n);
            notifyDataSetChanged();
        }

    }
}
