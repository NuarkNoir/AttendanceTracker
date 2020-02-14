package xyz.nuark.attendancetracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView uslist;
    UslistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uslist = findViewById(R.id.uslist);
        uslist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UslistAdapter(this);
        uslist.setAdapter(adapter);

        FloatingActionButton fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("List of users");

            final EditText input = new EditText(this);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            input.setSingleLine(false);
            input.setLines(5);
            input.setMaxLines(10);
            input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            builder.setView(input);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateAdapter(input.getText().toString().trim().split("\n"));
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        FloatingActionButton fab_clear = findViewById(R.id.fab_clear);
        fab_clear.setOnClickListener(view -> Snackbar.make(view, "Clear table?", Snackbar.LENGTH_SHORT)
                .setAction("YES", (v) -> MainActivity.this.clearTable()).show());

    }

    @Override
    protected void onPause() {
        super.onPause();
        serializeAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        deserializeAdapter();
    }

    private void serializeAdapter() {
        PreferenceUtils.init(this).setList(C.PREFS_ADAPTER_DATA, adapter.getUserModels());
    }

    private void deserializeAdapter() {
        List<UserModel> userModels = PreferenceUtils.init(this).getList(C.PREFS_ADAPTER_DATA, UserModel[].class);
        adapter.setUserModels(new ArrayList<>(userModels));
    }

    private void updateAdapter(String[] new_users) {
        adapter.clearUsersData();

        for (String new_user : new_users) {
            new_user = new_user.trim();
            if (new_user.isEmpty()) {
                continue;
            }
            adapter.addUser(new_user);
        }
    }

    private void clearTable() {
        adapter.clearUsersData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
