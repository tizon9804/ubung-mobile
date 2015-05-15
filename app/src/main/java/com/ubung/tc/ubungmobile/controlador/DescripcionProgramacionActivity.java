package com.ubung.tc.ubungmobile.controlador;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.controlador.adapters.ListaInscritosAdapter;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionComunicacion;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.Date;


public class DescripcionProgramacionActivity extends ActionBarActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private static final int EVENTO_ENVIADO = 1;

    private static final String INSCRITOS_TITULO = "Inscritos: ";
    private Evento evento;
    private ListView inscritos;
    private ListaInscritosAdapter l;
    private Zona zona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_programacion);
        String id_zona = getIntent().getStringExtra(ListaZonasActivity.ZONA);

        Singleton.getInstance().setNfcActivity(this);

        try {
            zona=Singleton.getInstance().darZona(id_zona);
            setTitle(zona.getNombre());
            String id_evento= getIntent().getStringExtra(MainUbungActivity.POSITION);
            evento = Singleton.getInstance().darEvento(id_evento);
            if (evento != null) {
                //llamado a todos los elementos graficos
                TextView organiza = (TextView) findViewById(R.id.txt_organiza);
                ImageView img = (ImageView) findViewById(R.id.image_descripcion_programacion);
                TextClock hora = (TextClock) findViewById(R.id.textClock_descripcion_programacion);
                TextView inscritosTitulo = (TextView) findViewById(R.id.title_inscritos_descripcion_programacion);
                //carga de informacion a los elementos graficos
                initListInscritos();
                try {
                    organiza.setText(evento.getUsuarioOrganizador().getNombreUsuario());
                    img.setImageResource(getResources().getIdentifier(evento.getDeporte().getNombreArchivoImagen(), "drawable", getPackageName()));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date d = evento.getFechaHoraEvento();
                String horap = d.getHours() + ":" + d.getMinutes();
                hora.setText(horap);
                inscritosTitulo.setText(INSCRITOS_TITULO + evento.getUsuariosInscritos().size());
            }
            initButtons();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initListInscritos() {
        inscritos = (ListView) findViewById(R.id.listView_inscritos);
        l = new ListaInscritosAdapter(this, evento);
        inscritos.setAdapter(l);
        inscritos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void initButtons() {
        final Button start = (Button) findViewById(R.id.button_inscripcion);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });
        start.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start.setTextColor(getResources().getColor(R.color.holo_orange_light));

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    start.setTextColor(getResources().getColor(R.color.white));
                    try {
                        Singleton.getInstance().inscribirseEvento(evento.getId());
                        Toast.makeText(getBaseContext(), "Se ha inscrito al evento", Toast.LENGTH_LONG).show();
                        nextActivity();
                    } catch (ExcepcionComunicacion excepcionComunicacion) {
                        excepcionComunicacion.printStackTrace();
                    } catch (ParseException e) {
                        Toast.makeText(getBaseContext(), "Hubo un problema de inscripción: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        nextActivity();
                    }

                }
                return true;
            }
        });
    }

// -----------------------------------------------------
// CONEXION SIGUIENTE ACTIVIDAD
// -----------------------------------------------------

    private void nextActivity() {
        finish();
        Intent i = new Intent(this, LocationActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent t = new Intent(this, ProgramacionActivity.class);
        t.putExtra(ListaZonasActivity.ZONA,zona.getNombre());
        t.putExtra(MainUbungActivity.POSITION, getIntent().getStringExtra(ListaZonasActivity.ZONA));
        startActivity(t);

    }
// -----------------------------------------------------
// Compartir actividad por NFC
// -----------------------------------------------------
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage ndefMessage = Singleton.getInstance().enviarEventoNFC(evento.getId());
        return ndefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        handler.obtainMessage(EVENTO_ENVIADO).sendToTarget();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case EVENTO_ENVIADO:
                    // ToDo manejar string en archivo XML
                    Singleton.getInstance().notificarUsuario("Se ha compartido el evento vía NFC");
                    break;
            }
        }
    };

    protected void onResume() {
        super.onResume();
        try {
            Singleton singleton = Singleton.getInstance();
            singleton.inicializar(this.getApplicationContext());
            // Capturar el Intent enviado por el OS
            Intent intent = getIntent();
            // Si el Intent corresponde a NFC
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                Log.w("DescrProgAct.onResume()", intent.getAction() + " corresponde a un evento de NFC");
                // Se lo paso al síngleton para que lo procese y recupere el evento correspondiente
                Evento evento = singleton.recibirEventoNFC(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.w("DescrProgAct.onNewIntent()","Se ha recibido un nuevo Intent con acción "+intent.getAction());
        setIntent(intent);
    }
}
