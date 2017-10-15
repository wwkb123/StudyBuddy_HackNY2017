package lagcc.studybuddy;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class CameraActivity extends Fragment {

    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.camera, container, false);
        cameraView = v.findViewById(R.id.surfaceView);
        textView = v.findViewById(R.id.text_view);

        final Button button = v.findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {

                int count = 0;
                String search_keyword="";
                for(String word:textView.getText().toString().trim().split("\\s")){
                    count++;
                    search_keyword=word;
                    if(count>1){
                        break;
                    }
                }
                search_keyword = search_keyword.trim();

                Bundle bundle = new Bundle();
                bundle.putString("search_keyword", search_keyword);
                Fragment fragment = new SearchResult();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFrame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        final Button button2 = v.findViewById(R.id.clear);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vvv) {
                textView.setText("");
            }
        });

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {

            cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    System.out.println(item.getValue());
                                    if(item.getValue().contains("integration") || item.getValue().contains("Integration")|| item.getValue().contains("calculus")|| item.getValue().contains("Calculus")){
                                        String tag="calculus";
//                                        stringBuilder.append("Tommy");
                                        String curr_text = textView.getText().toString();
                                        if(!curr_text.contains(tag)) {
                                            textView.setText(curr_text + "\n" + tag);
                                        }
                                }
                                if(item.getValue().contains("endothermic") || item.getValue().contains("Reaction")||item.getValue().contains("2H")|| item.getValue().contains("equation")||item.getValue().contains("H")&&item.getValue().contains("O")){
                                    String tag="chemistry";
                                    String curr_text = textView.getText().toString();
                                    if(!curr_text.contains(tag)){
                                        textView.setText(curr_text+"\n"+tag);
                                    }
                                }
                                    stringBuilder.append("\n");
                                }
//                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    return v;
    }

}