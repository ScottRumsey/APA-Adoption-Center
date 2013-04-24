package srumsey.apa;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class AdoptionCenter extends BaseActivity {

	Intent intent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_center);
        intent = new Intent(this, petList.class);
        Spinner spinner = (Spinner) findViewById(R.id.sex_spinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.age_spinner);
        spinner.setOnItemSelectedListener(new SpinnerActivity());
        spinner2.setOnItemSelectedListener(new SpinnerActivity2());
    }

    //dog = true;
	public void onDogClick(View view) {
		intent.putExtra("species", true);
		startActivity(intent);
	}
	
	//cat = false;
	public void onCatClick(View view) {
		intent.putExtra("species", false);
		startActivity(intent);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    public class SpinnerActivity extends Activity implements OnItemSelectedListener {
        
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, 
                int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            intent.putExtra("sex", pos);
            Log.d("RAWR", "item chosen: " + pos);
        }
    	
    	@Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    };
    
    public class SpinnerActivity2 extends Activity implements OnItemSelectedListener {
        
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, 
                int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            intent.putExtra("age", pos);
            //Log.d("RAWR", "item chosen: " + parent.getItemAtPosition(pos));
        }
    	
    	@Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    };
}
