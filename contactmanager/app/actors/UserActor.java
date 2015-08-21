package actors;

import actors.UserActorProtocol.*;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import models.Contact;
import models.Contacts;
import models.User;
import models.Users;
import play.Configuration;
import play.api.Play;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import java.util.List;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

public class UserActor extends UntypedActor {
    private String userWsCallGetUsers;
    private String userWsCallGetUser;
    private String userWsCallDeleteUser;
    private String userWsCallAddUser;
    private String userWsUrl;
    private String userWsPort;
    private String contactsWsCallGetContacts;
    private String contactsWsCallDeleteContact;
    private String contactsWsCallAddContact;
    private String contactsWsUrl;
    private String contactsWsPort;

    public UserActor() {
        Configuration configuration = new Configuration(Play.current().configuration());
        userWsUrl = configuration.getString("userws.userWsUrl");
        userWsPort = configuration.getString("userws.userWsPort");
        userWsCallGetUsers = configuration.getString("userws.userWsCallGetUsers");
        userWsCallGetUser = configuration.getString("userws.userWsCallGetUser");
        userWsCallDeleteUser = configuration.getString("userws.userWsCallDeleteUser");
        userWsCallAddUser = configuration.getString("userws.userWsCallAddUser");
        contactsWsUrl = configuration.getString("contactsws.userWsUrl");
        contactsWsPort = configuration.getString("contactsws.userWsPort");
        contactsWsCallGetContacts = configuration.getString("userws.contactsWsCallGetContacts");
        contactsWsCallDeleteContact = configuration.getString("userws.contactsWsCallDeleteContact");
        contactsWsCallAddContact = configuration.getString("userws.contactsWsCallAddContact");
    }

    public static Props props = Props.create(UserActor.class);

    public void onReceive(Object msg) throws Exception {
        final ActorRef sender = sender();
        if (msg instanceof GetUsers) {
            getUsers().map(wsResponses -> {
                sender.tell(buildUsers(wsResponses), self());
                return null;
            });
        } else if (msg instanceof GetUser) {
            String uuid = ((GetUser) msg).uuid;
            getUser(uuid).map(wsResponses -> {
                sender.tell(buildUser(wsResponses), self());
                return null;
            });
        } else if (msg instanceof AddUser) {
            User user = ((AddUser) msg).user;
            addUser(user).map(wsResponses -> {
                sender.tell(true, self());
                return null;
            });
        } else if (msg instanceof AddContact) {
            Contact contact = ((AddContact) msg).contact;
            addContact(contact).map(wsResponses -> {
                sender.tell(true, self());
                return null;
            });
        } else if (msg instanceof DeleteContact) {
            String uuid = ((DeleteContact) msg).uuid;
            deleteContact(uuid).map(wsResponses -> {
                sender.tell(true, self());
                return null;
            });
        } else if (msg instanceof DeleteUser) {
            String uuid = ((DeleteUser) msg).uuid;
            deleteUser(uuid).map(wsResponses -> {
                sender.tell(true, self());
                return null;
            });
        }
    }

    private User buildUser(List<WSResponse> wsResponses) {
        JsonNode jsonUser = wsResponses.get(0).asJson();
        JsonNode jsonContacts = wsResponses.get(1).asJson();
        User user = fromJson(jsonUser, User.class);
        Contacts contacts = fromJson(jsonContacts, Contacts.class);
        user.setContacts(contacts.getContacts());
        return user;
    }

    private List<User> buildUsers(WSResponse wsResponse) {
        JsonNode jsonUsers = wsResponse.asJson();
        return fromJson(jsonUsers, Users.class).getUsers();
    }

    private F.Promise<List<WSResponse>> getUser(String uuid) {
        final F.Promise<WSResponse> userResponsePromise = WS.url("http://" + userWsUrl + ":" + userWsPort + "/" + userWsCallGetUser + "/" + uuid).get();
        final F.Promise<WSResponse> contactResponsePromise = WS.url("http://" + contactsWsUrl + ":" + contactsWsPort + "/" + contactsWsCallGetContacts + "/" + uuid).get();
        F.Promise<List<WSResponse>> sequence = F.Promise.sequence(userResponsePromise, contactResponsePromise);
        return sequence;
    }

    private F.Promise<WSResponse> getUsers() {
        return WS.url("http://" + userWsUrl + ":" + userWsPort + "/" + userWsCallGetUsers).get();
    }

    private F.Promise<WSResponse> addUser(User user) {
        return WS.url("http://" + userWsUrl + ":" + userWsPort + "/" + userWsCallAddUser).post(toJson(user));
    }

    private F.Promise<WSResponse> addContact(Contact contact) {
        return WS.url("http://" + contactsWsUrl + ":" + contactsWsPort + "/" + contactsWsCallAddContact).post(toJson(contact));
    }

    private F.Promise<WSResponse> deleteUser(String uuid) {
        return WS.url("http://" + userWsUrl + ":" + userWsPort + "/" + userWsCallDeleteUser + "/" + uuid).delete();
    }

    private F.Promise<WSResponse> deleteContact(String uuid) {
        return WS.url("http://" + contactsWsUrl + ":" + contactsWsPort + "/" + contactsWsCallDeleteContact + "/" + uuid).delete();
    }


}