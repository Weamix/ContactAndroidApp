package com.example.contacts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;

public class QrCodeActivity extends AppCompatActivity {

    Bitmap bitmap ;
    public final static int QRcodeWidth = 400;
    private static final int blue = 0xFFFFFFFF;
    private static final int white = 0xFF000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        long id = getIntent().getLongExtra("id",38);
        String qrData = getIntent().getStringExtra("qrStr");

        // ImageView to display the QR code in.  This should be defined in
        // your Activity's XML layout file
        ImageView imageView = (ImageView) findViewById(R.id.qrCode);

        //String qrData = "Data I want to encode in QR code";
        //int qrCodeDimention = 400;

        /*QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);*/

        try {
            Bitmap bitmap = encodeAsBitmap(qrData);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    /**
     * Source : https://www.it-swarm.dev/fr/android/android-comment-lire-le-code-qr-dans-mon-application/940979426/
     */

    private Bitmap encodeAsBitmap(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ? blue:white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public static String convertContactToString(String firstname, String lastname, String phone, String mail, String address) {
        String qrData =  "CONTACT:" +
                "FIRSTNAME:" + firstname +
                "LASTNAME:" + lastname +
                "PHONE:" + phone +
                "MAIL:" + mail +
                "ADDRESS:" + address;
        return qrData;
    }

}
