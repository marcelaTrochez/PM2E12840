package com.aplicacion.pm2e12840;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aplicacion.pm2e12840.Transacciones.SQLiteConexion;
import com.aplicacion.pm2e12840.Transacciones.Transaction;

public class MainActivity extends AppCompatActivity {

    EditText name, phone, note;
    Spinner spinnerCountries;
    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCountries = findViewById(R.id.SpnCountries);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(adapter);

        Button btnSave = findViewById(R.id.btnSave);
        spinnerCountries = findViewById(R.id.SpnCountries);
        name = findViewById(R.id.txtName);
        phone = findViewById(R.id.txtPhone);
        note = findViewById(R.id.txtNote);
        country = spinnerCountries.getSelectedItem().toString();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        Button savedContacts = findViewById(R.id.btnContactList);
        savedContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Pantalla2.class);
                startActivity(intent);
            }
        });
    }

    private void addContact() {
        SQLiteConexion connection = new SQLiteConexion(this, Transaction.nameDataBase, null, 1);
        SQLiteDatabase db = connection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Transaction.country, country = spinnerCountries.getSelectedItem().toString());
        name.setError(null);
        phone.setError(null);
        note.setError(null);
        String verifiedName = name.getText().toString();
        String verifiedPhone = phone.getText().toString();
        String verifiedNote = note.getText().toString();
//        String onlyLetterSpaces = "[a-zA-Z][a-zA-Z ]*"; Expresion regular para solo letras y espacios

        if(verifiedName.trim().isEmpty() || verifiedName.length() > 50)  {
            name.setError("Ingrese Su Nombre Completo Porfavor (Maximo 50 caracteres)");
        } else if(verifiedPhone.trim().isEmpty() || verifiedPhone.length() > 15){
            phone.setError("Ingrese Su Telefono Por Favor (Maximo 15 caracteres)");
        } else if(verifiedNote.trim().isEmpty() || verifiedNote.length() > 50){
            note.setError("Ingrese Una Nota Por Favor (Maximo 50 caracteres)");
        } else {
            values.put(Transaction.name, verifiedName);
            values.put(Transaction.phone, verifiedPhone);
            values.put(Transaction.note, verifiedNote);

            Long result = db.insert(Transaction.tableContacts, Transaction.id, values);
            Toast.makeText(getApplicationContext(),"Contacto Guardado Correctamente: "+ result.toString(), Toast.LENGTH_LONG).show();
            db.close();

            ClearFields();
        }
    }

    private void ClearFields() {
        name.setText("");
        phone.setText("");
        note.setText("");
        spinnerCountries.setSelection(0);
    }


}