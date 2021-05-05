package protestspacec.om;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;

    ImageButton addNewPost, btnlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navView = findViewById(R.id.nav_view);
        addNewPost = findViewById(R.id.addNewPost);
        btnlogout = findViewById(R.id.btnLogout);

        btnlogout.setOnClickListener(v -> Helper.makemeOffline(MainActivity.this));
        addNewPost.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddPostActivity.class)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                    MainActivity.this.openFragment(new HomeFragment());
                    return true;
                case R.id.navigation_person:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
                    MainActivity.this.openFragment(new ProfileFragment());
                    return true;
                case R.id.request:
                    MainActivity.this.openFragment(new RequetsFragment());
                    return true;

            }
            return true;
        });
    }

    private void openFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}