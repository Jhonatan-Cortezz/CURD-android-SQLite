package com.jac.crudsqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Modal {
    Dialog myDialog;
    AlertDialog.Builder dialogo;

    boolean validaInput = false;
    String codigo;
    String descripcion;
    String precio;

    SQLiteDatabase bd = null;

    Producto datos = new Producto();

    public void search(final Context context){
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.ventana1);
        myDialog.setTitle("Buscar");
        myDialog.setCancelable(false);
        final ConexionSqlite conexion = new ConexionSqlite(context);
        final EditText et_cod = myDialog.findViewById(R.id.etCod);
        Button btnBuscar = myDialog.findViewById(R.id.btnBuscar);
        TextView tvClose = myDialog.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_cod.getText().toString().length() == 0){
                    validaInput = false;
                    et_cod.setError("Campo obligatorio");
                } else {
                    validaInput = true;
                }

                if (validaInput){
                    String cod = et_cod.getText().toString();
                    datos.setCodigo(Integer.parseInt(cod));

                    if (conexion.consultaCodigo(datos)){
                        codigo = String.valueOf(datos.getCodigo());
                        descripcion = datos.getDescripcion();
                        precio = String.valueOf(datos.getPrecio());

                        String action;

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("senal", "1");
                        intent.putExtra("codigo", codigo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("precio", precio);
                        context.startActivity(intent);

                        myDialog.dismiss();
                    } else {
                        Toast.makeText(context, "No se ha encontrado para la busqueda especificada", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "No ha especificado ninguna busqueda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
    }
}
