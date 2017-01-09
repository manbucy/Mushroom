package com.manbu.mushroom.ui.citypicter;

import java.util.HashMap;
import java.util.List;

import com.manbu.mushroom.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

@SuppressLint("InflateParams")
public class PlaceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
    private int selectedIndex = 0;
    private boolean flag = true;
    // 用于记录每个Radiobutton的状态，并保证只可选一个
    private HashMap<String, Boolean> states = new HashMap<String, Boolean>();


    private List<? extends BaseModel> places;
    
    
    private boolean defaultSelectedPosition = true;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }



    public PlaceAdapter(Context context, List<? extends BaseModel> places) {
        this.context = context;
        this.places = places;
        this.mInflater = LayoutInflater.from(context);

    }


    // public void selectedIndex(int position){
    // this.selectedIndex = position;
    // notifyDataSetChanged();
    // }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        MSharePreferences sharePreferences = MSharePreferences
                .getInstance();
        sharePreferences.getSharedPreferences(context);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.province_adapter, null);
            // viewHolder.ProvinceRadioGroup = (RadioGroup)
            // convertView.findViewById(R.id.ProinceParent);
            viewHolder.PlaceItem = (RadioButton) convertView
                    .findViewById(R.id.province_choice);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       
            viewHolder.PlaceItem.setText(places.get(position).getName());

        viewHolder.PlaceItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 重置，确保最多只有一个被选中,但是这个方法每次都要便利，不是太好，以后再找个适合的
                for (String key : states.keySet()) {
                    states.put(key, false);

                }
                defaultSelectedPosition = false;
                states.put(String.valueOf(position),
                        viewHolder.PlaceItem.isChecked());
                PlaceAdapter.this.notifyDataSetChanged();

            }
        });

        boolean res = false;
        if (states.get(String.valueOf(position)) == null
                || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else {

            selectedIndex = position;
            res = true;
        }
        viewHolder.PlaceItem.setChecked(res);
        // if(selectedIndex == position){
        // viewHolder.PlaceItem.setChecked(true);
        //
        // }else{
        // viewHolder.PlaceItem.setChecked(false);
        // }
        
        if (flag) {
            if (position == sharePreferences.getInt(
                    Tools.KEY_PROVINCE, 0)
                    && defaultSelectedPosition) {
                selectedIndex = position;
                viewHolder.PlaceItem.setChecked(true);
            }
        } else {
            if (position == 0 && defaultSelectedPosition) {
                viewHolder.PlaceItem.setChecked(true);

            }
        }
        return convertView;
    }

    class ViewHolder {
        private RadioButton PlaceItem;
    }
}
