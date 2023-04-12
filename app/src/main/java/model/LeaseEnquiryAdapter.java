package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leasehub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaseEnquiryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LeaseRequestdetails> leaseList;
    LeaseRequestdetails leasedetail;

    @Override
    public int getCount() {
        return leaseList.size();
    }

    @Override
    public Object getItem(int i) {
        return leaseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View oneItem;
        TextView propSize,propAdd,tenantName,tenantPh, tenantEmail;
        ImageView propImg;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_item_lease, viewGroup,false);

        propSize = oneItem.findViewById(R.id.tvPropTitle);
        propAdd = oneItem.findViewById(R.id.tvPropAddress);
        tenantName = oneItem.findViewById(R.id.tvTenantName);
        tenantPh = oneItem.findViewById(R.id.tvPhoneNo);
        tenantEmail = oneItem.findViewById(R.id.tvEmailId);
        propImg = oneItem.findViewById(R.id.propImg);

        leasedetail = (LeaseRequestdetails) getItem(i);

        propSize.setText(leasedetail.getPropSize());
        propAdd.setText(leasedetail.getPropAdd());
        tenantName.setText(leasedetail.getTenantName());
        tenantPh.setText(leasedetail.getContactNo());
        tenantEmail.setText(leasedetail.getEmailId());
        Picasso.get().load(leasedetail.getSrcImage()).into(propImg);


        return oneItem;
    }

    public LeaseEnquiryAdapter(Context context, ArrayList<LeaseRequestdetails> leaseList) {
        this.context = context;
        this.leaseList = leaseList;
    }
}
