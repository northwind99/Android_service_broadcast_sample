package xgen.mobiroo.com.mobirooapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity implements MyDialogFragment.Listener {

    Button countriesBtn;

    MyDialogFragment myDialogFragment = new MyDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countriesBtn = (Button) findViewById(R.id.btnCountries);
    }

    public void clickCountries(View v) {

        new MyDialogFragment().show(getFragmentManager(), "MyDialog");
    }

    @Override
    public void startClicked() {
        Intent intent = new Intent(this, CountryActivity.class);
        startActivity(intent);
    }
}
