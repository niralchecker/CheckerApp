package com.checker.sa.android.helper;

import com.mor.sa.android.activities.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Calculation extends Activity {

	private Dialog dialog;
	private Button calcdia;

	private Button btnclr;
	private Button bt0;
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private Button bt4;
	private Button bt5;
	private Button bt6;
	private Button bt7;
	private Button bt8;
	private Button bt9;

	private Button btd;
	private Button btml;
	private Button btm;
	private Button btp;
	private Button bte;
	private Button btndeci;

	private EditText showtxt;

	String chk = "";
	String b = "";
	String rslt = "";
	int a = 0;

	float a1 = 919;
	float b1 = 919;

	int err = 0;
	int cr = 0;
	int wr = 0;

	EditText resulTtxt1;

	public void clear(Calculation Calculation) {

		showtxt.setText("");
		chk = "";
		b = "";
		rslt = "";
		a = 0;
		a1 = 0;
		b1 = 0;
		err = 0;
		cr = 0;
		wr = 0;

	}

	public Calculation(EditText resulttxt) {
		this.resulTtxt1 = resulttxt;
	}

	public void makeDialog(Context context) {

		dialog = new Dialog(context);

		dialog.setTitle("Calculator");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_calc);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		dialog.show();

		calcdia = (Button) dialog.findViewById(R.id.buttondia);

		bt0 = (Button) dialog.findViewById(R.id.button4);
		bt1 = (Button) dialog.findViewById(R.id.button9);
		bt2 = (Button) dialog.findViewById(R.id.button3);
		bt3 = (Button) dialog.findViewById(R.id.button11);
		bt4 = (Button) dialog.findViewById(R.id.button7);
		bt5 = (Button) dialog.findViewById(R.id.button51);
		bt6 = (Button) dialog.findViewById(R.id.button8);
		bt7 = (Button) dialog.findViewById(R.id.button2);
		bt8 = (Button) dialog.findViewById(R.id.button5);
		bt9 = (Button) dialog.findViewById(R.id.button6);

		btd = (Button) dialog.findViewById(R.id.button12);
		btml = (Button) dialog.findViewById(R.id.button13);
		btm = (Button) dialog.findViewById(R.id.button14);
		btp = (Button) dialog.findViewById(R.id.button15);
		bte = (Button) dialog.findViewById(R.id.buttoneql);

		btndeci = (Button) dialog.findViewById(R.id.buttondecimal);

		btnclr = (Button) dialog.findViewById(R.id.buttonclr);

		btnclr.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				clear(Calculation.this);

			}
		});

		btndeci.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				decimal(Calculation.this);

			}
		});

		calcdia.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				calculate(Calculation.this);

			}
		});

		bt0.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				zero(Calculation.this);
			}
		});

		bt1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				one(Calculation.this);
			}
		});

		bt2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				two(Calculation.this);

			}
		});

		bt3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				three(Calculation.this);

			}
		});

		bt4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				four(Calculation.this);

			}
		});

		bt5.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				five(Calculation.this);

			}
		});

		bt6.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				six(Calculation.this);

			}
		});

		bt7.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				seven(Calculation.this);

			}
		});

		bt8.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				eight(Calculation.this);

			}
		});

		bt9.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				nine(Calculation.this);

			}
		});

		btd.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// int w,x,y,z;
				// String num;
				// num = showtxt.getText().toString();
				//
				// w=num.indexOf ( "/" );
				// x=num.indexOf ( "*" );
				// y=num.indexOf ( "-" );
				// z=num.indexOf ( "+" );
				//
				// if ((w>=0)||(x>=0)||(y>=0)||(z>=0)) {

				if (!showtxt.getText().toString().equals("."))
					devide(Calculation.this);

				// }

			}
		});

		btml.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// int w,x,y,z;
				// String num;
				// num = showtxt.getText().toString();
				//
				// w=num.indexOf ( "/" );
				// x=num.indexOf ( "*" );
				// y=num.indexOf ( "-" );
				// z=num.indexOf ( "+" );
				//
				// if ((w>=0)||(x>=0)||(y>=0)||(z>=0)) {

				if (!showtxt.getText().toString().equals("."))
					multiply(Calculation.this);
				// }

			}
		});

		btm.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// int w,x,y,z;
				// String num;
				// num = showtxt.getText().toString();
				//
				// w=num.indexOf ( "/" );
				// x=num.indexOf ( "*" );
				// y=num.indexOf ( "-" );
				// z=num.indexOf ( "+" );
				//
				// if ((w>=0)||(x>=0)||(y>=0)||(z>=0)) {
				if (!showtxt.getText().toString().equals("."))
					minus(Calculation.this);

				// }

			}
		});

		btp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// int w,x,y,z;
				// String num;
				// num = showtxt.getText().toString();
				//
				// w=num.indexOf ( "/" );
				// x=num.indexOf ( "*" );
				// y=num.indexOf ( "-" );
				// z=num.indexOf ( "+" );
				//
				// if ((w>=0)||(x>=0)||(y>=0)||(z>=0)) {
				if (!showtxt.getText().toString().equals("."))
					plus(Calculation.this);

				// }

			}
		});

		bte.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				int w, x, y, z;
				String num;
				num = showtxt.getText().toString();
				w = num.indexOf("/");
				x = num.indexOf("*");
				y = num.indexOf("-");
				z = num.indexOf("+");
				if ((w >= 0) || (x >= 0) || (y >= 0) || (z >= 0)) {
				} else {
					if (!showtxt.getText().toString().equals("."))
						equal(Calculation.this);

				}

			}
		});

		showtxt = (EditText) dialog.findViewById(R.id.editTextrslt);

	}

	// numbers

	public void zero(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "0";
		showtxt.setText(b);

	}

	public void one(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "1";
		showtxt.setText(b);

	}

	public void two(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "2";
		showtxt.setText(b);

	}

	public void three(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "3";
		showtxt.setText(b);

	}

	public void four(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "4";
		showtxt.setText(b);

	}

	public void five(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "5";
		showtxt.setText(b);

	}

	public void six(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "6";
		showtxt.setText(b);

	}

	public void seven(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "7";
		showtxt.setText(b);

	}

	public void eight(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "8";
		showtxt.setText(b);

	}

	public void nine(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		b = aa + "9";
		showtxt.setText(b);

	}

	public void decimal(Calculation Calculation) {

		if (err == 1) {
			showtxt.setText("");
			err = 0;
		} else if ((cr == 1) || (wr == 1)) {
			showtxt.setText("");
			cr = 0;
			wr = 0;
		}

		a = 1;
		String aa = showtxt.getText().toString();
		int dci;
		dci = aa.indexOf(".");

		if (dci >= 0) {
		} else {
			b = aa + ".";
			showtxt.setText(b);

		}

	}

	// operations

	public void plus(Calculation Calculation) {

		int w, x, y, z;
		String num;
		num = showtxt.getText().toString();
		w = num.indexOf("/");
		x = num.indexOf("*");
		y = num.indexOf("-");
		z = num.indexOf("+");
		if ((w >= 0) || (x >= 0) || (y >= 0) || (z >= 0)) {
		} else {

			check();

			if (err == 1) {
				showtxt.setText("");
				err = 0;
			}

			if (a == 1) {
				a = 0;
				chk = "plus";
				a1 = Float.parseFloat(showtxt.getText().toString());
				b = a1 + "+";
				showtxt.setText(b);
				cr = 1;

			}

		}

	}

	public void minus(Calculation Calculation) {

		int w, x, y, z;
		String num;
		num = showtxt.getText().toString();
		w = num.indexOf("/");
		x = num.indexOf("*");
		y = num.indexOf("-");
		z = num.indexOf("+");
		if ((w >= 0) || (x >= 0) || (y >= 0) || (z >= 0)) {
		} else {

			check();

			if (err == 1) {
				showtxt.setText("");
				err = 0;
			}

			if (a == 1) {
				a = 0;
				chk = "minus";
				a1 = Float.parseFloat(showtxt.getText().toString());
				b = a1 + "-";
				showtxt.setText(b);
				cr = 1;

			}

		}
	}

	public void multiply(Calculation Calculation) {

		int w, x, y, z;
		String num;
		num = showtxt.getText().toString();
		w = num.indexOf("/");
		x = num.indexOf("*");
		y = num.indexOf("-");
		z = num.indexOf("+");
		if ((w >= 0) || (x >= 0) || (y >= 0) || (z >= 0)) {
		} else {

			check();

			if (err == 1) {
				showtxt.setText("");
				err = 0;
			}

			if (a == 1) {
				a = 0;
				chk = "multiply";
				a1 = Float.parseFloat(showtxt.getText().toString());

				b = a1 + "*";
				showtxt.setText(b);
				cr = 1;

			}

		}

	}

	public void devide(Calculation Calculation) {

		int w, x, y, z;
		String num;
		num = showtxt.getText().toString();
		w = num.indexOf("/");
		x = num.indexOf("*");
		y = num.indexOf("-");
		z = num.indexOf("+");
		if ((w >= 0) || (x >= 0) || (y >= 0) || (z >= 0)) {
		} else {

			check();

			if (err == 1) {
				showtxt.setText("");
				err = 0;
			}

			if (a == 1) {
				a = 0;
				chk = "devide";
				a1 = Float.parseFloat(showtxt.getText().toString());
				b = a1 + "/";
				showtxt.setText(b);
				cr = 1;
			}

		}
	}

	public void equal(Calculation Calculation) {

		wr = 1;
		if (err == 1) {
			showtxt.setText("");
			err = 0;
		}

		String num;
		num = showtxt.getText().toString();
		int dci;
		dci = num.indexOf(".");

		if ((num.matches("[0-9]+")) || (dci >= 0)) {

			if (chk == "plus") {

				float b1 = Float.parseFloat(showtxt.getText().toString());
				float ans = (float) a1 + (float) b1;

				rslt = String.format("%.2f", ans);
				showtxt.setText(rslt);

			} else if (chk == "minus") {

				float b1 = Float.parseFloat(showtxt.getText().toString());
				float ans = (float) a1 - (float) b1;

				rslt = String.format("%.2f", ans);

				showtxt.setText(rslt);

			} else if (chk == "multiply") {

				float b1 = Float.parseFloat(showtxt.getText().toString());
				float ans = (float) a1 * (float) b1;

				rslt = String.format("%.2f", ans);

				showtxt.setText(rslt);

			} else if (chk == "devide") {

				float b1 = Float.parseFloat(showtxt.getText().toString());

				if (b1 != 0) {

					float ans = (float) a1 / (float) b1;

					rslt = String.format("%.2f", ans);

					// Toast.makeText(this, rslt, Toast.LENGTH_SHORT).show();
					showtxt.setText(rslt);
				} else {
					err = 1;
					showtxt.setText("Math Error");

				}

			} else {

			}

			chk = "";
		}

	}

	public void check() {

		if (rslt.length() == 1 && rslt.contains(".")) {
			rslt = rslt + "0";
		}
		if (rslt.length() == 0) {
			rslt = "0";
		}
		if (rslt == "") {
			a1 = Float.parseFloat(rslt);
		}

		String num;
		num = showtxt.getText().toString();
		if (num.length() == 1 && num.contains(".")) {
			num = num + "0";
		}
		if (num.length() == 0) {
			num = "0";
		}
		int dci;
		dci = num.indexOf(".");

		if ((num.matches("[0-9]+")) || (dci >= 0)) {

			if (chk == "plus") {

				float b1 = Float.parseFloat(num);
				float ans = (float) a1 + (float) b1;

				rslt = String.format("%.2f", ans);

				showtxt.setText(rslt);

			} else if (chk == "minus") {

				float b1 = Float.parseFloat(num);
				float ans = (float) a1 - (float) b1;

				rslt = String.format("%.2f", ans);

				showtxt.setText(rslt);

			} else if (chk == "multiply") {

				float b1 = Float.parseFloat(num);
				float ans = (float) a1 * (float) b1;

				rslt = String.format("%.2f", ans);

				showtxt.setText(rslt);

			} else if (chk == "devide") {

				float b1 = Float.parseFloat(num);

				if (b1 != 0) {
					float ans = (float) a1 / (float) b1;
					rslt = String.format("%.2f", ans);
					showtxt.setText(rslt);
				} else {
					err = 1;
					showtxt.setText("Math Error");

				}

			} else {

			}

			chk = "";

		}

	}

	public void calculate(Calculation Calculation) {

		int w, x, y, z;
		String num;
		num = showtxt.getText().toString();

		w = num.indexOf("/");
		x = num.indexOf("*");
		y = num.indexOf("-");
		z = num.indexOf("+");
		if ((w >= 0) || (x >= 0) || (y >= 0) || (z >= 0)) {

			showtxt.setText("");
			if (!rslt.equals(""))
				resulTtxt1.setText(rslt);
			dialog.cancel();

		} else {

			String num1;
			num1 = showtxt.getText().toString();
			int dci;
			dci = num1.indexOf(".");

			if ((num1.matches("[0-9]+")) || (dci >= 0)) {
				equal(Calculation.this);
			}
			if (!rslt.equals(""))
				showtxt.setText("");
			if (!rslt.equals(""))
				resulTtxt1.setText(rslt);
			dialog.cancel();

		}

	}

}