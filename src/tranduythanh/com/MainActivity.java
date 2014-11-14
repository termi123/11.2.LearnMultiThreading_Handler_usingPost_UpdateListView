package tranduythanh.com;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//khai báo Handler để quản lý Main Thread
	Handler hangido=new Handler();
	ListView lvgido;
	//Tạo đối tượng ArrayList để lưu trữ danh sách các số nguyên
	ArrayList<Integer>arr=new 
			ArrayList<Integer>();
	//khai báo Adapter để gán vào listview
	ArrayAdapter<Integer> adapter=null;
	//khai báo Atomic thay thế cho boolean
	AtomicBoolean ab=new AtomicBoolean(false);

	Button btnOk;
	EditText edtOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvgido =(ListView) findViewById(R.id.listView1);
		btnOk=(Button) findViewById(R.id.button1);
		edtOK=(EditText) findViewById(R.id.editText1);
		//khởi tạo đối tượng adapter
		adapter=new ArrayAdapter<Integer>(this,
				android.R.layout.simple_list_item_1, arr);
		//gán adapterc cho listview
		lvgido.setAdapter(adapter);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doStart();
			}
		});
	}
	private void doStart()
	{
		//lấy số lượng số được nhập từ edittext
		final int so=Integer.parseInt(edtOK.getText()+"");
		//tạo đối tượng có khả năng sinh số ngẫu nhiên
		final Random rd=new Random();
		ab.set(false);
		//tạo tiến trình con
		Thread thCon=new Thread(new Runnable() {

			@Override
			public void run() {
				//vòng lặp để thực hiện cập nhập giao diện
				for(int i=0;i<so && ab.get();i++)
				{
					//cho tiến trình ngừng 150 milisecond
					SystemClock.sleep(150);
					//gọi phương thức post để gửi message ra main Thread
					hangido.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//hàm này thuộc Main Thread để có thể cập nhập giao diện
							//rd.nextInt(100) để trả về số ngẫu nhiên từ 0-->99
							capnhapgiaodien(rd.nextInt(100));
						}
					});
				}
				//kết thúc vòng lặp thì gửi tiếp message ra Main Thread
				//để xác lập đã kết thúc tiến trình
				hangido.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//gọi hàm hiển thị kết thúc ở Main Thread
						thongbaoketthuccapnhat();
					}
				});
			}
		});
		ab.set(true);
		//khởi tạo Tiến trình
		thCon.start();
	}
	private void capnhapgiaodien(int so)
	{
		//đưa dữ liệu mới gửi từ Child Thread gửi về vào arr
		arr.add(so);
		//cập nhập lại Listview:
		adapter.notifyDataSetChanged();
	}
	private void thongbaoketthuccapnhat()
	{
		//thông báo đã kết thúc
		Toast.makeText(this, "Xong rồi đó....", Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
