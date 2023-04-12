package model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leasehub.DetailInformationActivity;
import com.example.leasehub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PropertyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Property> propertyList;
    Property property;

    @Override
    public int getCount() {
        return propertyList.size();
    }

    @Override
    public Object getItem(int i) {
        return propertyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public PropertyAdapter(Context context, ArrayList<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View oneItem =null;
        ImageView imPhoto,imMore;
        TextView tvPropTitle,tvAddress,tvPrice = null;

        int position = i;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_item, viewGroup,false);

        imPhoto = oneItem.findViewById(R.id.PropImg);
        tvPropTitle = oneItem.findViewById(R.id.tvPropTitle);
        tvAddress = oneItem.findViewById(R.id.tvPropAddress);
        tvPrice = oneItem.findViewById(R.id.tvPropPrice);
        imMore = oneItem.findViewById(R.id.imMore);
        imMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                property = propertyList.get(position);
                //MOve to detail Info Activity;
                //Toast.makeText(context, property.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        property = (Property) getItem(i);
        tvPropTitle.setText(property.getPropSize());
        tvAddress.setText(property.getPropAddress());
        tvPrice.setText(property.getPrice().toString());
        String photoStr = property.getSrcImage();
        Picasso.get().load(photoStr).into(imPhoto);

        return oneItem;
    }
}
