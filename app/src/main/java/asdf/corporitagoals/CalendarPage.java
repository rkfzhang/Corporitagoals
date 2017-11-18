package asdf.corporitagoals;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarPage extends AppCompatActivity {
    static DBAdapter goalsDB;
    CompactCalendarView compactCalendar;
    private static final String TAG = "Testing";
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.CANADA);
    private SimpleDateFormat formateDates = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);

    static String [] goalsName;
    static String [] goalsTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        openDB();


        setMonthTitle();
        setEventDates();
        addEvents(goalsTime);


        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener(){
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setMonthTitle();

            }

            @Override
            public void onDayClick(Date dateClicked) {
                Log.d(TAG, "Day was clicked: " + dateClicked);

                updateList(dateClicked, goalsTime, goalsName);
            }
        });
    }

    public void updateList(Date date, String[] dateAr, String[] goalAr){

        String textMsg = "";

        for (int i = 0; i < dateAr.length; i++){
            try {
                Date day = formateDates.parse(dateAr[i]);
                if (day.getTime() == date.getTime()){

                    textMsg = textMsg + goalAr[i] + "\n\n";

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        TextView goalsDis = (TextView) findViewById(R.id.goalsDis);

        goalsDis.setText(textMsg);
    }

    public void addEvents(String [] datesAdd){

        for (String date:datesAdd){
            try {
                Date event = formateDates.parse(date);
                Log.i(TAG, date + " successful");
                Event ev = new Event(R.color.appGeneral, event.getTime() );

                compactCalendar.addEvent(ev,true);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i(TAG, date + " unsuccessful");

            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        closeDB();
    }

    public void setEventDates(){
        List<String> gName = new ArrayList<>();
        List<String> gTime = new ArrayList<>();


        Cursor cursor = goalsDB.getAllRows();
        if (cursor.moveToFirst()){

            do {

                String goal = cursor.getString(DBAdapter.COL_GOAL);
                String time = cursor.getString(DBAdapter.COL_TIME);


                gName.add(goal);
                gTime.add(time);
            }while (cursor.moveToNext());

        }
        cursor.close();

        goalsName = gName.toArray(new String[gName.size()]);
        goalsTime = gTime.toArray(new String[gTime.size()]);
        //datesToAdd = reduce(goalsTime);


    }



    public void setMonthTitle(){

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        TextView calTitle = (TextView)findViewById(R.id.calTitle);
        String month = dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth());
        Log.i(TAG, month);
        calTitle.setText(month);

    }

    private void openDB(){

        goalsDB = new DBAdapter(this);
        goalsDB.open();

    }

    private void closeDB(){
        goalsDB.close();
    }
}
