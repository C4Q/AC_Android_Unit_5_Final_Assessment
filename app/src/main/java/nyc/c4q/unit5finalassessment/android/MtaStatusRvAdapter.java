package nyc.c4q.unit5finalassessment.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.unit5finalassessment.R;
import nyc.c4q.unit5finalassessment.model.LineStatus;

/**
 * Takes a list of LineStatus objects and uses them to populate views in a RecyclerView.
 * <p>
 * Created by charlie on 2/1/18.
 */

class MtaStatusRvAdapter extends RecyclerView.Adapter<MtaStatusRvAdapter.MtaStatusViewHolder> {

    private List<LineStatus> lineStatuses;

    public MtaStatusRvAdapter() {
        this.lineStatuses = new ArrayList<>();
    }

    void updateData(List<LineStatus> lineStatuses) {
        this.lineStatuses.clear();
        this.lineStatuses.addAll(lineStatuses);
        notifyDataSetChanged();
    }

    @Override
    public MtaStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mta_status_rv_entry, parent, false);
        return new MtaStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MtaStatusViewHolder holder, int position) {
        LineStatus lineStatus = lineStatuses.get(position);
        holder.name.setText(lineStatus.getName());
        holder.status.setText(lineStatus.getStatus());
        holder.time.setText(lineStatus.getFormattedDateTime(MtaAlertJobService.DATE_FORMAT_PATTERN));
    }

    @Override
    public int getItemCount() {
        return lineStatuses.size();
    }

    class MtaStatusViewHolder extends RecyclerView.ViewHolder {

        TextView name, status, time;

        MtaStatusViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rv_entry_name);
            status = itemView.findViewById(R.id.rv_entry_status);
            time = itemView.findViewById(R.id.rv_entry_time);
        }
    }
}
