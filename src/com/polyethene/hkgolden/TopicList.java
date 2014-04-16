package com.polyethene.hkgolden;

import java.util.List;

public class TopicList {
	public boolean success;
	public String error_msg;
	public List<Topic> topic_list;
	
	public static class Topic {
		public String Message_ID;
		public String Message_Title;
		public String Author_ID;
		public String Author_Name;
		public String Last_Reply_Date;
		public String Total_Replies;
		public String Message_Status;
		public String Message_Body;
		public String Rating;
		@Override
		public String toString() {
			return "Topic [Message_ID=" + Message_ID + ", Message_Title="
					+ Message_Title + ", Author_ID=" + Author_ID
					+ ", Author_Name=" + Author_Name + ", Last_Reply_Date="
					+ Last_Reply_Date + ", Total_Replies=" + Total_Replies
					+ ", Message_Status=" + Message_Status + ", Message_Body="
					+ Message_Body + ", Rating=" + Rating + "]";
		}	
	}

	@Override
	public String toString() {
		return "TopicList [success=" + success + ", error_msg=" + error_msg
				+ ", topic_list=" + topic_list + "]";
	}
	
}
