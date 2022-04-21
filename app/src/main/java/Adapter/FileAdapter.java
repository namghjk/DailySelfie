package Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.namghjk.dailyselfie.R;

import java.util.List;

import Model.File;

public class FileAdapter extends ArrayAdapter<File> {
    @NonNull Context context;
    int resource;
    @NonNull List<File> objects;
    public FileAdapter(@NonNull Context context, int resource, @NonNull List<File> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_image,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageview);
        TextView fileName = convertView.findViewById(R.id.textView);

        File fileModel = this.objects.get(position);
        imageView.setImageBitmap(BitmapFactory.decodeFile(fileModel.getFilePath()));
        fileName.setText(fileModel.getFileName());


        return convertView;
    }
}
