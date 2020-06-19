package com.example.notas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPrefs; //Para leer datos guardados en el disco: SOLO LECTURA!!
    private SharedPreferences.Editor mEditor;//  Para escribir datos en Shared Prefs.

    private boolean mSound;// Para activar/desactivar el sonido de la app

    public static  final int FAST=0; //Animaciones rápidas
    public static  final int SLOW=1; //animaciones lenta
    public static  final int NONE=2; // Sin animaciones

    private int mAnimationOption;   //Para cambiar el tipo de animación de la app


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPrefs=getSharedPreferences("PearTrees Notes",MODE_PRIVATE);
        mEditor=mPrefs.edit();

        //Lógica para activar y desactivar sonido
        mSound=mPrefs.getBoolean("sound",true);

        CheckBox checkBoxSound=(CheckBox)findViewById(R.id.sound_checkbox);

        if (mSound){
            checkBoxSound.setChecked(true);
        }else {
            checkBoxSound.setChecked(false);
        }
        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Si el sonido está en marcha se apaga
                //si esta apagado se pone en marcha
                mSound=!mSound;

                mEditor.putBoolean("sound",mSound);

                //Guardar los cambios:
                mEditor.commit();

            }
        });
    }
}
