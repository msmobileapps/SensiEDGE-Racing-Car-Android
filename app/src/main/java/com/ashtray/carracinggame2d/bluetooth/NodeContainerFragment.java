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
package com.ashtray.carracinggame2d.bluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.ashtray.carracinggame2d.R;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;
public class NodeContainerFragment extends Fragment {

    final static String NODE_TAG = NodeContainerFragment.class.getCanonicalName() + ".NODE_TAG";
    boolean userAskToKeepConnection = false;
    private ProgressDialog mConnectionWait;
    private Node mNode = null;
    private Node.NodeStateListener mNodeStateListener = new Node.NodeStateListener() {
        @Override
        public void onStateChange(final Node node, Node.State newState, Node.State prevState) {
            final Activity activity = NodeContainerFragment.this.getActivity();
            if ((newState == Node.State.Connected) && activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectionWait.dismiss();
                        mConnectionWait  = null;
                    }
                });
            } else {
                mConnectionWait.show();
                mNode.connect(getActivity());
            }
        }
    };


    public static Bundle prepareArguments(Node n) {
        Bundle args = new Bundle();
        args.putString(NODE_TAG, n.getTag());
        return args;
    }

    private void setUpProgressDialog(String nodeName) {
        mConnectionWait = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        mConnectionWait.setTitle(R.string.progressDialogConnTitle);
    }


    public Node getNode() {
        return mNode;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments()==null)
            return;
        String nodeTag = getArguments().getString(NODE_TAG);
        mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);
        if (mNode != null)
            setUpProgressDialog(mNode.getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNode != null && !mNode.isConnected()) {
            mConnectionWait.show(); //show the dialog and set the listener for hide it
            mNode.addNodeStateListener(mNodeStateListener);
            mNode.connect(getActivity());
        }//if
    }//onResume

    @Override
    public void onPause() {
        if (mConnectionWait!=null && mConnectionWait.isShowing()) {
            mConnectionWait.dismiss();
        }
        super.onPause();
    }//onPause

    /**
     * if true avoid to disconnect the node when the fragment is destroyed
     * @param doIt true for skip the disconnect, false for disconnect, default = false
     */
    public void keepConnectionOpen(boolean doIt) {
        userAskToKeepConnection = doIt;
    }

    /**
     * if we are connected we disconnect the node
     */
    @Override
    public void onDestroy() {
        if (mNode != null && mNode.isConnected()) {
            if (!userAskToKeepConnection) {
                mNode.removeNodeStateListener(mNodeStateListener);
                mNode.disconnect();
            }
        }
        super.onDestroy();
    }

}

