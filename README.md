WebRTC STUN, TURN Communication in the real world.

This tutorial is based on this article which is basically a complete understanding of [WebRTC in the real world: STUN, TURN and signaling](https://www.html5rocks.com/en/tutorials/webrtc/infrastructure/) for Web Browsers.


If you want to do communication using WebRTC, then Good luck guys!

Don't beating around the bush (Redundant Information About WebRTC on Internet (Mostly For Web Browerses)). Just calm down and read this article. You will end up with a realtime communication over TURN and STUN using WebRTC.

----

#### What is Signaling:

The most important thing to remember is that "**Signaling is not the part of WebRTC**  Why?
Because of these few reasons such as;

- To avoid redundancy,

- To maximize compatibility with established technologies

Signaling methods and protocols are not specified by WebRTC standards.

On the other hand,  Signaling is the process of coordinating communication. In order for a android application to;

- Set up a 'call' {Voice, Video},

- Share media files i.e. (Voice Notes, Documents etc)

Its clients (terminals, nodes, members) need to exchange information such as:

1. Session control messages used to open or close communication.

It involves a JSON object with multiple two values (sdp, type)

```
{
sdp : "v=0 o=- 3709108758280432862 2 IN IP4 127.0.0.1 s=- t=0 0 a=msid-semantic: WMS m=application 9 DTLS/SCTP 5000 c=IN IP4 0.0.0.0 a=ice-ufrag:/MM7lfHOlMNfSMRk a=ice-pwd:EogZo3Zihb1g0XWgYFKHpeTk a=fingerprint:sha-256 BE:69:CE:D2:D6:41:41:DB:93:3E:3C:F5:D5:3D:D2:5A:33:8A:B0:A6:47:08:AE:24:A0:F6:FE:8F:39:65:21:CE a=setup:actpass a=mid:data a=sctpmap:5000 webrtc-datachannel 1024 ",

type : "offer" 
}

```

2. Error messages.

3. Media metadata such as codecs and codec settings, bandwidth and media types.

4. Key data, used to establish secure connections.

5. Network data, such as a host's IP address and port as seen by the outside world.
This signaling process needs a way for clients to pass messages back and forth. That mechanism is not implemented by the WebRTC APIs: you need to build it yourself. We describe below some ways to build a signaling service. First, however, a little context...


----

#### What is TURN:


#### What is STUN:




WebRTC enables peer to peer communication.

BUT...

WebRTC still needs servers:

For clients to exchange metadata to coordinate communication: this is called signaling.
To cope with network address translators (NATs) and firewalls.
In this article we show you how to build a signaling service, and how to deal with the quirks of real-world connectivity by using STUN and TURN servers. We also explain how WebRTC apps can handle multi-party calls and interact with services such as VoIP and PSTN (aka telephones).

If you're not familiar with the basics of WebRTC, we strongly recommend you take a look at Getting Started With WebRTC before reading this article.


