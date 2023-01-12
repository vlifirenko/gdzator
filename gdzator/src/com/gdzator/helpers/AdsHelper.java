package com.gdzator.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;

import com.gdzator.content.provider.Contract;
import com.gdzator.content.provider.DatabaseHelper;
import com.gdzator.ui.MainActivity;

import java.security.MessageDigest;

public class AdsHelper {

    public static boolean checkOffAds(Context context) {
        SQLiteDatabase db = null;
        try {
            db = DatabaseHelper.getInstance(context).getReadableDatabase();
            Cursor c = db.query(Contract.PurchaseData.TABLE_NAME, null,
                    Contract.PurchaseData.COLUMN_NAME_KEY + "= '" + MainActivity.FIELD_OFF_ADS + "'", null, null, null, null);
            c.moveToFirst();
            if (c.getCount() == 0)
                return false;
            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            byte[] bytesOfMessage = (android_id + MainActivity.big_string).getBytes("UTF-8");
            String code = new String(MessageDigest.getInstance("MD5").digest(bytesOfMessage));
            String codeFromBd = c.getString(c.getColumnIndex(Contract.PurchaseData.COLUMN_NAME_CODE));
            c.close();
            return code.equals(codeFromBd);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null)
                db.close();
        }
    }
}
