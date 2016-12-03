package manager;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.impl.id.CBID.PipeID;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeService;
import net.jxta.platform.Module;
import net.jxta.platform.ModuleClassID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class P2PCommunicationManager {
	
	private static String sharedSecretKey;
	private static String sharedSecretKeyMsg;
	
    private String peer_name;
    private PeerID peer_id;	
    private File conf;
    private NetworkManager manager;
    
    
    public P2PCommunicationManager(int port){
    	
    	
        peer_name = "Peer " + new Random().nextInt(1000000); 


        peer_id = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, peer_name.getBytes());

        conf = new File("." + System.getProperty("file.separator") + peer_name);

         try {
            manager = new NetworkManager(
                    NetworkManager.ConfigMode.ADHOC,
                    peer_name, conf.toURI());
        }
        catch (IOException e) {
            // Will be thrown if you specify an invalid directory in conf
            e.printStackTrace();
        }

        NetworkConfigurator configurator;
        try {
            configurator = manager.getConfigurator();
            configurator.setTcpPort(port);
            configurator.setTcpEnabled(true);
            configurator.setTcpIncoming(true);
            configurator.setTcpOutgoing(true);
            configurator.setUseMulticast(true);
            configurator.setPeerID(peer_id);
        } 
        catch (IOException e) {
        	e.printStackTrace();
        }
    }

    public void setSharedSecretKey(String inputSKey){
    	this.sharedSecretKey = inputSKey;
    }
    
    public String getSharedSecretKey(){
    	return this.sharedSecretKey;
    }
    
    private static final String subgroup_name = "gateway_Calendar";
    private static final String subgroup_desc = "grouping gateway App and Calendar App";
    private static final PeerGroupID subgroup_id = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, subgroup_name.getBytes());

    private static final String unicast_name = "gateway_Calendar";
    private static final String multicast_name = "gateway_Calendar";

    private static final String service_name = "Grouping_gateway_Calendar";

    private PeerGroup subgroup;
    private PipeService pipe_service;
    private PipeID unicast_id;
    private PipeID multicast_id;
    private PipeID service_id;
    private DiscoveryService discovery;
    private ModuleSpecAdvertisement mdadv;

    public void start() throws PeerGroupException, IOException {
    	
        PeerGroup net_group = manager.startNetwork();

        ModuleImplAdvertisement mAdv = null;
        try {
            mAdv = net_group.getAllPurposePeerGroupImplAdvertisement();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        subgroup = net_group.newGroup(subgroup_id, mAdv, subgroup_name, subgroup_desc);

        // 그루핑 되었는지
        if (Module.START_OK != subgroup.startApp(new String[0]))
            System.err.println("Cannot start child peergroup");

        unicast_id = (PipeID) IDFactory.newPipeID(subgroup.getPeerGroupID(), unicast_name.getBytes());
        multicast_id = (PipeID) IDFactory.newPipeID(subgroup.getPeerGroupID(), multicast_name.getBytes());

        pipe_service = subgroup.getPipeService();
//        pipe_service.createInputPipe(get_advertisement(unicast_id, false), this);
//        pipe_service.createInputPipe(get_advertisement(multicast_id, true), this);


        discovery = subgroup.getDiscoveryService();
        discovery.addDiscoveryListener((DiscoveryListener) this);        

        ModuleClassAdvertisement mcadv = (ModuleClassAdvertisement)
        AdvertisementFactory.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());

        mcadv.setName("PAIRING_GATEWAY_CALENDAR");
        mcadv.setDescription("JXTA module advertisement Framework to pair users' apps");

        ModuleClassID mcID = IDFactory.newModuleClassID();

        mcadv.setModuleClassID(mcID);


        discovery.publish(mcadv);
        discovery.remotePublish(mcadv);

        mdadv = (ModuleSpecAdvertisement)
                AdvertisementFactory.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());
        mdadv.setName("PAIRING_GATEWAY_CALENDAR");
        mdadv.setVersion("Version 1.0");
        mdadv.setCreator("sun.com");
        mdadv.setModuleSpecID(IDFactory.newModuleSpecID(mcID));
        mdadv.setSpecURI("http://www.jxta.org/Ex1");

        service_id = (PipeID) IDFactory.newPipeID(subgroup.getPeerGroupID(), service_name.getBytes());
        PipeAdvertisement pipeadv = get_advertisement(service_id, false);
        mdadv.setPipeAdvertisement(pipeadv);

        discovery.publish(mdadv);
        discovery.remotePublish(mdadv);

//        pipe_service.createInputPipe(pipeadv, this);
    }

    private static PipeAdvertisement get_advertisement(PipeID id, boolean is_multicast) {
        PipeAdvertisement adv = (PipeAdvertisement )AdvertisementFactory.
            newAdvertisement(PipeAdvertisement.getAdvertisementType());
        adv.setPipeID(id);
        if (is_multicast)
            adv.setType(PipeService.PropagateType); 
        else 
            adv.setType(PipeService.UnicastType); 
        adv.setName("pipeAd");
        adv.setDescription("pipAd");
        return adv;
    }

    public void discoveryEvent(DiscoveryEvent event,String msgToSend) {	//메세지 보내기
        String found_peer_id = "urn:jxta:" + event.getSource().toString().substring(7);
        send_to_peer(msgToSend, found_peer_id);
    }


    private void send_to_peer(String message, String found_peer_id) {

        PipeAdvertisement adv = get_advertisement(unicast_id, false);

        Set<PeerID> ps = new HashSet<PeerID>();		//그룹이 한명
        try {
            ps.add((PeerID)IDFactory.fromURI(new URI(found_peer_id)));
        } 
        catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // msg sending pipe
        OutputPipe sender = null;
        try {
            sender = pipe_service.createOutputPipe(adv, ps, 10000);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        Message msg = new Message();
        MessageElement fromElem = null;
        MessageElement msgElem = null;
        try {
            fromElem = new ByteArrayMessageElement("From", null, peer_id.toString().getBytes("ISO-8859-1"), null);
            msgElem = new ByteArrayMessageElement("Msg", null, message.getBytes("ISO-8859-1"), null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


         msg.addMessageElement(fromElem);
         msg.addMessageElement(msgElem);

         try {
            sender.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //메세지 받기
    public void pipeMsgEvent(PipeMsgEvent event) {
       
        try {
            Message msg = event.getMessage();
            byte[] msgBytes = msg.getMessageElement("Msg").getBytes(true);  
            byte[] fromBytes = msg.getMessageElement("From").getBytes(true); 
            String from = new String(fromBytes);
            String message = new String(msgBytes);
            //System.out.println(message + " says " + from);

            
            byte[] encodedKey = Base64.decodeBase64(sharedSecretKey);
            String decodedMsg = AEScodingManager.decodeByAES(new SecretKeySpec(encodedKey,0,encodedKey.length,"AES"));
            
            setSharedSecretKey(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void fetch_advertisements() {
      new Thread("fetch advertisements thread") {
         public void run() {
            while(true) {
                discovery.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", "PAIRING_GATEWAY_CALENDAR", 1, null);
                try {
                    sleep(10000);

                }
                catch(InterruptedException e) {} 
            }
         }
      }.start();
   }
}
