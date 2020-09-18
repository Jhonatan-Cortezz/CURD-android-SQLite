package com.jac.crudsqlite;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ConexionSqlite extends SQLiteOpenHelper {

    boolean estadoDelete = true;
    ArrayList<String> listaArticulos;

    //representa la tabla producto
    ArrayList<Producto> artList;



    public ConexionSqlite(Context context) {
        super(context, "articulos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table articulos(codigo integer not null primary key, descripcion text, precio real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists articulos");
        onCreate(db);
    }

    public SQLiteDatabase bd(){
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd;
    }

    public boolean insertTrad(Producto dto){
        boolean estado = true;
        int resultado;
        try {
            int codigo = dto.getCodigo();
            String descripcion = dto.getDescripcion();
            double precio = dto.getPrecio();

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + codigo + "' ", null);

            if (fila.moveToFirst()== true){
                estado = false;
            } else {
                String Sql = "insert into articulos " + "(codigo, descripcion, precio) " +
                        "values " + "('" + String.valueOf(codigo) + "', '" + descripcion + "', '" + String.valueOf(precio) + "');";
                bd().execSQL(Sql);
                bd().close();
                estado = true;
            }
        } catch (Exception ex){
            estado = false;
            Log.e(" Error ", ex.toString());
        }
        return estado;
    }

    public boolean intertarDatos(Producto dto){
        boolean estado = true;
        int resultado;
        ContentValues registro = new ContentValues();
        try {
            registro.put("codigo", dto.getCodigo());
            registro.put("descripcion", dto.getDescripcion());
            registro.put("precio", dto.getPrecio());
            Cursor fila = bd().rawQuery("select * from articulos where codigo='" + dto.getCodigo()+ "'", null);
            if (fila.moveToFirst() == true){
                estado = false;
            } else {
                resultado = (int) bd().insert("articulos", null, registro);
                if (resultado > 0){
                    estado = true;
                } else {
                    estado = false;
                }
            }

        } catch (Exception ex){
            estado = false;
            ex.printStackTrace();
        }

        return estado;
    }

    public boolean insertFile(Producto dto){
        boolean estado = true;
        int resultado;
        try {
            int codigo = dto.getCodigo();
            String descripcion = dto.getDescripcion();
            double precio = dto.getPrecio();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha1 = sdf.format(calendar.getTime());

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + dto.getCodigo() + "'", null);
            if (fila.moveToFirst()== true){
                estado = false;
            } else {
                String SQL = "insert into articulos (codigo, descripcion, precio) values(?, ?, ?);";

                bd().execSQL(SQL, new String[]{String.valueOf(codigo), descripcion, String.valueOf(precio)});
                estado = true;
            }
        } catch (Exception ex){
            estado = false;
            Log.e("error", ex.toString());
        }

        return estado;
    }

    public boolean consultaCodigo(Producto dto){
        boolean estado = true;
        int resultado;

        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = dto.getCodigo();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos where codigo=" + codigo, null);
            if (fila.moveToFirst()){
                dto.setCodigo(Integer.parseInt(fila.getString(0)));
                dto.setDescripcion(fila.getString(1));
                dto.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }

            bd.close();
        } catch (Exception e){
            estado = false;
            Log.e("Error", e.toString());
        }

        return estado;
    }

    public boolean consultaArticulos(Producto dto){
        boolean estado = true;
        int resultao;

        SQLiteDatabase bd = this.getReadableDatabase();
        try{
            String[] parametros = {String.valueOf(dto.getCodigo())};
            String[] campos = {"codigo", "descripcion", "precio"};
            Cursor fila = bd.query("articulos", campos, "codigo=?", parametros, null, null, null);

            if (fila.moveToFirst()){
                dto.setCodigo(Integer.parseInt(fila.getString(0)));
                dto.setDescripcion(fila.getString(1));
                dto.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }
            fila.close();
            bd.close();
        } catch (Exception ex){
            estado = false;
            Log.e("error", ex.toString());
        }

        return estado;
    }

    public boolean consultaDescripcion(Producto dto){
        boolean estado = true;
        int resultao;

        SQLiteDatabase bd = this.getReadableDatabase();
        try{
            String[] parametros = {String.valueOf(dto.getDescripcion())};
            String[] campos = {"codigo", "descripcion", "precio"};
            Cursor fila = bd.query("articulos", campos, "descripcion=?", parametros, null, null, null);

            if (fila.moveToFirst()){
                dto.setCodigo(Integer.parseInt(fila.getString(0)));
                dto.setDescripcion(fila.getString(1));
                dto.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }
            fila.close();
            bd.close();
        } catch (Exception ex){
            estado = false;
            Log.e("error", ex.toString());
        }

        return estado;
    }

    public boolean bajaCodigo(final Context context, final Producto dto){

        estadoDelete = true;
        try {
            int codigo = dto.getCodigo();
            Cursor fila = bd().rawQuery("select * from articulos where codigo=" + codigo,null);
            if (fila.moveToFirst()){
                dto.setCodigo(Integer.parseInt(fila.getString(0)));
                dto.setDescripcion(fila.getString(1));
                dto.setPrecio(Double.parseDouble(fila.getString(2)));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_launcher_background);
                builder.setTitle("Alerta");
                builder.setMessage("¿Está seguro de borrar el registro? \nCódigo: " + dto.getCodigo() +
                        "\nDescripcion: " + dto.getDescripcion());
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int codigo = dto.getCodigo();
                        int cant = bd().delete("articulos", "codigo=" + codigo, null);

                        if (cant > 0){
                            estadoDelete = true;
                            Toast.makeText(context, "Registro eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();
                        } else {
                            estadoDelete = false;
                        }
                        bd().close();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(context, "No hay coincidencias en la busqueda", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex){
            estadoDelete = false;
            Log.e("Error", ex.toString());
        }

        return estadoDelete;
    }

    public boolean modifcar(Producto dto){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = dto.getCodigo();
            String descripcion = dto.getDescripcion();
            double precio = dto.getPrecio();

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cant = (int) bd.update("articulos", registro, "codigo=" + codigo, null);

            bd.close();
            if (cant > 0){
                estado = true;
            } else {
                estado = false;
            }

        } catch (Exception x){
            estado = false;
            Log.e("Error", x.toString());
        }

        return estado;
    }

    public ArrayList<Producto> consultaListaArtSpiner(){
        boolean estado = false;

        SQLiteDatabase bd = this.getReadableDatabase();
        Producto art = null;
        artList = new ArrayList<Producto>();

        try {
            Cursor fila = bd.rawQuery("select * from articulos", null);
            while (fila.moveToNext()){
                art = new Producto();
                art.setCodigo(fila.getInt(0));
                art.setDescripcion(fila.getString(1));
                art.setPrecio(fila.getDouble(2));

                artList.add(art);

                Log.i("codigo", String.valueOf(art.getCodigo()));
                Log.i("descripcion", art.getDescripcion().toString());
                Log.i("precio", String.valueOf(art.getPrecio()));
            }

            obtenerListaArticulos();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return artList;
    }

    public ArrayList<String> obtenerListaArticulos() {
        listaArticulos = new ArrayList<String>();

        listaArticulos.add("Seleccione");

        for (int i = 0; i < artList.size(); i++){
            listaArticulos.add(artList.get(i).getCodigo()+" - "+ artList.get(i).getDescripcion());
        }

        return listaArticulos;
    }

    public ArrayList<String> consultaListaArticulo1(){
        boolean estado = false;
        SQLiteDatabase bd = this.getReadableDatabase();

        Producto articulo = null;
        artList = new ArrayList<Producto>();

        try {
            Cursor fila = bd.rawQuery("select * from articulos", null);
            while(fila.moveToNext()){
                articulo = new Producto();
                articulo.setCodigo(fila.getInt(0));
                articulo.setDescripcion(fila.getString(1));
                articulo.setPrecio(fila.getDouble(2));

                artList.add(articulo);
            }

            listaArticulos = new ArrayList<String>();

            for (int i=0; i< artList.size(); i++ ){
                listaArticulos.add(artList.get(i).getCodigo() + " - " + artList.get(i).getDescripcion());
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return listaArticulos;
    }

}
