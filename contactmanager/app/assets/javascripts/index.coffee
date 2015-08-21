$ ->
 $.get "/users", (users) ->
   $.each users, (index,user) ->
     $('#users').append("<option value=\"#{user.uuid}\">#{user.name} </option>");
   loadUser();

 $('#users').change ->
    loadUser();

loadUser = () ->
    useruid = $('#users :selected').val()
    $('#contacts li').remove();
    $.get "/user/"+useruid, (user) ->
       $('#name').val user.name;
       $('#uuid').val user.uuid;
       $('#addContactUserUuid').val user.uuid;
       $.each user.contacts, (index,contact) ->
         $('#contacts').append $("<li>").html contact.name+" - "+ contact.email + "&nbsp;&nbsp;<a href=# onClick=\"deleteContact('"+contact.uuid+"')\">[Delete Contact]</a>";

@deleteContact = (uuid) ->
    $.ajax({
        url: '/contact/'+uuid,
        type: 'DELETE',
        success: (data, textStatus, jqXHR) ->
            window.location.reload(false);
    });

@deleteUser = () ->
    $.ajax({
        url: '/user/'+$('#uuid').val(),
        type: 'DELETE',
        success: (data, textStatus, jqXHR) ->
            window.location.reload(false);
    });