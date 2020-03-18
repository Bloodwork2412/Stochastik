package com.example.stochastik;

import androidx.appcompat.app.AppCompatActivity;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editNumber;
    Button startButton;
    boolean[] checked;
    Integer[] firstDouble;
    Integer[] allAt;
    int repeat;
    TableLayout table;

    int sumDoppelt;
    int anzDoppelt;
    int sumAll;
    int anzAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNumber = findViewById(R.id.editNumber);
        startButton = findViewById(R.id.startButton);
        table = findViewById(R.id.table);
    }

    public void clicked(View view){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startRandom();
            }
        });
        thread.start();
    }

    public void startRandom() {
        repeat = Integer.parseInt(editNumber.getText().toString());
        Switch switcher = findViewById(R.id.switcher);
        int maxRolls = 100;
        if(switcher.isChecked()){
            maxRolls = 50;
        }
        System.out.println(!switcher.isChecked());

        firstDouble = new Integer[repeat];
        allAt = new Integer[repeat];
        anzAll = 0;
        sumAll = 0;
        anzDoppelt = 0;
        sumDoppelt = 0;

        for (int i = 0; i < repeat; i++) {
            checked = new boolean[6];
            for (int j = 0; j < checked.length; j++) {
                checked[j] = false;
            }
            for (int n = 0; n < maxRolls; n++) {

                int rand = (int) (Math.random() * 6);
                if (checked[rand] && firstDouble[i] == null) {
                    firstDouble[i] = n + 1;
                }
                checked[rand] = true;
                boolean gotAll = true;
                for (int g = 0; g < 6; g++) {
                    if (!checked[g]) {
                        gotAll = false;
                    }
                }
                if (gotAll) {
                    allAt[i] = n + 1;
                    n = maxRolls;
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                print();
            }
        });
    }

    public void print(){
        table.removeAllViews();
        for(int i = 0; i < repeat; i++) {
            TextView view1 = new TextView(this);
            TextView view2 = new TextView(this);
            TextView view3 = new TextView(this);
            view1.setGravity(Gravity.END);
            view2.setGravity(Gravity.END);
            view3.setGravity(Gravity.END);
            TableRow row = new TableRow(this);
            String text = Integer.toString(i + 1);
            view1.setText(text);
            row.addView(view1);
            if(allAt[i] != null) {
                view2.setText(allAt[i].toString());
                anzAll ++;
                sumAll += allAt[i];
            } else {
                view2.setText("nicht gefunden");
            }
            row.addView(view2);
            if(firstDouble[i] != null) {
                view3.setText(firstDouble[i].toString());
                anzDoppelt ++;
                sumDoppelt += firstDouble[i];
            } else {
                view3.setText("keine");
            }
            row.addView(view3);
            table.addView(row);
            System.out.println(i);
        }

        String s = "   Würfe: " + sumAll + "  " + anzAll;
        s = s + "\nDoppelt: " + sumDoppelt + "  " + anzDoppelt;
        System.out.println("Würfe: " + sumAll + "  " + anzAll);
        System.out.println("Doppelt: " + sumDoppelt + "  " + anzDoppelt);
        ((TextView) findViewById(R.id.wuerfeD)).setText(Double.toString((double)sumAll/anzAll));
        ((TextView) findViewById(R.id.doppeltD)).setText((Double.toString((double)sumDoppelt/anzDoppelt) + "  "));
        ((TextView) findViewById(R.id.texts)).setText(s);
    }
}
