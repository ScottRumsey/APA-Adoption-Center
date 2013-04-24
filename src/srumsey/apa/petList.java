package srumsey.apa;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class petList extends BaseActivity{
	
	ArrayList<PetListElement> pets;

	private DisplayImageOptions options;
	
	GridView gridView;
	
	Boolean species;
	
	Context temp;
	
	int sex;
	int age;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		
		Log.d("RAWR", "got to new Activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Available Pets");
		
		Bundle mBundle = getIntent().getExtras();
		species = mBundle.getBoolean("species");
		sex = mBundle.getInt("sex");
		age = mBundle.getInt("age");
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		pets = new ArrayList<PetListElement>();
		


	    Log.d("RAWR", "Post Fetch");
	    
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.stub_image)
			.showImageForEmptyUri(R.drawable.image_for_empty_url)
			.cacheInMemory()
			.cacheOnDisc()
			.build();
		
	    Log.d("RAWR", "Post Option set");
		
		gridView = (GridView) findViewById(R.id.gridview);
		new GetPets().execute();
		temp = this.getBaseContext();
	}
    private class GetPets extends AsyncTask<Void, Void, Void>{
    	
    	ProgressDialog pd;
    	
    	protected void onPreExecute(){
    		pd = new ProgressDialog(petList.this);
    		pd.setMessage("Loading");
    		pd.setIndeterminate(true);
    		pd.show();
    	};

    	protected Void doInBackground(Void... params){
	        Document doc2;
	        
			final String listURL;
			
			if(species)
				listURL = "http://www.austinpetsalive.org/adopt/dogs/";
			else
				listURL = "http://www.austinpetsalive.org/adopt/cats/";
	        
	        try {                                              
				doc2 = Jsoup.connect(listURL).get();
				Log.d("RAWR", "Made Connection");
				Elements links = doc2.select("a[rel=bookmark]");
				for(Element e: links){
					String name = e.attr("title");
					if(!name.equals("")){
						String img = e.children().select("img").attr("src");
						String url = "http://www.austinpetsalive.org" + e.attr("href");
						
						String elemText = e.toString();
						Boolean female = elemText.contains("Female");
						Boolean year = elemText.contains("year");
						
						if(sex == 0 && age == 0){
							pets.add(new PetListElement(name, url, img));
						}
						else{
							if(sex == 0){
								if(age == 1 && !year){
									pets.add(new PetListElement(name, url, img));
								}
								else if(age == 2 && year){
									pets.add(new PetListElement(name, url, img));
								}
							}
							else if(sex == 1){
								if(age == 0 && !female){
									pets.add(new PetListElement(name, url, img));
								}
								else if(age == 1 && !year && !female){
									pets.add(new PetListElement(name, url, img));
								}
								else if(age == 2 && year && !female){
									pets.add(new PetListElement(name, url, img));
								}
							}
							else if(sex == 2){
								if(age == 0 && female){
									pets.add(new PetListElement(name, url, img));
								}
								else if(age == 1 && !year && female){
									pets.add(new PetListElement(name, url, img));
								}
								else if(age == 2 && year && female){
									pets.add(new PetListElement(name, url, img));
								}
							}
						}
					}
				}
				Log.d("RAWR", "Done Parsing. Total Items: " + pets.size());
			    
	        } catch (IOException e) {                          
	            e.printStackTrace();                           
	        } 
	        
	        return null;
    	}
    	
		@Override
    	protected void onPostExecute(Void params){
    		pd.dismiss();
			Log.d("RAWR", "POSTEXECUTE");
    		gridView.setAdapter(new ImageAdapter());
    		
    		gridView.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    				Intent petProfile = new Intent(temp, PetProfileActivity.class);
    				petProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				petProfile.putExtra("url", pets.get(position).getURL());
    				petProfile.putExtra("img", pets.get(position).getImgURL());
    				petProfile.putExtra("name", pets.get(position).getName());
    				petProfile.putExtra("species", species);
    				Log.d("RAWR", "HERE");
    				Log.d("RAWR", pets.get(position).getURL());
    				startActivity(petProfile);
    			}
    		});
    		
    	}
    }

	
	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return pets.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			imageLoader.displayImage(pets.get(position).getImgURL(), imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(petList.this, R.anim.fade_in);
					imageView.setAnimation(anim);
					anim.start();
				}
			});

			return imageView;
		}
	}
	
}