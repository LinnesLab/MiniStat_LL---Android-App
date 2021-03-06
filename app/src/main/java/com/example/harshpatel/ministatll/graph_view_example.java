package com.example.harshpatel.ministatll;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import static android.R.id.message;

public class graph_view_example extends AppCompatActivity {

    //this gets the date and time, because you wanna remember when you did the experiment
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date;

    private static final String TAG = "graph_view_example";

//    double x;
//    double y;
    double[] xs = {-33,-67,-134,-201,-268,-336,-403,-470,-537 ,-604,
            -672, -739 ,-806 ,-739 ,-672 ,-604 ,-537 ,-470 ,-403 ,-336,
            -268 ,-201 ,-134 ,-67 ,-33 ,0 ,33,67,134 ,201,
            268, 336 ,403 ,470 ,537 ,604 ,672 ,739 ,806 ,739 ,
            672, 604 ,537 ,470 ,403 ,336 ,268 ,201 ,134 ,67 ,
             33 ,0, -33 ,-67 ,-134 ,-201 ,-268 ,-336 ,-403 ,-470 ,
            -537, -604 ,-672 ,-739 ,-806,-739 ,-672 ,-604 ,-537 ,-470,
            -403 ,-336 ,-268 ,-201 ,-134 ,-67 ,-33, 0 ,33 ,67 ,
            134, 201 ,268 ,336 ,403 ,470 ,537 ,604 ,672 ,739 ,
            806 ,739, 672 ,604 ,537 ,470 ,403 ,336 ,268 ,201 ,
            134 ,67 ,33,0};

    double[] ys = {-2209 ,-2448 ,-2328 ,-2090 ,-2090 ,-2090 ,-2209 ,-2448, -2567 ,-2567 ,
                  -2448 ,-2328 ,-2328 ,-2328 ,-2209 ,-2209 ,-2209, -1851 ,-1731,-1612 ,
                -1492 ,-1492 ,-1492 ,-1373 ,-1373 ,-1254, -1134 ,-537 ,2926 ,8539 ,
                10928 ,8897 ,6986 ,5912 ,5195 ,4717, 4359 ,4001 ,4001 ,3165 ,
            2687 ,2448 ,2328 ,2328 ,2209 ,1970, 1492 ,-537 ,-5314 ,-9375,
            -8420 ,-7345 ,-6389 ,-5792 -5314, -4478,-4120 ,-3762 ,-3642,-3642 ,
            -3881 ,-4120 ,-3762 ,-3523 , -3403 ,-3165 ,-3045 ,-2926 ,-2687 ,-2328,
            -2090 ,-1970 ,-1970, -1970 ,-1851 ,-1731 ,-1731 ,-1612,-1373 ,-895 ,
            2687 ,8420, 10569,8659 ,6628 ,5553 ,4956 ,4478 ,4120 ,3881 ,
            3762 ,3045, 2567 ,2328 ,2209 ,2090 ,2090 ,1851 ,1373 ,-656 ,
            -5434 ,-9495, -8539 ,-7464};


    static ArrayList<Double> target_x;
    static ArrayList<Double> target_y;

    int currentPosition;
    PointsGraphSeries<DataPoint> dataSeries;
    GraphView scatterPlot;
    Button add_points_button;
    boolean end_of_list;

    private ArrayList<XYValue> valueArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view_example);

        target_y = new ArrayList<Double>();
        target_x = new ArrayList<Double>();

        scatterPlot = (GraphView) findViewById(R.id.scatterPlot);
//        dataSeries = new PointsGraphSeries<>();
        valueArray = new ArrayList<>();
        add_points_button = (Button) findViewById(R.id.add_points_button);
        add_points_button.setEnabled(true);
        end_of_list = false;
        init();


    }

    private void init(){
        if (currentPosition-2 >= xs.length) {
            end_of_list = true;
        }

        dataSeries = new PointsGraphSeries<>();

        if (end_of_list) {
            add_points_button.setEnabled(false);
            Toast.makeText(getBaseContext(), "There are no more values to add...", Toast.LENGTH_LONG).show();
        }

        add_points_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!end_of_list){
                        if (end_of_list) {
                            add_points_button.setEnabled(false);
                            Toast.makeText(getBaseContext(), "There are no more values to add...", Toast.LENGTH_LONG).show();
                        }
                        addEntry();
                        if (end_of_list) {
                            add_points_button.setEnabled(false);
                            Toast.makeText(getBaseContext(), "There are no more values to add...", Toast.LENGTH_LONG).show();
                        }
                        init();
                    }

                }
        });


        if (valueArray.size() != 0) {
            createScatterPlot();
        }

    }

    private void addEntry() {

        if (currentPosition-2 >= xs.length) {
            end_of_list = true;
        }

        if (end_of_list) {
            add_points_button.setEnabled(false);
            Toast.makeText(getBaseContext(), "There are no more values to add...", Toast.LENGTH_LONG).show();
        }
/*        if (end_of_list) {
            add_points_button.setEnabled(false);
            Toast.makeText(getBaseContext(), "There are no more values to add...", Toast.LENGTH_LONG).show();
        }*/

        if (!end_of_list) {
            valueArray.add(new XYValue(xs[currentPosition], ys[currentPosition]));
            Log.e(TAG, "adding " + xs[currentPosition] + " " + ys[currentPosition] + " position " + currentPosition);
            valueArray = sortArray(valueArray);


            target_x.add(xs[currentPosition]);
            target_y.add(ys[currentPosition]);
            currentPosition++;
        }


//        if(valueArray.size() != 0) {
//            createScatterPlot();
//        } else {
//
//        }
    }

    private void createScatterPlot() {
//        valueArray = sortArray(valueArray);
//        valueArray.sort(ArrayList<XYValue> valueArray);

        for (int i = 0; i < valueArray.size(); i++) {
            try {
                double x = valueArray.get(i).getX();
                double y = valueArray.get(i).getY();
                //Log.e(TAG, "adding " + x + " " + y + ".");
                dataSeries.appendData(new DataPoint(x,y), true, 10000);
            } catch (IllegalArgumentException e) {

            }
        }


        dataSeries.setShape(PointsGraphSeries.Shape.POINT);
        dataSeries.setColor(Color.BLUE);
        dataSeries.setSize(5f);

        scatterPlot.getViewport().setScalable(true);
        scatterPlot.getViewport().setScalableY(true);
        scatterPlot.getViewport().setScrollable(true);
        scatterPlot.getViewport().setScrollableY(true);

        scatterPlot.getViewport().setYAxisBoundsManual(true);
        scatterPlot.getViewport().setMaxY(15000);
        scatterPlot.getViewport().setMinY(-15000);

        scatterPlot.getViewport().setXAxisBoundsManual(true);
        scatterPlot.getViewport().setMaxX(1000);
        scatterPlot.getViewport().setMinX(-1000);
        //Log.e(TAG, "appending data");

        scatterPlot.removeAllSeries();
        scatterPlot.addSeries(dataSeries);
    }

    private ArrayList<XYValue> sortArray (ArrayList<XYValue> array) {
        int factor = Integer.parseInt(String.valueOf(Math.round(Math.pow(array.size(),2))));
        int m = array.size() -1;
        int count = 0;

        while (true) {
            m--;
            if(m <= 0) {
                m = array.size() -1;
            }
            try {

                double tempX = array.get(m-1).getX();
                double tempY = array.get(m-1).getY();

                if(tempX > array.get(m).getX()) {
                    array.get(m - 1).setY(array.get(m).getY());
                    array.get(m).setY(tempY);

                    array.get(m - 1).setX(array.get(m).getX());
                    array.get(m).setX(tempX);
                } else if(tempX == array.get(m).getX()) {
                    count++;
                } else if(array.get(m).getX() > array.get(m-1).getX()) {
                    count++;
                }
                if(count == factor) {
                    break;
                }


            } catch (ArrayIndexOutOfBoundsException e) {
                e.getMessage();
                break;
            }
        }
        return array;
    }

    public void save_csv_and_send(View view) {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = simpleDateFormat.format(calendar.getTime());

        String data ="" + date + "\n";
        for (int i = 0; i < target_x.size(); i++) {
            data += "" + target_x.get(i) + ',' + target_y.get(i) + "\n";
        }

//        String file_name = "experiment.csv";
//        try {
//            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
//            fileOutputStream.write(data.getBytes());
//            fileOutputStream.close();
//            Toast.makeText(getBaseContext(), "Values are saved into experiment.csv", Toast.LENGTH_LONG).show();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//        emailIntent.setType("*/*");
//        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"sample_email@gmail.com"});
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
//                "Experiment Performed");
//        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
//                "go on read the emails");
//
//        Uri uri = Uri.fromFile(new File(file_name));
//        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        String FILENAME = "experiment.csv"; //it can be changed to .csv after
        String state;
        state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath()+"/MiniStatLL-App/");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir, FILENAME);
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(data.getBytes());
                out.close();
                Toast.makeText(getApplicationContext(), "Content saved!! File name is " + FILENAME, Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "SD card not found", Toast.LENGTH_LONG).show();
        }

//        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//        emailIntent.setType("*/*");
//
////        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"m"});
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
//                "Experiment Performed");
//        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
//                "go on read the emails");
//
//        File file2 = new File(Environment.getExternalStorageState()+"/MiniStatLL-App/" + FILENAME);
//        Uri uri = Uri.fromFile(file2);
//        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        File Root= Environment.getExternalStorageDirectory();
        String filelocation=Root.getAbsolutePath() + "/MiniStatLL-App/" + FILENAME;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*");
        String message="File to be shared is " + FILENAME + ".";
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+filelocation));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setData(Uri.parse("mailto:xyz@gmail.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }


}
