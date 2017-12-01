package com.lovelqq.julong.jdbc.userlogin;

import com.lovelqq.julong.jdbc.R;
import com.lovelqq.julong.jdbc.user.Phonenumber;
import com.lovelqq.julong.jdbc.jdbcUtils.SqlSentence;
import com.lovelqq.julong.jdbc.user.User;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Homepage extends Activity {
	//是否已经上传标志
	private static boolean threadRun=false;
	public static Context context;
	//上传按钮,下载按钮
	Button btsc,btxz;
	private static TextView tv;
	static int conten=0;//同步个数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		tv=(TextView) findViewById(R.id.tv);
		SqlSentence.numberAllConten();
		init();
		//上传按钮
		btsc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//	SqlSentence.numberAllConten();
				if(threadRun==false){
					//上传手机里的联系人
					update();
				}else {
					Toast.makeText(Homepage.this, "本次已经同步完成", Toast.LENGTH_SHORT).show();
				}
			}
		});
		//下载通讯类按钮
		btxz.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				//添加向手机号添加手机号
				try {
					//查询本地联系人
					ArrayList<Phonenumber>location=gethostuser();
					//列出在云端查到的联系人,写入前先判断手机里是否已经存在
					ArrayList<Phonenumber> yunlist=SqlSentence.numberAllConten();
					//存放云端和手机本地不同的联系人
					ArrayList<Phonenumber> differentarr=new ArrayList<Phonenumber>();
					//判断写入的条数
					int conint=0;
					for(Phonenumber u:yunlist){
						//是否本地存在用户
						boolean flag=false;
						Log.e("云端联系人", u.getUsername()+u.getPhonenumber());
							for(Phonenumber locu:location)
							{
								//判断如果本地不存在此联系人或者手机号不一样则写入本地通讯录
								if(u.getPhonenumber()==locu.getPhonenumber()&&u.getUsername()==locu.getUsername());
								{
									flag=true;
                                    Log.e("通讯已存在",locu.getUsername() + locu.getPhonenumber());
									break;
								}
							}
							if(flag){
//								Log.e("通讯已存在",u.getUsername() + u.getPhonenumber());

							}else {
								//addLocalhostUser(u.getUsername(), u.getPhonenumber());
                                Phonenumber phonenumber=new Phonenumber(u.getUsername(), u.getPhonenumber());
                                differentarr.add(phonenumber);
								Log.e("写入的", u.getUsername() + u.getPhonenumber());
								conint++;//写入的条数加1
							}

						}
						//不同的联系人
                    for (Phonenumber phonenumber:differentarr)
                    {
                        Log.e("不同的联系人",phonenumber.getUsername()+phonenumber.getPhonenumber());
                    }
						Log.e("写入多少条记录：",""+conint);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}



	public static Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					int conte=msg.arg1;
					tv.setText("本次同步"+conte+"条记录");
					break;
				case 2:
					int ind=msg.arg1;
					tv.setText("云端记录"+ ind+"条");
					break;
				case 3:
					tv.setText("正在同步.......");
					break;
				default:
					break;
			}
		};
	};
	//初始化控件
	private void init() {
		btsc=(Button) findViewById(R.id.btsc);
		btxz= (Button) findViewById(R.id.btxz);
	}
	//向手机里添加联系人
	public void  addLocalhostUser( String usernaem ,String phonenumber) throws Exception{
		ContentValues values=new ContentValues();
		Uri rawContacturi =getContentResolver().insert(RawContacts.CONTENT_URI, values);
		long rawContactId=ContentUris.parseId(rawContacturi);
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		//设置内容类型
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		//设置联系人名字
		values.put(StructuredName.GIVEN_NAME, usernaem);
		//向联系人Uri添加联系人名字
		getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		//设置联系人电话号码
		values.put(Phone.NUMBER, phonenumber);
		//设置电话类型
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
		Log.e("联系人插入", "插入成功");
	}
	//获取本地联系人
	public ArrayList<Phonenumber> gethostuser(){
		int con=0;
		ArrayList<Phonenumber> list = new ArrayList<Phonenumber>();
		Cursor cursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		while (cursor.moveToNext()) {
			String contactId=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactId, null, null);
			//每个用户可能有多个手机号
			while(phones.moveToNext())
			{
				String PhNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				//Log.e("Hpmepage", name+"电话号码："+PhNumber);
				Phonenumber phonenumber=new Phonenumber(name, PhNumber);
				list.add(phonenumber);
				con++;
			}
			}
		cursor.close();
		Log.e("本地联系人","读取"+con+"条记录");
		return list;
	}
	//上传通讯录
	public  void  update(){
		try {
            int upconint=0;
            upconint=updiff().size();
            //把数组里联系人上传
			SqlSentence.insetPhoneNumber(updiff());
			Message msg=new Message();
			msg.what=1;
			msg.arg1=upconint;
			handler.sendMessage(msg);
			threadRun=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查找出需要上传联系人
        public ArrayList<Phonenumber> updiff(){
            //查询本地联系人
            ArrayList<Phonenumber>location1=gethostuser();
            //列出在云端查到的联系人,写入前先判断手机里是否已经存在
            ArrayList<Phonenumber> yunlist1=SqlSentence.numberAllConten();
            //存放云数据库中没有，将要上传的的联系人
            ArrayList<Phonenumber> differentarr=new ArrayList<Phonenumber>();
            //判断上传的条数
            int upconint=0;
            boolean flag=false;
            //查找出不再云数据库中的联系人
            for (int i=0;i<location1.size();i++)
            {
                flag=false;
                for (int j=0;j<yunlist1.size();j++)
                {
                    if(location1.get(i).getUsername().equals(yunlist1.get(j).getUsername())&&
                            location1.get(i).getPhonenumber().equals(yunlist1.get(j).getPhonenumber()))
                    {
                        flag=true;
                        break;
                    }
                }
                if (flag==true)
                {
                    //Log.e("联系人在云端已经存在",location1.get(i).getUsername()+ location1.get(i).getPhonenumber());
                }
                else {
                    //把不在云数据库的号码上传
                    Phonenumber phonenumber1 = new Phonenumber(User.getId(), location1.get(i).getUsername(), location1.get(i).getPhonenumber());
                    differentarr.add(phonenumber1);
                    upconint++;
                }
            }
            Log.e("需要上传联系人的数量：","需要上传"+upconint+"个");
            return differentarr;
        }


	@Override
	protected void onPause() {
		super.onPause();
		User.id=-1;
		threadRun=false;//清除上传表示，如果不清除，第一次登录成功后，第二次将无法上传
		Log.e("清除id","清除id");
	}

}
