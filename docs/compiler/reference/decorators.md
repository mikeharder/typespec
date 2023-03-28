---
title: "Decorators"
toc_min_heading_level: 2
toc_max_heading_level: 3
---

# Decorators

## TypeSpec

### `@format` {#@format}

Specify a known data format hint for this string type. For example `uuid`, `uri`, etc.
This differ from the

```typespec
dec format(target: TypeSpec.string | TypeSpec.bytes | TypeSpec.Reflection.ModelProperty, format: TypeSpec.string)
```

#### Target

`union TypeSpec.string | TypeSpec.bytes | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name   | Type                     | Description  |
| ------ | ------------------------ | ------------ |
| format | `scalar TypeSpec.string` | format name. |

### `@doc` {#@doc}

Attach a documentation string.

```typespec
dec doc(target: unknown, doc: TypeSpec.string, formatArgs?: TypeSpec.object)
```

#### Target

`(intrinsic) unknown`

#### Parameters

| Name       | Type                     | Description                                                     |
| ---------- | ------------------------ | --------------------------------------------------------------- |
| doc        | `scalar TypeSpec.string` | Documentation string                                            |
| formatArgs | `model TypeSpec.object`  | Record with key value pair that can be interpolated in the doc. |

### `@withUpdateableProperties` {#@withUpdateableProperties}

```typespec
dec withUpdateableProperties(target: TypeSpec.object)
```

#### Target

`model TypeSpec.object`

#### Parameters

None

### `@withoutOmittedProperties` {#@withoutOmittedProperties}

```typespec
dec withoutOmittedProperties(target: TypeSpec.object, omit: TypeSpec.string | TypeSpec.Reflection.Union)
```

#### Target

`model TypeSpec.object`

#### Parameters

| Name | Type                                                 | Description |
| ---- | ---------------------------------------------------- | ----------- |
| omit | `union TypeSpec.string \| TypeSpec.Reflection.Union` |             |

### `@withoutDefaultValues` {#@withoutDefaultValues}

```typespec
dec withoutDefaultValues(target: TypeSpec.object)
```

#### Target

`model TypeSpec.object`

#### Parameters

None

### `@withDefaultKeyVisibility` {#@withDefaultKeyVisibility}

```typespec
dec withDefaultKeyVisibility(target: TypeSpec.object, visibility: unknown)
```

#### Target

`model TypeSpec.object`

#### Parameters

| Name       | Type                  | Description |
| ---------- | --------------------- | ----------- |
| visibility | `(intrinsic) unknown` |             |

### `@summary` {#@summary}

Typically a short, single-line description.

```typespec
dec summary(target: unknown, summary: TypeSpec.string)
```

#### Target

`(intrinsic) unknown`

#### Parameters

| Name    | Type                     | Description     |
| ------- | ------------------------ | --------------- |
| summary | `scalar TypeSpec.string` | Summary string. |

### `@deprecated` {#@deprecated}

Mark this type as deprecated

```typespec
dec deprecated(target: unknown, message: TypeSpec.string)
```

#### Target

`(intrinsic) unknown`

#### Parameters

| Name    | Type                     | Description          |
| ------- | ------------------------ | -------------------- |
| message | `scalar TypeSpec.string` | Deprecation message. |

### `@service` {#@service}

Mark this namespace as describing a service and configure service properties.

```typespec
dec service(target: TypeSpec.Reflection.Namespace, options?: TypeSpec.ServiceOptions)
```

#### Target

`Namespace`

#### Parameters

| Name    | Type                            | Description                             |
| ------- | ------------------------------- | --------------------------------------- |
| options | `model TypeSpec.ServiceOptions` | Optional configuration for the service. |

### `@error` {#@error}

Specify that this model is an error type. Operations return error types when the operation has failed.

```typespec
dec error(target: TypeSpec.object)
```

#### Target

`model TypeSpec.object`

#### Parameters

None

#### Examples

```typespec
@error
model PetStoreError {
code: string;
message: string;
}
```

### `@pattern` {#@pattern}

Specify the the pattern this string should respect using simple regular expression syntax.
The following syntax is allowed: alternations (`|`), quantifiers (`?`, `*`, `+`, and `{ }`), wildcard (`.`), and grouping parentheses.
Advanced features like look-around, capture groups, and references are not supported.

```typespec
dec pattern(target: TypeSpec.string | TypeSpec.bytes | TypeSpec.Reflection.ModelProperty, pattern: TypeSpec.string)
```

#### Target

`union TypeSpec.string | TypeSpec.bytes | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name    | Type                     | Description         |
| ------- | ------------------------ | ------------------- |
| pattern | `scalar TypeSpec.string` | Regular expression. |

### `@minLength` {#@minLength}

Specify the minimum length this string type should be.

```typespec
dec minLength(target: TypeSpec.string | TypeSpec.Reflection.ModelProperty, value: TypeSpec.integer)
```

#### Target

`union TypeSpec.string | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description    |
| ----- | ------------------------- | -------------- |
| value | `scalar TypeSpec.integer` | Minimum length |

### `@maxLength` {#@maxLength}

Specify the maximum length this string type should be.

```typespec
dec maxLength(target: TypeSpec.string | TypeSpec.Reflection.ModelProperty, value: TypeSpec.integer)
```

#### Target

`union TypeSpec.string | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description    |
| ----- | ------------------------- | -------------- |
| value | `scalar TypeSpec.integer` | Maximum length |

### `@minItems` {#@minItems}

Specify the minimum number of items this array should have.

```typespec
dec minItems(target: unknown[] | TypeSpec.Reflection.ModelProperty, value: TypeSpec.integer)
```

#### Target

`union unknown[] | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description    |
| ----- | ------------------------- | -------------- |
| value | `scalar TypeSpec.integer` | Minimum number |

### `@maxItems` {#@maxItems}

Specify the maximum number of items this array should have.

```typespec
dec maxItems(target: unknown[] | TypeSpec.Reflection.ModelProperty, value: TypeSpec.integer)
```

#### Target

`union unknown[] | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description    |
| ----- | ------------------------- | -------------- |
| value | `scalar TypeSpec.integer` | Maximum number |

### `@minValue` {#@minValue}

Specify the minimum value this numeric type should be.

```typespec
dec minValue(target: TypeSpec.numeric | TypeSpec.Reflection.ModelProperty, value: TypeSpec.numeric)
```

#### Target

`union TypeSpec.numeric | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description   |
| ----- | ------------------------- | ------------- |
| value | `scalar TypeSpec.numeric` | Minimum value |

### `@maxValue` {#@maxValue}

Specify the maximum value this numeric type should be.

```typespec
dec maxValue(target: TypeSpec.numeric | TypeSpec.Reflection.ModelProperty, value: TypeSpec.numeric)
```

#### Target

`union TypeSpec.numeric | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description   |
| ----- | ------------------------- | ------------- |
| value | `scalar TypeSpec.numeric` | Maximum value |

### `@minValueExclusive` {#@minValueExclusive}

Specify the minimum value this numeric type should be, exclusive of the given
value.

```typespec
dec minValueExclusive(target: TypeSpec.numeric | TypeSpec.Reflection.ModelProperty, value: TypeSpec.numeric)
```

#### Target

`union TypeSpec.numeric | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description   |
| ----- | ------------------------- | ------------- |
| value | `scalar TypeSpec.numeric` | Minimum value |

### `@maxValueExclusive` {#@maxValueExclusive}

Specify the maximum value this numeric type should be, exclusive of the given
value.

```typespec
dec maxValueExclusive(target: TypeSpec.numeric | TypeSpec.Reflection.ModelProperty, value: TypeSpec.numeric)
```

#### Target

`union TypeSpec.numeric | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name  | Type                      | Description   |
| ----- | ------------------------- | ------------- |
| value | `scalar TypeSpec.numeric` | Maximum value |

### `@secret` {#@secret}

Mark this string as a secret value that should be treated carefully to avoid exposure

```typespec
dec secret(target: TypeSpec.string | TypeSpec.Reflection.ModelProperty)
```

#### Target

`union TypeSpec.string | TypeSpec.Reflection.ModelProperty`

#### Parameters

None

#### Examples

```typespec
@secret
scalar Password is string;
```

### `@tag` {#@tag}

Attaches a tag to an operation, interface, or namespace. Multiple `@tag` decorators can be specified to attach multiple tags to a TypeSpec element.

```typespec
dec tag(target: TypeSpec.Reflection.Namespace | TypeSpec.Reflection.Interface | TypeSpec.Reflection.Operation, tag: TypeSpec.string)
```

#### Target

`union TypeSpec.Reflection.Namespace | TypeSpec.Reflection.Interface | TypeSpec.Reflection.Operation`

#### Parameters

| Name | Type                     | Description |
| ---- | ------------------------ | ----------- |
| tag  | `scalar TypeSpec.string` | Tag value   |

### `@friendlyName` {#@friendlyName}

Specifies how a templated type should name their instances.

```typespec
dec friendlyName(target: unknown, name: TypeSpec.string, formatArgs?: unknown)
```

#### Target

`(intrinsic) unknown`

#### Parameters

| Name       | Type                     | Description                            |
| ---------- | ------------------------ | -------------------------------------- |
| name       | `scalar TypeSpec.string` | name the template instance should take |
| formatArgs | `(intrinsic) unknown`    |                                        |

### `@knownValues` {#@knownValues}

Provide a set of known values to a string type.

```typespec
dec knownValues(target: TypeSpec.string | TypeSpec.numeric | TypeSpec.Reflection.ModelProperty, values: TypeSpec.Reflection.Enum)
```

#### Target

`union TypeSpec.string | TypeSpec.numeric | TypeSpec.Reflection.ModelProperty`

#### Parameters

| Name   | Type   | Description        |
| ------ | ------ | ------------------ |
| values | `Enum` | Known values enum. |

### `@key` {#@key}

Mark a model property as the key to identify instances of that type

```typespec
dec key(target: TypeSpec.Reflection.ModelProperty, altName?: TypeSpec.string)
```

#### Target

`ModelProperty`

#### Parameters

| Name    | Type                     | Description |
| ------- | ------------------------ | ----------- |
| altName | `scalar TypeSpec.string` |             |

#### Examples

```typespec
model Pet {
@key id: string;
}
```

### `@overload` {#@overload}

Specify this operation is an overload of the given operation.

```typespec
dec overload(target: TypeSpec.Reflection.Operation, overloadbase: TypeSpec.Reflection.Operation)
```

#### Target

`Operation`

#### Parameters

| Name         | Type        | Description                                            |
| ------------ | ----------- | ------------------------------------------------------ |
| overloadbase | `Operation` | Base operation that should be a union of all overloads |

### `@projectedName` {#@projectedName}

Provide an alternative name for this type.

```typespec
dec projectedName(target: unknown, targetName: TypeSpec.string, projectedName: TypeSpec.string)
```

#### Target

`(intrinsic) unknown`

#### Parameters

| Name          | Type                     | Description       |
| ------------- | ------------------------ | ----------------- |
| targetName    | `scalar TypeSpec.string` | Projection target |
| projectedName | `scalar TypeSpec.string` | Alternative name  |

### `@discriminator` {#@discriminator}

Specify the property to be used to discriminate this type.

```typespec
dec discriminator(target: TypeSpec.object | TypeSpec.Reflection.Union, propertyName: TypeSpec.string)
```

#### Target

`union TypeSpec.object | TypeSpec.Reflection.Union`

#### Parameters

| Name         | Type                     | Description |
| ------------ | ------------------------ | ----------- |
| propertyName | `scalar TypeSpec.string` |             |

#### Examples

```typespec
@discriminator("kind")
union Pet{ cat: Cat, dog: Dog }

model Cat {kind: "cat", meow: boolean}
model Dog {kind: "dog", bark: boolean}
```

```typespec
@discriminator("kind")
model Pet{ kind: string }

model Cat extends Pet {kind: "cat", meow: boolean}
model Dog extends Pet  {kind: "dog", bark: boolean}
```

### `@visibility` {#@visibility}

Indicates that a property is only considered to be present or applicable ("visible") with
the in the given named contexts ("visibilities"). When a property has no visibilities applied
to it, it is implicitly visible always.

As far as the TypeSpec core library is concerned, visibilities are open-ended and can be arbitrary
strings, but the following visibilities are well-known to standard libraries and should be used
with standard emitters that interpret them as follows:

- "read": output of any operation.
- "create": input to operations that create an entity..
- "query": input to operations that read data.
- "update": input to operations that update data.
- "delete": input to operations that delete data.

See also: [Automatic visibility](https://microsoft.github.io/typespec/standard-library/rest/operations#automatic-visibility)

```typespec
dec visibility(target: TypeSpec.Reflection.ModelProperty, ...visibilities: TypeSpec.string[])
```

#### Target

`ModelProperty`

#### Parameters

| Name         | Type                      | Description |
| ------------ | ------------------------- | ----------- |
| visibilities | `model TypeSpec.string[]` |             |

#### Examples

```typespec
model Dog {
// the service will generate an ID, so you don't need to send it.
@visibility("read") id: int32;
// the service will store this secret name, but won't ever return it
@visibility("create", "update") secretName: string;
// the regular name is always present
name: string;
}
```

### `@withVisibility` {#@withVisibility}

Removes properties that are not considered to be present or applicable
("visible") in the given named contexts ("visibilities"). Can be used
together with spread to effectively spread only visible properties into
a new model.

See also: [Automatic visibility](https://microsoft.github.io/typespec/standard-library/rest/operations#automatic-visibility)

When using an emitter that applies visibility automatically, it is generally
not necessary to use this decorator.

```typespec
dec withVisibility(target: TypeSpec.object, ...visibilities: TypeSpec.string[])
```

#### Target

`model TypeSpec.object`

#### Parameters

| Name         | Type                      | Description |
| ------------ | ------------------------- | ----------- |
| visibilities | `model TypeSpec.string[]` |             |

#### Examples

```typespec
model Dog {
@visibility("read") id: int32;
@visibility("create", "update") secretName: string;
name: string;
}

// The spread operator will copy all the properties of Dog into DogRead,
// and @withVisibility will then remove those that are not visible with
// create or update visibility.
//
// In this case, the id property is removed, and the name and secretName
// properties are kept.
@withVisibility("create", "update")
model DogCreateOrUpdate {
...Dog;
}

// In this case the id and name properties are kept and the secretName property
// is removed.
@withVisibility("read")
model DogRead {
...Dog;
}
```

### `@inspectType` {#@inspectType}

```typespec
dec inspectType(target: unknown, text: TypeSpec.string)
```

#### Target

`(intrinsic) unknown`

#### Parameters

| Name | Type                     | Description |
| ---- | ------------------------ | ----------- |
| text | `scalar TypeSpec.string` |             |

### `@inspectTypeName` {#@inspectTypeName}

```typespec
dec inspectTypeName(target: unknown, text: TypeSpec.string)
```

#### Target

`(intrinsic) unknown`

#### Parameters

| Name | Type                     | Description |
| ---- | ------------------------ | ----------- |
| text | `scalar TypeSpec.string` |             |