package controllers;

import models.Contact;
import models.Contacts;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.UUID;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public Result addContact() {
        Contact contact = Form.form(Contact.class).bindFromRequest().get();
        contact.setUuid(UUID.randomUUID().toString());
        contact.save();
        return ok(toJson(contact));
    }

    public Result getContacts(String userUuid) {
        List<Contact> contacts = Contact.find.where().eq("userUuid", userUuid).findList();
        return ok(toJson(new Contacts(contacts)));
    }

    public Result getContact(String uuid) {
        List<Contact> contacts = Contact.find.where().eq("uuid", uuid).findList();
        if (!contacts.isEmpty()) {
            return ok(toJson(contacts.get(0)));
        }
        return notFound();
    }

    public Result deleteContact(String uuid) {
        List<Contact> contacts = Contact.find.where().eq("uuid", uuid).findList();
        if (!contacts.isEmpty()) {
            contacts.get(0).delete();
            return ok();
        }
        return notFound();
    }
}
