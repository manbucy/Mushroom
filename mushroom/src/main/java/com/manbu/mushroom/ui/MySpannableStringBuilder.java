package com.manbu.mushroom.ui;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
/**
 * 
* @ClassName: MySpannableStringBuilder
* @Description: TODO(设置文字的字体颜色 并且可以单独设置其中的某一个字)
* @author ManBu 
* @date 2016年8月23日 上午10:40:26
*
 */
public class MySpannableStringBuilder{
	
	public SpannableStringBuilder setText(String str,String tr){
//		int fstart = str.indexOf(tr);
//		int fend = fstart + tr.length();
		SpannableStringBuilder style = new SpannableStringBuilder(str);
//		style.setSpan(new ForegroundColorSpan(Color.BLUE), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//		style.setSpan(new AbsoluteSizeSpan(70), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.setSpan(new  StyleSpan(Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		
		return style;
	}

}
