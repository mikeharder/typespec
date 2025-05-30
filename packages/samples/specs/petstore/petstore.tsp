import "@typespec/rest";
import "@typespec/openapi";
import "./decorators.js";

using Http;

@service(#{ title: "Pet Store Service" })
@doc("This is a sample server Petstore server.  You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, you can use the api key `special-key` to test the authorization filters.")
namespace PetStore;

// Model types
model Pet {
  name: string;
  tag?: string;

  @minValue(0)
  @maxValue(20)
  age: int32;
}

model Toy {
  id: int64;
  petId: int64;
  name: string;
}

@doc("Error")
@error
model Error {
  code: int32;
  message: string;
}

@doc("Not modified")
model NotModified<Body> {
  @statusCode _: 304;
  @body body: Body;
}

@friendlyName("{name}ListResults", Item)
model ResponsePage<Item> {
  items: Item[];
  nextLink?: string;
}

model PetId {
  @path petId: int32;
}

@doc("Manage your pets.")
@route("/pets")
namespace Pets {
  @doc("Delete a pet.")
  @delete
  op delete(...PetId): OkResponse | Error;

  @fancyDoc("List pets.")
  op list(@query nextLink?: string): ResponsePage<Pet> | Error;

  @doc("Returns a pet. Supports eTags.")
  op read(...PetId): Pet | (NotModifiedResponse & Pet) | Error;

  @post op create(@body pet: Pet): Pet | Error;
}

@route("/pets/{petId}/toys")
namespace ListPetToysResponse {
  op list(@path petId: string, @query nameFilter: string): ResponsePage<Toy> | Error;
}
