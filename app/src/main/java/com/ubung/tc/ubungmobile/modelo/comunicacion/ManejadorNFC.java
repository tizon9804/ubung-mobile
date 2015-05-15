package com.ubung.tc.ubungmobile.modelo.comunicacion;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;

import com.ubung.tc.ubungmobile.controlador.DescripcionProgramacionActivity;
import com.ubung.tc.ubungmobile.modelo.Singleton;

/**
 * Created by cvargasc on 13/05/15.
 */
public class ManejadorNFC {

    public static final String LOG_NAME = "ManejadorNFC";

    private Singleton singleton;
    private NfcAdapter nfcAdapter;

    public ManejadorNFC(Singleton singleton) {
        this.singleton = singleton;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(singleton.darContexto());
    }

    public String recibirIdEvento(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        return new String(msg.getRecords()[0].getPayload());
    }

    public void definirNfcActivity(DescripcionProgramacionActivity activity) {
        nfcAdapter.setNdefPushMessageCallback(activity, activity);
        nfcAdapter.setOnNdefPushCompleteCallback(activity, activity);
    }

    public NdefMessage enviarIdEvento(String idEvento) {

        Log.e(LOG_NAME+"enviarId","Enviando evento "+idEvento);

        if (!nfcAdapter.isEnabled()) {
            singleton.darContexto().startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        if (!nfcAdapter.isNdefPushEnabled()) {
            singleton.darContexto().startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        byte[] bytesOut = idEvento.getBytes();

        NdefMessage ndefMessage = new NdefMessage(NdefRecord.createMime("application/com.ubung.tc.ubungmobile",
                idEvento.getBytes()),NdefRecord.createApplicationRecord("com.ubung.tc.ubungmobile"));
        return ndefMessage;
    }
}
