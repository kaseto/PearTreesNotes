package com.example.notas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class dialog_new_note extends DialogFragment {


    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();

        View dialogView=inflater.inflate(R.layout.dialog_new_note,null);

        final EditText editTitle=(EditText)dialogView.findViewById(R.id.editTitle);
        final EditText editDescription=(EditText)dialogView.findViewById((R.id.editDescription));

        final CheckBox checBoxIdea=(CheckBox)dialogView.findViewById(R.id.checkBoxIdea);
        final CheckBox checkBoxTodo=(CheckBox)dialogView.findViewById(R.id.checkBoxToDo);
        final CheckBox checkBoxImportant=(CheckBox)dialogView.findViewById(R.id.checkBoxImportant);

        Button btnCancel=(Button)dialogView.findViewById(R.id.btnCancel);
        Button btnOk=(Button)dialogView.findViewById(R.id.btnOk);

        //Casting a MainActivity que es la que ha llamdo al dialogo
        final MainActivity callingActivity=(MainActivity) getActivity();



        builder.setView(dialogView)
                .setMessage("Añadir una nueva nota");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se crea nota vacía
                Note newNote=new Note();

                //Configura las 5 variables de una nueva Nota.

                if (!editTitle.getText().toString().isEmpty()) {

                    if(editTitle.getText().toString().length()<50) {
                        if (editDescription.getText().toString().length()<140) {
                            newNote.setTitle(editTitle.getText().toString());
                            newNote.setDescription(editDescription.getText().toString());

                            callingActivity.createNewNote(newNote);
                        }else{
                            Toast.makeText(getActivity(), R.string.description_size,
                                    Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(getActivity(), R.string.title_size, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), R.string.title_empty,Toast.LENGTH_LONG).show();

                }

                newNote.setIdea(checBoxIdea.isChecked());
                newNote.setTodo(checkBoxTodo.isChecked());
                newNote.setImportant(checkBoxImportant.isChecked());



                //Cierra el dialogo
                dismiss();

            }
        });

        return builder.create();
    }
}
