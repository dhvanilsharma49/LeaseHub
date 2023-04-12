package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leasehub.R;

import java.util.ArrayList;

public class TenantAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LeaseRequestdetails> teantList;
    LeaseRequestdetails leaseRequestdetails;

    @Override
    public int getCount() {
        return teantList.size();
    }

    @Override
    public Object getItem(int i) {
        return teantList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View oneItem =null;
        TextView name,address,phoneNo,emailId = null;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_item_tenant, viewGroup,false);

        name = oneItem.findViewById(R.id.tvTenantName);
        address = oneItem.findViewById(R.id.tvPropAddress);
        phoneNo = oneItem.findViewById(R.id.tvPhoneNo);
        emailId = oneItem.findViewById(R.id.tvEmailId);

        leaseRequestdetails = (LeaseRequestdetails) getItem(i);

        name.setText(leaseRequestdetails.getTenantName());
        address.append(leaseRequestdetails.getPropAdd());
        phoneNo.append(leaseRequestdetails.getContactNo());
        emailId.append(leaseRequestdetails.getEmailId());

        return oneItem;
    }

    public TenantAdapter(Context context, ArrayList<LeaseRequestdetails> teantList) {
        this.context = context;
        this.teantList = teantList;
    }
}
