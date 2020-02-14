package xyz.nuark.attendancetracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

/**
 * Created with love by Nuark on 14.02.2020.
 */
public class UslistAdapter extends RecyclerView.Adapter<UslistAdapter.ULViewHolder> {

    private Context context;
    private ArrayList<UserModel> userModels;

    public UslistAdapter(Context context) {
        userModels = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ULViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.uslist_layout, parent, false);
        return new ULViewHolder(v);
    }

    @Override
    public void onViewRecycled(@NonNull ULViewHolder holder) {
        super.onViewRecycled(holder);
        holder.chip_holder.removeAllViews();
    }

    @Override
    public void onBindViewHolder(@NonNull ULViewHolder holder, int position) {
        UserModel cu = userModels.get(position);
        holder.uname.setText(cu.getName());

        int np = holder.chip_holder.getChildCount();
        for (int ordinal : cu.getAttended()) {
            addNewChip(holder, np++, position, UserStates.getByOrdinal(ordinal));
        }
        holder.fab_add.setOnClickListener((v) -> {
            addNewChip(holder, holder.chip_holder.getChildCount(), position, UserStates.UNKNOWN);
        });

        holder.fab_clear.setOnClickListener((v) -> {
            userModels.get(position).clearAttendance();
            notifyItemChanged(position);
        });

        holder.fab_remove.setOnClickListener((v) -> {
            userModels.remove(position);
            notifyDataSetChanged();
        });
    }

    private void addNewChip(ULViewHolder holder, int np, int position, UserStates state) {
        Chip nc = new Chip(holder.chip_holder.getContext());
        setTint(nc, getColorForState(state));
        nc.setText(getStringForState(state));
        nc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        nc.setMinWidth(Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                context.getResources().getDimension(R.dimen.chip_width),
                context.getResources().getDisplayMetrics()
        )));
        userModels.get(position).setAttendance(np, state);

        nc.setOnClickListener((sv1 -> {
            UserStates cs = UserStates.getStateFromString(nc.getText().toString());
            UserStates ns = UserStates.getNextState(cs);
            userModels.get(position).setAttendance(np, ns);
            setTint((Chip)holder.chip_holder.getChildAt(np), getColorForState(ns));
            nc.setText(getStringForState(ns));
        }));

        nc.setOnLongClickListener((sv1) -> {
            userModels.get(position).removeAttendance(np);
            notifyItemChanged(position);
            return true;
        });

        holder.chip_holder.addView(nc, np);
    }

    private String getStringForState(UserStates state) {
        switch (state) {
            default:
            case UNKNOWN:
                return "Unknown";
            case MISSED:
                return "Missed";
            case ATTENDED:
                return "Attended";
        }
    }

    private ColorStateList getColorForState(UserStates state) {
        switch (state) {
            default:
            case UNKNOWN:
                return ColorStateList.valueOf(context.getColor(R.color.unknown));
            case MISSED:
                return ColorStateList.valueOf(context.getColor(R.color.unattended));
            case ATTENDED:
                return ColorStateList.valueOf(context.getColor(R.color.attended));
        }
    }

    private void setTint(Chip cp, ColorStateList tint) {
        cp.setChipBackgroundColor(tint);
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public void clearUsersData() {
        for (UserModel um : userModels) {
            um.clearAttendance();
        }
        notifyDataSetChanged();
    }

    public void addUser(String name) {
        userModels.add(new UserModel(name));
        notifyDataSetChanged();
    }

    public void setUserModels(ArrayList<UserModel> userModels) {
        clearUsersData();
        this.userModels = userModels;
        notifyDataSetChanged();
    }

    public ArrayList<UserModel> getUserModels() {
        return userModels;
    }

    public class ULViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView uname;
        FloatingActionButton fab_add;
        FloatingActionButton fab_clear;
        FloatingActionButton fab_remove;
        ChipGroup chip_holder;

        public ULViewHolder(@NonNull View itemView) {
            super(itemView);
            uname = itemView.findViewById(R.id.uname);
            fab_add = itemView.findViewById(R.id.fab_add);
            fab_clear = itemView.findViewById(R.id.fab_clear);
            fab_remove = itemView.findViewById(R.id.fab_remove);
            chip_holder = itemView.findViewById(R.id.chip_holder);
        }
    }
}
