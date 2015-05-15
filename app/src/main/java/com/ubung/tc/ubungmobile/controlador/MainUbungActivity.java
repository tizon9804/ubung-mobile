package com.ubung.tc.ubungmobile.controlador;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.GettingStartAdapter;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.Ubung;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;

import java.util.ArrayList;


public class MainUbungActivity extends Activity {

    public static final String POSITION = "position";
    public static final String ID = "id";
    public static final String USER = "usuario";
    public static final String PHONE = "phone";

    private Ubung singleton;
    protected boolean active = true;
    protected int ubungTime = 1000;
    private Thread ubungThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        animationUbung();
        Singleton singleton = Singleton.getInstance();
        this.singleton = singleton;


    }

    private void animationUbung() {

        setContentView(R.layout.inicio_ubung);
        TextView deslice = (TextView) findViewById(R.id.deslice_inicio);
        deslice.setText("");
        final MainUbungActivity context = this;
        final Handler h= new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                boolean b=(boolean)msg.obj;
                if(b) {
                    setContentView(R.layout.activity_main_ubung);
                    GettingStartAdapter adapter = new GettingStartAdapter(context);
                    ViewPager myPager = (ViewPager) findViewById(R.id.gettingstartpager);
                    myPager.setAdapter(adapter);
                    myPager.setCurrentItem(0);
                }
                else{
                    openMap();
                }
                return true;
            }
        });

        ubungThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    while (ApplicationUbung.active) {
                        sleep(100);
                    }
                } catch (InterruptedException e) {

                } finally {
                    Message m=new Message();
                    if (singleton.darPropietario() != null) {
                        m.obj=false;
                        h.sendMessage(m);
                    } else {
                        m.obj=true;
                        h.sendMessage(m);
                    }

                }
            }
        };
        ubungThread.start();
    }

    // -----------------------------------------------------
// carga informacion
// -----------------------------------------------------
    public void initView() throws ParseException {
        //recorre la lista de deportes
        final ArrayList<Deporte> deportes = Singleton.getInstance().darDeportes();
        if (deportes != null) {
            Integer[] identificadores = new Integer[deportes.size()];
            for (int i = 0; i < deportes.size(); i++) {
                //asigna la imagen del deporte a su respectivo boton
                ImageButton button = (ImageButton) findViewById(getResources().getIdentifier(ChooseSportActivity.IMAGEBUTTON + i, "id", getPackageName()));
                button.setImageResource(getResources().getIdentifier(deportes.get(i).getNombreArchivoImagen(), "drawable", getPackageName()));
                final int finalI = i;
                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            //todo boton seleccionado
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                        //    String usuario = ((EditText) findViewById(R.id.user_name)).getText().toString();
                       //     String phone = ((EditText) findViewById(R.id.phone)).getText().toString();

                        //    intentDescription(deportes.get(finalI).getId(), v.getId(), usuario, phone);
                        }
                        return true;
                    }
                });
            }

        } else {
            Log.e("Carga deportes", " deportes[]:" + deportes);
            Toast.makeText(this, "Hubo un problema al Cargar Deportes ", Toast.LENGTH_LONG).show();
        }

    }

    public void intentDescription(String id_deporte, long id, String usuario, String phone) {
        finish();
        Intent t = new Intent(this, DescriptionSportActivity.class);
        t.putExtra(POSITION, id_deporte + "");
        t.putExtra(ID, id + "");
        t.putExtra(USER, usuario);
        t.putExtra(PHONE, phone);
        startActivity(t);
    }


    public void initUser_registation() {

        final Button registrar=(Button)findViewById(R.id.button_registrar);
        final Button iniciar=(Button)findViewById(R.id.button_iniciar_sesion);

        registrar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    registrar.setTextColor(getResources().getColor(R.color.holo_red_dark));

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    registrar.setTextColor(getResources().getColor(R.color.white));
                    LoginActivity l= new LoginActivity();
                    newActivity(RegisterActivity.class);

                }
                return true;
            }

        });

        iniciar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    iniciar.setTextColor(getResources().getColor(R.color.holo_red_dark));

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                   iniciar.setTextColor(getResources().getColor(R.color.white));
                    LoginActivity l= new LoginActivity();
                    newActivity(LoginActivity.class);

                }
                return true;
            }

        });
       // final EditText user = (EditText) findViewById(R.id.user_name);
       // user.requestFocus();
      //  user.setOnTouchListener(new View.OnTouchListener() {
      //      @Override
      //      public boolean onTouch(View v, MotionEvent event) {
        //        user.requestFocus();
        //        InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        //        return true;
        //    }
       //
    }

    private void openMap() {
        finish();
        startActivity(new Intent(this, LocationActivity.class));
    }

    protected void onResume() {
        super.onResume();
        try {
            Singleton singleton = Singleton.getInstance();
            singleton.inicializar(this.getApplicationContext());
            this.singleton = singleton;
            // Capturar el Intent enviado por el OS
            Intent intent = getIntent();
            // Si el Intent corresponde a NFC
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                Log.w("MainUbung.onResume()",intent.getAction()+" corresponde a un evento de NFC");
                // Se lo paso al síngleton para que lo procese y recupere el evento correspondiente
                Evento evento = singleton.recibirEventoNFC(intent);
                // Creo el Intent para pasarle la información del evento a la actividad correspondiente
                Intent t= new Intent(this,DescripcionProgramacionActivity.class);
                t.putExtra(ListaZonasActivity.ZONA,evento.getZona().getId());
                t.putExtra(POSITION,evento.getId());
                // Inicio la actividad correspondiente
                startActivity(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.w("MainUbung.onNewIntent()","Se ha recibido un nuevo Intent con acción "+intent.getAction());
        setIntent(intent);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Singleton singleton = Singleton.getInstance();
        try {
            singleton.inicializar(this.getApplicationContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void newActivity(Class activity){
        Intent t= new Intent(this,activity);
        startActivity(t);
    }
}
