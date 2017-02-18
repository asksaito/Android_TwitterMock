package com.example.timelinemock;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public final class TimelineArrayAdapter extends ArrayAdapter<TimelineArrayAdapter.TimelineListItem> {
	private Context context;
	private LayoutInflater inflater;
	private List<TimelineArrayAdapter.TimelineListItem> timelineListItems;
	
	public TimelineArrayAdapter(Context context, int resource,
			List<TimelineArrayAdapter.TimelineListItem> objects) {
		super(context, resource, objects);
		
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.timelineListItems = objects;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        View v = convertView;
        if(v == null){
            v = inflater.inflate(R.layout.row, null);
        }
 
        ImageView imageIcon = (ImageView)v.findViewById(R.id.ImageIcon);
        TextView textScreenName = (TextView)v.findViewById(R.id.TextScreenName);
        TextView textTweet = (TextView)v.findViewById(R.id.TextTweet);
        ImageView imageThumb = (ImageView)v.findViewById(R.id.ImageThumb);
        Button buttonReply = (Button)v.findViewById(R.id.ButtonReply);
        Button buttonRetweet = (Button)v.findViewById(R.id.ButtonRetweet);
        Button buttonFavorite = (Button)v.findViewById(R.id.ButtonFavorite);
        Button buttonTransfer = (Button)v.findViewById(R.id.ButtonTransfer);
        Button buttonDetail = (Button)v.findViewById(R.id.ButtonDetail);
//        ProgressBar waitBar = (ProgressBar)v.findViewById(R.id.WaitBar);
// 
//        //�摜���B���A�v���O���X�o�[��\��
//        waitBar.setVisibility(View.VISIBLE);
//        imageView.setVisibility(View.GONE);
 
        TimelineListItem item = timelineListItems.get(position);
//        Log.d("debuggg", String.valueOf(position) + " "+String.valueOf(item.isOperationCell));
        
        if (item.isOperationCell) {
        	imageIcon.setVisibility(View.GONE);
        	textScreenName.setVisibility(View.GONE);
        	textTweet.setVisibility(View.GONE);
        	imageThumb.setVisibility(View.GONE);
        	buttonReply.setVisibility(View.VISIBLE);
        	buttonRetweet.setVisibility(View.VISIBLE);
        	buttonFavorite.setVisibility(View.VISIBLE);
        	buttonTransfer.setVisibility(View.VISIBLE);
        	buttonDetail.setVisibility(View.VISIBLE);
        	return v;
        } else {
        	imageIcon.setVisibility(View.VISIBLE);
        	textScreenName.setVisibility(View.VISIBLE);
        	textTweet.setVisibility(View.VISIBLE);
        	imageThumb.setVisibility(View.VISIBLE);
        	buttonReply.setVisibility(View.GONE);
        	buttonRetweet.setVisibility(View.GONE);
        	buttonFavorite.setVisibility(View.GONE);
        	buttonTransfer.setVisibility(View.GONE);
        	buttonDetail.setVisibility(View.GONE);
        }
        
        //���ۂɎg�p����摜��URL��ێ�
        textScreenName.setText(item.screenName);
        textTweet.setText(item.tweetText);
 
        //���̉摜�ݒ�
        imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.no_icon));
        imageThumb.setImageDrawable(context.getResources().getDrawable(R.drawable.titanfall_xone));
 
        //�摜�Ǎ�
        try{
        	imageIcon.setTag(item.imageProfileUrl);
            // AsyncTask�͂P�񂵂����s�ł��Ȃ��ׁA����C���X�^���X�𐶐�
            ImageGetTask task = new ImageGetTask(context, imageIcon);
            task.execute(item.imageProfileUrl);
        }
        catch(Exception e){
//            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.x));
//            waitBar.setVisibility(View.GONE);
//            imageView.setVisibility(View.VISIBLE);
        }
 
        return v;
    }

	public static class TimelineListItem {
		public ImageView imageProfile;
		public String imageProfileUrl;
		public String screenName;
		public String accountName;
		public String tweetText;
		public String retweetBy;
		public ImageView imageThumb;
		
		public boolean isOperationCell = false;
		
	}
}
