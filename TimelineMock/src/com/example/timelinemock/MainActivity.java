package com.example.timelinemock;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.timelinemock.TimelineArrayAdapter.TimelineListItem;
import com.example.timelinemock.twitter.GetTimelineAsyncTask;

public class MainActivity extends ListActivity {

	static final int ANIMATION_DURATION = 200;

	private Integer operationCellPosition = null;

	private final TimelineListItem operationCell;

	public MainActivity() {
		operationCell = new TimelineListItem();
		operationCell.isOperationCell = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<TimelineListItem> tlItemList = new ArrayList<TimelineListItem>();
		for (int i = 0; i < 100; i++) {
			TimelineListItem item = new TimelineListItem();
			item.screenName = "スクリーン名前" + i;
			item.accountName = "AccountName" + i;
			item.tweetText = "GX GAMINGのマウス「MAURUS X」の取り扱いを開始しました！ お値段税別2,760円で販売中です◎サンプルも在庫のところにぶら下げてありますので気になる方は触ってみてくださいね！"
					+ i;
			item.retweetBy = null;
			// item.iconImage = ;

			tlItemList.add(item);
		}

		TimelineArrayAdapter adapter = new TimelineArrayAdapter(this,
				R.layout.row, tlItemList);
		setListAdapter(adapter);

		this.getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TimelineListItem myCell = new TimelineListItem();
						// myCell.isOperationCell = true;
						// addNewCell(myCell, position);

						// TranslateAnimation transanim = new
						// TranslateAnimation(
						// Animation.RELATIVE_TO_SELF, 0.0f,
						// Animation.RELATIVE_TO_SELF, 0.0f,
						// Animation.RELATIVE_TO_SELF, -1.0f,
						// Animation.RELATIVE_TO_SELF, 0.0f);
						// transanim.setDuration(ANIMATION_DURATION);
						//
						// LayoutAnimationController controller = new
						// LayoutAnimationController(
						// transanim, 1.0f);
						// MainActivity.this.getListView().setLayoutAnimation(
						// controller);

						TimelineArrayAdapter adapter = ((TimelineArrayAdapter) MainActivity.this
								.getListAdapter());

						boolean isCloseOnly = false;
						if (operationCellPosition != null) {
							// 操作セルを消す
							adapter.remove(operationCell);

							if (operationCellPosition == position + 1) {
								isCloseOnly = true;
							} else if (operationCellPosition < position) {
								position--;
							}
							operationCellPosition = null;
						}

						if (operationCellPosition == null && !isCloseOnly) {
							// 操作セルを出す
							operationCellPosition = position + 1;

							adapter.insert(operationCell, operationCellPosition);
						}
						// MainActivity.this.getListView().startLayoutAnimation();
					}

				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu_layouts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;

		switch (item.getItemId()) {
		case R.id.menu_tweet:
			updateTimeline();

			result = true;
			break;
		}

		return result;
	}

	private void updateTimeline() {
		GetTimelineAsyncTask task = new GetTimelineAsyncTask(
				new GetTimelineAsyncTask.GetTimelineResultListner() {

					@Override
					public void onSuccess(List<Status> result) {
						TimelineArrayAdapter adapter = ((TimelineArrayAdapter) MainActivity.this
								.getListAdapter());

						List<TimelineListItem> listItems = new ArrayList<TimelineListItem>();
						for (Status status : result) {
							TimelineListItem item = new TimelineListItem();
							item.screenName = status.getUser().getName();
							item.tweetText = status.getText();
							item.imageProfileUrl = status.getUser().getProfileImageURL();
							listItems.add(item);
						}
						adapter.clear();
						adapter.addAll(listItems);
						
						Toast t = Toast.makeText(MainActivity.this, String.format("ツイートを%d件取得しました", listItems.size()), Toast.LENGTH_SHORT);
						t.show();
					}

					@Override
					public void onError(Throwable cause) {
						Toast t = Toast.makeText(MainActivity.this,
								"タイムライン取得失敗", Toast.LENGTH_SHORT);
						t.show();
					}
				});
		
		task.execute();
	}

	private void addNewCell(final TimelineListItem cell, final int position) {

		Bitmap bitmap = getListViewBitmap(position + 1);
		final ImageView imageView = (ImageView) findViewById(R.id.bitmapImageView);
		imageView.setImageBitmap(bitmap);
		// imageView.setBackgroundDrawable(new BitmapDrawable(getResources(),
		// bitmap));
		imageView.setVisibility(View.VISIBLE);

		final View rowView = this.getListView().getChildAt(position);
		TranslateAnimation transanim = new TranslateAnimation(0, 0,
				rowView.getBottom() - 100, rowView.getBottom());
		transanim.setDuration(ANIMATION_DURATION);
		transanim.setAnimationListener(new AnimationListener() {

			private int startPos;

			@Override
			public void onAnimationEnd(Animation animation) {
				// ((ArrayAdapter<TimelineListItem>)
				// MainActivity.this.getListAdapter()).insert(cell, position+1);
				imageView.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		((ArrayAdapter<TimelineListItem>) this.getListAdapter()).insert(cell,
				position + 1);
		imageView.startAnimation(transanim);
	}

	private Bitmap getListViewBitmap(int index) {
		this.getListView().getChildAt(index).setDrawingCacheEnabled(true);
		Bitmap bitmap = this.getListView().getChildAt(index).getDrawingCache();
		Bitmap bitmap1 = Bitmap.createBitmap(bitmap);
		this.getListView().getChildAt(index).setDrawingCacheEnabled(false);
		return bitmap1;
	}

}
