package com.example.webrtc.webRTCModules;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.webrtc.Audio;
import com.example.webrtc.NTSTokenAsyncTask;
import com.example.webrtc.NetworkCallback;
import com.example.webrtc.models.DataModel;
import com.example.webrtc.models.Example;
import com.example.webrtc.models.IceCandidateModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WebRTCConnection implements NetworkCallback {
    private static final String TAG = "Connect_webRTC";

    private Context context;

    private DataChannel dataChannel;
    private MediaConstraints constraints;
    private PeerConnection peerConnection;
    private PeerConnectionFactory peerConnectionFactory;
    private DatabaseReference databaseReference;


    private Boolean createdOffer = false;
    private Audio audio;
    private Queue<byte[]> byteQueue;
    private String meetingId;
    private String username = "";
    private String usernameRoot = "";
    private String usernameCandidate = "";
    private String callingTo = "";
    private String callingToRoot = "";
    private String callingToCandidates = "";

    public WebRTCConnection(Context context, String meetingId, String username, String callingTo, boolean createdOffer) {
        this.context = context;
        this.meetingId = meetingId;
        this.username = username;
        this.usernameRoot = username+"Root";
        this.usernameCandidate = username + "Candidates";

        this.callingTo = callingTo;
        this.callingToRoot = callingTo+"Root";
        this.callingToCandidates = callingTo + "Candidates";

        this.createdOffer = createdOffer;


        byteQueue = new LinkedList<>();

        PeerConnectionFactory.initializeAndroidGlobals(context, true, true, true);

        constraints = new MediaConstraints();
        peerConnectionFactory = new PeerConnectionFactory();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(meetingId);

        NTSTokenAsyncTask ntsToken = new NTSTokenAsyncTask(context, this);
        ntsToken.execute();

        audio = new Audio();
        audio.inititalize();

        audio.startRecording();
        audio.startPlaying();



    }

    //todo: to Play Audio Bytes
    public void playAudio() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!byteQueue.isEmpty()) {
                            audio.playBytes(byteQueue.remove());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "PlayAudio Exception: " + e.toString());
                    }
                }
            }
        }).start();
    }

    //todo: to create offer
    private void createOffer() {
        peerConnection.createOffer(sdpObserver, constraints);
    }

    //todo: to setup firebase database signaling listener
    // to actively fetching IceCandidates from either Parties {Steve, Bill}
    public void setupListeners() {

        /*todo: Make a signaling Call back channel to send CandidateDataListener
        todo: You can use Firebase Database model to save the information of Steve
        todo: So that Bill Can this information
         */

        databaseReference.child(callingToRoot).addValueEventListener(candidateDataListener);

    }

    //todo: Start Sending Data using DataChannel to either Parties {Steve, Bill}
    public boolean startSend() {

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        ByteBuffer buffer = ByteBuffer.wrap(audio.getRecordedData());
                        dataChannel.send(new DataChannel.Buffer(buffer, false));
                    }
                }
            }).start();
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //todo: Session Description Callbacks
    SdpObserver sdpObserver = new SdpObserver() {
        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            peerConnection.setLocalDescription(sdpObserver, sessionDescription);
            DataModel dataModel = new DataModel(sessionDescription.description, sessionDescription.type.toString().toLowerCase());

            // todo: Do some signaling Stuff to Send Steve or Bill Local Session Description
            // todo: Send dataModel to other Person
        }

        @Override
        public void onSetSuccess() {

        }

        @Override
        public void onCreateFailure(String s) {

        }

        @Override
        public void onSetFailure(String s) {

        }
    };

    //todo: Signaling Channel, Here is simplest firebase model, You Should try your own approach
    // to set Parameters
    ValueEventListener candidateDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            DataModel dataModel = dataSnapshot.child(callingTo).getValue(DataModel.class);
            if (dataModel != null) {
                SessionDescription sdp2 = new SessionDescription(SessionDescription.Type.fromCanonicalForm(dataModel.type), dataModel.sdp);
                peerConnection.setRemoteDescription(sdpObserver, sdp2);

                if(!createdOffer)
                    peerConnection.createAnswer(sdpObserver, constraints);

                for (DataSnapshot postSnapshot : dataSnapshot.child(callingToCandidates).getChildren()) {
                    IceCandidateModel iceCandidateModel = postSnapshot.getValue(IceCandidateModel.class);

                    IceCandidate iceCandidate1 = new IceCandidate(iceCandidateModel.sdpMid, iceCandidateModel.sdpMLineIndex, iceCandidateModel.candidate);
                    peerConnection.addIceCandidate(iceCandidate1);
                }

            } else {
                Log.e(TAG, "DATA MODEL IS NULL");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //todo: PeerConnection Callbacks
    PeerConnection.Observer peerConnectionObserver = new PeerConnection.Observer() {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(TAG, "onSignalingChange() " + signalingState.name());
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Log.d(TAG, "onIceConnectionChange() " + iceConnectionState.name());
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Log.d(TAG, "onIceConnectionReceivingChange(): " + b);
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(TAG, "onIceGatheringChange() " + iceGatheringState.name());
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.d(TAG, "onIceCandidate: " + iceCandidate.toString());

            IceCandidateModel iceCandidateModel = new IceCandidateModel("candidate", iceCandidate.sdpMLineIndex, iceCandidate.sdpMid, iceCandidate.sdp);
            // Do Some Signaling stuff to share IceCandidate Model with other Steve or Bill
            databaseReference.child(usernameRoot).child(usernameCandidate).push().setValue(iceCandidateModel);

        }

        @Override
        public void onAddStream(MediaStream mediaStream) {

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel1) {
            dataChannel = dataChannel1;
            dataChannel.registerObserver(dataChannelObserver);
        }

        @Override
        public void onRenegotiationNeeded() {
            Log.d(TAG, "onRenegotiationNeeded()");

        }
    };

    //todo: DataChannel Callbacks
    DataChannel.Observer dataChannelObserver = new DataChannel.Observer() {
        @Override
        public void onBufferedAmountChange(long l) {

        }

        @Override
        public void onStateChange() {

        }

        @Override
        public void onMessage(final DataChannel.Buffer buffer) {
            try {

                if (!buffer.binary) {
                    int limit = buffer.data.limit();
                    byte[] data = new byte[limit];
                    buffer.data.get(data);

                    byteQueue.add(data);

                } else {
                    Log.e(TAG, "Data is received but not binary.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ERROR: " + e.toString());
            }
        }
    };

    //todo: Initial PeerConnection and DataChannel
    @Override
    public void InitializePeerConnection(Example example) {

        /*
                url: 'turn:192.158.29.39:3478?transport=udp',
                credential: 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
                username: '28224511:1379330808'
         */
        List<PeerConnection.IceServer> iceServers = new LinkedList<>();
        for (int i = 0; i < example.iceServers.size(); i++) {
            if (!example.iceServers.get(i).username.isEmpty())
                iceServers.add(new PeerConnection.IceServer(example.iceServers.get(i).url, example.iceServers.get(i).username, example.iceServers.get(i).credential));
            else
                iceServers.add(new PeerConnection.IceServer(example.iceServers.get(i).url));
        }

        constraints = new MediaConstraints();
        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, constraints, peerConnectionObserver);

        DataChannel.Init init = new DataChannel.Init();
        init.ordered = true;
        dataChannel = peerConnection.createDataChannel("RTCDataChannel", init);
        dataChannel.registerObserver(dataChannelObserver);

        setupListeners();

        if(createdOffer)
        {
            createOffer();
        }
    }


}