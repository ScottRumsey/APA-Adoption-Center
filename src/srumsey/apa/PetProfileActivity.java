package srumsey.apa;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class PetProfileActivity extends BaseActivity {
	
	String name;
	String description;
	String imgURL;
	ArrayList<String> labels = new ArrayList<String>();
	ArrayList<String> data = new ArrayList<String>();
	Context c = getBaseContext();
	
	Boolean species;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Pet Profile");
        
        Bundle mBundle = getIntent().getExtras();
        final String url = mBundle.getString("url");
        Log.d("RAWR", url);
        name = mBundle.getString("name");
        imgURL = mBundle.getString("img");
        species = mBundle.getBoolean("species");
        
        TextView petName = (TextView) findViewById(R.id.pet_name);
		petName.setText("" + name);
		
		ImageView imgView = (ImageView) findViewById(R.id.img);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		imageLoader.displayImage(imgURL, imgView);
		
		new GetInfo().execute(url);
        
        /*
		Thread downloadThread = new Thread() {   
			
		    public void run() {                                    
		        Document pet;                                      
		        try {                                              
		    		pet = Jsoup.connect(url).get();
		    		Elements pics = pet.select("img[name=imgAnimalPhoto]");
		    		Elements info = pet.select("table[id=detail-table]");
		    		Elements moreInfo = info.select("td");
		    		
		    		if(moreInfo.get(2).previousElementSibling().text().equals("Current Weight:"))
		    			moreInfo.remove(2);
		    		
		    		TextView petName = (TextView) findViewById(R.id.pet_name);
		    		petName.setText("" + name);
		    		
		    		TextView sex = (TextView) findViewById(R.id.sex);
		    		sex.setText(moreInfo.get(0).text());
				    
		    		TextView breed = (TextView) findViewById(R.id.breed);
		    		breed.setText(moreInfo.get(1).text());
		    		
		    		TextView size = (TextView) findViewById(R.id.size);
		    		size.setText(moreInfo.get(2).text());
		    		
		    		TextView dob = (TextView) findViewById(R.id.DOB);
		    		dob.setText(moreInfo.get(3).text());
		    		
		    		TextView age = (TextView) findViewById(R.id.age);
		    		age.setText(moreInfo.get(4).text());
		    		
		    		TextView colors = (TextView) findViewById(R.id.color);
		    		colors.setText(moreInfo.get(5).text());
		    		
		    		TextView spayed = (TextView) findViewById(R.id.spayed);
		    		spayed.setText(moreInfo.get(6).text());
		    		
		    		TextView fee = (TextView) findViewById(R.id.fee);
		    		fee.setText(moreInfo.get(7).text());
		    		
		        } catch (IOException e) {                          
		            e.printStackTrace();                           
		        }                                                  
		    }                                                      
		};                                                         
		downloadThread.start();     
	    try {
			downloadThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
    }
	public void onAdoptClick(View view) {
		if(species){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.austinpetsalive.org/adopt/how-to-adopt-a-dog/"));
			startActivity(browserIntent);
		}
		else{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("http://www.austinpetsalive.org/adopt/how-to-adopt-a-cat/"));
			startActivity(browserIntent);
		}
	}
	
	public void onLocationClick(View view) {
		if(species){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.austinpetsalive.org/events/daily-dog-locations/"));
			startActivity(browserIntent);
		}
		else{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("http://www.austinpetsalive.org/events/daily-cat-locations/"));
			startActivity(browserIntent);
		}
	}
    
    private class GetInfo extends AsyncTask<String, Void, Void>{
    	ProgressDialog pd;
    	
    	protected void onPreExecute(){
    		pd = new ProgressDialog(PetProfileActivity.this);
    		pd.setMessage("Loading");
    		pd.setIndeterminate(true);
    		pd.show();
    	};

    	protected Void doInBackground(String...strings){
    		Elements moreInfo= new Elements();
	        try {                                              
		        Document pet = Jsoup.connect(strings[0]).get();
	    		//Elements pics = pet.select("img[name=imgAnimalPhoto]");
	    		Elements info = pet.select("table[id=detail-table]");
	    		moreInfo = info.select("td");
	    		for(Element x: moreInfo){
	    			if(x.previousElementSibling().text().equals("Location:")){
	    				//Nothing; Links are inconsisent.
	    			}
	    			else{
	    				labels.add(x.previousElementSibling().text());
	    				data.add(x.text());
	    			}
	    				
	    		}
	    		
	    		description = pet.select("[id = lbDescription]").text();
	    		
	        } catch (IOException e) {                          
	        	e.printStackTrace();                           
	        } 
	        return null;
    	}
    	
		@Override
    	protected void onPostExecute(Void v){
			
			Log.d("RAWR", "Table Shiz");
    		TableLayout tl = (TableLayout) findViewById(R.id.table);
    		
    		for(int i = 0; i< labels.size();i++){
    			TableRow tr = new TableRow(getApplicationContext());
    			
    			TextView t1 = new TextView(getApplicationContext());
    			t1.setText(labels.get(i));
    			t1.setTextColor(Color.BLACK);
    			t1.setTypeface(null, Typeface.BOLD);
    			tr.addView(t1);
    			
    			TextView t2 = new TextView(getApplicationContext());
    			t2.setText(data.get(i));
    			t2.setGravity(Gravity.RIGHT);
    			t2.setTextColor(Color.BLACK);
    			tr.addView(t2);
    			
    			tl.addView(tr,new TableLayout.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
    			
    		}
			
    		TextView desc = (TextView) findViewById(R.id.description);
    		desc.setText("Description: \n\n\t" + description);
    		pd.dismiss();
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
