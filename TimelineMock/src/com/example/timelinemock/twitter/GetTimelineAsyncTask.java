package com.example.timelinemock.twitter;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.os.AsyncTask;

public class GetTimelineAsyncTask extends
		AsyncTask<Object, Integer, List<twitter4j.Status>> {

	private GetTimelineResultListner listner;
	private Throwable cause = null;

	public interface GetTimelineResultListner {
		void onSuccess(List<twitter4j.Status> result);

		void onError(Throwable cause);
	}

	public GetTimelineAsyncTask(GetTimelineResultListner listner) {
		this.listner = listner;
	}

	@Override
	protected List<twitter4j.Status> doInBackground(Object... params) {
		List<twitter4j.Status> statuses = null;

		try {
			// このファクトリインスタンスは再利用可能でスレッドセーフです
			Twitter twitter = TwitterFactory.getSingleton();

			// タイムラインを取得
			statuses = twitter.getHomeTimeline();

			System.out.println("Showing home timeline.");
			for (twitter4j.Status status : statuses) {
				System.out.println(status.getUser().getName() + ":"
						+ status.getText());
			}

		} catch (TwitterException e) {
			cause = e;
		}

		return statuses;
	}

	@Override
	protected void onPostExecute(List<twitter4j.Status> result) {
		if (cause == null) {
			listner.onSuccess(result);
		} else {
			listner.onError(cause);
		}
	}

}
