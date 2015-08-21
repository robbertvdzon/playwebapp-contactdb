package actors;

import models.Contact;
import models.User;


public class UserActorProtocol {

    public static class GetUsers {
    }


    public static class GetUser {
        public final String uuid;

        public GetUser(String uuid) {
            this.uuid = uuid;
        }
    }

    public static class AddUser {
        public User user;

        public AddUser(User user) {
            this.user = user;
        }
    }

    public static class AddContact {
        public Contact contact;

        public AddContact(Contact contact) {
            this.contact = contact;
        }
    }

    public static class DeleteUser {
        public String uuid;

        public DeleteUser(String uuid) {
            this.uuid = uuid;
        }
    }

    public static class DeleteContact {
        public String uuid;

        public DeleteContact(String uuid) {
            this.uuid = uuid;
        }
    }


}
