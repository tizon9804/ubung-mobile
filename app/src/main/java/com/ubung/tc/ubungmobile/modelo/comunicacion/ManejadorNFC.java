package com.ubung.tc.ubungmobile.modelo.comunicacion;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.provider.Settings;

import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionComunicacion;

/**
 * Created by cvargasc on 13/05/15.
 */
public class ManejadorNFC implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private Singleton singleton;
    private NfcAdapter nfcAdapter;

    public ManejadorNFC(Singleton singleton) {
        this.singleton = singleton;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(singleton.darContexto());
    }

    public void enviarIdEvento(String idEvento) {
        if (!nfcAdapter.isEnabled()) {
            singleton.darContexto().startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        if (!nfcAdapter.isNdefPushEnabled()) {
            singleton.darContexto().startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }

    }

    public String recibirIdEvento(Intent intent) throws ExcepcionComunicacion {
        String accion = intent.getAction();

        if (accion.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage ndefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] ndefRecords = ndefMessage.getRecords();
            NdefRecord ndefRecord = ndefRecords[0];
            return new String(ndefRecord.getPayload());

        } else {
            throw new ExcepcionComunicacion("La acción del Intent no corresponde al intercambio de un evento :: "+accion);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return null;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }
}
