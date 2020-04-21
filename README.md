# "WebRTC Native Stack is considered as a single chain as close to the Hardware Abstraction Layer (HAL) as possible."

-----

**Getting Started**

------

This repository involves a **Step by Step Gide to perform WebRTC STUN, TURN Communication in the real world**.
This Guide is based on the referenced article which is basically a complete understanding of [WebRTC in the real world: STUN, TURN and signaling](https://www.html5rocks.com/en/tutorials/webrtc/infrastructure/) for Web Browsers.

If you want to do communication using WebRTC in *Android*, then Good luck guys! There is no need to beating around the bush (Redundant Information About WebRTC on Internet (Mostly For Web Browsers)). Just calm down and read this article. You will end up with a realtime communication over TURN and STUN using WebRTC.

For *Compilation and Building the WebRTC Library for Android*, you should have to look into this refernce:

- [Compile_WebRTC_Library_For_Android](https://github.com/mail2chromium/Compile_WebRTC_Library_For_Android)


For *real-time Communication and AudioProcessing* in Android, I will recommend you to must visit these refernces:

- [Android-Audio-Processing-Using-WebRTC](https://github.com/mail2chromium/Android-Audio-Processing-Using-WebRTC)
- [Android-Native-Development-For-WebRTC](https://github.com/mail2chromium/Android-Native-Development-For-WebRTC)


----

### Content of this Document

-----

[Quick Introduction](#quick-Introduction)

[Signaling](#signaling)

[STUN vs TURN](#stun-vs-TURN)

[Peer to Peer Communication](#peer-to-Peer-Communication)

 - [Exchange Media Configuration Information](#exchange-Media-Configuration-Information)
 - [Exchange Network Configuration Information](#exchange-Network-Configuration-Information)
 - [Communication Via DataChannel](#communication-Via-DataChannel)

 [Conclusion](#Conclusion)

----

### [Quick Introduction](#quick-Introduction)

-----

WebRTC applications need to do several things like:

- Get *streaming* audio, video or other data.
- Get *network information* such as IP addresses and ports, and exchange this with other WebRTC clients (known as peers) to enable connection, even through NATs and firewalls.
- Coordinate *signaling communication* to report errors and initiate or close sessions.
- *Exchange information* about media and client capability, such as resolution and codecs.
- *Communicate streaming* audio, video or data.

WebRTC implemented open standards for real-time, plugin-free video, audio and data communication.
WebRTC is used in various apps like *WhatsApp, Facebook Messenger, appear.in* and platforms such as *TokBox*.
To acquire and communicate streaming data, WebRTC implements three APIs:

- MediaStream
- PeerConnection
- DataChannel

All three APIs are supported on `mobile` and `desktop` by *Chrome, Safari, Firefox, Edge and Opera*.
You can get the complete documentation and details of these three APIs in these references:

- [WebRTC 1.0: Real-time Communication Between Browsers](https://w3c.github.io/webrtc-pc/)
- [Media Capture and Streams](https://www.w3.org/TR/mediacapture-streams/#intro)
- [WebRTC APIs](https://developer.mozilla.org/en-US/docs/Web/API/WebRTC_API)

All of the above three documentations belong to Web-browsers only. But here, we will discuss for Android.

----

### [Signaling](#signaling)

-----

The most important thing to remember is that "**Signaling is not the part of WebRTC! Why?**
Because of these few reasons such as;

- To avoid redundancy, and
- To maximize compatibility with established technologies.

The exchange of information via signaling must have completed successfully before **peer-to-peer streaming** can begin. Signaling is used to exchange three types of information:

- *Session control messages:* to initialize or close communication and report errors.
- *Network configuration:* to the outside world, what's my computer's IP address and port?
- *Media capabilities:* what codecs and resolutions can be handled by my browser and the browser it wants to communicate with?

Signaling methods and protocols are not specified by WebRTC standards. On the other hand,  Signaling is the process of coordinating communication. In order for a android application to;

- Set up a 'call' {Voice, Video},

- Share media files i.e. (Voice Notes, Documents etc)

Android clients (related terminologies {terminals, nodes, members}) need to exchange information such as:

**1.** Session control messages used to open or close communication.

SDP interface describes one end of a connection—or potential connection—and how it's configured. It involves a JSON object with multiple two values (sdp, type)

```
{
sdp : "v=0 o=- 3709108758280432862 2 IN IP4 127.0.0.1 s=- t=0 0 a=msid-semantic: WMS m=application 9 DTLS/SCTP 5000 c=IN IP4 0.0.0.0 a=ice-ufrag:/MM7lfHOlMNfSMRk a=ice-pwd:EogZo3Zihb1g0XWgYFKHpeTk a=fingerprint:sha-256 BE:69:CE:D2:D6:41:41:DB:93:3E:3C:F5:D5:3D:D2:5A:33:8A:B0:A6:47:08:AE:24:A0:F6:FE:8F:39:65:21:CE a=setup:actpass a=mid:data a=sctpmap:5000 webrtc-datachannel 1024 ",

type : "offer"
}

```
You can get more information about [SDP (Session Description Protocol)](https://developer.mozilla.org/en-US/docs/Glossary/SDP).

The property `SessionDescription.type` is a read-only value of type `SdpType` which describes the description's type. The possible values are defined by an enum of type `SdpType` are as follows.

```
"offer", the description is the initial proposal in an offer/answer exchange.
"answer", the description is the definitive choice in an offer/answer exchange.
"pranswer", the description is a provisional answer and may be changed when the definitive choice will be given.
"rollback", the description rolls back to offer/answer state to the last stable state.
```

Here is very specific and detailed intuition about [SdpType (Session Description Type)](https://developer.mozilla.org/en-US/docs/Web/API/RTCSessionDescription/type).

-----

**2.** Error messages and Callbacks.

**3.** Media metadata such as codecs and codec settings, bandwidth and media types.

**4.** Key data, used to establish secure connections.

**5.** Share ICECandidates with each other which is Network data, such as a host's IP address and port as seen by the outside world.

It involves a JSON object with 4 values (candidate, sdpMid, sdpMLineIndex, type):

```
{
 candidate:"candidate:5720275078 1 udp 8837102613 9201:398:am9u:14uf:2934:r39a:h753:z43i 38842 typ host generation 3 ufrag uEJl network-id 3 network-cost 82",
 sdpMid:"audio",
 sdpMLineIndex:2,
 type:"candidate"
}

```

IceCandidate Interface represents a candidate [ICE (Internet Connectivity Establishment)](https://developer.mozilla.org/en-US/docs/Glossary/ICE) configuration which may be used to establish an PeerConnection between Android Phones.

This signaling process needs a way for clients to pass messages back and forth. That mechanism is not implemented by the WebRTC APIs: You need to build it yourself. I will describe below some ways to build a signaling service.
However, a little context is, you can follow these references to implement signaling:

- [Google Talk Call Signaling](https://developers.google.com/talk/call_signaling?csw=1)
- [Process Signaling using Nats.io](https://docs.nats.io/nats-streaming-server/process-signaling)
- [Cloud Functions of Firebase for Signaling](https://firebase.google.com/docs/functions)

----

### [STUN vs TURN](#stun-vs-TURN)

-----

[STUN (Session Traversal Utilities for NAT)](https://tools.ietf.org/html/rfc5389) is a standardized set of methods, including a network protocol, for traversal of network address translator gateways in applications of real-time voice, video, messaging, and other interactive communications.
[TURN (Traversal Using Relay NAT)](https://tools.ietf.org/html/rfc5766) is a protocol that allows a client to obtain IP addresses and ports from such a relay.

While Communicating with Android Peers, there are mostly two alternate data paths are shown, although only one data pathway will be active in a connection.
This is because the *Data (`Audio, Video, Messages, Fax` etc) Pathway* can be

- Either a direct connection (**92%** of connection attempts can take place directly) or
- Through a relay server (**8%** of connection attempts require an intermediary relay server).

A third data pathway, not shown, is a direct connection from computer to computer when there is no intermediary firewall.
The following diagram shows these two pathways.

![Data Exchange Between Peers](https://github.com/mail2chromium/Android_Realtime_Communication_Using_WebRTC/blob/master/data_exchange.gif)

Now obviously, while doing such a stuff, it turns out that you will be confused with a question such as [STUN or TURN which one to refer and Why?](https://www.webrtc-experiment.com/docs/STUN-or-TURN.html)
You can use open source as well as paid STUN & TURN Services such as:

- [CoTURN Open Source TURN Server Project](https://github.com/coturn/coturn)
- [STUNTMAN Open Source STUN Server Project](https://github.com/jselbie/stunserver)
- [Twilio Network Traversal Service](https://www.twilio.com/docs/stun-turn)

**Request and Response Architecture of STUN/TURN Services:**

A REST API to Access the TURN Services basically involves the following steps such as:

**Request:**

The request includes the following parameters, specified in the URL:

-  service: specifies the desired service (turn)
-  username: an optional user id to be associated with the
-  credentials
-  key: if an API key is used for authentication, the API key

**Example:**
```
     GET /?service=turn&username=mbzrxpgjys
```

**Response:**

The response is returned with content-type "application/json", and consists of a JSON object with the following parameters:

- username
- password
- ttl
- uris

**Example:**

```
   {
     "username" : "12334939:mbzrxpgjys",
     "password" : "adfsaflsjfldssia",
     "ttl" : 86400,
     "uris" : [
       "turn:1.2.3.4:9991?transport=udp",
       "turn:1.2.3.4:9992?transport=tcp",
       "turns:1.2.3.4:443?transport=tcp"
     ]
    }
```

**WebRTC Interactions:**

The returned JSON is parsed into an `IceServer` object, and supplied as part to use when creating a `PeerConnection` as follows:

```
         List<PeerConnection.IceServer> iceServers = new LinkedList<>();
                for (int i = 0; i < example.iceServers.size(); i++) {
                    if (!example.iceServers.get(i).username.isEmpty())
                        iceServers.add(new PeerConnection.IceServer(example.iceServers.get(i).url, example.iceServers.get(i).username, example.iceServers.get(i).credential));
                    else
                        iceServers.add(new PeerConnection.IceServer(example.iceServers.get(i).url));
                }

        constraints = new MediaConstraints();
        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, constraints, peerConnectionObserver);
```

You can get the more details about the in  draft i.e [A REST API For Access To TURN Services draft-uberti-behave-turn-rest-00](https://tools.ietf.org/html/draft-uberti-behave-turn-rest-00). In general, WebRTC needs servers to fulfill four types of server-side functionality such as:

- User discovery and communication
- Signaling
- NAT/firewall traversal
- Relay servers in case peer-to-peer communication fails

----

### [Peer to Peer Communication](#peer-to-Peer-Communication)

-----

Here we will discuss the complete workaround to establish Peer-to-Peer communication between two Android Phones/Terminal/Nodes which basiclly depend on these modules:

 - [Exchange Media Configuration Information](#exchange-Media-Configuration-Information)
 - [Exchange Network Configuration Information](#exchange-Network-Configuration-Information)
 - [Communication Via DataChannel](#communication-Via-DataChannel)

I will start explaining the following Process in very specific detail. So first look at the following entire exchange of information between Two Peers:

![Entire Information Exchange Block Diagram](https://github.com/mail2chromium/Android_Realtime_Communication_Using_WebRTC/blob/master/exchanging_content.png)

**Basic Terminologies:**

- Peer-A <--> `Caller` <--> Steve Jobs (Steve) The Person who initiate PeerConnection
- Peer-A <--> `Callee` <--> Bill Gates (Bill) The Person who accepts PeerConnection

**PeerConnection** is the API used by the WebRTC Android Application to create connection between peers and communicate `audio` and `video`.
PeerConnection has two tasks to initialize the Process such as:

- Ascertain (make sure) the local media conditions such as *bandwidth*, **media types**, **resolution** & **codec capabilities** (`opus, speex` etc).
- Start Gathering the list of potential network addresses for the application's host, known as `IceCandidates`.

This metadata is used for the offer and answer mechanism. Once this local data has been ascertained (Gathered), it must be exchanged via a [signaling mechanism](https://developer.mozilla.org/en-US/docs/Web/API/WebRTC_API/Session_lifetime#Signaling) with the remote peer.

-----

### [Exchange Media Configuration Information](#exchange-Media-Configuration-Information)

-----

Now I will discuss complete Offer/Answer mechanism with all its glory details:


**1.** Steve creates a `PeerConnection` Object.

```
        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, constraints, peerConnectionObserver);

```

**2.** Steve creates an **offer** ([an SDP session description](https://developer.mozilla.org/en-US/docs/Web/API/RTCSessionDescription/RTCSessionDescription)) with the PeerConnection `createOffer()` method.

```
        peerConnection.createOffer(sdpObserver, constraints);

```

**3.** Steve calls `setLocalDescription()` with his offer.

```
        peerConnection.setLocalDescription(sdpObserver, sessionDescription);

```

**4.** Steve makes a json object for ([jsonify](https://developer.android.com/reference/org/json/JSONObject)) the offer and uses a signaling mechanism to send it to Bill.

```
        - [Google Talk Call Signaling](https://developers.google.com/talk/call_signaling?csw=1)
        - [Process Signaling using Nats.io](https://docs.nats.io/nats-streaming-server/process-signaling)
        - [Cloud Functions of Firebase for Signaling](https://firebase.google.com/docs/functions)

```

**5.** Bill calls `setRemoteDescription()` with Steve's offer, so that his *PeerConnection* knows about Steve's setup for audio or video communication.

```
        peerConnection.setRemoteDescription(sdpObserver, sdp2);
```

**6.** Bill then calls the `createAnswer()` method, and the success callback for this is passed a local session description: Bill's answer as follows

```
        peerConnection.createAnswer(sdpObserver, constraints);
```

**7.** Bill sets her answer as the local description by calling `setLocalDescription()`.

```
        peerConnection.setLocalDescription(sdpObserver, sessionDescription);
```

**8.** Bill then uses the signaling mechanism to send his json object of the (jsonify) answer back to Steve.

```
{
sdp : "v=0 o=- 3709108758280432862 2 IN IP4 127.0.0.1 s=- t=0 0 a=msid-semantic: WMS m=application 9 DTLS/SCTP 5000 c=IN IP4 0.0.0.0 a=ice-ufrag:/MM7lfHOlMNfSMRk a=ice-pwd:EogZo3Zihb1g0XWgYFKHpeTk a=fingerprint:sha-256 BE:69:CE:D2:D6:41:41:DB:93:3E:3C:F5:D5:3D:D2:5A:33:8A:B0:A6:47:08:AE:24:A0:F6:FE:8F:39:65:21:CE a=setup:actpass a=mid:data a=sctpmap:5000 webrtc-datachannel 1024 ",

type : "answer"
}

```

**9.** Steve then sets Bill's answer as the remote session description using `setRemoteDescription()`.

```
        peerConnection.setRemoteDescription(sdpObserver, sdp2);

```

-----

### [Exchange Network Configuration Information](#exchange-Network-Configuration-Information)

-----

In the portion, Each of pair start 'Gathering or finding candidates' which refers to the process of finding *network interfaces* and *ports* using the [Ice FrameWork](https://www.html5rocks.com/en/tutorials/webrtc/basics/#ice).

**1.** Steve creates an `PeerConnection` object with an `onicecandidate(IceCandidate iceCandidate)` handler.

```
        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            if (username.equals("Steve")) {
                IceCandidateModel iceCandidateModel = new IceCandidateModel("candidate", iceCandidate.sdpMLineIndex, iceCandidate.sdpMid, iceCandidate.sdp);
                Log.e(TAG, "STEVE's ICE CANDIDATE: " + iceCandidateModel.candidate + " " + iceCandidateModel.sdpMid + " " + iceCandidateModel.type + " " + iceCandidateModel.sdpMLineIndex);
                // Do some signaling Stuff
            } else if (username.equals("Bill")) {
                IceCandidateModel iceCandidateModel = new IceCandidateModel("candidate", iceCandidate.sdpMLineIndex, iceCandidate.sdpMid, iceCandidate.sdp);
                Log.e(TAG, "BILL's ICE CANDIDATE: " + iceCandidateModel.candidate + " " + iceCandidateModel.sdpMid + " " + iceCandidateModel.type + " " + iceCandidateModel.sdpMLineIndex);
                // Do some signaling Stuff
            }

```

**2.** The handler is run when network candidates become available.

**3.** Steve sends *serialized candidate data* (json object) to Bill, via whatever signaling channel they are using: TCPSocket, Firebase or some other mechanism.

**4.** When Bill gets a candidate message from Steve, he calls `addIceCandidate()` method, to add the candidate to the remote peer description.

```
        IceCandidate iceCandidate = new IceCandidate(iceCandidateModel.sdpMid, iceCandidateModel.sdpMLineIndex, iceCandidateModel.candidate);
        peerConnection.addIceCandidate(iceCandidate);
```

#### Summary

To sum up the above discussion, We have a **Android Client** also known as *Steve* first create an offer using `PeerConnection createOffer()` method. The return from this is passed an SessionDescription: Steve's local session description.
In the callback, Steve sets the *local description* using `setLocalDescription()` and then sends this session description to Bill via their signaling channel. Note that `PeerConnection` won't start gathering candidates until `setLocalDescription()` is called.
Bill sets the description which Steve sent him as the *remote description* using `setRemoteDescription()`.
Bill runs the `PeerConnection createAnswer()` method, passing it the remote description he got from Steve, so a local session can be generated that is compatible with him. The `createAnswer()` callback is passed an `SessionDescription`: Bill sets that as the *local description* and sends it to Steve.
When Steve gets Bill's session description, he sets that as the *remote description* with setRemoteDescription. Its a very simple and pretty straight forward discussion: Bravo!!!

![Steve and Bill Communication](https://github.com/mail2chromium/Android_Realtime_Communication_Using_WebRTC/blob/master/steve_bill.PNG)

----

### [Communication Via DataChannel](#communication-Via-DataChannel)

-----

Once the `PeerConnection` is established between the *Peer-A* and *Peer-B*, they both can use DataChannel API to share Data between each other. The DataChannel API enables peer-to-peer exchange of arbitrary data (audio, video, etc), with low latency and high throughput.
Steve sends its useful information in the form of bytes using DataChannel `send(new DataChannel.Buffer(buffer, false))`.

```
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        ByteBuffer buffer = ByteBuffer.wrap(audio.getRecordedData());
                        dataChannel.send(new DataChannel.Buffer(buffer, false));
                    }
                }
            }).start();
```

On the other side, Bill receives, steve information using DataChannel `buffer.data.get(data)` method.

```
        @Override
        public void onMessage(final DataChannel.Buffer buffer) {
            try {

                if (!buffer.binary) {
                    int limit = buffer.data.limit();
                    byte[] data = new byte[limit];
                    buffer.data.get(data);
                    byteQueue.add(data);

                } else {
                    Log.e(TAG, "Data is not received.");

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ERROR: " + e.toString());
            }
        }
```

Communication occurs directly between Android Phones. DataChannel communication can be much faster than Simple Socket communication even if a relay (TURN) server is required when 'hole punching' to cope with firewalls and NATs fails.

There are many potential use cases for the API, including:

- Gaming
- Remote desktop applications
- Real-time text chat
- File transfer
- Decentralized networks

The API has several features to make the most of RTCPeerConnection and enable powerful and flexible peer-to-peer communication:

- Leveraging of RTCPeerConnection session setup.
- Multiple simultaneous channels, with prioritization.
- Reliable and unreliable delivery semantics.
- Built-in security (DTLS) and congestion control.
- Ability to use with or without audio or video.

Each `P2P TransportChannel` represents a data channel between the local and remote Android Terminals. This channel actually obscures a complex system designed for robustness and performance. P2P TransportChannel manages a number of different Connection objects,
each of which is specialized for a different connection type (UDP, TCP, etc). A Connection object actually wraps a pair of objects:

- A Port subclass, representing the local connection; and
- An address representing the remote connection.

If a particular connection fails, P2P TransportChannel will seamlessly switch to the next best connection. The following diagram shows a high level view of the data pathway inside the Peer to Peer Component.

![P2P Transport Channel](https://github.com/mail2chromium/Android_Realtime_Communication_Using_WebRTC/blob/master/port_socket_connection.gif)

-----

### [Conclusion](#Conclusion)

-----

The WebRTC APIs and standards can very useful and optimized tools for content creation and communication—for real-time audio, gaming, video production, music making, news gathering and many other applications.
I have tried my best to give you guys the more appropriate and detailed information with the simple implementation. All you need is to create and Android Application and inside the
application `build.gradle` file just includes the following dependency:

```
        implementation 'io.pristine:libjingle:11139@aar'

```

As [libjingle (Build on the top of WebRTC Native Stack)](https://chromium.googlesource.com/external/webrtc/stable/talk/+/3798b4190c4a196ed1f38f68492a1e51072d8024/libjingle.gyp) has implemented all of the *three* above mentioned WebRTC APIs in a very detailed and specific way to follow the standards of WebRTC. To get the detailed intuition about
libjingle you can visit [Google Talk for Developers](https://developers.google.com/talk/libjingle/developer_guide). Good Luck Guys!!!
