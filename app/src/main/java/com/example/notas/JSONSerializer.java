package com.example.notas;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JSONSerializer {

    private String mFileName;//Nombre de fichero JSON   que va a guardar la clase
    private Context mContext; //Contecto de dónde se va aa guardar ese fichero JSON.

    //Contructor de objeto que va a serializar en fichero JSON
    public JSONSerializer(String filename, Context context) {

        this.mFileName = filename;
        this.mContext = context;
    }

    public void save(List<Note> notes) throws IOException, JSONException {

        //Array de obetos JSON
        JSONArray jArray = new JSONArray();

        //Convertir cada una de las NOte en objetos JSONObject y guardarlos en el JSON Array:
        for (Note n : notes) {
            jArray.put(n.convertNoteToJSON());
        }

        //Para guardar el fichero de objetos JSON hay que usar el WRITER:
        Writer writer = null;

        try {
            //EL OutputStream abre el fichero donde guarda el JSON:
            OutputStream out = mContext.openFileOutput(mFileName, mContext.MODE_PRIVATE);

            //EL escritor ya sabe donde escribir contenido
            writer = new OutputStreamWriter(out);

            //EL escritor escribe en el disco tdo el array pasado a formato String
            writer.write((jArray.toString()));
        } finally {

            //Si el writer había podido abrir el fichero, lo tiene que cerrar para q no se corrompa
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Note> load() throws IOException, JSONException {

        //Array de objetos Note en Java
        ArrayList<Note> notes = new ArrayList<Note>();

        //Buffered reader para leer el fichero JASON
        BufferedReader reader = null;
        try {
            //ImputSTream abre el fichero JSON que vamos a leer y procesar
            InputStream in = mContext.openFileInput(mFileName);
            //El lector, ya sabe donde leer los datos, de que fichero JSON
            reader = new BufferedReader(new InputStreamReader(in));
            //Leemos los strings del fichero JSON con un String Builder
            StringBuilder jsonString = new StringBuilder();
            //Variable para leer la linea actual:
            String currentLine = null;

            /*Leer el fichero JSON entero, hasta acabarlo y pasarlo tdo a STring
            Mientras la línea actual no sea nula....
             */
            while ((currentLine = reader.readLine()) != null) {
                jsonString.append(currentLine);
            }
            //HEmos pasado de unJASON a un STring con todos los objetos Note

            //Ahora pasamos de un array de STrings a un array de objetos JSON:
            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jArray.length(); i++) {
                notes.add(new Note(jArray.getJSONObject(i)));

            }
            //ya teemos el array de notes con todos los objetos de la clase Note...
        } catch (FileNotFoundException e) {
            // la primera vez fallara si o si pq no hay fichero de notas
            // basta con ignorar la excepción

        } finally {
            //Si se habia abierto el fichero hay que cerrrrlo para q no se corrompa
            if (reader != null) {
                reader.close();
            }

        }
        return notes;
    }
}

