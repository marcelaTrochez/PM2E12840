package com.aplicacion.pm2e12840;

import static android.Manifest.permission.CALL_PHONE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aplicacion.pm2e12840.Tables.Contacts;
import com.aplicacion.pm2e12840.Transacciones.SQLiteConexion;
import com.aplicacion.pm2e12840.Transacciones.Transaction;

import java.util.ArrayList;

public class Pantalla2 extends AppCompatActivity {

    SQLiteConexion connection;
    ListView ListContacts;
    ArrayList<Contacts> arrayListContacts;
    ArrayList<String> arrayListStringContacts;
    int listrowposition;
    String phoneItemSelected;
    String regionCode;
    String regionCodeExtracted;
    String rowNameSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla2);

        connection = new SQLiteConexion(this, Transaction.nameDataBase, null, 1);
        ListContacts = findViewById(R.id.listContacts);

        getListContacts();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListStringContacts);
        ListContacts.setAdapter(arrayAdapter);

        ListContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listrowposition = arrayListContacts.get(position).getId();
                rowNameSelected = arrayListContacts.get(position).getName();
                regionCode = arrayListContacts.get(position).getCountry();
                phoneItemSelected = arrayListContacts.get(position).getPhone();

                if(regionCode.contains("504")){
                    regionCodeExtracted = "+504";
                } else if(regionCode.contains("506")){
                    regionCodeExtracted = "+506";
                } else if(regionCode.contains("503")){
                    regionCodeExtracted = "+503";
                } else if(regionCode.contains("502")){
                    regionCodeExtracted = "+502";
                }
                String positionString = String.valueOf(listrowposition);
                Toast.makeText(getApplicationContext(), "Contacto: "+positionString+" Seleccionado", Toast.LENGTH_SHORT).show();
            }
        });


        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listrowposition > 0){
                    AlertDialog.Builder alertDelete = new AlertDialog.Builder(Pantalla2.this);
                    alertDelete.setMessage("Desea Eliminar a "+rowNameSelected)
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteItem();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), Pantalla2.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog tittle = alertDelete.create();
                    tittle.setTitle("ADVERTENCIA");
                    tittle.show();

                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un Contacto Para Poder Eliminar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listrowposition > 0){
                    finish();
                    getValuesToSendUpdateScreen();
                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un Contacto Para Poder Actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listrowposition> 0){
                    AlertDialog.Builder alertCall = new AlertDialog.Builder(Pantalla2.this);
                    alertCall.setMessage("Llamar a "+rowNameSelected)
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(Intent.ACTION_CALL);
                                    i.setData(Uri.parse("tel:"+ regionCodeExtracted + phoneItemSelected));

                                    if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        startActivity(i);
                                    } else {
                                        requestPermissions(new String[]{CALL_PHONE}, 1);
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog tittle = alertCall.create();
                    tittle.setTitle("ACCION");
                    tittle.show();
                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un Contacto Para Poder Llamar", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void DeleteItem(){
        SQLiteDatabase db = connection.getWritableDatabase();
        String[] parameterId = {String.valueOf(listrowposition)};

        db.delete(Transaction.tableContacts, Transaction.id + "=?", parameterId);
        Toast.makeText(getApplicationContext(), "Contacto Eliminado", Toast.LENGTH_LONG).show();
    }

    private void getValuesToSendUpdateScreen(){
        SQLiteDatabase db = connection.getWritableDatabase();
        String[] parameterId = {String.valueOf(listrowposition)};
        String[] fields = {Transaction.id,
                Transaction.country,
                Transaction.name,
                Transaction.phone,
                Transaction.note};

        String whereCondition = Transaction.id + "=?";

        try {
            Cursor cursorQueryContact = db.query(Transaction.tableContacts, fields, whereCondition, parameterId,
                    null, null, null);

            cursorQueryContact.moveToFirst();

            Intent intentUpdate = new Intent(this, Pantalla3.class);
            Bundle sendValuesforUpdate = new Bundle();
            sendValuesforUpdate.putInt("id", cursorQueryContact.getInt(0));
            sendValuesforUpdate.putString("country", cursorQueryContact.getString(1));
            sendValuesforUpdate.putString("name", cursorQueryContact.getString(2));
            sendValuesforUpdate.putString("phone", cursorQueryContact.getString(3));
            sendValuesforUpdate.putString("note", cursorQueryContact.getString(4));

            intentUpdate.putExtras(sendValuesforUpdate);
            startActivity(intentUpdate);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Seleccione un Contacto Primero", Toast.LENGTH_SHORT).show();
        }
    }

    private void getListContacts() {
        SQLiteDatabase db = connection.getReadableDatabase();
        Contacts listContacts;
        arrayListContacts = new ArrayList<>();

        Cursor cursorQueryContacts = db.rawQuery("SELECT * FROM " + Transaction.tableContacts, null);

        while (cursorQueryContacts.moveToNext()) {
            listContacts = new Contacts();
            listContacts.setId(cursorQueryContacts.getInt(0));
            listContacts.setCountry(cursorQueryContacts.getString(1));
            listContacts.setName(cursorQueryContacts.getString(2));
            listContacts.setPhone(cursorQueryContacts.getString(3));
            listContacts.setNote(cursorQueryContacts.getString(4));

            arrayListContacts.add(listContacts);
        }

        cursorQueryContacts.close();
        fillList();
    }

    private void fillList() {
        arrayListStringContacts = new ArrayList<>();
        for(int i= 0; i < arrayListContacts.size(); i++){
            arrayListStringContacts.add(arrayListContacts.get(i).getId()+" | "
                    +arrayListContacts.get(i).getName() + " | "
                    +arrayListContacts.get(i).getPhone());
        }
    }

}