package controllers;

import actors.UserActor;
import actors.UserActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.Contact;
import models.User;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;

import static akka.pattern.Patterns.ask;
import static play.libs.Json.toJson;

@Singleton
public class Application extends Controller {

    final ActorRef userActor;

    @Inject
    public Application(ActorSystem system) {
        userActor = system.actorOf(UserActor.props);
    }

    public Result index() {
        return ok(index.render());
    }

    public F.Promise<Result> addUser() {
        User newUser = Form.form(User.class).bindFromRequest().get();
        return F.Promise.wrap(ask(userActor, new UserActorProtocol.AddUser(newUser), 1000))
                .map(users -> redirect(routes.Application.index()));
    }

    public F.Promise<Result> addContact() {
        Contact newContact = Form.form(Contact.class).bindFromRequest().get();
        return F.Promise.wrap(ask(userActor, new UserActorProtocol.AddContact(newContact), 1000))
                .map(users -> redirect(routes.Application.index()));
    }

    public F.Promise<Result> getUsers() {
        return F.Promise.wrap(ask(userActor, new UserActorProtocol.GetUsers(), 1000))
                .map(users -> ok(toJson(users)).as("text/json"));
    }

    public F.Promise<Result> getUser(String uuid) {
        return F.Promise.wrap(ask(userActor, new UserActorProtocol.GetUser(uuid), 1000))
                .map(user -> ok(toJson(user)).as("text/json"));
    }

    public F.Promise<Result> deleteUser(String uuid) {
        return F.Promise.wrap(ask(userActor, new UserActorProtocol.DeleteUser(uuid), 1000))
                .map(users -> redirect(routes.Application.index()));
    }

    public F.Promise<Result> deleteContact(String uuid) {
        return F.Promise.wrap(ask(userActor, new UserActorProtocol.DeleteContact(uuid), 1000))
                .map(users -> redirect(routes.Application.index()));
    }

}
