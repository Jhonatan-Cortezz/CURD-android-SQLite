package com.jac.crudsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityConsultaSpinner extends AppCompatActivity {

    private Spinner sp_options;
    private TextView tv_cod, tv_descripcion, tv_precio;

    ConexionSqlite conexion = new ConexionSqlite(this);
    Producto datos = new Producto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_spinner);

        sp_options = findViewById(R.id.sp_options);
        tv_cod = findViewById(R.id.tvCod);
        tv_descripcion = findViewById(R.id.tvDescripcionSp);
        tv_precio = findViewById(R.id.tvPrecioSp);

        conexion.consultaListaArtSpiner();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, conexion.obtenerListaArticulos());
        sp_options.setAdapter(adapter);

        sp_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    tv_cod.setText("Código: " + conexion.consultaListaArtSpiner().get(i-1).getCodigo());
                    tv_descripcion.setText("Descripcion: " + conexion.consultaListaArtSpiner().get(i-1).getDescripcion());
                    tv_precio.setText("Precio: "+ String.valueOf(conexion.consultaListaArtSpiner().get(i-1).getPrecio()));
                } else{
                    tv_cod.setText("Codigo: ");
                    tv_descripcion.setText("Descripcion: ");
                    tv_precio.setText("Precio: ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}