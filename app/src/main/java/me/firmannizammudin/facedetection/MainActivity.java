package me.firmannizammudin.facedetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {

    private Button btnProcess;
    private ImageView imgProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnProcess = (Button) findViewById(R.id.main_btn_process);
        imgProcess = (ImageView) findViewById(R.id.main_iv_imgproc);

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processImage();
            }
        });
    }

    private void processImage(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap imgBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.img1, options);

        Paint rect = new Paint();
        rect.setStrokeWidth(5);
        rect.setColor(Color.GREEN);
        rect.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(imgBitmap.getWidth(), imgBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(imgBitmap, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .build();
        if (!faceDetector.isOperational()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Could not set up the face detector!")
                    .show();
        }

        Frame frame = new Frame.Builder()
                .setBitmap(imgBitmap)
                .build();
        SparseArray<Face> faceSparseArray = faceDetector.detect(frame);

        for (int i = 0; i < faceSparseArray.size(); i++) {
            Face thisface = faceSparseArray.valueAt(i);
            float x1 = thisface.getPosition().x;
            float y1 = thisface.getPosition().y;
            float x2 = x1 + thisface.getWidth();
            float y2 = y1 + thisface.getHeight();
            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, rect);
        }
        imgProcess.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
}
