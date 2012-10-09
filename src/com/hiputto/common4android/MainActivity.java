package com.hiputto.common4android;

import com.hiputto.common4android.superclass.HP_BaseActivity;
import com.hiputto.common4android.util.HP_BitmapUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestBitmapFinished;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends HP_BaseActivity {

	private ImageView imageView;
	private LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4;
	private Button button,resetButton;
	
	private Bitmap bm ;
	
	private float []carray=new float[20]; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		linearLayout1 = (LinearLayout) findViewById(R.id.edit_text_linearlayout1);
		linearLayout2 = (LinearLayout) findViewById(R.id.edit_text_linearlayout2);
		linearLayout3 = (LinearLayout) findViewById(R.id.edit_text_linearlayout3);
		linearLayout4 = (LinearLayout) findViewById(R.id.edit_text_linearlayout4);
		button = (Button) findViewById(R.id.button);
		resetButton = (Button) findViewById(R.id.button_reset);
		
		
		String imageUrl = "http://m2.img.libdd.com/farm4/2012/1008/09/CCCA769D1889542204D0ECBB0DFA0F19148484189977_500_316.jpg";
		
		HP_NetWorkUtils hp_NetWorkUtils = new HP_NetWorkUtils();
		
		hp_NetWorkUtils.getAsyncRequestBitmap(imageUrl, new OnRequestBitmapFinished() {
			
			@Override
			public void onRequestBitmapFinished(String resultStr, Bitmap bitmap,
					boolean isSuccess) {
				bm = HP_BitmapUtils.bitmap2Round(bitmap, (float) 20.0);
				bm = HP_BitmapUtils.bitmap2WatermarkText(bm, "asdfasdfaasdfadsfdsafdasfadsasdfdasfad", Color.RED, 50, 0, 0,true);
				
				imageView.setImageBitmap(bm);
			}
		});
		
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				imageView.setImageBitmap(bm);
				
			}
		});
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText et = (EditText) linearLayout1.getChildAt(0);//1
				carray[0] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout1.getChildAt(1);//0
				carray[1] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout1.getChildAt(2);//0
				carray[2] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout1.getChildAt(3);//0
				carray[3] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout1.getChildAt(4);//0
				carray[4] = Float.valueOf(et.getText().toString());
				
				et = (EditText) linearLayout2.getChildAt(0);//0
				carray[5] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout2.getChildAt(1);//1
				carray[6] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout2.getChildAt(2);//0
				carray[7] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout2.getChildAt(3);//0
				carray[8] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout2.getChildAt(4);//0
				carray[9] = Float.valueOf(et.getText().toString());
				
				et = (EditText) linearLayout3.getChildAt(0);//0
				carray[10] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout3.getChildAt(1);//0
				carray[11] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout3.getChildAt(2);//1
				carray[12] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout3.getChildAt(3);//0
				carray[13] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout3.getChildAt(4);//0
				carray[14] = Float.valueOf(et.getText().toString());
				
				et = (EditText) linearLayout4.getChildAt(0);//0
				carray[15] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout4.getChildAt(1);//0
				carray[16] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout4.getChildAt(2);//0
				carray[17] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout4.getChildAt(3);//1
				carray[18] = Float.valueOf(et.getText().toString());
				et = (EditText) linearLayout4.getChildAt(4);//0
				carray[19] = Float.valueOf(et.getText().toString());
				
				Bitmap bitmap = Bitmap.createBitmap(bm);
				Paint paint = new Paint(); 
				paint.setColorFilter(null); 
				Canvas canvas = new Canvas(bitmap);
				ColorMatrix cm = new ColorMatrix(); 
				//设置颜色矩阵 
				cm.set(carray); 
				//颜色滤镜，将颜色矩阵应用于图片 
				paint.setColorFilter(new ColorMatrixColorFilter(cm)); 
				//绘图 
				canvas.drawBitmap(bitmap, 0, 0, paint); 
				
				imageView.setImageBitmap(bitmap);
			}
		});
		
	}
	
	/**
	 * 怀旧效果(相对之前做了优化快一倍)
	 * @param bmp
	 * @return
	 */
	private Bitmap oldRemeber(Bitmap bmp)
	{
		// 速度测试
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		
		for (int i = 0; i < height; i++)
		{
			for (int k = 0; k < width; k++)
			{
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
//				R' = a*R + b*G + c*B + d*A + e;
//				G' = f*R + g*G + h*B + i*A + j;
//				B' = k*R + l*G + m*B + n*A + o;
//				A' = p*R + q*G + r*B + s*A + t;
				
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				
				int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixels[width * i + k] = newColor;

			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}
