package com.example.timelinemock;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

class ImageGetTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView imageView;
//	private ProgressBar progress;
	private String tag;

	private Context context;

	public ImageGetTask(Context context, ImageView imageView) {
		this.context = context;
		// 対象の項目を保持しておく
		this.imageView = imageView;
//		progress = _progress;
		this.tag = imageView.getTag().toString();
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// ここでHttp経由で画像を取得します。取得後Bitmapで返します。
		synchronized (context) {
			try {
				// //キャッシュより画像データを取得
				// Bitmap image = ImageCache.getImage(params[0]);
				// if (image == null) {
				// キャッシュにデータが存在しない場合はwebより画像データを取得
				URL imageUrl = new URL(params[0]);
				InputStream imageIs;
				imageIs = imageUrl.openStream();
				Bitmap bitmap = BitmapFactory.decodeStream(imageIs);
				// //取得した画像データをキャッシュに保持
				// ImageCache.setImage(params[0], image);
				// }
				return bitmap;
			} catch (MalformedURLException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// Tagが同じものか確認して、同じであれば画像を設定する
		// （Tagの設定をしないと別の行に画像が表示されてしまう）
		if (tag.equals(imageView.getTag())) {
			if (result != null) {
				// 画像の設定
				imageView.setImageBitmap(result);
			} else {
				// //エラーの場合は×印を表示
				// imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.x));
			}
			// //プログレスバーを隠し、取得した画像を表示
			// progress.setVisibility(View.GONE);
			// image.setVisibility(View.VISIBLE);
		}
	}
}
