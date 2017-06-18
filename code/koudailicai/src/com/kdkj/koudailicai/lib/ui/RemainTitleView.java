package com.kdkj.koudailicai.lib.ui;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.util.ResourceUtil;
import com.kdkj.koudailicai.view.KDLCApplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class RemainTitleView extends RelativeLayout {

	private RelativeLayout titleView;
	private RelativeLayout leftTitleView;
	private RelativeLayout rightTitleView;
	private ImageView mLeftImageBtn;
	private TextView mLeftTextBtn;
	private ImageView mRightImageBtn;
	private TextView mRightTextBtn;
	private TextView mTitle;
	private int titleSize;
	private int textSize;
	private ImageView arrawImage;
	private RelativeLayout remainLayout;
	public void setTitle(String text) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(text);
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
	}
	
	public void setTitle(int stringID) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(stringID);
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
	}
	// 显示标题左侧
	public void showLeftButton(OnClickListener listener) {
		leftTitleView.setVisibility(View.VISIBLE);
		leftTitleView.setOnClickListener(listener);
	}
	public void showMidButton(OnClickListener listener) {
		remainLayout.setOnClickListener(listener);
	}
	
	// 隐藏标题左侧
	public void hiddenLeftButton() {
		leftTitleView.setVisibility(View.GONE);
		leftTitleView.setOnClickListener(null);
	}

	// 设置标题左侧图片内容
	public void setLeftImageButton(int imageID) {
		mLeftImageBtn.setVisibility(View.VISIBLE);
		mLeftImageBtn.setBackgroundResource(imageID);
	}

	// 隐藏标题左侧图片
	public void hiddenLeftImageButton() {
		mLeftImageBtn.setVisibility(View.GONE);
	}

	// 设置标题左侧文字内容
	public void setLeftTextButton(String text) {
		mLeftTextBtn.setVisibility(View.VISIBLE);
		mLeftTextBtn.setText(text);
		mLeftTextBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}

	// 设置标题左侧文字内容
	public void setLeftTextButton(int stringID) {
		mLeftTextBtn.setVisibility(View.VISIBLE);
		mLeftTextBtn.setText(stringID);
		mLeftTextBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}

	// 隐藏标题左侧文字
	public void hiddenLeftTextButton() {
		mLeftTextBtn.setVisibility(View.GONE);
	}

	// 显示标题右侧
	public void showRightButton(OnClickListener listener) {
		rightTitleView.setVisibility(View.VISIBLE);
		rightTitleView.setOnClickListener(listener);
	}

	// 隐藏标题右侧
	public void hiddenRightButton() {
		rightTitleView.setVisibility(View.GONE);
		leftTitleView.setOnClickListener(null);
	}

	// 设置标题右侧图片内容
	public void setRightImageButton(int imageID) {
		mRightImageBtn.setVisibility(View.VISIBLE);
		mRightImageBtn.setBackgroundResource(imageID);
	}

	// 隐藏标题右侧图片
	public void hiddenRightImageButton() {
		mRightImageBtn.setVisibility(View.GONE);
	}

	// 设置标题右侧文字内容
	public void setRightTextButton(String text) {
		mRightTextBtn.setVisibility(View.VISIBLE);
		mRightTextBtn.setText(text);
		mRightTextBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}

	// 设置标题右侧文字内容
	public void setRightTextButton(int stringID) {
		mRightTextBtn.setVisibility(View.VISIBLE);
		mRightTextBtn.setText(stringID);
		mRightTextBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}

	// 隐藏标题右侧文字
	public void hiddenRightTextButton() {
		mRightTextBtn.setVisibility(View.GONE);
	}

	public RemainTitleView(Context context) {
		this(context, null);
	}

	public RemainTitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public void Aarrw(int ID)
	{
		arrawImage.setBackgroundResource(ID);
	}

	public RemainTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.remain_title_view, this, true);
		initViews();
		initSize();
		hidenAll();
	}

	public void initViews() {
		titleView = (RelativeLayout) findViewById(R.id.title_view);
		leftTitleView = (RelativeLayout) findViewById(R.id.title_left);
		rightTitleView = (RelativeLayout) findViewById(R.id.title_right);
		mLeftImageBtn = (ImageView) findViewById(R.id.left_image_btn);
		mLeftTextBtn = (TextView) findViewById(R.id.left_text_btn);
		mRightImageBtn = (ImageView) findViewById(R.id.right_image_btn);
		mRightTextBtn = (TextView) findViewById(R.id.right_text_btn);
		mTitle = (TextView) findViewById(R.id.title_text);
		arrawImage=(ImageView)findViewById(R.id.arrawimage);
		remainLayout=(RelativeLayout)findViewById(R.id.remainlayout);
	}

	public void initSize() {
		int screenHeight = Integer.parseInt(KDLCApplication.app.getSession()
				.get("screenHeight"));
		int titleHeight = (int) (screenHeight * 0.08);
		RelativeLayout.LayoutParams titleViewParams = (RelativeLayout.LayoutParams) titleView
				.getLayoutParams();
		titleViewParams.height = titleHeight;
		titleView.setLayoutParams(titleViewParams);
		titleSize = ResourceUtil.getXmlDef(this.getContext(),
				R.dimen.title_text_size);
		textSize = ResourceUtil.getXmlDef(this.getContext(),
				R.dimen.title_other_text_size);
		Log.d("TitleView", "titleSize:" + titleSize);
	}

	public void setTitlePadding(int padding) {
		leftTitleView.setPadding(padding, 0, padding, 0);
		rightTitleView.setPadding(padding, 0, padding, 0);
	}

	public void hidenAll() {
		mTitle.setVisibility(View.GONE);
		leftTitleView.setVisibility(View.GONE);
		rightTitleView.setVisibility(View.GONE);
		mLeftImageBtn.setVisibility(View.GONE);
		mLeftTextBtn.setVisibility(View.GONE);
		mRightImageBtn.setVisibility(View.GONE);
		mRightTextBtn.setVisibility(View.GONE);
	}

}
