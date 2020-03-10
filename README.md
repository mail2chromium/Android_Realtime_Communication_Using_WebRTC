WebRTC STUN, TURN Communication in the real world.

This tutorial is based on this article which is basically a complete understanding of [WebRTC in the real world: STUN, TURN and signaling](https://www.html5rocks.com/en/tutorials/webrtc/infrastructure/) for Web Browsers.

#### What is Signaling:


#### What is TURN:


#### What is STUN:




WebRTC enables peer to peer communication.

BUT...

WebRTC still needs servers:

For clients to exchange metadata to coordinate communication: this is called signaling.
To cope with network address translators (NATs) and firewalls.
In this article we show you how to build a signaling service, and how to deal with the quirks of real-world connectivity by using STUN and TURN servers. We also explain how WebRTC apps can handle multi-party calls and interact with services such as VoIP and PSTN (aka telephones).

If you're not familiar with the basics of WebRTC, we strongly recommend you take a look at Getting Started With WebRTC before reading this article.
