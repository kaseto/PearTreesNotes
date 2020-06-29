package com.example.notas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.RandomAccess;

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
               // mEditor.commit();

            }
        });

        //Lógica de cambiar de tipo animación:
        mAnimationOption=mPrefs.getInt("anim option", SLOW);

        final RadioGroup radioGroup=(RadioGroup)findViewById(R.id.rg_sound);
        radioGroup.clearCheck();//Se limpian los RadioButtons

        //En función de las preferencia de usuario, selecciono uno de los modos de animación
        switch (mAnimationOption){

            case FAST:
                radioGroup.check(R.id.rb_fast);
                break;

            case SLOW:
                radioGroup.check(R.id.rb_slow);
                break;

            case NONE:
                radioGroup.check(R.id.rb_none);
                break;

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //Recuperamos el radioButton del radioGroup que ha sido señeccionado a través del checkedId
                RadioButton rb=(RadioButton)radioGroup.findViewById(checkedId);

                // En una comparación poner el null antes que el obejto es mucho más óptimo
                if(null!=rb && checkedId > -1){

                    switch (rb.getId()) {

                        case R.id.rb_fast:
                            mAnimationOption=FAST;
                            break;

                        case R.id.rb_slow:
                            mAnimationOption=SLOW;
                            break;

                        case R.id.rb_none:
                            mAnimationOption=NONE;
                            break;


                    }

                    mEditor.putInt("anim option",mAnimationOption);

                    //mEditor.commit();
                }

            }
        });

    }

    @Override
    protected void onPause() {

        //Al hacer el commit dentro del ONPAUSE al abandonar la actividad
        //se hace el guardado para ahorrar recursos en lugar de en cada acción
        mEditor.commit();
        super.onPause();
    }
}
