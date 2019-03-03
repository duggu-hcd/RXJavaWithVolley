package rxjava.com.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rxjava.com.R;
import rxjava.com.model.AlbumDataModel;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumListViewHolder> {
    private List<AlbumDataModel> albumDataModelList;
    private int layout;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class AlbumListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private AppCompatTextView mTextView;
        private AlbumListViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumListAdapter(int layout) {
        this.layout = layout;
    }

    public void setArray(List<AlbumDataModel> mDataset) {
        this.albumDataModelList = mDataset;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(layout,parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AlbumListViewHolder holder, int position) {
        holder.mTextView.setText(albumDataModelList.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(albumDataModelList == null || albumDataModelList.isEmpty()){
            return 0;
        }
        return albumDataModelList.size();
    }
}