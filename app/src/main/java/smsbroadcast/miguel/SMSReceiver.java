package smsbroadcast.miguel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import smsbroadcast.smsbroadcast.R;

public class SMSReceiver extends BroadcastReceiver {
    // SmsManager class is responsible for all SMS related actions
    final SmsManager sms = SmsManager.getDefault();
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    // Get sender phone number
                    String phoneNumber = sms.getDisplayOriginatingAddress();
                    String sender = phoneNumber;
                    String message = sms.getDisplayMessageBody();

                    String[] contentSms = message.split(":");
                    String typeSms = contentSms[0];
                    Uri uri = Uri.parse(contentSms[1]);
                    Log.d("DEBUG", typeSms);
                    Log.d("DEBUG", String.valueOf(uri));

                    String formattedText = String.format(context.getResources().getString(R.string.sms_message), sender, message);
                    // Display the SMS message in a Toast
                    Toast.makeText(context, formattedText, Toast.LENGTH_LONG).show();
                    MainActivity inst = MainActivity.instance();
                    inst.updateList(formattedText);
                    Log.d("DEBUG", "updatedList");
                    inst.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}