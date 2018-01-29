package tungu.sas.souk;

/**
 * Created by Student on 1/28/2018.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        startActivity(new Intent(Splash.this,WebScraper.class));
        // close splash activity
        finish();
    }
}