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

public class Pantalla3 extends AppCompatActivity {

    SQLiteConexion connection;
    Spinner spCountry;
    String country;
    Integer idReceived;
    EditText updateName, updatePhone, updateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla3);

        connection = new SQLiteConexion(this, Transaction.nameDataBase, null, 1);

        spCountry = findViewById(R.id.spCountry);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(adapter);

        updateName = findViewById(R.id.txttUpdateName);
        updatePhone = findViewById(R.id.txtUpdatePhone);
        updateNote = findViewById(R.id.txtUpdateNote);

        Bundle recoverValuesBundle = this.getIntent().getExtras();

        idReceived = recoverValuesBundle.getInt("id");
        updateName.setText(recoverValuesBundle.getString("name"));
        updatePhone.setText(recoverValuesBundle.getString("phone"));
        updateNote.setText(recoverValuesBundle.getString("note"));

        Button btnUpdate = findViewById(R.id.btnUpdateContact);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
                Intent intentListView = new Intent(getApplicationContext(), Pantalla2.class);
                startActivity(intentListView);
            }
        });

    }

    private void updateContact() {
        SQLiteDatabase db = connection.getWritableDatabase();
        String[] params = {String.valueOf(idReceived)};

        ContentValues values = new ContentValues();
        country = spCountry.getSelectedItem().toString();
        values.put(Transaction.country, country);
        values.put(Transaction.name, updateName.getText().toString());
        values.put(Transaction.phone, updatePhone.getText().toString());
        values.put(Transaction.note, updateNote.getText().toString());

        db.update(Transaction.tableContacts, values, Transaction.id + "=?", params);
        Toast.makeText(getApplicationContext(), "Contacto Actualizado", Toast.LENGTH_LONG).show();


    }
}