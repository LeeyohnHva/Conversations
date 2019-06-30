package eu.siacs.conversations.tests;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.entities.Contact;
import eu.siacs.conversations.entities.Conversation;
import eu.siacs.conversations.entities.DownloadableFile;
import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.entities.Presence;
import eu.siacs.conversations.ui.RecordingActivity;
import rocks.xmpp.addr.Jid;

import static eu.siacs.conversations.entities.Conversational.MODE_SINGLE;
import static eu.siacs.conversations.entities.Message.TYPE_IMAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;


public class TestClass {

  Jid jid = new Jid() {
    @Override
    public boolean isFullJid() {
      return false;
    }

    @Override
    public boolean isBareJid() {
      return false;
    }

    @Override
    public boolean isDomainJid() {
      return false;
    }

    @Override
    public Jid asBareJid() {
      return null;
    }

    @Override
    public Jid withLocal(CharSequence local) {
      return null;
    }

    @Override
    public Jid withResource(CharSequence resource) {
      return null;
    }

    @Override
    public Jid atSubdomain(CharSequence subdomain) {
      return null;
    }

    @Override
    public String getLocal() {
      return null;
    }

    @Override
    public String getEscapedLocal() {
      return null;
    }

    @Override
    public String getDomain() {
      return null;
    }

    @Override
    public String getResource() {
      return null;
    }

    @Override
    public String toEscapedString() {
      return null;
    }

    @Override
    public int length() {
      return 0;
    }

    @Override
    public char charAt(int i) {
      return 0;
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
      return null;
    }

    @Override
    public int compareTo(Jid jid) {
      return 0;
    }
  };

  private Account testAccount;
  private File file;
  private String photoUri;
  private String imageFilePath;
  private int lastSeen;
  private int invalidLastSeen;
  @Before
  public void CreateAccount() throws IOException {
    testAccount = new Account(jid,"test");
    file  = folder.newFile("myfile1.txt");
    photoUri = "local/test";
    imageFilePath = "local/test";
    lastSeen = 1500;
    invalidLastSeen = -20;
  }

  //@Test(timeout = 100)
  @Test
  public void testLastSeen()
  {
    Contact mockedContact = mock(Contact.class);
    Contact contact = new Contact(jid);

    contact.setLastseen(invalidLastSeen);
    assertFalse("Last seen can't be a negative timestamp",contact.getLastseen() < 0);

    contact.setLastseen(lastSeen);
    mockedContact.setLastseen(lastSeen);
    verify(mockedContact).setLastseen(lastSeen);
    assertEquals(mockedContact.getLastseen(),contact.getLastseen());
  }

  //@Test(timeout = 500)
  @Test
  public void testRecordVoiceMessage()
  {
    RecordingActivity mockedRecording =  Mockito.mock(RecordingActivity.class);
    mockedRecording.startRecording();
    mockedRecording.stopRecording(true);
    verify(mockedRecording).startRecording();
    verify(mockedRecording).stopRecording(true);
  }

  @Test(timeout = 100)
  public void testChangeStatus()
  {
    Contact testContact = new Contact(jid);
    Presence onlinePresence = new Presence(Presence.Status.ONLINE,"test","test","test","test");
    Presence inChatPresence = new Presence(Presence.Status.CHAT,"test","test","test","test");
    Presence offlinePresence = new Presence(Presence.Status.OFFLINE,"test","test","test","test");

    testContact.updatePresence("test",onlinePresence);
    assertEquals(testContact.getShownStatus(),Presence.Status.ONLINE);

    testContact.updatePresence("test",inChatPresence);
    assertEquals(testContact.getShownStatus(),Presence.Status.CHAT);

    testContact.updatePresence("test",offlinePresence);
    assertEquals(testContact.getShownStatus(),Presence.Status.OFFLINE);
  }

  @Test(timeout = 100)
  public void testOpenImage()
  {
    Conversation testConversation = new Conversation("test", testAccount,jid,MODE_SINGLE);
    Message imageMessage = new Message(testConversation, "I sent you an image!",0);
    imageMessage.setRelativeFilePath(imageFilePath);
    imageMessage.setType(TYPE_IMAGE);
    assertEquals(imageMessage.getType(), TYPE_IMAGE);
    assertTrue(imageMessage.isFileOrImage());
    assertEquals(imageMessage.getRelativeFilePath(),imageFilePath);
  }


  @Test
  public void testAddContact()
  {
    Contact mockedContact = mock(Contact.class);
    Contact contact = new Contact(jid);

    contact.setAccount(testAccount);
    mockedContact.setAccount(testAccount);
    verify(mockedContact).setAccount(testAccount);

    contact.setPhotoUri(photoUri);
    when(mockedContact.getProfilePhoto()).thenReturn(photoUri);
    assertEquals(mockedContact.getProfilePhoto(),contact.getProfilePhoto());
  }

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test(timeout = 1000)
  public void testDownloadFile()  {
    String path = file.getAbsolutePath();
    assertNotNull(file);
    DownloadableFile download = new DownloadableFile(path);
    assertNotNull(download);
  }


  @Test(timeout = 1000)
  public void testSendFile() {
    String path = file.getAbsolutePath();
    assertNotNull(file);
    DownloadableFile download = new DownloadableFile(path);
    assertEquals(download.getAbsolutePath(),path);
    assertEquals(download.length(),file.length());
    assertEquals(file, download.getAbsoluteFile());
  }




  @Test(timeout = 100)
  public void testSendMessage(){
    String body = "test body";
    Conversation testConversation = new Conversation("test", testAccount,jid,MODE_SINGLE);
    Message testMessage = new Message(testConversation, body ,0);
    assertNotNull(testMessage);
    assertEquals("Strings should be equal",testMessage.getBody(),body);
    assertFalse("Only files need uploading", testMessage.needsUploading());
//    XmppConnectionService service = new XmppConnectionService();
//    assertNotNull(service);
//    service.sendMessage(testMessage);
  }
}
