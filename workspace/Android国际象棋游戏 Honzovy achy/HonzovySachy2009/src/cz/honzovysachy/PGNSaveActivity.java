package cz.honzovysachy;

import cz.honzovysachy.pravidla.PGNHeaderData;
import cz.honzovysachy.resouces.S;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PGNSaveActivity extends Activity implements OnDateSetListener {
	int mYear = 2009;
	int mMonth = 9;
	int mDay = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(S.g("SAVE_PGN"));
        setContentView(R.layout.pgn_header);
        final Button save = (Button)findViewById(R.id.save);
        final Button date = (Button)findViewById(R.id.date);
        final EditText filename = (EditText)findViewById(R.id.filename);
        final EditText white = (EditText)findViewById(R.id.white);
        final EditText black = (EditText)findViewById(R.id.black);
        final EditText event = (EditText)findViewById(R.id.event);
        final EditText site = (EditText)findViewById(R.id.site);
        final EditText round = (EditText)findViewById(R.id.round);
        final EditText whiteelo = (EditText)findViewById(R.id.whiteelo);
        final EditText blackelo = (EditText)findViewById(R.id.blackelo);
        final Spinner gameResult = (Spinner)findViewById(R.id.result);
        
        TextView tFileName = (TextView)findViewById(R.id.filenamestatic);
        TextView tWhite = (TextView)findViewById(R.id.whitestatic);
        TextView tBlack = (TextView)findViewById(R.id.blackstatic);
        TextView tResult = (TextView)findViewById(R.id.resultstatic);
        TextView tEvent = (TextView)findViewById(R.id.eventstatic);
        TextView tSite = (TextView)findViewById(R.id.sitestatic);
        TextView tDate = (TextView)findViewById(R.id.datestatic);
        TextView tRound = (TextView)findViewById(R.id.roundstatic);
        TextView tWhiteElo = (TextView)findViewById(R.id.whiteelostatic);
        TextView tBlackElo = (TextView)findViewById(R.id.blackelostatic);
        
        tFileName.setText(S.g("FILE_NAME") + ":");
        tWhite.setText(S.g("WHITE") + ":");
        tBlack.setText(S.g("BLACK") + ":");
        tResult.setText(S.g("RESULT") + ":");
        tEvent.setText(S.g("EVENT") + ":");
        tSite.setText(S.g("SITE") + ":");
        tDate.setText(S.g("DATE") + ":");
        tRound.setText(S.g("ROUND") + ":");
        tWhiteElo.setText(S.g("WHITE_ELO") + ":");
        tBlackElo.setText(S.g("BLACK_ELO") + ":");
        
        
        
        save.setText(S.g("SAVE"));
        filename.setText("/sdcard/" + S.g("GAME") + ".pgn");
        white.setText(S.g("WHITE"));
        black.setText(S.g("BLACK"));
        event.setText(S.g("EVENT"));
        site.setText(S.g("PRAGUE"));
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item,
        		PGNHeaderData.RESULTS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameResult.setAdapter(adapter);
        
        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(0);
            }
        });
        
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent result = new Intent();
            	int iWhiteElo = 1550;
            	try {
            		iWhiteElo = Integer.parseInt(whiteelo.getText().toString());
            	} catch (NumberFormatException e) {};
            	int iBlackElo = 1550;
            	try {
            		iBlackElo = Integer.parseInt(blackelo.getText().toString());
            	} catch (NumberFormatException e) {};
            	int iRound = 1;
            	try {
            		iRound = Integer.parseInt(round.getText().toString());
            	} catch (NumberFormatException e) {};
                result.putExtra("PGNHeader", 
                		new PGNHeaderData(
                				filename.getText().toString(),
                				white.getText().toString(),
                				black.getText().toString(),
                				event.getText().toString(),
                				site.getText().toString(),
                				iRound,
                				iWhiteElo,
                				iBlackElo,
                				gameResult.getSelectedItemPosition(),
                				mYear,
                				mMonth + 1,
                				mDay));
                setResult(10, result);
                finish();
            }
        });
        setResult(10, null);
	}
	
	   @Override
	protected Dialog onCreateDialog(int id) {
		   if (id == 0) {
			   return new DatePickerDialog(this,
                       this,
                       mYear, mMonth, mDay);
		   }
		   return null;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
		
		Button date = (Button)findViewById(R.id.date);
		date.setText(year + "-" + (monthOfYear + 1) + "-" +dayOfMonth);
		
	}

}
