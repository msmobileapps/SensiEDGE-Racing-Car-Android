/*******************************************************************************
 * COPYRIGHT(c) 2015 STMicroelectronics
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of STMicroelectronics nor the names of its contributors
 *      may be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 ******************************************************************************/
package com.msapps.carracing.bluetooth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.msapps.carracing.MainActivity;
import com.msapps.carracing.R;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;
import com.st.BlueSTSDK.Utils.NodeScanActivity;

import static com.msapps.carracing.MainActivity.enableDeviceAcc;
import static com.msapps.carracing.bluetooth.FeatureListActivity.NODE_FRAGMENT;

/**
 * This activity will show a list of device that are supported by the sdk
 */
public class ScanActivity extends NodeScanActivity implements AbsListView.OnItemClickListener {


    private final static int SCAN_TIME_MS = 10 * 1000; //10sec
    private NodeArrayAdapter mAdapter;
    private ProgressDialog mConnectionWait;


    private Manager.ManagerListener mUpdateDiscoverGui = new Manager.ManagerListener() {

        /**
         * call the stopNodeDiscovery for update the gui state
         * @param m manager that start/stop the process
         * @param enabled true if a new discovery start, false otherwise
         */
        @Override
        public void onDiscoveryChange(Manager m, boolean enabled) {
            if (!enabled)
                ScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopNodeDiscovery();
                    }//run
                });
        }//onDiscoveryChange

        @Override
        public void onNodeDiscovered(Manager m, Node node) {
            runOnUiThread(() -> updateNodeList());
        }
    };
    private NodeContainerFragment mNodeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        if(enableDeviceAcc){
            findViewById(R.id.textView2).setOnClickListener(view -> {
                Intent i = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(i);
            });
        }

        AbsListView listView = findViewById(R.id.nodeListView);
        mAdapter = new NodeArrayAdapter(this);
        listView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);

        //add the already discovered nodes
        mAdapter.addAll(mManager.getNodes());
        mConnectionWait = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        mConnectionWait.setTitle(R.string.searchDialogConnTitle);
        mConnectionWait.show();

    }

    /**
     * clear the adapter and the manager list of nodes
     */
    private void resetNodeList() {
        mManager.resetDiscovery();
        mAdapter.clear();
        mAdapter.addAll(mManager.getNodes());
    }

    private void updateNodeList() {
        mConnectionWait.dismiss();
        mAdapter.clear();
        mAdapter.addAll(mManager.getNodes());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * check that the bluetooth is enabled and register the lister to the manager
     */
    @Override
    protected void onStart() {
        super.onStart();
        mManager.addListener(mUpdateDiscoverGui);
        mAdapter.disconnectAllNodes();
        mManager.addListener(mAdapter);
        resetNodeList();
        startNodeDiscovery();
    }//onStart

    /**
     * stop the discovery and remove all the lister that we attach to the manager
     */
    @Override
    protected void onStop() {
        if (mManager.isDiscovering())
            mManager.stopDiscovery();
        //remove the listener add by this class
        mManager.removeListener(mUpdateDiscoverGui);
        mManager.removeListener(mAdapter);
        super.onStop();
    }//onPause



    public void startNodeDiscovery() {
        super.startNodeDiscovery(SCAN_TIME_MS);
        invalidateOptionsMenu(); //ask to redraw the menu for change the menu icon
    }

    public void stopNodeDiscovery() {
        super.stopNodeDiscovery();
        invalidateOptionsMenu();//ask to redraw the menu for change the menu icon
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Node n = mAdapter.getItem(position);
        if (n == null)
            return;
        mNodeContainer = new NodeContainerFragment();
        Bundle bundle = NodeContainerFragment.prepareArguments(n);
        mNodeContainer.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(mNodeContainer, NODE_FRAGMENT).commit();
    }


    public void goNext(Node node) {
        Intent i = MainActivity.getStartIntent(ScanActivity.this, node);
        startActivity(i);
        return;
    }


}
