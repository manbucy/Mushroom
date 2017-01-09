package com.manbu.mushroom.ui.citypicter;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.manbu.mushroom.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CityPickerDialog extends Dialog implements android.view.View.OnClickListener {
	// private Context mContext;

	private TextView title;

	private ListView provinceListview;
	private Button btnCancel;
	private Button btnOk;
	// private Dialog customDialog;
	private MSharePreferences sharePreferences;
	private OnCityPikerListener mlistener;

	private List<ProvinceModel> provinces;
	private List<CityModel> cities;
	private List<DistrictModel> districtes;

	private PlaceAdapter provinceAdapter;
	private PlaceAdapter cityAdapter;
	private PlaceAdapter districtAdapter;

	// private ProvinceAdapter provinceAdapter;
	// private CityAdapter cityAdapter;
	// private DistrictAdapter districtAdapter;

	public interface OnCityPikerListener {
		void onCityPicker(String province, String city, String district);
	}

	public CityPickerDialog(Context context, OnCityPikerListener onCityPickerListener) {
		// super(context,R.style.customdialog);
		// TODO Auto-generated constructor stub
		this(context, R.style.customdialog, onCityPickerListener);
	}

	public CityPickerDialog(Context context, int theme, OnCityPikerListener onCityPickerListener) {
		super(context, theme);
		mlistener = onCityPickerListener;
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.province_listview, null);
		title = (TextView) view.findViewById(R.id.title);
		provinceListview = (ListView) view.findViewById(R.id.provinceList);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnOk = (Button) view.findViewById(R.id.btnOk);
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);

		setContentView(view);
		setCancelable(true);

		sharePreferences = MSharePreferences.getInstance();
		sharePreferences.getSharedPreferences(getContext());
		initProvinceDatas();
		initProvince();
		initDialogSize();

	}

	private boolean isCity;
	private boolean isDistrict;
	private boolean hasDistrict;

	public void initProvince() {
		title.setText(R.string.province);
		// provinceAdapter = new ProvinceAdapter(getContext(), provinces);
		provinceAdapter = new PlaceAdapter(getContext(), provinces);
		provinceListview.setAdapter(provinceAdapter);
		provinceListview.setSelection(sharePreferences.getInt(Tools.KEY_PROVINCE, 0));
		isCity = true;
		isDistrict = true;
		hasDistrict = true;
	}

	public void initDialogSize() {

		Window dialogWindow = getWindow();
		DisplayMetrics d = getContext().getResources().getDisplayMetrics();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.height = LayoutParams.WRAP_CONTENT;
		p.width = (int) (d.widthPixels * 0.8);
		dialogWindow.setAttributes(p);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnCancel:
			dismiss();
			break;
		case R.id.btnOk:
			int provincePosition = provinceAdapter.getSelectedIndex();
			String province = provinces.get(provincePosition).getName();
			String city = "";
			String district = "";
			if (isCity) {
				cities = provinces.get(provincePosition).getCityList();
				title.setText(R.string.city);
				cityAdapter = new PlaceAdapter(getContext(), cities);
				provinceListview.setAdapter(cityAdapter);
				isCity = false;
				int i = cities.get(cityAdapter.getSelectedIndex()).getDistrictList().size();
				hasDistrict = 0 == i ? false : true;
			} else if (isDistrict && hasDistrict) {
				int cityPosition = cityAdapter.getSelectedIndex();
				districtes = cities.get(cityPosition).getDistrictList();
				title.setText(R.string.district);
				districtAdapter = new PlaceAdapter(getContext(), districtes);
				provinceListview.setAdapter(districtAdapter);
				isDistrict = false;
			} else {
				int cityPosition = cityAdapter.getSelectedIndex();
				city = cities.get(cityPosition).getName();
				if(hasDistrict){
					int districtPosition = districtAdapter.getSelectedIndex();
					district = districtes.get(districtPosition).getName();
				}
				sharePreferences.putInt(Tools.KEY_PROVINCE, provincePosition);
				mlistener.onCityPicker(province, city, district);
				dismiss();
			}

			break;
		default:
			break;
		}

	}

	public void initProvinceDatas() {

		AssetManager asset = getContext().getAssets();

		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinces = handler.getDataList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
