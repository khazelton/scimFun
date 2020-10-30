import com.unboundid.scim2.client.ScimService;
import com.unboundid.scim2.common.exceptions.ScimException;
import com.unboundid.scim2.common.types.UserResource;
import com.unboundid.scim2.common.types.Name;
import com.unboundid.scim2.common.types.Email;
import com.unboundid.scim2.common.GenericScimResource;
import com.unboundid.scim2.common.messages.ListResponse;
import com.unboundid.scim2.common.filters.Filter;

// Create a ScimService
Client client = ClientBuilder.newClient().register(OAuth2ClientSupport.feature("..bearerToken.."));;
WebTarget target = client.target("https://example.com/scim/v2");
ScimService scimService = new ScimService(target);

// Create a user
UserResource user = new UserResource();
user.setUserName("babs");
user.setPassword("secret");
Name name = new Name()
  .setGivenName("Barbara")
  .setFamilyName("Jensen");
user.setName(name);
Email email = new Email()
  .setType("home")
  .setPrimary(true)
  .setValue("babs@example.com");
user.setEmails(Collections.singletonList(email));
user = scimService.create("Users", user);

// Retrieve the user as a UserResource and replace with a modified instance using PUT
user = scimService.retrieve("Users", user.getId(), UserResource.class);
user.setDisplayName("Babs");
user = scimService.replace(user);

// Retrieve the user as a GenericScimResource and replace with a modified instance using PUT
GenericScimResource genericUser =
    scimService.retrieve("Users", user.getId(), GenericScimResource.class);
genericUser.replaceValue("displayName", TextNode.valueOf("Babs Jensen"));
genericUser = scimService.replaceRequest(genericUser).invoke();

// Perform a partial modification of the user using PATCH
scimService.modifyRequest("Users", user.getId())
           .replaceValue("displayName", "Babs")
           .invoke(GenericScimResource.class);

// Perform a password change using PATCH
scimService.modifyRequest("Users", user.getId())
           .replaceValue("password", "new-password")
           .invoke(GenericScimResource.class);

// Search for users with the same last name as our user
ListResponse<UserResource> searchResponse =
  scimService.searchRequest("Users")
        .filter(Filter.eq("name.familyName", user.getName().getFamilyName()).toString())
        .page(1, 5)
        .attributes("name")
        .invoke(UserResource.class);
