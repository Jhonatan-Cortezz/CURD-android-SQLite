package com.jac.crudsqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //componentes
    private EditText edtCodigo, edtDescripcion, edtPrecio;
    private Button btnGuardar, btnConsultar1, btnConsultar2, btnEliminar, btnActualizar;
    private TextView tvResultado;

    private FABToolbarLayout morph;

    boolean inputEt = false;
    boolean inputEd = false;
    boolean input1 = false;
    int resultadoInsert = 0;

    Modal ventanas = new Modal();

    ConexionSqlite conexion = new ConexionSqlite(this);
    Producto datos = new Producto();
    AlertDialog.Builder dialogo;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                    .setTitle("Advertencia")
                    .setMessage("¿Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    }).show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_exit_to_app_24));
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("Jhonatan Cortez");
        toolbar.setTitle("CRUD-SQLite");
        setSupportActionBar(toolbar);
        //asignar colores

        //this for full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmacion();
            }
        });

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ventanas.search(MainActivity.this);
            }
        });
        */

        //barra toolbar del floatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        morph = findViewById(R.id.fabtoolbar);
        View uno, dos, tres, cuatro, cinco;

        fab.setOnClickListener(this);
        uno = findViewById(R.id.uno);
        dos = findViewById(R.id.dos);
        tres = findViewById(R.id.tres);
        cuatro = findViewById(R.id.cuatro);
        cinco = findViewById(R.id.cinco);

        edtCodigo = findViewById(R.id.edtCodigo);
        edtDescripcion = findViewById(R.id.edDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnConsultar1 = findViewById(R.id.btnConsultarCode);
        btnConsultar2 = findViewById(R.id.btnCOnsultaDesc);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);

        uno.setOnClickListener(this);
        dos.setOnClickListener(this);
        tres.setOnClickListener(this);
        cuatro.setOnClickListener(this);
        cinco.setOnClickListener(this);

        String senal = "";
        String codigo = "";
        String descripcion = "";
        String precio = "";

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                codigo = bundle.getString("codigo");
                senal = bundle.getString("senal");
                descripcion = bundle.getString("descripcion");
                precio = bundle.getString("precio");

                if (senal.equals("1")){
                    edtCodigo.setText(codigo);
                    edtDescripcion.setText(descripcion);
                    edtPrecio.setText(precio);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void confirmacion(){
        String mensaje = "¿Realmente desea salir?";
        dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setIcon(R.drawable.ic_baseline_exit_to_app_24);//change icon
        dialogo.setTitle("Advertencia");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogo.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_limpiar) {
            edtCodigo.setText(null);
            edtDescripcion.setText(null);
            edtPrecio.setText(null);
            return true;
        } else if (id == R.id.action_listaArticulos){
            Intent spinnerActivity = new Intent(MainActivity.this, ActivityConsultaSpinner.class);
            startActivity(spinnerActivity);
            return true;
        } else if (id == R.id.action_listaArticulos1){
            Intent listViewActivity = new Intent(MainActivity.this, ListViewArticulos.class);
            startActivity(listViewActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alta(View view){
        if (edtCodigo.getText().toString().length() == 0){
            edtCodigo.setError("Campo obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }

        if (edtDescripcion.getText().toString().length() == 0){
            edtDescripcion.setError("Campo obligatorio");
            inputEd = false;
        } else {
            inputEd = true;
        }

        if (edtPrecio.getText().toString().length() == 0){
            edtPrecio.setError("Campo obligatorio");
            input1 = false;
        } else {
            input1 = true;
        }

        if (inputEt && inputEd && input1){
            try {
                datos.setCodigo(Integer.parseInt(edtCodigo.getText().toString()));
                datos.setDescripcion(edtDescripcion.getText().toString());
                datos.setPrecio(Double.parseDouble(edtPrecio.getText().toString()));

                if (conexion.insertTrad(datos)){
                    Toast.makeText(this, "Registro Agregado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                } else {
                    Toast.makeText(getApplicationContext(), "Error ya existe un registro con el mismo codigo", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }
            } catch (Exception ex){
                Toast.makeText(this, "Error, ya existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mensaje(String mensaje){
        Toast.makeText(this, "" + mensaje, Toast.LENGTH_SHORT).show();
    }

    public void limpiarDatos(){
        edtCodigo.setText(null);
        edtDescripcion.setText(null);
        edtPrecio.setText(null);
        edtCodigo.requestFocus();
    }

    public void consultaCode(View view){
        if (edtCodigo.getText().toString().length() == 0){
            edtCodigo.setError("Campo obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }

        if (inputEt){
            String codigo = edtCodigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));

            if (conexion.consultaArticulos(datos)){
                edtDescripcion.setText(datos.getDescripcion());
                edtPrecio.setText(""+datos.getPrecio());
            } else {
                Toast.makeText(this, "No existe articulo con dicho codigo", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        } else {
            Toast.makeText(this, "Ingrese el codigo del articulo a buscar", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultaDesc(View view){

        if (edtDescripcion.getText().toString().length() == 0){
            edtDescripcion.setError("Campo obligatorio");
            inputEd = false;
        } else {
            inputEd = true;
        }

        if (inputEd){
            String descripcion = edtDescripcion.getText().toString();
            datos.setDescripcion(descripcion);
            if (conexion.consultaDescripcion(datos)){
                edtCodigo.setText(""+datos.getCodigo());
                edtDescripcion.setText(datos.getDescripcion());
                edtPrecio.setText(""+datos.getPrecio());
            } else {
                Toast.makeText(this, "No existe articulo con dicha descripcion", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        } else {
            Toast.makeText(this, "Ingrese la descripcion del articulo a buscar", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCode(View view){
        if (edtCodigo.getText().toString().length() == 0){
            edtCodigo.setError("Campo obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }

        if (inputEt){
            String cod = edtCodigo.getText().toString();
            datos.setCodigo(Integer.parseInt(cod));
            if (conexion.bajaCodigo(MainActivity.this, datos)){
                limpiarDatos();
            } else {
                Toast.makeText(this, "No existe articulo con dicho codigo", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }
    }

    public void editar(View view){
        if (edtCodigo.getText().toString().length() == 0){
            edtCodigo.setError("Campo obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }

        if (inputEt){
            String cod = edtCodigo.getText().toString();
            String descripcion = edtDescripcion.getText().toString();
            double precio = Double.parseDouble(edtPrecio.getText().toString());

            datos.setCodigo(Integer.parseInt(cod));
            datos.setDescripcion(descripcion);
            datos.setPrecio(precio);

            if (conexion.modifcar(datos)){
                Toast.makeText(this, "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se ha encontrado resultado para la busqueda", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab){
            morph.show();
        }

        switch (view.getId()){
            case R.id.uno:
                alta(view);
                break;

            case R.id.dos:
                deleteCode(view);
                break;

            case R.id.tres:
                editar(view);
                break;

            case R.id.cuatro:
                morph.hide();
                break;

            case R.id.cinco:
                ventanas.search(MainActivity.this);
                break;
        }
    }
}