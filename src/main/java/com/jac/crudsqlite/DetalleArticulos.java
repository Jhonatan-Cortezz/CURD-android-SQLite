package com.jac.crudsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetalleArticulos extends AppCompatActivity{
    private TextView tv_codigo, tv_descripcion, tv_precio;
    private TextView tv_codigo1, tv_descripcion1, tv_precio1, tv_fecha;
    private Button btnAcuali;

    Producto datos = new Producto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulos);

        /*
        tv_codigo = findViewById(R.id.tvCodigo);
        tv_descripcion = findViewById(R.id.tvDescripcion);
        tv_precio = findViewById(R.id.tvPrecio);*/

        tv_codigo1 = findViewById(R.id.tvCodigo1);
        tv_descripcion1 = findViewById(R.id.tvDescripcion1);
        tv_precio1 = findViewById(R.id.tvPrecio1);
        tv_fecha = findViewById(R.id.tvFecha);
        btnAcuali = findViewById(R.id.btnActualizarSP);


        Bundle objeto = getIntent().getExtras();
        Producto dto = null;
        if (objeto != null){
            dto = (Producto) objeto.getSerializable("articulo");
            /*
            tv_codigo.setText(""+ dto.getCodigo());
            tv_descripcion.setText(dto.getDescripcion());
            tv_precio.setText(String.valueOf(dto.getPrecio()));*/

            tv_codigo1.setText(""+ dto.getCodigo());
            tv_descripcion1.setText(dto.getDescripcion());
            tv_precio1.setText(String.valueOf(dto.getPrecio()));
            tv_fecha.setText("Fecha de creacion: " + getDateTime());
        }
    }

    private String getDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

}