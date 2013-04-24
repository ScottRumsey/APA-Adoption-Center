package srumsey.apa;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PetListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pet_list, menu);
        return true;
    }
}
