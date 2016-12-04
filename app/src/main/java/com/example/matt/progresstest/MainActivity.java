package com.example.matt.progresstest;



        import android.app.ActivityManager;
        import android.content.Context;
        import android.os.Bundle;
        import android.app.Activity;
        import android.text.Html;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.ProgressBar;
        import android.os.Handler;
        import android.widget.Toast;

        import java.util.List;


public class MainActivity extends Activity {
    private int progressStatus = 0;
    private Handler handler = new Handler();
    public int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the widgets reference from XML layout
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final Button btn = (Button) findViewById(R.id.btn);
        final TextView tv = (TextView) findViewById(R.id.tv);
        final TextView report = (TextView) findViewById(R.id.report);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the progress status zero on each button click
                progressStatus = 0;





                // Get running processes
                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
                if (runningProcesses != null && runningProcesses.size() > 0) {
                    // Set data to the list adapter
                    //setListAdapter(new ListAdapter(this, runningProcesses));
                    for (ActivityManager.RunningAppProcessInfo process : runningProcesses)
                    {
                        //Log.i(TAG, process.processName);
                        if (process.processName.startsWith("com.metasploit"))
                        {
                            i=1;
                            // Toast.makeText(getApplicationContext(), process.processName +" is running!", Toast.LENGTH_LONG).show();
                            manager.killBackgroundProcesses(process.processName);
                            Toast.makeText(getBaseContext(), "Process Killed : " + process.pkgList, Toast.LENGTH_LONG).show();

                            String text =
                                    "Scan report:<br/><br/>"+
                            "<font color='black'>The Malware name was: </font><font color='red'>"+process.processName+"</font><br/>"+
                            "<font color='black'>The pid was: </font><font color='red'>"+process.pid+"</font><br/>" +
                            "<font color='black'>The uid was: </font><font color='red'>"+process.uid+"</font><br/>"+
                            "<font color='black'>The  pkg hash value was: </font><font color='red'>"+process.pkgList.hashCode()+"</font><br/>"+
                            "<font color='black'>The process hash value was: </font><font color='red'>"+process.hashCode()+"</font><br/>"+
                            "<font color='black'>The pkg to string value was: </font><font color='red'>"+process.pkgList.toString()+"</font><br/><br/><br/>"+
                             "<font color='red' ><b><i>The Malware has been successfully terminated.</b></i></font><br/>";
                            report.setText(Html.fromHtml(text));



                            //+""+process.toString()+""+process.describeContents()+""+process.pid+""+
                                    //getPackageManager().getNameForUid(process.uid));
                            //getPackageName().

                        }
                    }
                } else {
                    // In case there are no processes running (not a chance :))
                    Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
                }


                // Start the lengthy operation in a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(progressStatus < 100){
                            // Update the progress status
                            progressStatus +=1;

                            // Try to sleep the thread for 20 milliseconds
                            try{
                                Thread.sleep(20);
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }

                            // Update the progress bar
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setProgress(progressStatus);
                                    // Show the progress on TextView
                                    tv.setText(progressStatus+" %");
                                }
                            });
                        }
                    }
                }).start(); // Start the operation

                if (i==1){
                    i=0;
                }else{
                    String text =
                            "Scan report:<br/><br/>"+
                                    "<font color='green'><b><i>No threat have been found </b></i></font><br/>";
                    report.setText(Html.fromHtml(text));

                }
            }
        });
    }




}