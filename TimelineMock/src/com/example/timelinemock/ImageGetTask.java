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
		// �Ώۂ̍��ڂ�ێ����Ă���
		this.imageView = imageView;
//		progress = _progress;
		this.tag = imageView.getTag().toString();
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// ������Http�o�R�ŉ摜���擾���܂��B�擾��Bitmap�ŕԂ��܂��B
		synchronized (context) {
			try {
				// //�L���b�V�����摜�f�[�^���擾
				// Bitmap image = ImageCache.getImage(params[0]);
				// if (image == null) {
				// �L���b�V���Ƀf�[�^�����݂��Ȃ��ꍇ��web���摜�f�[�^���擾
				URL imageUrl = new URL(params[0]);
				InputStream imageIs;
				imageIs = imageUrl.openStream();
				Bitmap bitmap = BitmapFactory.decodeStream(imageIs);
				// //�擾�����摜�f�[�^���L���b�V���ɕێ�
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
		// Tag���������̂��m�F���āA�����ł���Ή摜��ݒ肷��
		// �iTag�̐ݒ�����Ȃ��ƕʂ̍s�ɉ摜���\������Ă��܂��j
		if (tag.equals(imageView.getTag())) {
			if (result != null) {
				// �摜�̐ݒ�
				imageView.setImageBitmap(result);
			} else {
				// //�G���[�̏ꍇ�́~���\��
				// imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.x));
			}
			// //�v���O���X�o�[���B���A�擾�����摜��\��
			// progress.setVisibility(View.GONE);
			// image.setVisibility(View.VISIBLE);
		}
	}
}
