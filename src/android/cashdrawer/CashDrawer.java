package android.cashdrawer;

public class CashDrawer {
	private static final String TAG = "CashDrawer";
	
/* CashDrawer.
 * 
 */
	public CashDrawer() throws SecurityException {
	}

	// Getters and setters
	public void openCashDrawer() {
		open();
	}

	

	// JNI
	private native static void open();
		static {
		System.loadLibrary("cash_drawer");
	}
}
