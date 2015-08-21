$ ->
 $.get "/users", (users) ->
   $.each users, (index,user) ->
     $('#users').append("<option value=\"#{user.uuid}\">#{user.name} </option>");
   loadSelectedUser();
   loadUser();

 $('#users').change ->
    loadUser();
    saveSelectedUser();

loadSelectedUser = () ->
  $('#users option[value='+Cookies.set('lastuser')+']').attr('selected','selected');

saveSelectedUser = () ->
  useruid = $('#users :selected').val()
  Cookies.set('lastuser', useruid);

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